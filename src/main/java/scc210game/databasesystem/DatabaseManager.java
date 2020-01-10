package scc210game.databasesystem;

import java.io.*;
import java.util.*;

public class DatabaseManager
{
    private String DBNAME; //name of database (name of directory)
    private File databasePath; //path to the various files within the database directory


    /**
     * basic constructor which builds the database and requires a filename with a .txt extension
     */
    public DatabaseManager(String dbName)
    {
        this.DBNAME = dbName;
        this.databasePath = new File(DBNAME);
        build();
    }

    /**
     * secondary constructor which allows user to add an array of keys with null values for later use
     * @param keysToCreate array of keys to create
     */
    public DatabaseManager(String dbName, String[] keysToCreate)
    {
        this.DBNAME = dbName;
        this.databasePath =  new File(DBNAME);
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
     * This method checks to see if there is a database that exists with the name provided in the constructor
     * if there is not it will create a new database with the given name and create all the inner structure
     * for the db to operate.
     */
    private void build()
    {
        File directory = new File(DBNAME);
        if(!directory.exists())
        {
            if(directory.mkdir()) //creates a new directory
            {

                for (String file : Constants.filenames)
                {
                    try
                    {
                        String path = DBNAME + "/" + file + ".txt";
                        File fileData = new File(path);
                        FileWriter writer = new FileWriter(fileData.getAbsoluteFile()); //file writer is a class used to create simple character files
                        BufferedWriter bWriter = new BufferedWriter(writer);
                        bWriter.write("");
                        bWriter.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }

            }
            else
            {
                System.out.println("Error: error creating the directory '" + DBNAME + "'");
            }

        }


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
     * This method gets the content of a given key provided as an argument. It checks
     * the reference file to see if that key exists, then takes the first letter of the keyId
     * and matches it to the file its content is stored in, then a method called getPositionInFile() is called
     * and this is run on a separate thread beacuse it goes through the reference file to locate the positionId
     * associated with the keyId and then that id is used as in index on the given file that the key content is stored
     * in.
     * @param keyId id provided by the user to find the associated content
     * @return the keyData
     */

    public String getKey(String keyId)
    {
        String keyData = "";
        String keys = openSegment("keys").toString();

        if(keys.contains(keyId))
        {

            String fileName = String.valueOf(keyId.charAt(0));
            String[] file = openSegment(fileName).toString().split(Constants.DELIMITER);
            String indexData = getPositionInFile(Constants.GET_KEY_POSITION, keys.split(Constants.DELIMITER), keyId);
            if(!indexData.equals("null"))
            {
                int index = Integer.parseInt(indexData);
                keyData = file[index].replace(keyId + "=", "");
            }
            else
            {
                System.out.println("Error: key '" + keyId + "' does not exist in the database, please use addKey() to add any new data");
            }

        }
        else
        {
            System.out.println("Error: key '" + keyId + "' does not exist in the database");
        }
        return keyData;
    }


    /**
     * this method passes the array of reference keys and the keyId being searched for
     * to the mutli-threaded class which will execute on a separate thread so processing will
     * be faster.
     * @param opcode operation code depending on whether a key's position is being searched for or a key's reference is being searched for
     * @param keys
     * @param keyId
     * @return
     */
    private String getPositionInFile(int opcode, String[] keys, String keyId)
    {
        DataProcessor dp = new DataProcessor(keys, keyId);
        ProcessManager pm = new ProcessManager(dp, opcode);
        pm.run();
        return pm.getResult();
    }


    /**
     * this method adds a new key to the database, it first checks if the key exists in the reference file, structures the
     * data then appends a new reference to the ref file and a new key to the relevant file depending on the what the first letter of the
     * keyId is.
     * @param keyId keyId used to find the key
     * @param keyData content of the key to be stored
     */
    public void addKey(String keyId, String keyData)
    {
        String keys = openSegment("keys").toString();

        if(!keys.contains(keyId))
        {
            String segmentName = String.valueOf(keyId.charAt(0));
            String data = format(keyId, keyData);
            String refData = format(keyId, createId(segmentName));
            alterFile(Constants.APPEND_OPERATION, data, segmentName);
            alterFile(Constants.APPEND_OPERATION, refData, "keys");
        }
        else
        {
            System.out.println("Error: key '" + keyId + "' already exists please use updateKey() to alter any existing data");
        }
    }


    /**
     * this method performs two types of operations on existing keys, it can update or delete an existing key.
     * @param opcode operation code for update or delete
     * @param keyId keyId of the key to perform the operation on
     * @param replacementKeyData replacement data if the operation is update
     */
    private void keyOperation(int opcode, String keyId, String replacementKeyData){
        String keys = openSegment("keys").toString();

        if(keys.contains(keyId))
        {
            String segmentName = String.valueOf(keyId.charAt(0));
            String[] file = openSegment(segmentName).toString().split(Constants.DELIMITER);
            String indexData = getPositionInFile(Constants.GET_KEY_POSITION, keys.split(Constants.DELIMITER), keyId);
            if(!indexData.equals("null"))
            {
                int index = Integer.parseInt(indexData);
                ArrayList<String> sData2 = new ArrayList<String>(Arrays.asList(file));

                switch (opcode) {
                    case Constants.UPDATE_OPERATION: //update action
                        String newData = keyId + "=" + replacementKeyData;
                        sData2.remove(index);
                        sData2.add(index, newData);
                        break;
                    case Constants.DELETE_OPERATION: //delete action

                        String[] refFile = openSegment("keys").toString().split(Constants.DELIMITER);
                        ArrayList<String> structuredRefFile = new ArrayList<String>(Arrays.asList(refFile));

                        String refIndexData = getPositionInFile(Constants.GET_KEY_REF_POSITION, refFile, keyId);
                        if(!refIndexData.equals("null")) {
                            int refIndex = Integer.parseInt(refIndexData);
                            structuredRefFile.remove(refIndex);
                            String newRefFile = String.join(Constants.DELIMITER, structuredRefFile) + Constants.DELIMITER;
                            alterFile(Constants.REPLACE_OPERATION, newRefFile, "keys");
                            sData2.remove(index);
                        }
                        else
                        {
                            System.out.println("Error: key '" + keyId + "' does not exist in the database, please use addKey() to add any new data");
                        }
                        break;

                }

                String newFile = String.join(Constants.DELIMITER, sData2) + Constants.DELIMITER;
                alterFile(Constants.REPLACE_OPERATION, newFile, segmentName);


            }
            else
            {
                System.out.println("Error: key '" + keyId + "' does not exist in the database, please use addKey() to add any new data");
            }
        }
        else
        {
            System.out.println("Error: key '" + keyId + "' does not exist in the database, please use addKey() to add any new data");
        }
    }


    /**
     * public method the user will call to perform an update operation on a key
     * @param keyId name of the key
     * @param replacementKeyData replacement data for the key
     */
    public void updateKey(String keyId, String replacementKeyData)
    {
        keyOperation(Constants.UPDATE_OPERATION, keyId, replacementKeyData);
    }

    /**
     * public method the user will call to perform a delete operation on an existing key
     * @param keyId name of the key
     */
    public void deleteKey(String keyId)
    {

        keyOperation(Constants.DELETE_OPERATION, keyId, null);

    }

    /**
     * this method creates an id for a key to be stored in the ref file and it is basically
     * the position of the key in the file that it is stored in so when the file is converted
     * to an array or ArrayList then the key is easily found beacuse this id is used to index the
     * data
     * @param segment name of file e.g. A.txt, B.txt
     * @return returns the position to be allocated the id in the ref file for a given key
     */
    private String createId(String segment)
    {
        String data = openSegment(segment).toString();
        if(data.contains(Constants.DELIMITER))
        {
            String[] file = openSegment(segment).toString().split(Constants.DELIMITER);
            return String.valueOf(file.length);
        }
        else
        {
            return "0";
        }
    }


    /**
     * this method opens a segment of the database and provides it as a StringBuilder
     * @param segment name of file
     * @return returns the file contents in a StringBuilder structure
     */
    private StringBuilder openSegment(String segment)
    {

        StringBuilder file = new StringBuilder();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(new File(DBNAME + "/" + segment + ".txt")));
            String data;
            while((data = reader.readLine()) != null)
            {
                file.append(data);
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * this method alters a given file and has two operations, append and replace
     * @param opcode the code to signify the operation to perform
     * @param data the data to append or replace
     * @param filename the filename of the file to open and change
     */
    private void alterFile(int opcode, String data, String filename)
    {

        try
        {
            String path = DBNAME + "/" + filename + ".txt";
            File fileData = new File(path);
            FileWriter writer;
            BufferedWriter buffer;
            switch (opcode)
            {
                case Constants.APPEND_OPERATION://append action

                    writer = new FileWriter(fileData.getAbsoluteFile(), true);
                    buffer = new BufferedWriter(writer);
                    buffer.append(data);
                    buffer.close();

                    break;
                case Constants.REPLACE_OPERATION: //replace action

                    writer = new FileWriter(fileData.getAbsoluteFile());
                    buffer = new BufferedWriter(writer);
                    buffer.write(data);
                    buffer.close();

                    break;
            }


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    /**
     * this simply formats keys so they are inline with the databases structure
     * @param keyId id of the key
     * @param keyData content of the key
     * @return formatted key data
     */
    private String format(String keyId, String keyData)
    {
        return keyId + "=" + keyData + Constants.DELIMITER;
    }



    public void clearDatabase()
    {
        String[] fileNames = {"keys", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        for(String file : fileNames)
        {
            try
            {
                String path = DBNAME + "/" + file + ".txt";
                File fileData = new File(path);
                FileWriter writer = new FileWriter(fileData.getAbsoluteFile());
                BufferedWriter bWriter = new BufferedWriter(writer);
                bWriter.write("");
                bWriter.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
