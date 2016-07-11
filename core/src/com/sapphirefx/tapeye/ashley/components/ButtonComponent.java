package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sapphirefx.tapeye.tools.GameParameters.Buttons;

/**
 * Created by sapphire on 18.12.2015.
 */
public class ButtonComponent implements Component
{
    public Actor actor = null;
    public boolean addedInStage = false;
    public Buttons typeButton = Buttons.NONE;
    public boolean isTouched = false;
}
