package com.example;
import search.indexing.Indexing;

/**
 * java -cp ./demo-1.0-SNAPSHOT.jar com.example.App
 * java -jar ./demo/target/java-indexing-1.0-SNAPSHOT.jar http://host.docker.internal:9209
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // new Indexing();
        Indexing myThread = new Indexing(args);
        myThread.start();
        System.out.println("main end");
    }
}


