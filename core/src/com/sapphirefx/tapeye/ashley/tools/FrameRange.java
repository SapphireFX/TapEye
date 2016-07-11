package com.sapphirefx.tapeye.ashley.tools;

/**
 * Created by sapphire on 19.10.15.
 */
public class FrameRange
{
    public String name;
    public int startFrame;
    public int endFrame;

    public FrameRange()
    {
    }

    public FrameRange(String name, int startFrame, int endFrame)
    {
        this.name = name;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }
}
