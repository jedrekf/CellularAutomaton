package automaton.file;

import automaton.helper.InformBox;
import models.Grid;
import models.rules.RuleSet;

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
            try {
                obj = in.readObject();
            }catch (Exception e){
                InformBox.display("File Loader","File is invalid.");
            }
            in.close();
            fileIn.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return obj;
    }
    /**
     * Serializes the Grid to a file.
     * @param obj Object to be serialized.
     * @param path Relative Path to a file to serialize to Object to.
     */
    public static void saveGrid(Grid obj, String path){
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
    /**
     * Serializes the Rules to a file.
     * @param obj Object to be serialized.
     * @param path Relative Path to a file to serialize to Object to.
     */
    public static void saveRules(RuleSet obj, String path){
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
