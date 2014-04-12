import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

public class Main
{ 
    static class BuggyQueue
    {
        // Resizable-array implementation.
        private List<String> mQ = new ArrayList<String>();

        // Insert msg at the tail of the queue.
        public void put(String msg){ mQ.add(msg); }

        // Remove msg from the head of the queue.
        public String take(){ return mQ.remove(0); }
    }

    static int mMaxIterations = 100;

    public static void main(String argv[]) {
        final BuggyQueue buggyQueue = new BuggyQueue();

        Thread producer = 
            new Thread(new Runnable(){ 
                    public void run(){ 
                        for(int i = 0; i < mMaxIterations; i++)
                            buggyQueue.put(Integer.toString(i)); 
                    }});
        Thread consumer =
            new Thread(new Runnable(){
                    public void run(){ 
                        for(int i = 0; i < mMaxIterations; i++)
                            System.out.println(buggyQueue.take());
                    }});

        consumer.start();
        producer.start();
    }
}
