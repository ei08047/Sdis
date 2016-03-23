package messages;

/**
 * Created by zabrn on 19/03/2016.
 */
public class Stored extends GetChunk {


    public Stored(String version, String senderID, String fileId, int chunkNo) {
        super(version, senderID, fileId, chunkNo);
        this.type = "STORED";


    }

    @Override
    public String getHeader() {
        return type + ws + version + ws + senderID + ws + fileID + ws + chunkNo + ws + CRLF + CRLF;
    }

    public static void main(String []args)
    {

        String version = args[0];
        String senderID = args[1];
        String fileID = args[2];
        int chunkNo = Integer.parseInt(args[3]);

        Stored stored = new Stored(version,senderID,fileID,chunkNo);

        String header = stored.getHeader();

        ParseHeader parse = new ParseHeader();

        String[] fields = parse.parse(header);

        for(int i = 0; i<fields.length ; i++)
        {
            System.out.println("i: " +  fields[i]);
        }
        
    }
}
