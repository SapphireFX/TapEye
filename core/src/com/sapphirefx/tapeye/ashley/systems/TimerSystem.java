package com.sapphirefx.tapeye.ashley.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sapphirefx.tapeye.ashley.components.TimerComponent;

/**
 * Created by sapphire on 31.05.2016.
 */
public class TimerSystem extends IteratingSystem
{
    public TimerSystem()
    {
        super(Family.all(TimerComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        TimerComponent timerComponent = entity.getComponent(TimerComponent.class);
        timerComponent.timer -= deltaTime;
    }
}
