package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by sapphire on 19.10.15.
 */
public class TransformComponent implements Component
{
    public float x;
	public float y;
	public float scaleX	=	1f;
	public float scaleY	=	1f;
	public float originX;
	public float originY;
	public float rotation;

	TransformComponent backup = null;

	@Override
	public String toString()
	{
		return "[x="+x+"][y="+y+"][scaleX="+scaleX+"][scaleY="+scaleY+"][originX="+originX+"][originY="+originY+"][rotation="+rotation+"]";
	}

	public TransformComponent()
    {
    }

	public TransformComponent(TransformComponent component)
	{
		x = component.x;
		y = component.y;
		scaleX = component.scaleX;
		scaleY = component.scaleY;
		rotation = component.rotation;
		originX = component.originX;
		originY = component.originY;
	}

	public void disableTransform()
	{
		backup = new TransformComponent(this);
		x = 0;
		y = 0;
		scaleX = 1f;
		scaleY = 1f;
		rotation = 0;
	}

	public void enableTransform()
	{
		if(backup == null) return;
		x = backup.x;
		y = backup.y;
		scaleX = backup.scaleX;
		scaleY = backup.scaleY;
		rotation = backup.rotation;
		originX = backup.originX;
		originY = backup.originY;
		backup = null;
	}

	public void setPosition(Vector2 position)
	{
		x = position.x;
		y = position.y;
	}

	public void setPosition(float positionX, float positionY)
	{
		x = positionX;
		y = positionY;
	}
}
