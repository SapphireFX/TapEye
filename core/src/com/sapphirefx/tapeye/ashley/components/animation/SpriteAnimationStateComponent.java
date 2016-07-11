package com.sapphirefx.tapeye.ashley.components.animation;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.sapphirefx.tapeye.ashley.tools.FrameRange;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by sapphire on 19.10.15.
 */
public class SpriteAnimationStateComponent implements Component
{
    public Array<AtlasRegion> allRegions;
    public Animation currentAnimation;
    public float time = 0.0f;
    public boolean paused = false;

    @Override
    public String toString()
    {
        return "[currentAnimation=" + currentAnimation + "][time=" + time + "]";
    }

    public SpriteAnimationStateComponent(Array<AtlasRegion> allRegions)
    {
        this.allRegions = allRegions;
        time = 0.0f;
    }

    public Animation get()
    {
        return currentAnimation;
    }

    public void set(SpriteAnimationComponent sac)
    {
        set(sac.frameRangeMap.get(sac.currentAnimation), sac.fps, sac.playMode);
    }

    public void set(FrameRange range, int fps, Animation.PlayMode playMode)
    {
        Array<AtlasRegion> textureRegions = new Array<AtlasRegion>(range.endFrame - range.startFrame + 1);
        for (int r = range.startFrame; r <= range.endFrame; r++)
        {
            textureRegions.add(allRegions.get(r));
        }
        currentAnimation =  new Animation(1f/fps, textureRegions, playMode);
        time = 0.0f;
    }

    private Array<AtlasRegion> sortAndGetRegions(Array<AtlasRegion> regions)
    {
        regions.sort(new SortRegionsComparator());

        return regions;
    }

    private class SortRegionsComparator implements Comparator<AtlasRegion>
    {
        @Override
        public int compare(AtlasRegion o1, AtlasRegion o2)
        {
            int index1 = regNameToFrame(o1.name);
            int index2 = regNameToFrame(o2.name);
            return index1 < index2 ? -1 : index1 == index2 ? 0 : 1;
        }
    }

    private int regNameToFrame(String name)
    {
        final Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");
        Matcher matcher = lastIntPattern.matcher(name);
        if (matcher.find()) {
            String someNumberStr = matcher.group(1);
            return Integer.parseInt(someNumberStr);
        }
        throw new RuntimeException(
                "Frame name should be something like this '*0001', but not "
                        + name + ".");
    }
}
