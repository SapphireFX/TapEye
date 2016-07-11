package com.sapphirefx.tapeye.ashley.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sapphirefx.tapeye.ashley.components.ShaderComponent;
import com.sapphirefx.tapeye.ashley.components.TimerComponent;

/**
 * Created by sapphire on 31.05.2016.
 */
public class ShaderSystem extends IteratingSystem
{
    public ShaderSystem()
    {
        super(Family.all(ShaderComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        ShaderComponent shaderComponent = entity.getComponent(ShaderComponent.class);
        if(shaderComponent != null)
        {
            // если этот компонент имеет таймер у себя и  таймер приблизился к нулю
            if(shaderComponent.timerComponent != null && shaderComponent.timerComponent.timer < 0)
            {
                // нужно удалить сам таймер и шейдер из entity
                entity.remove(ShaderComponent.class);
                entity.remove(TimerComponent.class);
                shaderComponent.timerComponent = null;
            }
        }
    }
}
