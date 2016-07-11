package com.sapphirefx.tapeye.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by sapphire on 23.12.2015.
 */
public class SoundManager
{
    private static HashMap<String, Sound> soundMap;
    private static HashMap<String, Music> musicMap;
    private Random random;

    private static SoundManager soundManager = new SoundManager();

    public static SoundManager getInstance()
    {
        return soundManager;
    }

    private SoundManager()
    {
        random = new Random();

        // короткие звуки
        soundMap = new HashMap<String, Sound>();

        soundMap.put("EyePop1", Gdx.audio.newSound(Gdx.files.internal("sound/EyePop1.mp3")));
        soundMap.put("EyePop2", Gdx.audio.newSound(Gdx.files.internal("sound/EyePop2.mp3")));
        soundMap.put("EyePop3", Gdx.audio.newSound(Gdx.files.internal("sound/EyePop3.mp3")));
        soundMap.put("EyePop4", Gdx.audio.newSound(Gdx.files.internal("sound/EyePop4.mp3")));

        soundMap.put("FlyPop1", Gdx.audio.newSound(Gdx.files.internal("sound/FlyPop1.mp3")));
        soundMap.put("FlyPop2", Gdx.audio.newSound(Gdx.files.internal("sound/FlyPop2.mp3")));
        soundMap.put("FlyPop3", Gdx.audio.newSound(Gdx.files.internal("sound/FlyPop3.mp3")));

        soundMap.put("BadEyePop1", Gdx.audio.newSound(Gdx.files.internal("sound/BadEyePop1.mp3")));
        soundMap.put("BadEyePop2", Gdx.audio.newSound(Gdx.files.internal("sound/BadEyePop2.mp3")));
        soundMap.put("BadEyePop3", Gdx.audio.newSound(Gdx.files.internal("sound/BadEyePop3.mp3")));

        soundMap.put("Table1", Gdx.audio.newSound(Gdx.files.internal("sound/Table1.mp3")));
        soundMap.put("Table2", Gdx.audio.newSound(Gdx.files.internal("sound/Table2.mp3")));

        soundMap.put("BadFlyBang1", Gdx.audio.newSound(Gdx.files.internal("sound/BadFlyBang1.mp3")));
        soundMap.put("BadFlyBang2", Gdx.audio.newSound(Gdx.files.internal("sound/BadFlyBang2.mp3")));

        soundMap.put("CatMeow1", Gdx.audio.newSound(Gdx.files.internal("sound/CatMeow1.mp3")));
        soundMap.put("CatMeow2", Gdx.audio.newSound(Gdx.files.internal("sound/CatMeow2.mp3")));

        soundMap.put("CatPurr1", Gdx.audio.newSound(Gdx.files.internal("sound/CatPurr1.mp3")));
        soundMap.put("CatPurr2", Gdx.audio.newSound(Gdx.files.internal("sound/CatPurr2.mp3")));

        soundMap.put("FlyBuzz1", Gdx.audio.newSound(Gdx.files.internal("sound/FlyBuzz1.mp3")));
        soundMap.put("FlyBuzz2", Gdx.audio.newSound(Gdx.files.internal("sound/FlyBuzz2.mp3")));
        soundMap.put("FlyBuzz3", Gdx.audio.newSound(Gdx.files.internal("sound/FlyBuzz3.mp3")));

        soundMap.put("Button1", Gdx.audio.newSound(Gdx.files.internal("sound/Button1.mp3")));
        soundMap.put("Button2", Gdx.audio.newSound(Gdx.files.internal("sound/Button2.mp3")));

        soundMap.put("MissedEye", Gdx.audio.newSound(Gdx.files.internal("sound/MissedEye.mp3")));

        // музыка
        musicMap = new HashMap<String, Music>();
        musicMap.put("maintheme", Gdx.audio.newMusic(Gdx.files.internal("sound/maintheme.mp3")));
        musicMap.put("PixelEye", Gdx.audio.newMusic(Gdx.files.internal("sound/PixelEye.mp3")));

    }


    /**
     * косание кнопки
     */
    public void playSoundButtonTouch()
    {
        int i = random.nextInt(2);
        switch (i)
        {
            case 0:
                soundMap.get("Button1").play();
                break;
            case 1:
                soundMap.get("Button2").play();
                break;
        }
    }

    /**
     * просто касание стола, когда не попал по объектам
     */
    public void playSoundTableTouch()
    {
        int i = random.nextInt(2);
        switch (i)
        {
            case 0:
                soundMap.get("Table1").play();
                break;
            case 1:
                soundMap.get("Table2").play();
                break;
        }
    }

    /**
     * появление котейки
     */
    public void playSoundCatShow()
    {
        int i = random.nextInt(2);
        switch (i)
        {
            case 0:
                soundMap.get("CatMeow1").play();
                break;
            case 1:
                soundMap.get("CatMeow2").play();
                break;
        }
    }

    /**
     * касание котейки
     */
    public void playSoundCatTouch()
    {
        int i = random.nextInt(2);
        switch (i)
        {
            case 0:
                soundMap.get("CatPurr1").play();
                break;
            case 1:
                soundMap.get("CatPurr2").play();
                break;
        }
    }

    /**
     * лопание глазика дающего очки
     */
    public void playSoundEyePop()
    {
        int i = random.nextInt(4);
        switch (i)
        {
            case 0:
                soundMap.get("EyePop1").play();
                break;
            case 1:
                soundMap.get("EyePop2").play();
                break;
            case 2:
                soundMap.get("EyePop3").play();
                break;
            case 3:
                soundMap.get("EyePop4").play();
                break;
        }
    }

    /**
     * лопание глазика, который нельзя трогать
     */
    public void playSoundBadEyePop()
    {
        int i = random.nextInt(3);
        switch (i)
        {
            case 0:
                soundMap.get("BadEyePop1").play();
                break;
            case 1:
                soundMap.get("BadEyePop2").play();
                break;
            case 2:
                soundMap.get("BadEyePop3").play();
                break;
        }
    }

    /**
     * давим муху
     */
    public void playSoundFlyPop()
    {
        int i = random.nextInt(3);
        switch (i)
        {
            case 0:
                soundMap.get("FlyPop1").play();
                break;
            case 1:
                soundMap.get("FlyPop2").play();
                break;
            case 2:
                soundMap.get("FlyPop3").play();
                break;
        }
    }

    /**
     * звук мухи летающей в воздухе
     */
    public long playSoundFlyInAir()
    {
        switch (random.nextInt(3))
        {
            case 0:
                return soundMap.get("FlyBuzz1").play();
            case 1:
                return soundMap.get("FlyBuzz2").play();
            case 2:
                return soundMap.get("FlyBuzz3").play();
            default:
                return soundMap.get("FlyBuzz1").play();
        }
    }

    public void playSound(String name)
    {
        soundMap.get(name).play();
    }

    public void playLoopMusic(String name)
    {
        musicMap.get(name).setLooping(true);
        musicMap.get(name).play();
    }

    public void stopMusic(String name)
    {
        musicMap.get(name).stop();
    }

    public void stopThisSoundInstance(long sound)
    {
        soundMap.get("FlyBuzz1").stop(sound);
        soundMap.get("FlyBuzz2").stop(sound);
        soundMap.get("FlyBuzz3").stop(sound);
    }

    public void stopAll()
    {
        for(Iterator<Sound> i=soundMap.values().iterator(); i.hasNext();)
        {
            i.next().stop();
        }
        for(Iterator<Music> i=musicMap.values().iterator(); i.hasNext();)
        {
            i.next().stop();
        }
    }
}
