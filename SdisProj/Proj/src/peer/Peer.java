package peer;

import chanels.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ei08047 on 15-03-2016.
 */
public class Peer {
    public static int id;

    public static String path = "data/";
    public static String[] directories;

    public static BackupChannel mdb;
    public static ControlChannel mc;
    public static RestoreChannel mdr;

    static String host_name;
    static int port_number;

    static InterfaceChannel interfaceChannel;

    public Peer(){
    }



    public static void main(String[] args) throws IOException {
        if(args.length < 1 )
            System.out.println("missing arguments : usage '<IP address>:<port number>' \n");
        else
        {
            retrieveDirectories();
            String argumnents []=  args[0].split(":");
            String patternHostName = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}";
            String patternPortNumber = "^[0-9]{4}$";
            Pattern pattern = Pattern.compile(patternHostName);
            Matcher matcher = pattern.matcher(argumnents[0]);
            if(matcher.matches()){
                host_name = args[0];
                pattern = Pattern.compile(patternPortNumber);
                matcher = pattern.matcher(argumnents[1]);

                if(matcher.matches()) {
                    port_number = Integer.parseInt(argumnents[1]);


                    interfaceChannel = new InterfaceChannel(port_number);
                    interfaceChannel.start();

                    mdr = new RestoreChannel("239.254.254.5", 8883);
                    mdb = new BackupChannel("239.254.254.4", 8884);
                    mc = new ControlChannel("239.254.254.3", 8885);
                    mdr.start();
                    mdb.start();
                    mc.start();


                }else{
                    System.out.println("error: <port_number>");
                }
            }else{
                System.out.println("error: <host_name>");
            }
        }
    }


    public static void retrieveDirectories(){
        File file = new File(path);
        directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        System.out.println(Arrays.toString(directories));
    }



    public static String getFileId(String file){
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


    public static void split(String filename) throws FileNotFoundException {
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

    public static boolean checkIfDirExists(String fileId){
        boolean ret = false;
        for (int i = 0; i < directories.length ; i++) {
            if(fileId.equals(directories[i]))
                ret = true;
        }
        return ret;
    }


    public static void saveChunk(String fileId, int chunkNo, byte[] body){
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

}
