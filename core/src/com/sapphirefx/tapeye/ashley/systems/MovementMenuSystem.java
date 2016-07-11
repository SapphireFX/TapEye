package com.sapphirefx.tapeye.ashley.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.sapphirefx.tapeye.ashley.components.ButtonComponent;
import com.sapphirefx.tapeye.ashley.components.MovementMenuComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;

/**
 * Created by sapphire on 23.03.2016.
 */
public class MovementMenuSystem extends IteratingSystem
{
    public MovementMenuSystem()
    {
        super(Family.all(MovementMenuComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        Vector2 newPosition = entity.getComponent(MovementMenuComponent.class).updatePosition(deltaTime);
        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
        transformComponent.setPosition(newPosition);
    }
}
