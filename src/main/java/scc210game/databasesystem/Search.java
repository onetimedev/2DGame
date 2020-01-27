package scc210game.databasesystem;

public class Search{

    public int position = -1;
    public boolean isFound = false;
    public String data = DatabaseOperation.EMPTY_KEY;

    private String keyId;
    private String[] keys;

    public Search(String keyId, String[] keys) {
        this.keyId = keyId;
        this.keys = keys;
        search();
    }


    private void search()
    {
        int start = 0;
        int end = keys.length-1;
        while(start <= end){
            int index = start + (end - start) /2;

            String[] currentData = keys[index].split("=");

            int result = keyId.compareTo(currentData[0]);

            if(result == 0)
            {
                isFound = true;
                position = index;
                data = currentData.length == 2 ? currentData[1] : "null";
            }

            if(result > 0)
            {
                start = index + 1;
            }
            else
                {
                end = index -1;
            }


        }

    }

}
