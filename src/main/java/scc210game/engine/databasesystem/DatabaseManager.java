package scc210game.engine.databasesystem;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DatabaseManager {

    private String DBNAME; //name of database (name of directory)
    private String dataFilePath;
    private String logFilePath;

    public DatabaseManager(String dbName)
    {
        this.DBNAME = dbName;
        this.dataFilePath = DBNAME + "-database/data.txt";
        this.logFilePath = DBNAME + "-database/logs.txt";
        build();
    }


    public DatabaseManager(String dbName, String[] keysToCreate)
    {
        this.DBNAME = dbName;
        build();
        createMultipleKeys(keysToCreate);
    }

    public void createMultipleKeys(String[] keys){

        for(String key : keys)
        {
            addKey(key, "");
        }
    }


    private void build()
    {
        File directory = new File(DBNAME + "-database");
        if(!directory.exists())
        {
            if(directory.mkdir()) //creates a new directory
            {
                LOG(DatabaseOperation.BUILDING_DATABASE, "", getTS());
                for (String file : DatabaseOperation.filenames)
                {
                    try
                    {
                        String path = DBNAME + "-database/" + file + ".txt";
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




    public String getKey(String keyId)
    {
        if(Keys().length > 0)
        {

            if(new SearchEngine(keyId, Keys()).keyExists())
            {
                String keyData = new SearchEngine(keyId, Keys()).findKeyData();
                if(!keyData.equals(DatabaseOperation.EMPTY_KEY))
                {
                    LOG(DatabaseOperation.PROVIDING_KEY_OPERATION, keyId, getTS());
                    return keyData;
                }
                else
                {
                    System.out.println("Key is empty");
                }
            }
            else
            {
                System.out.println("Key '" + keyId + "' does not exist");
                LOG(DatabaseOperation.PROVIDING_KEY_ERROR, keyId, getTS());
            }

        }
        return DatabaseOperation.EMPTY_KEY;
    }



    public int getKeyAsInt(String keyId)
    {

        int value = 0;

        try
        {
            LOG(DatabaseOperation.PROVIDING_KEY_OPERATION, keyId, getTS());
            value = Integer.parseInt(getKey(keyId));
        }
        catch (Exception e)
        {
            LOG(DatabaseOperation.PROVIDING_KEY_ERROR, keyId, getTS());
            System.out.println("Value of '" + keyId + "' cannot be converted to an integer, use getKey() to obtain String value");
        }

        return value;
    }


    public void addKey(String keyId, String keyData)
    {
        if(Keys().length > 0)
        {
            if (!new SearchEngine(keyId, Keys()).keyExists())
            {

                LOG(DatabaseOperation.ADD_KEY_OPERATION, format(keyId, keyData).replace(DatabaseOperation.DELIMITER, ""), getTS());
                String newData = format(keyId, keyData);
                alterFile(DatabaseOperation.APPEND_OPERATION, newData, dataFilePath);

            }
            else
            {
                LOG(DatabaseOperation.ADD_KEY_ERROR, format(keyId, keyData).replace(DatabaseOperation.DELIMITER, ""), getTS());
                System.out.println("Error: key '" + keyId + "' already exists in the database, use updateKey() to alter its value");
            }
        }
        else
        {
            LOG(DatabaseOperation.ADD_KEY_OPERATION, format(keyId, keyData).replace(DatabaseOperation.DELIMITER, ""), getTS());
            String newData = format(keyId, keyData);
            alterFile(DatabaseOperation.APPEND_OPERATION, newData, dataFilePath);
        }


    }


    public void updateKey(String keyId, String replacementKeyData)
    {

        if(Keys().length > 0)
        {
            if(new SearchEngine(keyId, Keys()).keyExists())
            {

                LOG(DatabaseOperation.UPDATE_KEY_OPERATION, format(keyId, replacementKeyData).replace(DatabaseOperation.DELIMITER, ""), getTS());
                ArrayList<String> keysList = new ArrayList<String>(Arrays.asList(Keys()));

                int index = new SearchEngine(keyId, Keys()).findKeyPosition();

                keysList.remove(index);

                keysList.add(index, format(keyId, replacementKeyData).replace(DatabaseOperation.DELIMITER, ""));
                String newKeys = String.join(DatabaseOperation.DELIMITER, keysList);
                alterFile(DatabaseOperation.REPLACE_OPERATION, newKeys, dataFilePath);

            }
            else
            {
                LOG(DatabaseOperation.UPDATE_KEY_ERROR, format(keyId, replacementKeyData).replace(DatabaseOperation.DELIMITER, ""), getTS());
                System.out.println("Error: key '" + keyId + "' does not exists in the database, use addKey() to create it");
            }
        }
        else
        {
            LOG(DatabaseOperation.UPDATE_KEY_ERROR, format(keyId, replacementKeyData).replace(DatabaseOperation.DELIMITER, ""), getTS());
            System.out.println("Error: the database is empty, use addKey() to create a new key value pair");
        }


    }

    public void deleteKey(String keyId)
    {

        if(Keys().length > 0)
        {
            if(new SearchEngine(keyId, Keys()).keyExists())
            {

                LOG(DatabaseOperation.DELETE_KEY_OPERATION, keyId, getTS());
                ArrayList<String> keysList = new ArrayList<String>(Arrays.asList(Keys()));
                int index = new SearchEngine(keyId, Keys()).findKeyPosition();
                keysList.remove(index);
                String newKeys = String.join(DatabaseOperation.DELIMITER, keysList) + DatabaseOperation.DELIMITER;
                alterFile(DatabaseOperation.REPLACE_OPERATION, newKeys, dataFilePath);

            }
            else
            {
                LOG(DatabaseOperation.DELETE_KEY_ERROR, keyId, getTS());
                System.out.println("Error: key '" + keyId + "' does not exists in the database, use addKey() to create it");
            }
        }
        else
        {
            LOG(DatabaseOperation.DELETE_KEY_ERROR, keyId, getTS());
            System.out.println("Error: the database is empty, use addKey() to create a new key value pair");
        }

    }




    public List getAllKeys()
    {
        LOG(DatabaseOperation.COLLECT_ALL_KEYS, "All Keys", getTS());
        return Arrays.asList(Keys());

    }


    private String[] Keys()
    {

        StringBuilder file = new StringBuilder();
        try
        {
            File database = new File(DBNAME + "-database/data.txt");
            BufferedReader reader = new BufferedReader(new FileReader(database));
            String data;
            while((data = reader.readLine()) != null)
            {
                file.append(data);
            }
            reader.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }


        if(file.length() > 0)
        {
            if(file.toString().contains(DatabaseOperation.DELIMITER))
            {
                String[] keys = file.toString().split(DatabaseOperation.DELIMITER);
                Arrays.sort(keys);
                return keys;
            }
            else
            {
                return new String[0];
            }
        }
        else
        {
            return new String[0];
        }
    }



    public void clearKey(String keyId)
    {

        LOG(DatabaseOperation.CLEAR_KEY_OPERATION, keyId, getTS());
        updateKey(keyId, "");

    }


    private void alterFile(int opcode, String data, String file)
    {

        try
        {
            File fileData = new File(file);
            FileWriter writer;
            BufferedWriter buffer;
            switch (opcode)
            {
                case DatabaseOperation.APPEND_OPERATION://append action

                    writer = new FileWriter(fileData.getAbsoluteFile(), true);
                    buffer = new BufferedWriter(writer);
                    buffer.append(data);
                    buffer.close();

                    break;
                case DatabaseOperation.REPLACE_OPERATION: //replace action

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

    private String format(String keyId, String keyData)
    {
        return keyId + "=" + keyData + DatabaseOperation.DELIMITER;
    }


    public void wipe()
    {
        alterFile(DatabaseOperation.REPLACE_OPERATION, "", dataFilePath);
    }

    private void LOG(String operation, String keyId,  String timestamp)
    {

        StringBuilder log = new StringBuilder();
        log.append(operation).append(" '").append(keyId).append("' ").append("[").append(timestamp).append("]").append("\n");
        alterFile(DatabaseOperation.APPEND_OPERATION, log.toString(), logFilePath);


    }


    private String getTS()
    {
        Date date = new Date();
        return String.valueOf(new Timestamp(date.getTime()));

    }

}
