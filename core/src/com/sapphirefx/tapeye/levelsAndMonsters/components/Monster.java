package com.sapphirefx.tapeye.levelsAndMonsters.components;

import com.sapphirefx.tapeye.tools.GameParameters;

/**
 * Created by sapphire on 09.07.2016.
 */
public class Monster
{
    public GameParameters.ENEMY type;
    public float lifeTime;
    public float coolDown;
    public int maxCount;

    public Monster(String type, float lifeTime, float coolDown, int maxCount)
    {
        this.type = GameParameters.ENEMY.valueOf(type);
        this.lifeTime = lifeTime;
        this.coolDown = coolDown;
        this.maxCount = maxCount;
    }

    public void set(Monster monster)
    {
        this.type = monster.type;
        this.lifeTime = monster.lifeTime;
        this.coolDown = monster.coolDown;
        this.maxCount = monster.maxCount;
    }
}
