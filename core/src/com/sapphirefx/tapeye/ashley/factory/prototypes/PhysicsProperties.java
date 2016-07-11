package com.sapphirefx.tapeye.ashley.factory.prototypes;

/**
 * Created by sapphire on 19.10.15.
 */
public class PhysicsProperties
{
    public float gravityX;
    public float gravityY;
    public float sleepVelocity;
    public boolean enabled;

    public PhysicsProperties()
    {
        gravityX = 0;
        gravityY = 0;
        sleepVelocity = 0;
        enabled = false;
    }

    public PhysicsProperties(PhysicsProperties physicsPropertiesVO)
    {
        this.gravityX = physicsPropertiesVO.gravityX;
        this.gravityY = physicsPropertiesVO.gravityY;
        this.sleepVelocity = physicsPropertiesVO.sleepVelocity;
        this.enabled = physicsPropertiesVO.enabled;
    }
}
