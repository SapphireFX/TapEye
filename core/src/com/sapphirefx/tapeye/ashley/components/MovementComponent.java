package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by sapphire on 21.03.2016.
 */
public class MovementComponent implements Component
{
    Vector2 position;
    Vector2 direction;

    public MovementComponent(float posX, float posY)
    {
        position = new Vector2(posX, posY);
        direction = new Vector2( (float)Math.random()*50 - 25, (float)Math.random()*50 - 25 );
    }

    public Vector2 updatePosition(float delta)
    {
        return position.set(position.x + direction.x * delta, position.y + direction.y * delta);
    }
}
