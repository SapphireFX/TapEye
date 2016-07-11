package com.sapphirefx.tapeye.ashley.factory.prototypes;

import com.badlogic.ashley.core.Entity;
import com.sapphirefx.tapeye.ashley.components.LightObjectComponent;

/**
 * Created by sapphire on 19.10.15.
 */
public class PrototypeLightObject extends PrototypeObject
{
    public static enum LightType {POINT, CONE}
	public LightType type;
	public int rays = 12;
	public float distance = 300;
	public float directionDegree = 0;
	public float coneDegree = 30;
	public float softnessLength = -1f;
	public boolean isStatic = true;
	public boolean isXRay = true;

    public PrototypeLightObject()
    {
        tint = new float[4];
        tint[0] = 1f;
		tint[1] = 1f;
		tint[2] = 1f;
		tint[3] = 1f;
    }

    public PrototypeLightObject(PrototypeLightObject lo)
    {
		super(lo);
		type = lo.type;
		rays = lo.rays;
		distance = lo.distance;
		directionDegree = lo.directionDegree;
		coneDegree = lo.coneDegree;
		isStatic = lo.isStatic;
		isXRay = lo.isXRay;
		softnessLength = lo.softnessLength;
	}

    @Override
    public void loadFromEntity(Entity entity)
    {
        super.loadFromEntity(entity);

        LightObjectComponent lightObjectComponent = entity.getComponent(LightObjectComponent.class);
		type = lightObjectComponent.getType();
		rays = lightObjectComponent.rays;
		distance = lightObjectComponent.distance;
		directionDegree = lightObjectComponent.directionDegree;
		coneDegree = lightObjectComponent.coneDegree;
		isStatic = lightObjectComponent.isStatic;
		isXRay = lightObjectComponent.isXRay;
		softnessLength = lightObjectComponent.softnessLength;
    }
}
