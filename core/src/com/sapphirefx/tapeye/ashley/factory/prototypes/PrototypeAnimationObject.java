package com.sapphirefx.tapeye.ashley.factory.prototypes;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.sapphirefx.tapeye.ashley.components.animation.SpriteAnimationComponent;
import com.sapphirefx.tapeye.ashley.tools.FrameRange;

import java.util.ArrayList;

/**
 * Created by sapphire on 19.10.15.
 */
public class PrototypeAnimationObject extends PrototypeObject
{
    public String animationName = "";
    public int fps = 24;
    public String currentAnimation;
    public ArrayList<FrameRange> frameRangeMap = new ArrayList<FrameRange>();
    public int playMode = 0;

    public PrototypeAnimationObject()
    {
        super();
    }

    public PrototypeAnimationObject(PrototypeAnimationObject ao)
    {
        super(ao);
        animationName = new String(ao.animationName);
        fps = ao.fps;
        currentAnimation = new String(ao.currentAnimation);
        frameRangeMap = ao.frameRangeMap;
        playMode = ao.playMode;

    }

    @Override
    public void loadFromEntity(Entity entity)
    {
        super.loadFromEntity(entity);

        SpriteAnimationComponent component = entity.getComponent(SpriteAnimationComponent.class);
        animationName = component.animationName;
        fps = component.fps;
        currentAnimation = component.currentAnimation;
        frameRangeMap = new ArrayList<FrameRange>();
        for(FrameRange fr: component.frameRangeMap.values())
        {
            frameRangeMap.add(fr);
        }
        if(component.playMode == Animation.PlayMode.NORMAL) playMode = 0;
        if(component.playMode == Animation.PlayMode.REVERSED) playMode = 1;
        if(component.playMode == Animation.PlayMode.LOOP) playMode = 2;
        if(component.playMode == Animation.PlayMode.LOOP_REVERSED) playMode = 3;
        if(component.playMode == Animation.PlayMode.LOOP_PINGPONG) playMode = 4;
        if(component.playMode == Animation.PlayMode.LOOP_RANDOM) playMode = 5;
        if(component.playMode == Animation.PlayMode.NORMAL) playMode = 6;
    }
}
