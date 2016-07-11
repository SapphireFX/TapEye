package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by sapphire on 19.10.15.
 */
public class ViewPortComponent implements Component
{
    public Viewport viewPort;

    @Override
    public String toString()
    {
        return "[viewPort="+viewPort+"]";
    }
}
