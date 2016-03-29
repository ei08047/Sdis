package messages;

/**
 * Created by zabrn on 19/03/2016.
 */
public class DeleteMsg extends Header {


    public DeleteMsg(String version, int senderID, String fileID) {
        super(version, senderID, fileID);
        this.type = "DELETE";
    }

    @Override
    public String getHeader() {
        //ws = " "
        return type + ws + version + ws + senderID + ws + fileID + ws + CRLF + CRLF;
    }

}
