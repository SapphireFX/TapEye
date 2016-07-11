package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/**
 * Created by sapphire on 19.10.15.
 */
public class ParticleComponent implements Component
{
    public String particleName = "";
	public ParticleEffect particleEffect;
	public float worldMultiplyer = 1f;

	@Override
	public String toString()
	{
		return "[particleName="+particleName+"][particleEffect="+particleEffect+"][worldMultiplyer="+worldMultiplyer+"]";
	}


}
