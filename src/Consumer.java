import java.text.Normalizer;
import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;

/**
 * Consumer. Fetches a line in the queue and splits it to count words.
 */
public class Consumer implements Runnable {
    private BlockingQueue<String> sharedQueue;
   // public static Hashtable<String, Integer> wordCounter = new Hashtable<>();
   public static Hashtable<String, Integer> wordCounts;


    public  Consumer (BlockingQueue sharedQueue, Hashtable wordCounts ){
        this.sharedQueue = sharedQueue;
        Consumer.wordCounts = wordCounts;

    }

    public static String patternNormalize(String s){
        String rtn = new String(Normalizer.normalize(s, Normalizer.Form.NFD)
                //remove special caracters
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase())
                //remove all punctuation
                .replaceAll("[^a-zA-Z ]", "");
        //System.out.println("printado : "+rtn);
        return  rtn;

    }

    @Override
    public void run() {

        while (!sharedQueue.isEmpty()) {

            // Get a line from the queue
            // Retrieves and removes the head of this queue, or returns null if this queue is empty.
            String line = sharedQueue.poll();
            if (line == null) continue;

            //apply pattern in words and split to array
            // \s+ == space
            String[] words = patternNormalize(line).split("\\s+");
            for (String word : words) {
                int count = wordCounts.containsKey(word) ? wordCounts.get(word) + 1 : 1;
                wordCounts.put(word, count);
                //System.out.println("Inserindo: " + word);
            }

        }
    }

}