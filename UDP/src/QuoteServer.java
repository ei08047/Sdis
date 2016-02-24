/**
 * Created by Zé on 24/02/2016.
 */
import java.io.*;

public class QuoteServer {
    public static void main(String[] args) throws IOException {
        new QuoteServerThread().start();
    }
}
