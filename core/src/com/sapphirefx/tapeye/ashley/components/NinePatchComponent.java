package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.NinePatch;

/**
 * Created by sapphire on 19.10.15.
 */
public class NinePatchComponent implements Component
{
    public String textureRegionName;
    public NinePatch ninePatch;

    @Override
    public String toString()
    {
        return "[textureRegionName="+textureRegionName+"][ninePatch="+ninePatch+"]";
    }
}
