package com.sapphirefx.tapeye.ashley.factory.prototypes;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sapphire on 19.10.15.
 */
public class SceneObject
{
    public String sceneName = "";

    public CompositeComponents composite;

    public boolean lightSystemEnabled = false;

    public float[] ambientColor = {1f, 1f, 1f, 1f};

    public PhysicsProperties physicsPropertiesVO = new PhysicsProperties();

    public ArrayList<Float> verticalGuides = new ArrayList<Float>();
    public ArrayList<Float> horizontalGuides = new ArrayList<Float>();

    public SceneObject()
    {
    }

    public SceneObject(SceneObject vo)
    {
        sceneName = new String(vo.sceneName);
        composite = new CompositeComponents(vo.composite);
        ambientColor = Arrays.copyOf(vo.ambientColor, vo.ambientColor.length);
        physicsPropertiesVO = new PhysicsProperties(vo.physicsPropertiesVO);
        lightSystemEnabled = vo.lightSystemEnabled;
    }

    public String constructJsonString()
    {
        String str = "";
        Json json = new Json();
        json.setOutputType(OutputType.json);
        str = json.toJson(this);
        return str;
    }

    @Override
    public String toString()
    {
        String out = "";
        out += "sceneName=" + sceneName;
        return out;
    }
}
