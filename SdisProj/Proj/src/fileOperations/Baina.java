package fileOperations;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ei08047 on 21-03-2016.
 */
public class Baina {
    String filename;
    long size;
    long chunk_max_size = 64000;
    byte[] fileId;

    public Baina(String file){
        fileId = new byte[32];
        File f = new File("data/" + file );
        size = f.length();
        int noChunks = (int) (size / chunk_max_size);
        System.out.println("noChunks: " + noChunks);
        filename = file;


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

        new File("data/" + result).mkdir(); // create Dir

        System.out.println("result: " + result);
        if(result.equals("3c13748756fc439b10c7a00ad1799f87860b030147ee8a2a6b8808f7edb0c320")){
            System.out.println("works");
        }else
        {
            System.out.println("fuck!");
        }

        ///test save chunk
        String ze = "zeee";
        saveChunk(result,0, ze.getBytes());
    }


    public void saveChunk(String fileId, int chunkNo, byte[] body){
        File z = new File("data/"+fileId);
        if(!z.exists()){
            new File("data/" + fileId).mkdir(); // create Dir
        }

        byte data[] = body;
        Path file = Paths.get("data/" + fileId + chunkNo);
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
