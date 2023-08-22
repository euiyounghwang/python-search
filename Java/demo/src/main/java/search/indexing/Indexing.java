package search.indexing;


import java.io.IOException;
import java.util.logging.Logger;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class Indexing extends Thread{
    
    private String host, search_index;
    private String[] host_array = new String[2];
    private RestHighLevelClient client = null;
    
    private Logger logger = Logger.getLogger(Indexing.class.getName());
    
    public Indexing(String[] args) {
        this.host = args[0];
        this.search_index = "omnisearch_search";
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        host_array = this.host.replace("http://", "").split(":");
        logger.info(String.format("host: %s, port : %s", host_array[0], host_array[1]));

        this.client = get_instance(host_array);
        logger.info(this.client.toString());
    }
    
    /*
     * Get elasticsearch client instance
     */
    private RestHighLevelClient get_instance(String[] host_array) {
         RestHighLevelClient es_client = new RestHighLevelClient (
                            RestClient.builder
                            (
                                new HttpHost(host_array[0], Integer.parseInt(host_array[1]), "http"),
                                new HttpHost("localhost", 9201, "http")
                            )
                       );
        return es_client;
    }
    
    /*
    public void index() throws IOException {
        try {
            
            IndexRequest request = new IndexRequest(this.search_index);
            
            // Request
            IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
            logger.info(String.format("The number of total counts : %s", indexResponse));
            
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
     */
    
    public void search() throws IOException {
        try 
        {
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.queryStringQuery("video"));
            
            SearchRequest request = new SearchRequest(this.search_index);
            request.source(builder);

            // Request
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            logger.info(String.format("The number of total counts : %s", response.getHits().getTotalHits().toString()));
            
            results(response);
          
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    /*
     * JSON Pretty()
     */
    public String gson_pretty(String str) {
        if (str == null) {
            return null;
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(str);
        String prettyJsonString = gson.toJson(jsonElement);
        return prettyJsonString;
    }
    
    /*
     * Handle with Elasticsearch Response
     */
    public void results(SearchResponse response) {
        SearchHit[] results = response.getHits().getHits();
        for(SearchHit hit : results) 
            {
                String sourceAsString = hit.getSourceAsString();
                if (sourceAsString != null) {
                    String response_json = gson_pretty(sourceAsString);
                    if (response_json != null) {
                        logger.info(String.format("Response - %s", response_json));        
                    }
                }
            }
    }

    @Override
    public void run() {
        try{
            // index();
            search();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (this.client != null) {
                    this.client.close();    
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

