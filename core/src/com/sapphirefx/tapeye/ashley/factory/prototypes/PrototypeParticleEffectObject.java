package com.sapphirefx.tapeye.ashley.factory.prototypes;

import com.badlogic.ashley.core.Entity;
import com.sapphirefx.tapeye.ashley.components.ParticleComponent;

/**
 * Created by sapphire on 19.10.15.
 */
public class PrototypeParticleEffectObject extends PrototypeObject
{
    public String particleName = "";
	public float particleWidth = 100;
	public float particleHeight = 100;
	//TODO add other ParticleEffect properties

    public PrototypeParticleEffectObject()
    {
        super();
    }

    public PrototypeParticleEffectObject(PrototypeParticleEffectObject peo)
    {
        super(peo);
        particleName = new String(peo.particleName);
    }

    @Override
    public void loadFromEntity(Entity entity)
    {
        super.loadFromEntity(entity);

        ParticleComponent particleComponent = entity.getComponent(ParticleComponent.class);
		particleName = particleComponent.particleName;
    }
}
