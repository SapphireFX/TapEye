package com.sapphirefx.tapeye.levelsAndMonsters.components;

import java.util.List;

/**
 * Created by sapphire on 09.07.2016.
 */
public class Chapter
{
    public int idChapter;
    public String name;
    public List<Level> levelList;
    public Level selectedLevel;

    public Chapter(int idChapter, String name, List<Level> levelList)
    {
        this.idChapter = idChapter;
        this.name = name;
        this.levelList = levelList;
    }

    public Level nextLevelInChapter()
    {
        int index = levelList.indexOf(selectedLevel);
        if (index < levelList.size())
        {
            index++;
            selectedLevel = levelList.get(index);
        } else selectedLevel = null;
        return selectedLevel;
    }

    public void addLevel(Level level)
    {
        int place = 0;
        for (int i = 0; i < levelList.size(); i++)
        {
            if(level.idLevel > levelList.get(i).idLevel) place = i;
        }
        levelList.add(place, level);
    }
}