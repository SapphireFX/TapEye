package com.sapphirefx.tapeye.tools;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by sapphire on 20.10.15.
 */
public class ActorBodyLoader
{
    private static ActorBodyLoader ourInstance = new ActorBodyLoader();

    public static ActorBodyLoader getInstance()
    {
        if(ourInstance == null)
        {
            ourInstance = new ActorBodyLoader();
        }
        return ourInstance;
    }

    private ActorBodyLoader()
    {
    }

    public Actor createActor(float width, float height)
    {
        Actor actor = new Actor();
        actor.setSize(width, height);
        return actor;
    }
}