package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by sapphire on 31.05.2016.
 */
public class TimerComponent implements Component
{
    public float timer = 0;

    public TimerComponent(float timer)
    {
        this.timer = timer;
    }
}
