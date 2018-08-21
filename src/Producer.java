import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class Producer  {
    private BlockingQueue<String> sharedQueue;
    private String inputDir;

    public  Producer (BlockingQueue sharedQueue, String inputDir){
        this.sharedQueue = sharedQueue;
        this.inputDir = inputDir;
    }

    private void fileReader(File input){
        //File input = new File(inputFile);
        int count = 0;
        //FileInputStream: in use to correct charset in inputfile
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(input), StandardCharsets.ISO_8859_1))) {
            String line;
            while ((line = br.readLine()) != null) {
                sharedQueue.put(line);
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void dirReader(){
        File dir = new File(inputDir);

        for (File file : dir.listFiles()) {
            //System.out.println("oi");
            fileReader(file);
        }
    }

    }

