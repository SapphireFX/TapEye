package com.sapphirefx.tapeye.ashley.factory.prototypes;

import com.badlogic.ashley.core.Entity;
import com.sapphirefx.tapeye.ashley.components.TextureComponent;

/**
 * Created by sapphire on 19.10.15.
 */
public class PrototypeImageObject extends PrototypeObject
{
    public String imageName = "";
    public boolean isRepeat = false;
    public boolean isPolygon = false;

    public PrototypeImageObject()
    {
        super();
    }

    public PrototypeImageObject(PrototypeImageObject io)
    {
        super(io);
        imageName = new String(io.imageName);
        isRepeat = io.isRepeat;
        isPolygon = io.isPolygon;
    }

    @Override
    public void loadFromEntity(Entity entity)
    {
        super.loadFromEntity(entity);

        TextureComponent component = entity.getComponent(TextureComponent.class);
        imageName = component.regionName;
        isRepeat = component.isRepeat;
        isPolygon = component.isPolygon;
    }
}
