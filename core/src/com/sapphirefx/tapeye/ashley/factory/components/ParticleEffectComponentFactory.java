package com.sapphirefx.tapeye.ashley.factory.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.sapphirefx.tapeye.ashley.factory.ComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.EntityFactory;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeParticleEffectObject;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.ParticleComponent;
import com.sapphirefx.tapeye.resources.IResourceRetriever;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.tools.ProjectInfo;

import box2dLight.RayHandler;
/**
 * Created by sapphire on 19.10.15.
 */
public class ParticleEffectComponentFactory extends ComponentFactory
{
    public ParticleEffectComponentFactory(IResourceRetriever rm)
    {
        super(rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, PrototypeObject po)
    {
        createCommonComponents(entity, po, EntityFactory.PARTICLE_TYPE);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
        createParticleComponent(entity, (PrototypeParticleEffectObject) po);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, PrototypeObject po)
    {
        DimensionsComponent component = new DimensionsComponent();

        ProjectInfo projectInfoVO = rm.getProjectVO();
        float boundBoxSize = 100f;
        component.boundBox = new Rectangle((-boundBoxSize/2f)/projectInfoVO.pixelToWorld, (-boundBoxSize/2f)/projectInfoVO.pixelToWorld, boundBoxSize/projectInfoVO.pixelToWorld, boundBoxSize/projectInfoVO.pixelToWorld);

        entity.add(component);
        return component;
    }

    protected ParticleComponent createParticleComponent(Entity entity, PrototypeParticleEffectObject peo)
    {
        ParticleComponent component = new ParticleComponent();
        component.particleName = peo.particleName;
		ParticleEffect particleEffect = new ParticleEffect(rm.getParticleEffect(peo.particleName));
        component.particleEffect = particleEffect;

        ProjectInfo projectInfoVO = rm.getProjectVO();

        component.worldMultiplyer = 1f/projectInfoVO.pixelToWorld;

        entity.add(component);
        return component;
    }
}
