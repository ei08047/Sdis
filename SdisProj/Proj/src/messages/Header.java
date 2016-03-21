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

    protected String type;
    protected String version;
    protected String senderID;
    protected String fileID;
    protected String CRLF  = "\n\r";
    protected String ws = " ";

    public Header(String version, String senderID, String fileID) {
        this.version = version;
        this.senderID = senderID;
        this.fileID = fileID;
    }

   abstract public String getHeader();


}
