package com.sapphirefx.tapeye.levelsAndMonsters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.sapphirefx.tapeye.ashley.components.PlaceEnemyRespawnComponent;
import com.sapphirefx.tapeye.levelsAndMonsters.components.Level;
import com.sapphirefx.tapeye.levelsAndMonsters.components.Monster;
import com.sapphirefx.tapeye.resources.ResourceManager;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.screens.ScreenManager;
import com.sapphirefx.tapeye.tools.GameParameters;

import java.util.List;
import java.util.Random;

/**
 * Created by sapphire on 24.01.2016.
 */
public class MonsterController
{
    public boolean needUpdateMonsters = false;

    public MonsterParameters goodEyeManager;
    public MonsterParameters evilEyeManager;
    public MonsterParameters goodFlyManager;
    public MonsterParameters evilFlyManager;
    public MonsterParameters catManager;
    public MonsterParameters spiderManager;

    private int currentLvl = -100;

    public MonsterController()
    {
        goodEyeManager = new MonsterParameters(GameParameters.ENEMY.GOODEYE);
        evilEyeManager = new MonsterParameters(GameParameters.ENEMY.EVILEYE);
        goodFlyManager = new MonsterParameters(GameParameters.ENEMY.GOODFLY);
        evilFlyManager = new MonsterParameters(GameParameters.ENEMY.EVILFLY);
        catManager = new MonsterParameters(GameParameters.ENEMY.CAT);
        spiderManager = new MonsterParameters(GameParameters.ENEMY.SPIDER);
    }

    public MonsterParameters getMonster(GameParameters.ENEMY type)
    {
        MonsterParameters result = null;
        switch (type)
        {
            case EVILEYE: result = evilEyeManager;
                break;
            case GOODEYE: result = goodEyeManager;
                break;
            case EVILFLY: result = evilFlyManager;
                break;
            case GOODFLY: result = goodFlyManager;
                break;
            case CAT: result = catManager;
                break;
            case SPIDER: result = spiderManager;
                break;
            default:
                System.out.println("WARNING !!!! Difficult.class hideOne(type) cannot find some type of enemy");
        }
        return result;
    }

    public void resetAll()
    {
        goodEyeManager.reset();
        evilEyeManager.reset();
        goodFlyManager.reset();
        evilFlyManager.reset();
        catManager.reset();
        spiderManager.reset();
    }

    public void setMonsterParameters(List<Monster> monsters)
    {
        for (int i=0; i < monsters.size(); i++)
        {
            switch (monsters.get(i).type)
            {
                case GOODEYE:
                    goodEyeManager.setParameters(monsters.get(i));
                    break;
                case EVILEYE:
                    evilEyeManager.setParameters(monsters.get(i));
                    break;
                case GOODFLY:
                    goodFlyManager.setParameters(monsters.get(i));
                    break;
                case EVILFLY:
                    evilFlyManager.setParameters(monsters.get(i));
                    break;
                case CAT:
                    catManager.setParameters(monsters.get(i));
                    break;
                case SPIDER:
                    spiderManager.setParameters(monsters.get(i));
                    break;
            }
        }
    }

    public void tick2(float delta)
    {
        goodEyeManager.tick(delta);
        evilEyeManager.tick(delta);
        goodFlyManager.tick(delta);
        evilFlyManager.tick(delta);
        catManager.tick(delta);
        spiderManager.tick(delta);

        GameParameters.logging = "eye1[" + goodEyeManager.showAvailibleAndBlocked() + "]";
        GameParameters.logging += " eye2[" + evilEyeManager.showAvailibleAndBlocked() + "]";
        GameParameters.logging += " fly1[" + goodFlyManager.showAvailibleAndBlocked() + "]";
        GameParameters.logging += " fly2[" + evilFlyManager.showAvailibleAndBlocked() + "]";
        GameParameters.logging += " cat[" + catManager.showAvailibleAndBlocked() + "]";
        GameParameters.logging += " spider[" + spiderManager.showAvailibleAndBlocked() + "]";
        Level level = ResourceManager.chapterManager.getSelectedLevel();
        if (GameParameters.score > level.scoreLimit)
        {
            needUpdateMonsters = true;
            ResourceManager.chapterManager.nextLVL();
            setMonsterParameters(ResourceManager.chapterManager.getSelectedLevel().monsterList);
        }
    }

