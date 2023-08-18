import json

import pandas as pd
from elasticsearch import Elasticsearch
from config.log_config import create_log
import argparse

logger = create_log()
MAX_BYTES = 1048576

def get_headers():
    ''' Elasticsearch Header '''
    return {'Content-type': 'application/json', 'Connection': 'close'}

def get_es_instance(_host):
    # create a new instance of the Elasticsearch client class
    es_client = Elasticsearch(hosts=_host, headers=get_headers(), timeout=5)
    return es_client

def create_index(es_client, _index):
    logger.info(es_client)
    mapping = {
        "mappings": {
            "properties": {
                "title": {"type": "text", "analyzer": "english"},
                "ethnicity": {"type": "text", "analyzer": "standard"},
                "director": {"type": "text", "analyzer": "standard"},
                "cast": {"type": "text", "analyzer": "standard"},
                "genre": {"type": "text", "analyzer": "standard"},
                "plot": {"type": "text", "analyzer": "english"},
                "year": {"type": "integer"},
                "wiki_page": {"type": "keyword"}
            }
        }
    }

    try:
        if es_client.indices.exists(index=_index):
            es_client.indices.delete(index=_index, ignore=[400, 404])
            logger.info("Successfully deleted: {}".format(_index))

        logger.info('Creating..')
        # now create a new index
        es_client.indices.create(index=_index, body=mapping)
        logger.info("Successfully created: {}".format(_index))
    except Exception as error:
        logger.error('Error: {}, index: {}'.format(error, _index))


def Get_Buffer_Length(docs):
    """
    :param docs:
    :return:
    """
    max_len = 0
    for doc in docs:
        max_len += len(str(doc))

    return max_len


def load():
    df = (
        pd.read_csv("../Dataset/wiki_movie_plots_deduped.csv")
        .dropna()
        .sample(5000, random_state=42)
        .reset_index()
    )
    return df

def sinngle_indexing_mode_run(es, _index):
    logger.info("sinngle_indexing_mode_run Loading..")
    df = load()
    logger.info(df.loc[0])

    for i, row in df.iterrows():
        doc = {
            "title": row["Title"],
            "ethnicity": row["Origin/Ethnicity"],
            "director": row["Director"],
            "cast": row["Cast"],
            "genre": row["Genre"],
            "plot": row["Plot"],
            "year": row["Release Year"],
            "wiki_page": row["Wiki Page"]
        }

        es.index(index=_index, id=i, body=doc)


def buffer_indexing_mode_run(es, _index):
    logger.info("buffer_indexing_mode_run Loading..")
    df = load()
    logger.info(df.loc[0])

    actions = []
    for i, row in df.iterrows():
        doc = {
            "title": row["Title"],
            "ethnicity": row["Origin/Ethnicity"],
            "director": row["Director"],
            "cast": row["Cast"],
            "genre": row["Genre"],
            "plot": row["Plot"],
            "year": row["Release Year"],
            "wiki_page": row["Wiki Page"]
        }

        actions.append({'index': {'_index': _index, '_id': i}})
        actions.append(doc)

        if Get_Buffer_Length(actions) > MAX_BYTES:
            response = es.bulk(body=actions)
            logger.info("** indexing ** : {}".format(json.dumps(response, indent=2)))
            del actions[:]

    # --
    # Index for the remain dataset
    # --
    response = es.bulk(body=actions)
    logger.info("** Remain Dataset indexing ** : {}".format(json.dumps(response, indent=2)))


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Index into Elasticsearch using this script")
    parser.add_argument('-t', '--target', dest='target', default="http://localhost:9209", help='host target')
    args = parser.parse_args()

    if args.target:
        host = args.target

    # --
    # Instance for the response time log
    es_host = get_es_instance(host)
    # --
    index = "test_omnisearch_v1"

    create_index(es_host, index)
    # sinngle_indexing_mode_run(es_host, index)
    buffer_indexing_mode_run(es_host, index)