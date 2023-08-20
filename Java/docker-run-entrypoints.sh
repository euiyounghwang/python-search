#!/bin/bash
set -ex


# poetry run python ./search-indexing-script.py --es $ES_HOST
# java -jar /app/ES-Services/indexing.jar http://host.docker.internal:9209
java -jar /app/indexing.jar $ES_HOST

sleep 10