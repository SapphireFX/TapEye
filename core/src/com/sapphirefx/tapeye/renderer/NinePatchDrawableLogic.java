package com.sapphirefx.tapeye.renderer;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sapphirefx.tapeye.ashley.components.ColorComponent;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.NinePatchComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;

/**
 * Created by sapphire on 19.10.15.
 */
public class NinePatchDrawableLogic implements Drawable
{
    private ComponentMapper<ColorComponent> tintMapper;
    private ComponentMapper<NinePatchComponent> ninePatchMapper;
    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<DimensionsComponent> dimensionMapper;

    public NinePatchDrawableLogic()
    {
        tintMapper = ComponentMapper.getFor(ColorComponent.class);
        ninePatchMapper = ComponentMapper.getFor(NinePatchComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        dimensionMapper = ComponentMapper.getFor(DimensionsComponent.class);
    }

    @Override
    public void draw(Batch batch, Entity entity, float parentAlpha)
    {
        ColorComponent colorComponent = tintMapper.get(entity);
        NinePatchComponent entityNinePatchComponent = ninePatchMapper.get(entity);
        TransformComponent entityTransformComponent = transformMapper.get(entity);
        DimensionsComponent entityDimensionsComponent = dimensionMapper.get(entity);
        batch.setColor(colorComponent.color);

        entityNinePatchComponent.ninePatch.draw(batch,
                entityTransformComponent.x, entityTransformComponent.y,
                entityDimensionsComponent.width, entityDimensionsComponent.height);

    }
}
