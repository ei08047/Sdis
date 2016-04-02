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

    public void save() throws IOException {

        final File folder = new File(path);
        folder.mkdir();

        if(! folder.exists()){
            System.out.println("nao existe");
        }else{
            try {

                int records = folder.listFiles().length;
                if(records !=0){
                    records-=1;
                }

                FileOutputStream fileOut = new FileOutputStream(path  + "rec" + records + ".ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                ///avoid rewrite
                out.writeObject(this);
                out.close();
                fileOut.close();
                // System.out.printf("Serialized data is saved in"  + path + chunkNo + ".ser");
            } catch (FileNotFoundException i) {
                i.printStackTrace();
            }
        }


    }

}
