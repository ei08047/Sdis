package fileManager;

import peer.Peer;

import java.io.*;

/**
 * Created by José on 02/04/2016.
 */
public class Record implements Serializable{
    String fileId;
    int senderId;
    int chunkNo;
    int replication;
    String path = Peer.databasePath  ;

    public Record(String file ,int sender ,int cNo ){
        fileId = file;
        senderId = sender;
        chunkNo = cNo;
    }
    public void print() {
        System.out.println("RECORD: "+senderId+" | " + chunkNo);
    }

    public boolean equals(Record r){
        if(this.fileId.equals(r.getFileId()) && this.senderId==r.getSenderId() && this.chunkNo==r.getChunkNo()) {
            return true;
        }else{
            return false;
        }
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public void setChunkNo(int chunkNo) {
        this.chunkNo = chunkNo;
    }


}
