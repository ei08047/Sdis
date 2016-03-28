package messages;

/**
 * Created by zabrn on 19/03/2016.
 */
public class PutChunkMsg extends Chunk {

    private int replicationDeg;


    public PutChunkMsg(String version, int senderID, String fileId, int chunkNo, String body, int replicationDeg) {
        super(version, senderID, fileId, chunkNo, body);
        this.type = "PUTCHUNK";
        this.replicationDeg = replicationDeg;


    }

    @Override
    public String getHeader() {
        // ws = " "
        return type + ws + version + ws + senderID + ws + fileID + ws + chunkNo + ws + replicationDeg + ws + CRLF + CRLF + body;
    }
}
