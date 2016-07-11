package com.sapphirefx.tapeye.tools;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeCompositeObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.SceneObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sapphire on 19.10.15.
 */
public class ProjectInfo
{
    public int pixelToWorld = 1;

    public ResolutionEntry originalResolution = new ResolutionEntry();

    public Array<ResolutionEntry> resolutions = new Array<ResolutionEntry>();
    public ArrayList<SceneObject> scenes = new ArrayList<SceneObject>();

    public HashMap<String, PrototypeCompositeObject> libraryItems = new HashMap<String, PrototypeCompositeObject>();

    public String constructJsonString()
    {
        String str = "";
        Json json = new Json();
        json.setOutputType(OutputType.json);
        str = json.toJson(this);
        json.prettyPrint(str);
        return str;
    }

    public ResolutionEntry getResolution(String name)
    {
        for (ResolutionEntry resolution : resolutions)
        {
            if (resolution.name.equalsIgnoreCase(name))
            {
                return resolution;
            }
        }
        return null;
    }
}
