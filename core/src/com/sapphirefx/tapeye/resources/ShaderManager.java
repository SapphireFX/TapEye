package com.sapphirefx.tapeye.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.sapphirefx.tapeye.ashley.components.ShaderComponent;

import java.io.UncheckedIOException;
import java.util.HashMap;

/**
 * Created by sapphire on 31.05.2016.
 */
public class ShaderManager
{
    public static ShaderManager instance = new ShaderManager();
    private HashMap<String,ShaderText> shaderBase;

    public static ShaderManager getInstance()
    {
        if (instance == null) instance = new ShaderManager();
        return instance;
    }

    private ShaderManager()
    {
        shaderBase = new HashMap<String, ShaderText>();

        // load all shader components
        shaderBase.put("fisheye", new ShaderText("shaders/fishyey.vert", "shaders/fishyey.frag", true));

        shaderBase.put("rotation", new ShaderText("shaders/default.vert", "shaders/rotate.frag", false));

    }

    // генерирует компонент с шейдером
    public ShaderComponent createShader(String name)
    {
        if (!shaderBase.containsKey(name)) throw new RuntimeException("Wrong name created shader");
        else return createShaderComponent(name,  shaderBase.get(name));
    }

    public ShaderComponent createShader(String name, float u_float)
    {
        if (!shaderBase.containsKey(name)) throw new RuntimeException("Wrong name created shader");
        else return createShaderComponent(name,  shaderBase.get(name), u_float);
    }

    private ShaderComponent createShaderComponent(String name, ShaderText shaderText)
    {
        ShaderComponent shaderComponent = new ShaderComponent();
        String FRAG = Gdx.files.internal(shaderText.fragment).readString();
        String VERT = Gdx.files.internal(shaderText.vertex).readString();
        shaderComponent.setShader(name, new ShaderProgram(VERT, FRAG));
        return shaderComponent;
    }

    private ShaderComponent createShaderComponent(String name, ShaderText shaderText, float u_float)
    {
        ShaderComponent shaderComponent = createShaderComponent(name, shaderText);
        if(u_float != 0)  shaderComponent.setFloat(u_float);
        return shaderComponent;
    }


    // хранит пути до файлов с шейдером
    private class ShaderText
    {
        String vertex;
        String fragment;
        boolean u_timer = false;

        public ShaderText (String vertex, String fragment, boolean u_timer)
        {
            this.vertex = vertex;
            this.fragment = fragment;
            this.u_timer = u_timer;
        }
    }
}
