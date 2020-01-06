package scc210game.databasesystem;

import com.sun.jdi.ArrayReference;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DatabaseManager
{
    private String FILEPATH;
    private File databasePath; //filepath for database text file


    /**
     * basic constructor which builds the database and requires a filename with a .txt extension
     */
    public DatabaseManager(String filepath)
    {
        this.FILEPATH = filepath;
        this.databasePath = new File(FILEPATH);
        build();
    }

    /**
     * secondary constructor which allows user to add an array of keys with null values for later use
     * @param keysToCreate array of keys to create
     */
    public DatabaseManager(String filepath, String[] keysToCreate)
    {
        this.FILEPATH = filepath;
        this.databasePath =  new File(FILEPATH);
        build();
        createMultipleKeys(keysToCreate);
    }


    /**
     * creates all the keys passed to the method
     * @param keys array of keys to create
     */
    public void createMultipleKeys(String[] keys){

        for(String key : keys)
        {
            addKey(key, "");
        }
    }

    /**
     * checks if the instance of File() called database path exists in the filesystem.
     * If nothing is found it will create a new text file called database.txt
     */
    private void build()
    {
        if(!databasePath.exists())
        {

            Path dbPath = Paths.get(FILEPATH);
            try
            {
                Files.write(dbPath, Arrays.asList(), StandardCharsets.UTF_8);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * adds a new key to the database. checks if the keyId and the keyData are different and also checks to see
     * if that key already exists or not, if it does then the user must use the updateKey() method to alter the value.
     * @param keyId the id for example 'score'
     * @param keyData the contents of the key for example '124'
     */
    public void addKey(String keyId, String keyData)
    {
        if(!keyId.equals(keyData))
        {
            if(!database().contains(keyId))
            {
                String nd = keyId + "=" + keyData + "-" + database(); //uses a '-' as a regex string to ensure structure
                clearAndRepopulate(nd);
            }
            else
                {
                System.out.println("The key " + keyId + " already exists in database. Use updateKey() to change an existing value");
            }
        }
        else
            {
            System.out.println("keyID cannot be the same as keyData");
        }
    }

    /**
     * gets the value of the key using the keyId provided as an argument
     * @param key keyId used to locate the contents
     * @return the contents of the key as a string
     */
    public String getKey(String key)
    {
        String keyData = "";
        if(databasePath.exists())
        {

            String[] fullFile = database().split("-");
            for(String entry : fullFile)
            {
                if(entry.contains(key + "="))
                {
                    String value = entry.replaceAll(key + "=", "");
                    keyData = (value.equals("")) ? "null" : value;
                }
            }

        }
        else
            {
            System.out.println("Cant Find Database");
        }

        return (keyData.equals("")) ? "Key not found" : keyData;

    }


    /**
     * does the same as getKey() except returns value as integer, if value cannot be converted to an int
     * an exception is raised
     * @param key keyId used to locate key value
     * @return returns an integer value of the keyValue
     */
    public int getKeyAsInt(String key)
    {

        int value = 0;

        try
        {
            value = Integer.parseInt(getKey(key));
        }
        catch (Exception e)
        {
            System.out.println("Value of '" + key + "' cannot be converted to an integer, use getKey() to obtain String value");
        }

        return value;
    }


    /**
     * uses the updateKey() method to set the keyValue of the keyId provided to null
     * @param keyId
     */
    public void clearKey(String keyId)
    {

        updateKey(keyId, "");

    }


    /**
     * updates a keys current value to a new value. if no key is found will return an error message
     * @param keyId keyId used to name the key
     * @param replacementData replacement value
     */
    public void updateKey(String keyId, String replacementData)
    {
        boolean keyFound = false;
        String[] fileData = database().split("-");
        for(String key : fileData)
        {
            if(key.contains(keyId + "="))
            {
                keyFound = true;
                String nd = database().replace(key, keyId + "=" + replacementData);
                clearAndRepopulate(nd);
                break;

            }
        }

        if(!keyFound) System.out.println("Cant find key '" + keyId + "' to update");
    }

    /**
     * method that returns a string containing the whole value of the text file and then other method use search and replace to
     * perform operations on the database
     * @return string value of text file
     */
    private String database()
    {
        StringBuilder file = new StringBuilder();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(databasePath));
            String data;
            while((data = reader.readLine()) != null)
            {
                file.append(data);
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return file.toString();
    }


    /**
     * clears the text file and repopulates it with new structured data
     * @param data new value of the database
     */
    public void clearAndRepopulate(String data)
    {
        try
        {
            PrintWriter writer = new PrintWriter("database.txt");
            writer.println("");
            writer.print(data);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * deletes a given key from the database
     * @param key keyId
     */
    public void deleteKey(String key)
    {
        String[] fileData = database().split("-");

        for(String keyData : fileData)
        {
            if(keyData.contains(key + "="))
            {
                String newData = database().replace(keyData, "");
                clearAndRepopulate(newData);
            }
        }

    }

}
