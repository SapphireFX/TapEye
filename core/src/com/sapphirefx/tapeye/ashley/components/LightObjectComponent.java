package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeLightObject.LightType;

import box2dLight.Light;

/**
 * Created by sapphire on 19.10.15.
 */
public class LightObjectComponent implements Component
{
    private LightType type;

	public LightObjectComponent(LightType type)
    {
		this.type = type;
	}

	public int rays = 12;
	public float distance = 300;
	public float directionDegree = 0;
	public float coneDegree = 30;
	public float softnessLength = 1f;
	public boolean isStatic = true;
	public boolean isXRay = true;
	public Light lightObject = null;

	public LightType getType()
    {
		return type;
	}

}
