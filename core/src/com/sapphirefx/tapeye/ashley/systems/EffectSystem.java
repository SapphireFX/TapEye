package com.sapphirefx.tapeye.ashley.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.sapphirefx.tapeye.ashley.components.ColorComponent;
import com.sapphirefx.tapeye.ashley.components.EffectComponent;
import com.sapphirefx.tapeye.ashley.components.MovementComponent;
import com.sapphirefx.tapeye.ashley.components.NodeComponent;
import com.sapphirefx.tapeye.ashley.components.ParentNodeComponent;
import com.sapphirefx.tapeye.ashley.components.ShaderComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.tools.GameParameters;

/**
 * Created by sapphire on 23.01.2016.
 */
public class EffectSystem extends IteratingSystem
{
    public EffectSystem()
    {
        super(Family.all(EffectComponent.class).get());
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        if(GameParameters.isPaused) return;

        EffectComponent effectComponent = entity.getComponent(EffectComponent.class);
        if(effectComponent != null)
        {
            effectComponent.lifeTime -= deltaTime;
            if (entity.getComponent(MovementComponent.class) != null)
            {
                entity.getComponent(TransformComponent.class).setPosition(entity.getComponent(MovementComponent.class).updatePosition(deltaTime));
            }
            // линейное изменение прозрачости эффекта на ее уменьшение
            if(effectComponent.nameEffect == GameParameters.EFFECT.ONSHOWWINDOW)
            {
                Color color = entity.getComponent(ColorComponent.class).color;
                float alpha = effectComponent.lifeTime / effectComponent.maxTime;
                if(alpha < 0) alpha = 0;
                color.set(color.r, color.g, color.b, alpha);
            }

            if(effectComponent.lifeTime < 0)
            {
                if (GameParameters.onShowWindow && effectComponent.nameEffect == GameParameters.EFFECT.ONSHOWWINDOW)
                {
                    GameParameters.onShowWindow = false;
                }
                NodeComponent rootComponent = entity.getComponent(ParentNodeComponent.class).parentEntity.getComponent(NodeComponent.class);
                rootComponent.removeChild(entity);
                SceneLoader.engine.removeEntity(entity);

            }
        }
    }
}
