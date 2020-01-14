package scc210game.databasesystem;

class SearchEngine {


    private Search s;
    public SearchEngine(String keyId, String[] keys)
    {
        s = new Search(keyId, keys);
    }


    public int findKeyPosition()
    {

        return s.position;

    }

    public String findKeyData()
    {
        return s.data;
    }

    public boolean keyExists()
    {

        return s.isFound;
    }




}
