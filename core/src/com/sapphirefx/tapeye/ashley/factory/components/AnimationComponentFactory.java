package com.sapphirefx.tapeye.ashley.factory.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sapphirefx.tapeye.ashley.components.animation.SpriteAnimationComponent;
import com.sapphirefx.tapeye.ashley.components.animation.SpriteAnimationStateComponent;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.TextureComponent;
import com.sapphirefx.tapeye.ashley.factory.ComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.EntityFactory;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeAnimationObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeObject;
import com.sapphirefx.tapeye.ashley.tools.FrameRange;
import com.sapphirefx.tapeye.resources.IResourceRetriever;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.tools.ProjectInfo;
import com.sapphirefx.tapeye.tools.ResolutionEntry;

import java.util.HashMap;

import box2dLight.RayHandler;

/**
 * Created by sapphire on 19.10.15.
 */
public class AnimationComponentFactory extends ComponentFactory
{
    public AnimationComponentFactory(IResourceRetriever rm)
    {
        super(rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, PrototypeObject po)
    {
        createCommonComponents(entity, po, EntityFactory.SPRITE_ANIMATION_TYPE);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
        createSpriteAnimationDataComponent(entity, (PrototypeAnimationObject) po);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, PrototypeObject po)
    {
        DimensionsComponent component = new DimensionsComponent();

        PrototypeAnimationObject sVo = (PrototypeAnimationObject) po;
        Array<TextureAtlas.AtlasRegion> regions = rm.getSpriteAnimation(sVo.animationName).getRegions();

        ResolutionEntry resolutionEntryVO = rm.getLoadedResolution();
        ProjectInfo projectInfoVO = rm.getProjectVO();
        float multiplier = resolutionEntryVO.getMultiplier(rm.getProjectVO().originalResolution);
        component.width = (float) regions.get(0).getRegionWidth() * multiplier / projectInfoVO.pixelToWorld;
        component.height = (float) regions.get(0).getRegionHeight() * multiplier / projectInfoVO.pixelToWorld;

        entity.add(component);
        return component;
    }

    protected SpriteAnimationComponent createSpriteAnimationDataComponent(Entity entity, PrototypeAnimationObject ao)
    {
        SpriteAnimationComponent spriteSpriteAnimationComponent = new SpriteAnimationComponent();
        spriteSpriteAnimationComponent.animationName = ao.animationName;

        spriteSpriteAnimationComponent.frameRangeMap = new HashMap<String, FrameRange>();
        for(int i = 0; i < ao.frameRangeMap.size(); i++)
        {
            spriteSpriteAnimationComponent.frameRangeMap.put(ao.frameRangeMap.get(i).name, ao.frameRangeMap.get(i));
        }
        spriteSpriteAnimationComponent.fps = ao.fps;
        spriteSpriteAnimationComponent.currentAnimation = ao.currentAnimation;

        if(ao.playMode == 0) spriteSpriteAnimationComponent.playMode = Animation.PlayMode.NORMAL;
        if(ao.playMode == 1) spriteSpriteAnimationComponent.playMode = Animation.PlayMode.REVERSED;
        if(ao.playMode == 2) spriteSpriteAnimationComponent.playMode = Animation.PlayMode.LOOP;
        if(ao.playMode == 3) spriteSpriteAnimationComponent.playMode = Animation.PlayMode.LOOP_REVERSED;
        if(ao.playMode == 4) spriteSpriteAnimationComponent.playMode = Animation.PlayMode.LOOP_PINGPONG;
        if(ao.playMode == 5) spriteSpriteAnimationComponent.playMode = Animation.PlayMode.LOOP_RANDOM;
        if(ao.playMode == 6) spriteSpriteAnimationComponent.playMode = Animation.PlayMode.NORMAL;

        Array<TextureAtlas.AtlasRegion> regions = rm.getSpriteAnimation(spriteSpriteAnimationComponent.animationName).getRegions();

        SpriteAnimationComponent animationComponent = new SpriteAnimationComponent();
        SpriteAnimationStateComponent stateComponent = new SpriteAnimationStateComponent(regions);

        if(spriteSpriteAnimationComponent.frameRangeMap.isEmpty())
        {
            spriteSpriteAnimationComponent.frameRangeMap.put("Default", new FrameRange("Default", 0, regions.size-1));
        }
        if(spriteSpriteAnimationComponent.currentAnimation == null)
        {
            spriteSpriteAnimationComponent.currentAnimation = (String) spriteSpriteAnimationComponent.frameRangeMap.keySet().toArray()[0];
        }
        if(spriteSpriteAnimationComponent.playMode == null)
        {
            spriteSpriteAnimationComponent.playMode = Animation.PlayMode.LOOP;
        }

        stateComponent.set(spriteSpriteAnimationComponent);

        TextureComponent textureRegionComponent = new TextureComponent();
        textureRegionComponent.region = regions.get(0);

        entity.add(textureRegionComponent);
        entity.add(stateComponent);
        entity.add(animationComponent);
        entity.add(spriteSpriteAnimationComponent);

        return spriteSpriteAnimationComponent;
    }
}
