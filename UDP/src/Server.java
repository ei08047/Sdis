import java.io.IOException;

/**
 * Created by Zé on 24/02/2016.
 */
public class Server {
    public static void main(String[] args) throws IOException {
        if(args.length != 1 )
            System.out.println("missing arguments : usage 'java Server <port_number>' \n");
        else
        {
            new ServerThread(args).start();
        }
    }
}
