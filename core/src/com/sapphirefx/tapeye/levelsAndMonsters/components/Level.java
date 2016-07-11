package com.sapphirefx.tapeye.levelsAndMonsters.components;

import java.util.List;

/**
 * Created by sapphire on 09.07.2016.
 */
public class Level
{
    public int idLevel;
    public int scoreLimit;
    public List<Monster> monsterList;

    public Level(int idLevel, int scoreLimit, List<Monster> monsterList)
    {
        this.idLevel = idLevel;
        this.scoreLimit = scoreLimit;
        this.monsterList = monsterList;
    }
}
