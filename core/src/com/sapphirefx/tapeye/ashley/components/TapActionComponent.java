package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by sapphire on 06.02.2016.
 */
public class TapActionComponent implements Component
{
    public ClickListener action;

    public TapActionComponent(ClickListener action)
    {
        this.action = action;
    }
}