    public void tick(float delta)
    {
        goodEyeManager.tick(delta);
        evilEyeManager.tick(delta);
        goodFlyManager.tick(delta);
        evilFlyManager.tick(delta);
        catManager.tick(delta);
        spiderManager.tick(delta);

        GameParameters.logging = "eye1[" + goodEyeManager.showAvailibleAndBlocked() + "]";
        GameParameters.logging += " eye2[" + evilEyeManager.showAvailibleAndBlocked() + "]";
        GameParameters.logging += " fly1[" + goodFlyManager.showAvailibleAndBlocked() + "]";
        GameParameters.logging += " fly2[" + evilFlyManager.showAvailibleAndBlocked() + "]";
        GameParameters.logging += " cat[" + catManager.showAvailibleAndBlocked() + "]";
        GameParameters.logging += " spider[" + spiderManager.showAvailibleAndBlocked() + "]";

        if(GameParameters.score < 5)
        {// 1 lvl
            if (currentLvl == 1) return;
            currentLvl = 1;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if(GameParameters.score >= 100)
        {
            if(currentLvl == -1) return;
            currentLvl = -1;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(1.5f, 0.2f, 6);
            evilEyeManager.setParameters(1.5f, 0.2f, 6);
            ScreenManager.requestLoadScene = "boss1";
        }
        /*else if(GameParameters.score >= 5 && GameParameters.score < 10)
        {// 2 lvl
            if (currentLvl == 2) return;
            currentLvl = 2;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if (GameParameters.score >= 10 && GameParameters.score < 50)
        {// 3 lvl
            if (currentLvl == 3) return;
            currentLvl = 3;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if (GameParameters.score >= 50 && GameParameters.score < 100)
        {// 4 lvl
            if (currentLvl == 4) return;
            currentLvl = 4;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if (GameParameters.score >= 100 && GameParameters.score < 200)
        {// 5 lvl
            if (currentLvl == 5) return;
            currentLvl = 5;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if (GameParameters.score >= 200 && GameParameters.score < 350)
        {// 6 lvl
            if (currentLvl == 6) return;
            currentLvl = 6;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if (GameParameters.score >= 350 && GameParameters.score < 500)
        {// 7 lvl
            if (currentLvl == 7) return;
            currentLvl = 7;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if (GameParameters.score >= 500 && GameParameters.score < 800)
        {// 8 lvl
            if (currentLvl == 8) return;
            currentLvl = 8;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if (GameParameters.score >= 800 && GameParameters.score < 1100)
        {// 9 lvl
            if (currentLvl == 9) return;
            currentLvl = 9;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if (GameParameters.score >= 1100 && GameParameters.score < 1400)
        {// 10 lvl
            if (currentLvl == 10) return;
            currentLvl = 10;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if (GameParameters.score >= 1400 && GameParameters.score < 1750)
        {// 11 lvl
            if (currentLvl == 11) return;
            currentLvl = 11;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if (GameParameters.score >= 1750 && GameParameters.score < 2100)
        {// 12 lvl
            if (currentLvl == 12) return;
            currentLvl = 12;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if (GameParameters.score >= 2100 && GameParameters.score < 2500)
        {// 13 lvl
            if (currentLvl == 13) return;
            currentLvl = 13;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);
        }else if (GameParameters.score >= 2500)
        {// 14 lvl
            if (currentLvl == 14) return;
            currentLvl = 14;
            needUpdateMonsters = true;
            goodEyeManager.setParameters(3, 0.2f, 6);
            evilEyeManager.setParameters(3f, 3, 2);
            goodFlyManager.setParameters(6f, 3, 2);
            evilFlyManager.setParameters(4f, 3, 2);
            catManager.setParameters(1f, 3, 1);
            spiderManager.setParameters(6f, 3, 1);

        }*/
    }

    public void hideOne(GameParameters.ENEMY type)
    {
        switch (type)
        {
            case EVILEYE: evilEyeManager.hideOne();
                break;
            case GOODEYE: goodEyeManager.hideOne();
                break;
            case EVILFLY: evilFlyManager.hideOne();
                break;
            case GOODFLY: goodFlyManager.hideOne();
                break;
            case CAT: catManager.hideOne();
                break;
            case SPIDER: spiderManager.hideOne();
                break;
            default:
                System.out.println("WARNING !!!! Difficult class hideOne(type) cannot find some type of enemy");
        }
    }

    public void createEnemyes()
    {
        // создание всех врагов
        ImmutableArray<Entity> allEntities = SceneLoader.engine.getEntities();
        for (int i = 0; i < allEntities.size(); i++)
        {
            PlaceEnemyRespawnComponent respawnComponent = allEntities.get(i).getComponent(PlaceEnemyRespawnComponent.class);
            if (respawnComponent != null && respawnComponent.enemyes.size()==0)
            {
                SceneLoader.engine.addEntity(SceneLoader.entityFactory.createEye(GameParameters.ENEMY.GOODEYE, allEntities.get(i)));
                SceneLoader.engine.addEntity(SceneLoader.entityFactory.createEye(GameParameters.ENEMY.EVILEYE, allEntities.get(i)));
            }
        }

        // не надо создавать монстров на менюшке настройках и т.п.
        String sceneName = SceneLoader.getSceneName();
        if (!sceneName.equals("menu") && !sceneName.equals("settings") && !sceneName.equals("stats") && !sceneName.equals("levels"))
        {
            SceneLoader.engine.addEntity(SceneLoader.entityFactory.createGameDesk(SceneLoader.getResolution().width, (SceneLoader.getResolution().height)));
            List<Monster> monsters = ResourceManager.chapterManager.getSelectedLevel().monsterList;
            for (int i = 0; i < monsters.size(); i++)
            {
                if (monsters.get(i).type != GameParameters.ENEMY.GOODEYE || monsters.get(i).type != GameParameters.ENEMY.EVILEYE)
                    continue;
                int count = monsters.get(i).maxCount;
                System.out.println("monsters on this lvl " + count);
                while (count > 0)
                {
                    System.out.println("create some enemy " + monsters.get(i).type);
                    createMonsters(monsters.get(i).type, monsters.get(i).maxCount);
                    count--;
                }
            }
            ResourceManager.monsterController.setMonsterParameters(ResourceManager.chapterManager.getSelectedLevel().monsterList);
        }
    }

    private void createMonsters(GameParameters.ENEMY type, int amount)
    {
        int added = amount - getMonster(type).getMaxCount();
        while (added > 0)
        {
            SceneLoader.engine.addEntity(SceneLoader.entityFactory.createEnemy(type));
            added--;
        }
    }
}
