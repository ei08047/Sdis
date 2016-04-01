package fileManager;

import chunks.Chunk;

import java.io.*;

/**
 * Created by zabrn on 29/03/2016.
 */
public class FileManager {

    static private String basePath = "..//Proj//files//";


    public FileManager() throws IOException {
        if(!fileExists(""))
        {
            createDirectory("");
        }
    }

    public static boolean fileExists(String path)
    {

        return (new File(path)).exists();
    }



    public static String getPath(String fileID)
    {
        String path = basePath + fileID;
        return path;
    }

    public static void deleteDirectory(String fileID)
    {
        String path = getPath(fileID);
        if(fileExists(path))
        {
            File file = new File(path);
            file.delete();
        }
        else
            System.out.println("--SDirectorio não existe");

    }


    public static void createDirectory(String fileID) throws IOException
    {

        String path = getPath(fileID);

        if(!fileExists(path))
        {
            System.out.println("Ficheiro não existe!");
            new File(path).mkdir();
        }
        else
            System.out.println("--Directorio já existente!");
    }

    public static void saveChunk(Chunk chunk) throws IOException {
        chunk.save();
    }
    
    public static Chunk loadChunk(String fileId,int chunkNo) throws FileNotFoundException, ClassNotFoundException
    {
        return Chunk.load(fileId,chunkNo);
    }





}
