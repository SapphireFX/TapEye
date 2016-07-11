package com.sapphirefx.tapeye.renderer;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sapphirefx.tapeye.ashley.components.ColorComponent;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.LabelComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;

/**
 * Created by Sapphire on 09.11.2015.
 */
public class LabelDrawableLogic implements Drawable
{
    private ComponentMapper<LabelComponent> labelComponentMapper;
    private ComponentMapper<ColorComponent> colorComponentMapper;
    private ComponentMapper<DimensionsComponent> dimensionsComponentMapper;
    private ComponentMapper<TransformComponent> transformMapper;

    private final Color tmpColor = new Color();

    public LabelDrawableLogic()
    {
        labelComponentMapper = ComponentMapper.getFor(LabelComponent.class);
        colorComponentMapper = ComponentMapper.getFor(ColorComponent.class);
        dimensionsComponentMapper = ComponentMapper.getFor(DimensionsComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void draw(Batch batch, Entity entity, float parentAlpha)
    {
        TransformComponent entityTransformComponent = transformMapper.get(entity);
        LabelComponent labelComponent = labelComponentMapper.get(entity);
        DimensionsComponent dimenstionsComponent = dimensionsComponentMapper.get(entity);
        ColorComponent tint = colorComponentMapper.get(entity);

        tmpColor.set(tint.color);

        if (labelComponent.style.background != null) {
            batch.setColor(tmpColor);
            labelComponent.style.background.draw(batch, entityTransformComponent.x, entityTransformComponent.y, dimenstionsComponent.width, dimenstionsComponent.height);
            //System.out.println("LAbel BG");
        }

        if(labelComponent.style.fontColor != null) tmpColor.mul(labelComponent.style.fontColor);
        //tmpColor.a *= TODO consider parent alpha

        labelComponent.cache.tint(tmpColor);
        labelComponent.cache.setPosition(entityTransformComponent.x, entityTransformComponent.y);
        labelComponent.cache.draw(batch);
    }
}
