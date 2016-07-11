package com.sapphirefx.tapeye.ashley.factory.prototypes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sapphirefx.tapeye.ashley.components.PhysicBodyComponent;

/**
 * Created by sapphire on 19.10.15.
 */
public class PrototypePhysics
{
    public int bodyType = 0;

	public float mass;
	public Vector2 centerOfMass;
	public float rotationalInertia;
	public float damping;
	public float gravityScale;
	public boolean allowSleep;
	public boolean awake;
	public boolean bullet;

    public float density;
    public float friction;
    public float restitution;

    public PrototypePhysics()
    {
        centerOfMass = new Vector2();
    }

    public PrototypePhysics(PrototypePhysics obj)
    {
        bodyType = obj.bodyType;
    	mass = obj.mass;
    	centerOfMass = obj.centerOfMass.cpy();
    	rotationalInertia = obj.rotationalInertia;
    	damping = obj.damping;
    	gravityScale = obj.gravityScale;
    	allowSleep = obj.allowSleep;
    	awake = obj.awake;
    	bullet = obj.bullet;
        density = obj.density;
        friction = obj.friction;
        restitution = obj.restitution;
    }

    public void loadFromComponent(PhysicBodyComponent physicBodyComponent)
    {
        Body tmpBody = physicBodyComponent.body;
        bodyType = tmpBody.getType().getValue();
    	mass = tmpBody.getMass();
    	centerOfMass = tmpBody.getLocalCenter();
    	rotationalInertia = tmpBody.getInertia();
    	damping = tmpBody.getLinearDamping();
    	gravityScale = tmpBody.getGravityScale();
    	allowSleep = tmpBody.isSleepingAllowed();
    	awake = tmpBody.isAwake();
    	bullet = tmpBody.isBullet();
        density = tmpBody.getFixtureList().get(0).getDensity();
        friction = tmpBody.getFixtureList().get(0).getFriction();
        restitution = tmpBody.getFixtureList().get(0).getRestitution();
    }
}
