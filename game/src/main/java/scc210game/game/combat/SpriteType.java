package scc210game.game.combat;

public class SpriteType {

    private String name;
    private String textureLocation;
    private boolean enemy;
    private int enemyLevel;

    public SpriteType(String name, String textureLocation, boolean enemy, int enemyLevel)
    {
        this.name = name;
        this.textureLocation = textureLocation;
        this.enemy = enemy;
        this.enemyLevel = enemyLevel;
    }

    public String getName()
    {
        return this.name;
    }

    public String getTextureLocation()
    {
       return this.textureLocation;
    }

    public boolean getEnemyStatus()
    {
        return enemy;
    }

    public int getEnemyLevel()
    {
        return enemyLevel;
    }
}
