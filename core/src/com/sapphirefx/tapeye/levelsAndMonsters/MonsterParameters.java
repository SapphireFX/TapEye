package com.sapphirefx.tapeye.levelsAndMonsters;

import com.sapphirefx.tapeye.levelsAndMonsters.components.Monster;
import com.sapphirefx.tapeye.tools.GameParameters;
import java.util.Random;

/**
 * Created by sapphire on 19.06.2016.
 */
public class MonsterParameters
{
    private boolean availible;
    private float coolDown;
    private float lastShow = 0;
    private int maxCount;
    private int percentageShow = 2;
    private float lifeTime = 7;

    private GameParameters.ENEMY type;
    private int availibleCount;
    private int blockedCount;
    private Random random;

    public String showAvailibleAndBlocked()
    {
        return availibleCount + "/" + blockedCount;
    }

    public MonsterParameters(GameParameters.ENEMY type)
    {
        random = new Random((long)Math.random()*100000);

        this.type = type;
        coolDown = 2;
        maxCount = 0;
        availible = false;
        availibleCount = maxCount;
        blockedCount = 0;
    }

    public void reset()
    {
        availible = false;
        availibleCount = maxCount;
        blockedCount = 0;
    }

    /**
     *
     * @param lifeTime
     * @param coolDown
     * @param maxCount
     */
    public void setParameters(float lifeTime, float coolDown, int maxCount)
    {
        this.lifeTime = lifeTime;
        this.coolDown = coolDown;
        this.maxCount = maxCount;
        availibleCount = maxCount - blockedCount;
    }

    public void setParameters(Monster monster)
    {
        setParameters(monster.lifeTime, monster.coolDown, monster.maxCount);
    }

    public boolean tryShowOne()
    {
        if(availible)
        {
            if(random.nextInt(100) <= percentageShow)
            {
                availible = false;
                lastShow = 0;
                availibleCount--;
                blockedCount++;
                return true;
            }else return false;
        }else return false;
    }

    public void hideOne()
    {
        availibleCount++;
        blockedCount--;
    }

    public void tick(float delta)
    {
        if(availibleCount > 0)
        {
            lastShow += delta;
            if (lastShow > coolDown) availible = true;
        }else
        {
            availible = false;
        }
    }

    public boolean isAvailible()
    {
        return availible;
    }

    public float getLifeTime()
    {
        return lifeTime;
    }

    public GameParameters.ENEMY getType()
    {
        return type;
    }

    public int getMaxCount()
    {
        return maxCount;
    }
}