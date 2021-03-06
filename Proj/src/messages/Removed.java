package messages;

/**
 * Created by zabrn on 19/03/2016.
 */
public class Removed extends GetChunkMsg {

    public Removed(String version, int senderID, String fileId, int chunkNo) {
        super(version, senderID, fileId, chunkNo);
        this.type = "REMOVED";
    }

    @Override
    public String getHeader() {
        return type + ws + version + ws + senderID + ws + fileID + ws + chunkNo + ws + CRLF + CRLF;
    }
}
