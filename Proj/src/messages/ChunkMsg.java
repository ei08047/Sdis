package messages;

/**
 * Created by zabrn on 19/03/2016.
 */
public class ChunkMsg extends GetChunkMsg {

    protected byte[] body;

    public ChunkMsg(String version, int senderID, String fileId, int chunkNo, byte[] body) {
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