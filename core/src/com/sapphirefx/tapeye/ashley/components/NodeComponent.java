package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created by sapphire on 19.10.15.
 */
public class NodeComponent implements Component
{
    public SnapshotArray<Entity> children = new SnapshotArray<Entity>(true, 1, Entity.class);

	@Override
	public String toString()
    {
        String result="NodeChildren: \n";
        children.begin();
        for (int i=0; i<children.size; i++)
        {
            result += "\t" + children.get(i).toString()+"\n";
        }
		return result;
	}

	public void removeChild(Entity entity)
    {
		children.removeValue(entity, false);
	}

	public void addChild(Entity entity)
    {
		children.add(entity);
	}

}
