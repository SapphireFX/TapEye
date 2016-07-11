package com.sapphirefx.tapeye.ashley.factory.prototypes;

import com.badlogic.ashley.core.Entity;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.NinePatchComponent;

/**
 * Created by sapphire on 19.10.15.
 */
public class Prototype9PatchObject extends PrototypeObject
{
    public String imageName = "";
    public float width = 0;
    public float height = 0;

    public Prototype9PatchObject()
    {
        super();
    }

    public Prototype9PatchObject(Prototype9PatchObject vo)
    {
        super(vo);
        imageName = new String(vo.imageName);
        width = vo.width;
        height = vo.height;
    }

    @Override
    public void loadFromEntity(Entity entity)
    {
        super.loadFromEntity(entity);

        NinePatchComponent ninePatchComponent = entity.getComponent(NinePatchComponent.class);
        DimensionsComponent dimensionsComponent = entity.getComponent(DimensionsComponent.class);
        imageName = ninePatchComponent.textureRegionName;

        width = dimensionsComponent.width;
        height = dimensionsComponent.height;
    }
}
