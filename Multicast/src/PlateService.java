import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;

/**
 * Created by Zé on 03/03/2016.
 */
public class PlateService extends Thread {

    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;
    protected int port;
    protected String operation;
    protected String plate;
    protected String name;

    public PlateService(String[] args) throws IOException {
        this.port = Integer.parseInt(args[0]);
        socket = new DatagramSocket(port);
    }


    public void run(){
        System.out.println("runnnig service");
    }



    protected String lookup(String plate) {
        String returnValue = null;
        try {
            in = new BufferedReader(new FileReader("plates-record.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            while((returnValue = in.readLine()) != null){
                String[] splited = returnValue.split(" ");
                System.out.println("line:" + splited[0].getBytes().length + "||splited:" + plate.getBytes().length + "||");

                if( ( plate.equals(splited[0])) ){
                    System.out.println("found it");
                    String full_name = "";
                    for (int i = 1; i < splited.length; i++) {
                        full_name += splited[i];
                    }
                    return full_name;
                }else{
                    moreQuotes = false;
                    returnValue = "No more quotes. Goodbye.";
                    return returnValue;
                }
            }
        } catch (IOException e) {
            returnValue = "IOException occurred in server.";
        }
        return returnValue;
    }
}
