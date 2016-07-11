package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by sapphire on 19.10.15.
 */
public class ZIndexComponent implements Component
{
    private int zIndex = 0;
    public boolean needReOrder = false;
    public String layerName = "";
    public int layerIndex;

    @Override
    public String toString()
    {
        return "[zIndex="+zIndex+"][needReOrder="+needReOrder+"][layerName="+layerName+"][layerIndex="+layerIndex+"]";
    }

    public int getZIndex()
    {
        return zIndex;
    }

    public void setZIndex(int zIndex)
    {
        this.zIndex = zIndex;
        needReOrder = true;
    }
}
