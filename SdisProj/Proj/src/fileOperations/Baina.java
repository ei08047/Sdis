package fileOperations;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by ei08047 on 21-03-2016.
 */
public class Baina {
    String path="data/";
    String[] directories;

    public void retrieveDirectories(){
        File file = new File(path);
        directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        System.out.println(Arrays.toString(directories));
    }


    public String getFileId(String file){
        File f = new File( path + file );
        String test = file + f.lastModified() ;
        MessageDigest digest = null;
        String result;
        try{
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(test.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            result = hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
        return result;
    } // given the filename.extension returns the fileId String


    public void split(String filename) throws FileNotFoundException {
        int partCounter = 1;
        int sizeOfFiles = 64000;
        byte[] buffer = new byte[sizeOfFiles];
        String fileId = getFileId(filename);
        new File("data/" + fileId).mkdir(); // create Dir
        File f = new File(path + filename);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
        int tmp = 0;
        try {
            while ((tmp = bis.read(buffer)) > 0) {
                //write each chunk of data into separate file with different number in name
                File newFile = new File("data/" + fileId,
                        String.format("%03d", partCounter++));
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, tmp);//tmp is chunk size
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


   public void saveChunk(String fileId, int chunkNo, byte[] body){
        File z = new File("data/"+fileId);
        if(!z.exists()){
            new File("data/" + fileId).mkdir(); // create Dir
        }

        byte data[] = body;
        Path file = Paths.get("data/" + fileId + "/" + chunkNo);
        try {
            Files.write(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    <FileId>
    This is the file identifier for the backup service.
    As stated above, it is supposed to be obtained by using the SHA256 cryptographic hash function.
    As its name indicates its length is 256 bit, i.e. 32 bytes, and should be encoded as a 64 ASCII character sequence.
     The encoding is as follows: each byte of the hash value is encoded by the two ASCII characters corresponding
     to the hexadecimal representation of that byte.
     E.g., a byte with value 0xB2 should be represented by the two char sequence 'B''2' (or 'b''2', it does not matter).
      The entire hash is represented in big-endian order, i.e. from the MSB (byte 31) to the LSB (byte 0).
    */
    //divide in chunks
    // restore a file from chunks
    //generate id
    //file size

}



