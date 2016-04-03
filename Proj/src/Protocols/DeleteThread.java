package Protocols;

import fileManager.FileManager;

/**
 * Created by zabrn on 03/04/2016.
 */
public class DeleteThread extends Thread {

    private String fileID;

    public DeleteThread(String fileID) {
        this.fileID = fileID;
    }


    public void run() {
        FileManager.deleteDirectory( fileID);
    }
}
