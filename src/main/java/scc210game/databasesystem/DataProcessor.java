package scc210game.databasesystem;

public class DataProcessor extends Thread {

    private String[] keys;
    private String keyId;

    public DataProcessor(String[] keys, String keyId)
    {
        this.keys = keys;
        this.keyId = keyId;
    }


    /**
     * gets the keys position in a given file
     * @return returns that position
     */
    public String getKeyPosition(){
        String result = "null";
        for(String key : keys)
        {
            if(key.contains(keyId + "="))
            {
                String data = key.replace(keyId + "=", "");
                result = data;
            }
        }
        return result;
    }


    /**
     * gets a keys position in the ref file
     * @return returns that position
     */
    public String getKeyRefPosition()
    {

        for(int key = 0; key < keys.length; key++)
        {
            if(keys[key].contains(keyId + "="))
            {
                return String.valueOf(key);
            }
        }
        return "null";
    }

}
