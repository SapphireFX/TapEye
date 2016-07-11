package com.sapphirefx.tapeye.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.EnemyComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;

/**
 * Created by sapphire on 29.12.2015.
 */
public class SaveSystem extends IntervalSystem
{
    private Family family;
    private ImmutableArray<Entity> entities;

    public SaveSystem(float interval)
    {
        super(interval);
        family = Family.all(EnemyComponent.class).get();
    }

    @Override
    public void addedToEngine (Engine engine)
    {
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void removedFromEngine (Engine engine)
    {
        entities = null;
    }
    @Override
    protected void updateInterval()
    {
        Entity[] cpy = new Entity[entities.size()];

        for (int i=0; i<cpy.length; i++)
        {
            Entity tmp = new Entity();

            tmp.add(entities.get(i).getComponent(TransformComponent.class));
            tmp.add(entities.get(i).getComponent(DimensionsComponent.class));
            //TODO save enemy !!!!
            //tmp.add(new EnemyComponent(entities.get(i).getComponent(EnemyComponent.class)));
            cpy[i] = tmp;
        }
        String lvl = "";
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        lvl = json.toJson(cpy);

        FileHandle file = Gdx.files.local("files/save_entity.json");
        file.writeString(lvl, false);
    }
}
