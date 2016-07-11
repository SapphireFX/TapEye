package com.sapphirefx.tapeye.ashley.factory.prototypes;

import com.badlogic.ashley.core.Entity;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;

/**
 * Created by sapphire on 19.10.15.
 */
public class PrototypeCompositeObject extends PrototypeObject
{
    public CompositeComponents composite;

	public float scissorX;
	public float scissorY;
	public float scissorWidth;
	public float scissorHeight;

	public float width;
	public float height;

    public PrototypeCompositeObject()
    {
        composite = new CompositeComponents();
    }

    public PrototypeCompositeObject(PrototypeObject po)
    {
        super(po);
    }

    public PrototypeCompositeObject(PrototypeCompositeObject cpo)
    {
        super(cpo);
        composite = new CompositeComponents(cpo.composite);
    }

    public PrototypeCompositeObject clone()
    {
        PrototypeCompositeObject tmp = new PrototypeCompositeObject();
        tmp.composite = composite;
        tmp.itemName = itemName;
        tmp.layerName = layerName;
        tmp.rotation = rotation;
        tmp.tint = tint;
        tmp.x = x;
        tmp.y = y;
        tmp.zIndex = zIndex;

        tmp.scissorX = scissorX;
        tmp.scissorY = scissorY;
        tmp.scissorWidth = scissorWidth;
        tmp.scissorHeight = scissorHeight;

		tmp.width = width;
		tmp.height = height;

        return tmp;
    }

    @Override
    public void loadFromEntity(Entity entity)
    {
        super.loadFromEntity(entity);

        composite = new CompositeComponents();
        composite.loadFromEntity(entity);

        DimensionsComponent dimensionsComponent = entity.getComponent(DimensionsComponent.class);
        width = dimensionsComponent.width;
        height = dimensionsComponent.height;
    }
}
