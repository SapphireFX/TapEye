package com.sapphirefx.tapeye.ashley.components.animation;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import java.util.HashMap;

/**
 * Created by sapphire on 22.12.2015.
 */
public class AnimationComponent implements Component
{
    public HashMap<String,Animation> animations = new HashMap<String,Animation>();
}
