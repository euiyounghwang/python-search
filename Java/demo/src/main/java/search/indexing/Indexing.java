package search.indexing;


import java.io.IOException;
import java.util.logging.Logger;

import org.apache.http.HttpHost;
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


public class Indexing extends Thread{
    
    private String host, search_index;
    private String[] host_array = new String[2];
    private RestHighLevelClient client = null;
    
    private Logger logger = Logger.getLogger(Indexing.class.getName());
    
    public Indexing(String[] args) {
        this.host = args[0];
        host_array = this.host.replace("http://", "").split(":");
        logger.info(String.format("host: %s, port : %s", host_array[0], host_array[1]));
        this.search_index = "omnisearch_search";
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        this.client = new RestHighLevelClient(
                            RestClient.builder
                            (
                                new HttpHost(host_array[0], Integer.parseInt(host_array[1]), "http")
                            //     new HttpHost("localhost", 9201, "http")
                            )
                       );
                       
        // System.out.println(this.client);
        logger.info(this.client.toString());
        
    }
    
    
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
        finally {
            this.client.close();
        }
    }
    
    public void results(SearchResponse response) {
        SearchHit[] results = response.getHits().getHits();
            for(SearchHit hit : results){
                String sourceAsString = hit.getSourceAsString();
                if (sourceAsString != null) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    // System.out.println(gson.fromJson(sourceAsString, Firewall.class));
                    logger.info(gson.toJson(sourceAsString));
                }
            }
    }

    @Override
    public void run() {
        try{
            search();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

