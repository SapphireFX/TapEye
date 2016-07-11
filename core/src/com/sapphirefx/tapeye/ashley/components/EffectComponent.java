package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.sapphirefx.tapeye.tools.GameParameters;

/**
 * Created by sapphire on 19.01.2016.
 */
public class EffectComponent implements Component
{
    public float maxTime;
    public float lifeTime;
    public GameParameters.EFFECT nameEffect;

    public EffectComponent(float lifeTime)
    {
        this.lifeTime = lifeTime;
        this.maxTime = lifeTime;
    }
}
