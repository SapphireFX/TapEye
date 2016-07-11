package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by sapphire on 23.03.2016.
 */
public class MovementMenuComponent implements Component
{
    Vector2 position;
    Vector2 direction = new Vector2( 30, 0 );
    float time;
    private float timeDirection = 1;

    public MovementMenuComponent(float posX, float posY)
    {
        position = new Vector2(posX, posY);
        time = timeDirection;
    }

    public void changeDirection()
    {
        direction = direction.set( -direction.x, direction.y);
        time = timeDirection;
    }

    public Vector2 updatePosition(float delta)
    {
        if(delta > 0.1f) return position; // елси слишком большое время пришло, то просто дать старую позицию
        time -= delta;
        // время вышло и надо двигаться в противоположном направлении
        if (time<0)
        {
            changeDirection();
            time = timeDirection;
        }
        return position.set(position.x + direction.x * delta, position.y + direction.y * delta);
    }
}
