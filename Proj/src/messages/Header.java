package messages;

/**
 * Created by zabrn on 18/03/2016.
 *
 * <MessageType>
 This is the type of the message.
 <Version>
 This is the version of the protocol.
 <SenderId>
 This is the id of the server that has sent the message.
 <FileId>
 This is the file identifier for the backup service.
 <ChunkNo>
 This field together with the FileId specifies a chunk in the file.
 <ReplicationDeg>
 This field contains the desired replication degree of the chunk.
  generic - <MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF>
 */

public abstract class Header {



    //// TODO: 01-04-2016 refactor message format  
    //byte[] b = s.getBytes(StandardCharsets.US_ASCII);
    //This is encoded as a variable length sequence of ASCII characters.
    protected String type;
    // It is a three ASCII char sequence with the format <n>'.'<m>,
    // where <n> and <m> are the ASCII codes of digits.
    protected String version;
    //This is encoded as a variable length sequence of ASCII digits.
    protected int senderID;
    //64 ASCII character sequence
    protected String fileID;
    protected String CRLF  = "\n\r";
    protected String ws = " ";

    public Header(String version, int senderID, String fileID) {
        this.version = version;
        this.senderID = senderID;
        this.fileID = fileID;
    }

   abstract public String getHeader();


    public byte[] getBytes(){
        return getHeader().getBytes();
    }

}
