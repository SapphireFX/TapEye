package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by sapphire on 19.10.15.
 */
public class ParentNodeComponent implements Component
{
    public Entity parentEntity = null;

    @Override
    public String toString()
    {
        return "[parentEntity="+parentEntity+"]";
    }
}
