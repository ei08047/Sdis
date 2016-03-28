package messages;

/**
 * Created by zabrn on 19/03/2016.
 */
public class GetChunk extends Header {

    protected int chunkNo;

    public GetChunk(String version, int senderID, String fileId, int chunkNo) {
        super(version, senderID, fileId);
        this.type = "GETCHUNK";
        this.chunkNo = chunkNo;
    }

    @Override
    public String getHeader() {
        return type + ws + version + ws + senderID + ws + fileID + ws + chunkNo + ws + CRLF + CRLF;
    }
}
