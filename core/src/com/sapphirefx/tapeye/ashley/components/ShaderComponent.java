package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by sapphire on 19.10.15.
 */
public class ShaderComponent implements Component
{
    public String shaderName;
    private ShaderProgram shaderProgram = null;
    public TimerComponent timerComponent;

    public boolean u_time = false;
    private long startTime;

    public boolean setable = false;
    private float u_float = 0;

    @Override
    public String toString()
    {
        return "[shaderName="+shaderName+"][shaderProgram="+shaderProgram+"]";
    }

    public void setShader(String name, ShaderProgram program)
    {
        shaderName = name;
        shaderProgram = program;
    }

    public ShaderProgram getShader()
    {
        return shaderProgram;
    }

    public void clear()
    {
        shaderName = null;
        shaderProgram = null;
    }

    public void setFloat(float u_float)
    {
        setable = true;
        this.u_float = u_float;
    }

    public void SetUniforms()
    {
        if(u_time) shaderProgram.setUniformf("u_time", System.currentTimeMillis()-startTime);
        if(setable) shaderProgram.setUniformf("u_float", (float)Math.toRadians(u_float));
    }

    public void setTimer(float timer)
    {
        startTime = System.currentTimeMillis();
        TimerComponent timerComponent = new TimerComponent(timer);
        this.timerComponent = timerComponent;
        u_time = true;
    }

}
