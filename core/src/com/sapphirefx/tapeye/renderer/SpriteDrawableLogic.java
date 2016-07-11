package com.sapphirefx.tapeye.renderer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sapphirefx.tapeye.ashley.components.EffectComponent;
import com.sapphirefx.tapeye.ashley.components.MainItemComponent;
import com.sapphirefx.tapeye.ashley.factory.EntityFactory;

/**
 * Created by sapphire on 19.10.15.
 */
public class SpriteDrawableLogic extends TexturRegionDrawLogic
{
    @Override
    public void draw(Batch batch, Entity entity, float parentAlpha)
    {
        super.draw(batch, entity, parentAlpha);
        //TODO in case we need specific things
    }
}
