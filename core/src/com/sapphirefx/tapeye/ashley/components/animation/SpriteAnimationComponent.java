package com.sapphirefx.tapeye.ashley.components.animation;

/**
 * Created by sapphire on 19.10.15.
 */

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.sapphirefx.tapeye.ashley.tools.FrameRange;
import java.util.HashMap;

/**
 * Created by sapphire on 17.09.15.
 */
public class SpriteAnimationComponent implements Component
{
    public String animationName = "";
    public int fps = 24;
    public HashMap<String, FrameRange> frameRangeMap = new HashMap<String, FrameRange>();
    public String currentAnimation;
    public Animation.PlayMode playMode = Animation.PlayMode.LOOP;

    @Override
    public String toString()
    {
        return "[animationName=" + animationName + "][frameRangeMap=" + frameRangeMap + "][currentAnimation=" + currentAnimation +
                "][playMode=" + playMode + "]";
    }
}
