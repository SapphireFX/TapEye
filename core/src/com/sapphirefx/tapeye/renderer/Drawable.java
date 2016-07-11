package com.sapphirefx.tapeye.renderer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by sapphire on 19.10.15.
 */
public interface Drawable
{
    public abstract void draw(Batch batch, Entity entity, float parentAlpha);
}
