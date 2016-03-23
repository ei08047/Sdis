package chunks;

/**
 * Created by ei08047 on 22-03-2016.
 */
public class Chunk {
    //chunk size??
    String fileId;
    String senderId;
    int chunkNo;
    byte[] content;

    public Chunk(String file ,String sender ,int cNo ){
        fileId = file;
        senderId = sender;
        chunkNo = cNo;
    }

    public void setContent(byte[] c ){
        content = c;
    }

    public boolean isSameChunk(Chunk k){
        if(k.chunkNo == chunkNo){
            if(k.fileId.equals(fileId)){
                if(k.senderId.equals(senderId))
                    return true;
            }
        }
        return false;
    }





}
