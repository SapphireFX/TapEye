package com.sapphirefx.tapeye.ashley.factory.prototypes;

import com.badlogic.ashley.core.Entity;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.LabelComponent;

/**
 * Created by sapphire on 19.10.15.
 */
public class PrototypeLabelObject extends PrototypeObject
{
    public String 	text 	= "Label";
	public String	style	=  "";
	public int		size;
	public int		align;

    public float width = 0;
    public float height = 0;

    public boolean multiline = false;

	public PrototypeLabelObject()
    {
		super();
	}

	public PrototypeLabelObject(PrototypeLabelObject lo)
    {
		super(lo);
		text 	= new String(lo.text);
		style 	= new String(lo.style);
		size 	= lo.size;
		align 	= lo.align;
        width 	= lo.width;
        height 	= lo.height;
        multiline 	= lo.multiline;
	}

	@Override
	public void loadFromEntity(Entity entity)
    {
		super.loadFromEntity(entity);
		LabelComponent labelComponent = entity.getComponent(LabelComponent.class);
		DimensionsComponent dimensionsComponent = entity.getComponent(DimensionsComponent.class);
		text = labelComponent.getText().toString();
		style = labelComponent.fontName;
		size = labelComponent.fontSize;
		align = labelComponent.labelAlign;
		multiline = labelComponent.wrap;

		width = dimensionsComponent.width;
		height = dimensionsComponent.height;
	}
}
