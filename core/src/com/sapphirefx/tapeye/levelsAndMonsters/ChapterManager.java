package com.sapphirefx.tapeye.levelsAndMonsters;


import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.sapphirefx.tapeye.levelsAndMonsters.components.Chapter;
import com.sapphirefx.tapeye.levelsAndMonsters.components.Level;
import com.sapphirefx.tapeye.levelsAndMonsters.components.Monster;
import com.sapphirefx.tapeye.resources.ResourceManager;
import com.sapphirefx.tapeye.tools.GameParameters;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sapphire on 07.06.2016.
 */
public class ChapterManager
{
    private static ChapterManager instance = new ChapterManager();

    private Chapter selectedChapter;
    private List<Chapter> chapterList;

    public static ChapterManager getInstance()
    {
        if (instance == null) instance = new ChapterManager();
        return instance;
    }

    public Level getSelectedLevel()
    {
        return selectedChapter.selectedLevel;
    }

    private ChapterManager()
    {
        chapterList = new ArrayList<>();
        loadAllChapters();
        selectedChapter = chapterList.get(0);
        if(selectedChapter.selectedLevel == null) selectedChapter.selectedLevel = selectedChapter.levelList.get(0);
    }

    public Level nextLVL()
    {
        Level level;
        level = selectedChapter.nextLevelInChapter();
        if (level == null)
        {
            int index = chapterList.indexOf(selectedChapter);
            if (index < chapterList.size())
            {
                selectedChapter = chapterList.get(++index);
            } else return null;
            level = selectedChapter.nextLevelInChapter();
        }
        ResourceManager.monsterController.createEnemyes();
        return level;
    }

    private void addChapter(Chapter chapter)
    {
        int place = 0;
        for (int i = 0; i < chapterList.size(); i++)
        {
            if(chapter.idChapter > chapterList.get(i).idChapter) place = i;
        }
        chapterList.add(place, chapter);
    }

    private void loadAllChapters()
    {
        String jsonData = readFile("./files/chapters.json");
        System.out.println("out " + jsonData);
        JSONObject jobj = new JSONObject(jsonData);
        JSONArray jarr = new JSONArray(jobj.getJSONArray("chapterList").toString());
        for(int i = 0; i < jarr.length(); i++)
        {
            int idChapter = jarr.getJSONObject(i).getInt("idChapter");
            String nameChapter = jarr.getJSONObject(i).getString("name");
            JSONArray levelJarr = jarr.getJSONObject(i).getJSONArray("levelList");
            List<Level> levelList = new ArrayList<Level>();
            Chapter chapter = new Chapter(idChapter, nameChapter, levelList);
            for (int l = 0; l<levelJarr.length(); l++)
            {
                int idLevel = levelJarr.getJSONObject(l).getInt("idLevel");
                int scoreLimit = levelJarr.getJSONObject(l).getInt("scoreForNext");
                JSONArray monsterJarr = levelJarr.getJSONObject(l).getJSONArray("monsterList");
                List<Monster> monsterList = new ArrayList<Monster>();
                for (int m=0; m<monsterJarr.length(); m++)
                {
                    String type = monsterJarr.getJSONObject(m).getString("type");
                    float lifeTime = (float)monsterJarr.getJSONObject(m).getDouble("lifeTime");
                    float coolDown = (float)monsterJarr.getJSONObject(m).getDouble("coolDown");
                    int maxCount = monsterJarr.getJSONObject(m).getInt("maxCount");
                    monsterList.add(new Monster(type, lifeTime, coolDown, maxCount));
                }
                chapter.addLevel(new Level(idLevel, scoreLimit, monsterList));
            }
            addChapter(chapter);
        }
    }

    private String readFile(String filename)
    {
        String result = "";

        try {
            FileHandle fileHandle = Gdx.files.internal(filename);
            System.out.println(fileHandle.exists());
            BufferedReader br = new BufferedReader(fileHandle.reader());
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null)
            {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
