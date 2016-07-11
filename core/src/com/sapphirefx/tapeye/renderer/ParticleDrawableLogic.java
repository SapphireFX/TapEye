package com.sapphirefx.tapeye.renderer;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.sapphirefx.tapeye.ashley.components.ParticleComponent;

/**
 * Created by sapphire on 19.10.15.
 */
public class ParticleDrawableLogic implements Drawable
{
    private ComponentMapper<ParticleComponent> particleMapper;

    public ParticleDrawableLogic()
    {
        particleMapper = ComponentMapper.getFor(ParticleComponent.class);
    }

    @Override
    public void draw(Batch batch, Entity entity, float parentAlpha)
    {
        ParticleComponent entityParticleComponent = particleMapper.get(entity);
        Matrix4 matrix4 = batch.getTransformMatrix().scl(entityParticleComponent.worldMultiplyer);
        batch.setTransformMatrix(matrix4);
        entityParticleComponent.particleEffect.draw(batch);
        batch.setTransformMatrix(batch.getTransformMatrix().scl(1f/entityParticleComponent.worldMultiplyer));
    }
}
