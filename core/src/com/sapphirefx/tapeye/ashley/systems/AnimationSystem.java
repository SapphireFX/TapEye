package com.sapphirefx.tapeye.ashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.SnapshotArray;
import com.sapphirefx.tapeye.ashley.components.MainItemComponent;
import com.sapphirefx.tapeye.ashley.components.NodeComponent;
import com.sapphirefx.tapeye.ashley.components.animation.SpriteAnimationComponent;
import com.sapphirefx.tapeye.ashley.components.animation.SpriteAnimationStateComponent;
import com.sapphirefx.tapeye.ashley.components.TextureComponent;

/**
 * Created by sapphire on 19.10.15.
 */
public class AnimationSystem extends IteratingSystem
{
    private ComponentMapper<SpriteAnimationComponent> sa;
    private ComponentMapper<TextureComponent> tm;
    private ComponentMapper<SpriteAnimationStateComponent> sm;

    public AnimationSystem()
    {
        super(Family.all(SpriteAnimationComponent.class, TextureComponent.class, SpriteAnimationStateComponent.class).get());
        sa = ComponentMapper.getFor(SpriteAnimationComponent.class);
        tm = ComponentMapper.getFor(TextureComponent.class);
        sm = ComponentMapper.getFor(SpriteAnimationStateComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        TextureComponent tex = tm.get(entity);
        SpriteAnimationStateComponent state = sm.get(entity);
        state.currentAnimation.setFrameDuration(1f/sa.get(entity).fps);
        tex.region = state.currentAnimation.getKeyFrame(state.time);

        /*
        if (sa.get(entity).animationName.equals("catBlue"))
        {
            tex.region = state.currentAnimation.getKeyFrames()[21];
            System.out.println(state.currentAnimation.getKeyFrameIndex(state.time));
        }
        */

        if(!state.paused)
        {
            state.time += deltaTime;
        }
    }
}
