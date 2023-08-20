package search.indexing;

public class Indexing extends Thread{
    int num;

    public Indexing() {
        System.out.println("Hellow Indexing..");
    }

    @Override
    public void run() {
        System.out.println(this.num + " thread start");
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(this.num +" thread end");
    }
}

