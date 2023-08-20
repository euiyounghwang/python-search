package search.indexing;

public class Indexing extends Thread{
    String host;

    public Indexing(String[] args) {
        this.host = args[0];
        System.out.println("Hellow Indexing..");
    }

    @Override
    public void run() {
        System.out.println(this.host + " thread start");
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(this.host +" thread end");
    }
}

