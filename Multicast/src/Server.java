
import java.io.IOException;


public class Server {



    public static void main(String[] args) throws IOException {

        if(args.length < 3){
            System.err.println("Incorrect number of arguments! \n  usage : Server <srvc_port> <mcast_addr> <mcast_port> ");
        }
        else{
            new ServerThread(args).start();
            new PlateService(args).start();
        }



    }

}
