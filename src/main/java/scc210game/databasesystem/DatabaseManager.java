package scc210game.databasesystem;

import javax.xml.crypto.Data;
import java.io.*;
import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseManager {

    private String DBNAME; //name of database (name of directory)


    public DatabaseManager(String dbName)
    {
        this.DBNAME = dbName;
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

                for (String file : DatabaseOperation.filenames)
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




    public String getKey(String keyId)
    {
        if(Keys().length > 0)
        {

            if(new SearchEngine(keyId, Keys()).keyExists())
            {
                String keyData = new SearchEngine(keyId, Keys()).findKeyData();
                if(!keyData.equals(DatabaseOperation.EMPTY_KEY))
                {
                    return keyData;
                }else{
                    System.out.println("Key is empty");
                }
            }
            else
            {
                System.out.println("Key '" + keyId + "' does not exist");
            }

        }
        return DatabaseOperation.EMPTY_KEY;
    }



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


    public void addKey(String keyId, String keyData)
    {
        if(Keys().length > 0)
        {
            if (!new SearchEngine(keyId, Keys()).keyExists())
            {

                String newData = format(keyId, keyData);
                alterFile(DatabaseOperation.APPEND_OPERATION, newData);

            }
        }
        else
        {
            String newData = format(keyId, keyData);
            alterFile(DatabaseOperation.APPEND_OPERATION, newData);
        }


    }


    public void updateKey(String keyId, String replacementKeyData)
    {

        if(Keys().length > 0)
        {
            if(new SearchEngine(keyId, Keys()).keyExists())
            {

                ArrayList<String> keysList = new ArrayList<String>(Arrays.asList(Keys()));

                int index = new SearchEngine(keyId, Keys()).findKeyPosition();

                keysList.remove(index);

                keysList.add(index, format(keyId, replacementKeyData).replace("-", ""));
                String newKeys = String.join("-", keysList);
                alterFile(DatabaseOperation.REPLACE_OPERATION, newKeys);

            }
            else
            {
                System.out.println("Error: key '" + keyId + "' does not exists in the database, use addKey() to create it");
            }
        }
        else
        {
            System.out.println("Error: the database is empty, use addKey() to create a new key value pair");
        }


    }

    public void deleteKey(String keyId)
    {

        if(Keys().length > 0)
        {
            if(new SearchEngine(keyId, Keys()).keyExists())
            {

                ArrayList<String> keysList = new ArrayList<String>(Arrays.asList(Keys()));
                int index = new SearchEngine(keyId, Keys()).findKeyPosition();
                keysList.remove(index);
                String newKeys = String.join("-", keysList) + "-";
                alterFile(DatabaseOperation.REPLACE_OPERATION, newKeys);

            }
            else
            {
                System.out.println("Error: key '" + keyId + "' does not exists in the database, use addKey() to create it");
            }
        }
        else
        {
            System.out.println("Error: the database is empty, use addKey() to create a new key value pair");
        }

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
            if(file.toString().contains("-"))
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

        updateKey(keyId, "");

    }


    private void alterFile(int opcode, String data)
    {

        try
        {
            String path = DBNAME + "-database/data.txt";
            File fileData = new File(path);
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
        alterFile(DatabaseOperation.REPLACE_OPERATION, "");
    }


}
