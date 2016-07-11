package com.sapphirefx.tapeye.tools;


import com.badlogic.gdx.math.Vector2;
import com.sapphirefx.tapeye.levelsAndMonsters.MonsterController;

/**
 * Created by Sapphire on 07.12.2015.
 */
public class GameParameters
{
    public enum Buttons {SETTINGS, START, EXIT, NONE, BACKTOMENU, BACK, MAINMENU, PAUSE, MUTE, LEVELS}
    public enum InteranlCommands {NONE, EXIT}
    public enum ENEMY {GOODEYE, EVILEYE, GOODFLY, EVILFLY, CAT, SPIDER}
    public enum EFFECT {SMASHEDGREENEYE, SMASHEDORANGEEYE, DEADEVILFLY, DEADGOODFLY, DEADSPIDER, ADDEYE, DELEYE, GREENTEXT, REDTEXT, BLOWEVILEYE,
        ONSHOWWINDOW, ONHIDEWINDOW, NEXTLVL}

    public static boolean musicOn = false;
    public static boolean isPaused = false;
    public static boolean onShowWindow = false;
    public static boolean onHideWindow = false;

    public static int score = 0;
    public static int fails = 3;
    public static float timeElapsed = 0;
    public static String timer = "";
    public static String logging = "123456";

    public static Vector2 positionScore = new Vector2();
    public static Vector2 positionFail = new Vector2();


    // ***********************************
    //      FOR SAVE / LOAD
    // ***********************************

    public static SaveParameters saveParametersParameters()
    {
        SaveParameters save = new SaveParameters();
        save.timeElapsedSaved = timeElapsed;
        save.scoreSaved = score;
        save.failsSaved = fails;
        save.musicOnSaved = musicOn;
        return save;
    }

    public static void loadParameters(SaveParameters saved)
    {
        timeElapsed =  saved.timeElapsedSaved;
        score =  saved.scoreSaved;
        fails = saved.failsSaved;
        musicOn = saved.musicOnSaved;
    }
}
