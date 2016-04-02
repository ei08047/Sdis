package fileManager;

import peer.Peer;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by José on 02/04/2016.
 */
public class Database implements Serializable{
    public ArrayList<Record> db = new ArrayList<>() ;
    String path = Peer.databasePath  ;

    public Database(){};

    public boolean exists(Record temp){
        boolean t=false;
        for (int i = 0; i < db.size(); i++) {
           t= temp.equals(db.get(i));
             if(t){
                return t;
             }
        }
        return t;
    }

    public void addRecord(Record r){
        db.add(r);
        r.print();
    }

     public ArrayList<Record> load() throws FileNotFoundException, ClassNotFoundException {

         Record e;
         ArrayList<Record> db = null;
        //String path = basePath + new String(fileId)+ "/" + chunkNo;

         File i = new File(this.path);
         if(! i.exists()){
             System.out.println("nao existe");
         }
         else {
             try {
                 int records = i.listFiles().length;
                 //possivel que seja para mudar para records>1 ....
                 if(records>0){
                     for (int j = 0; j < records; j++) {
                         FileInputStream fileIn = new FileInputStream(this.path + "rec" + j + ".ser");
                         ObjectInputStream in = new ObjectInputStream(fileIn);
                         Record r = (Record) in.readObject();
                         this.db.add(r);
                         in.close();
                         fileIn.close();
                     }
                 }

             } catch (IOException e1) {
                 e1.printStackTrace();
                 return null;
             }
             return db;
         }
         return db;
    }




}
