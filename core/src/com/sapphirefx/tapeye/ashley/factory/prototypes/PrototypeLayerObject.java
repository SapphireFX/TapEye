package com.sapphirefx.tapeye.ashley.factory.prototypes;

/**
 * Created by sapphire on 19.10.15.
 */
public class PrototypeLayerObject
{
    public String layerName = "";
	public boolean isLocked = false;
	public boolean isVisible = false;

	public PrototypeLayerObject()
    {
	}

    public PrototypeLayerObject(String name)
    {
        layerName = new String(name);
		isVisible = true;
    }

	public PrototypeLayerObject(PrototypeLayerObject vo)
    {
		layerName = new String(vo.layerName);
		isLocked = vo.isLocked;
		isVisible = vo.isVisible;
	}

	public static PrototypeLayerObject createDefault()
    {
        PrototypeLayerObject layerItemVO = new PrototypeLayerObject();
		layerItemVO.layerName = "Default";
		layerItemVO.isVisible = true;
		return layerItemVO;
	}
}
