package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.sapphirefx.tapeye.tools.GameParameters;

import java.util.HashMap;

/**
 * Created by Sapphire on 06.12.2015.
 */
public class PlaceEnemyRespawnComponent implements Component
{
    public float x;
    public float y;
    public boolean isBusy = false; // есть ли глаз в этом месте ?
    public HashMap<GameParameters.ENEMY, Entity> enemyes = new HashMap<GameParameters.ENEMY, Entity>();

    @Override
    public String toString()
    {
        return "[x="+x+"][y="+y+"][isBusy="+isBusy+"][Enemy.size="+enemyes.size()+"]";
    }
}
