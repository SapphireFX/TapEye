package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by sapphire on 19.10.15.
 */
public class ColorComponent implements Component
{
    public Color color = new Color();

    @Override
    public String toString()
    {
        return "Color=" + color;
    }
}
