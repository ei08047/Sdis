package chunks;

import java.io.*;

/**
 * Created by ei08047 on 22-03-2016.
 */
public class Chunk implements Serializable {
    //chunk size??
    String fileId;
    String senderId;
    int chunkNo;
    byte[] content;
    static private String basePath = "../Proj/files/";


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

    public void save() throws IOException {

        String path = basePath + new String(fileId)+ "/" ;
        final File folder = new File(path);
        folder.mkdir();
        System.out.println(folder.getPath());

        try {
            FileOutputStream fileOut = new FileOutputStream(path  + chunkNo + ".ser");

            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in"  + path + chunkNo + ".ser");
        } catch (FileNotFoundException i) {
            i.printStackTrace();
        }
    }

    static public Chunk load(String fileId,int chunkNo) throws FileNotFoundException, ClassNotFoundException {
        Chunk e;
        String path = basePath + new String(fileId)+ "/" + chunkNo;
        try
        {
            //FileInputStream fileIn = new FileInputStream("../Desktop/temp/outgoing/" +new String(fileid)+"/"+chunkNo+".ser");
            FileInputStream fileIn = new FileInputStream(path+".ser");
            System.out.println(path+".ser");

            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (Chunk) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
        return e;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public void setChunkNo(int chunkNo) {
        this.chunkNo = chunkNo;
    }







}
