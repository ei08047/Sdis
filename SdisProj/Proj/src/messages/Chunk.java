package messages;

/**
 * Created by zabrn on 19/03/2016.
 */
public class Chunk extends GetChunk {

    protected String body;

    public Chunk(String version, String senderID, String fileId, int chunkNo, String body) {
        super(version, senderID, fileId, chunkNo);
        this.type = "CHUNK";
        this.body = body;

    }

    @Override
    public String getHeader() {
        // ws = " ";
        return type + ws + version + ws + senderID + ws + fileID + ws + chunkNo + ws + CRLF + CRLF + body;
    }
}