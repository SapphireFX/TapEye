package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeLayerObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by sapphire on 19.10.15.
 */
public class LayerMapComponent implements Component
{
    public boolean autoIndexing = true;
    private ArrayList<PrototypeLayerObject> layers = new ArrayList<PrototypeLayerObject>();
    private HashMap<String, PrototypeLayerObject> layerMap = new HashMap<String, PrototypeLayerObject>();

    @Override
    public String toString()
    {
        return "[autoIndexing="+autoIndexing+"][layers="+layers+"][layerMap="+layerMap+"]";
    }

    public void setLayers(ArrayList<PrototypeLayerObject> layers)
    {
        this.layers = layers;
        layerMap.clear();
        for (PrototypeLayerObject vo : layers)
        {
            layerMap.put(vo.layerName, vo);
        }
    }

    public PrototypeLayerObject getLayer(String name)
    {
        return layerMap.get(name);
    }

    public int getIndexByName(String name)
    {
        if(layerMap.containsKey(name))
        {
            return layers.indexOf(layerMap.get(name));
        }

        return 0;
    }

    public boolean isVisible(String name)
    {
        PrototypeLayerObject vo = getLayer(name);
        if (vo != null)
        {
            return vo.isVisible;
        }

        return true;
    }

    public void addLayer(int index, PrototypeLayerObject layerVo)
    {
        layers.add(index, layerVo);
        layerMap.put(layerVo.layerName, layerVo);
    }

    public void addLayer(PrototypeLayerObject layerVo)
    {
        layers.add(layerVo);
        layerMap.put(layerVo.layerName, layerVo);
    }

    public ArrayList<PrototypeLayerObject> getLayers()
    {
        return layers;
    }

    public void deleteLayer(String layerName)
    {
        layers.remove(getIndexByName(layerName));
        layerMap.remove(layerName);
    }

    public void rename(String prevName, String newName)
    {
        PrototypeLayerObject vo = layerMap.get(prevName);
        layerMap.remove(prevName);
        layerMap.put(newName, vo);
    }

    public void swap(String source, String target)
    {
        PrototypeLayerObject sourceVO = getLayer(source);
        PrototypeLayerObject targetVO = getLayer(target);
        Collections.swap(layers, layers.indexOf(sourceVO), layers.indexOf(targetVO));
    }
}
