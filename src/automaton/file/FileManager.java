package automaton.file;

import automaton.AutomatonState;

import java.io.*;

/**
 * Class for serializing and deserializing objects to/from files.
 */
public class FileManager {
    /**
     * Deserializes Object from a file.
     * @param path Relative Path to a file containing the object.
     * @return Returns the deserialized object
     */
    public static Object read( String path ){
        Object obj = null;

        try
        {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            obj = in.readObject();
            in.close();
            fileIn.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return obj;
    }
    /**
     * Serializes the object to a file.
     * @param obj Object to be serialized.
     * @param path Relative Path to a file to serialize to Object to.
     */
    public static void save(AutomatonState obj, String path){
        try
        {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(obj);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in " + path);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
