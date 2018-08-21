import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;



public class Server {


    private static final String DIR_PATH = "C:\\Users\\regiu\\Desktop\\PROG6\\trabalho1\\Producao";
    //System.out.println(Runtime.getRuntime().availableProcessors());

    private static final Integer N_THREADS = 4;


    public static Integer wordMapFinder(Hashtable<String, Integer> wordCounter, String str){
        Integer s = wordCounter.get(str);
        if(s != null){
            return s;
        }else {
            return 0;
        }


    }


    public static void main(String args[]) throws InterruptedException {

        System.out.println("Iniciado o memory load");
        //shared memory
        BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<>();
        Hashtable<String, Integer> wordCounter = new Hashtable<>();
        Thread[] consumers;

        // Create array to store the consumer threads
        consumers = new Thread[N_THREADS];

        // Create and start Producer (read directory)
        Producer produtor1 = new Producer(sharedQueue, DIR_PATH);
        produtor1.dirReader();


        // Start timer
        System.out.println("Iniciando contagem com "+ N_THREADS + " threads!");
        long executionStartTime = System.currentTimeMillis();

        // Create and start Consumer Threads
        for (int i = 0; i < N_THREADS; i++) {
            consumers[i] = new Thread(new Consumer(sharedQueue,wordCounter ));
            consumers[i].start();
        }

        // Wait for all threads to finish
        for (int i = 0; i < N_THREADS; i++) {
            consumers[i].join();
        }

        // Print execution time for search
        long finalExecutionTime = System.currentTimeMillis() - executionStartTime;

        System.out.println("Tempo de execução  do memory load (ms): " + finalExecutionTime);
        //System.out.println("Check if key 2 exists: " + wordCounter.get("vangeleoneluolcombr"));



// Enumeration items = wordCounter.keys();
//        while(items.hasMoreElements()) {
//
//            System.out.println("oo: "+ items.nextElement());
//        }
//  ======================UDP server open=========================

        DatagramSocket serverSocket = null;

        try {
            serverSocket = new DatagramSocket(9876);
        } catch (SocketException e) {
            e.printStackTrace();
        }


        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        while(true)
        {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String sentence = null;
            sentence = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.ISO_8859_1);

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();


           // String Finalmessage = "Tempo de execução (loading) (ms): " + finalExecutionTime;
            //System.out.println(("FINAL: " +sentence +"Ocorrencias: "+ wordMapFinder(wordCounter, "lula")));

           // String Finalmessage = ("Palavra: " +sentence +"Ocorrencias: "+ wordMapFinder(wordCounter, patternNormalize(sentence)));
            StringBuilder message = new StringBuilder();
            String[] words = sentence.split("\\s+");
            long SearchStartTime = System.currentTimeMillis();
            for (String word : words) {
                message.append("Palavra: "+word + "\n Ocorrencias: "+ wordMapFinder(wordCounter,word));
                message.append("\n");

            }
            long finalSearchTime = System.currentTimeMillis() - SearchStartTime;

            //total execution time
            message.append("Tempo de contagem (ms): "+ finalExecutionTime);
            message.append("\n");
            //total memory load and mapping time
            message.append("Tempo de busca de todas as palavras (ms): " + finalSearchTime);
            message.append("\n");

            //prepare to send
            String finalMessage = message.toString();

            sendData = finalMessage.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);

            try {
                serverSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

// ======================UDP server close=========================

    }



}





