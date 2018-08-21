import java.text.Normalizer;
import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;



public class Main {

    private BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<>();
    public Hashtable<String, Integer> wordCounter = new Hashtable<>();
    private static Pattern specialCharsRemovePattern = Pattern.compile("[^a-zA-Z]");

    private static final String FOLDER_PATH = "";

    //System.out.println(Runtime.getRuntime().availableProcessors());
    private static final Integer N_THREADS = 4;

    public static String patternNormalize(String s){
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        return s.toLowerCase();

    }

    public static void main(String args[]) {
    String line = "maça macâ Maca êba";
        String[] words = patternNormalize(line).split("\\s+");
        for (String word : words) {
            System.out.println(word);
        }
    }
}




