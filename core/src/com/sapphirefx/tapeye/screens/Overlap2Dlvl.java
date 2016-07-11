package com.sapphirefx.tapeye.screens;

import com.sapphirefx.tapeye.TapEye;
import com.sapphirefx.tapeye.ashley.systems.ActorSystem;
import com.sapphirefx.tapeye.resources.ResourceManager;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.screens.ScreenManager;

/**
 * Created by sapphire on 20.10.15.
 */
public class Overlap2Dlvl
{
    private TapEye screenManager;

    private ResourceManager rm;
    private SceneLoader sl;
    private ScreenManager lm;

    public Overlap2Dlvl(TapEye screenManager)
    {
        this.screenManager = screenManager;

            long startTime = System.currentTimeMillis();
            System.out.print("start");
            System.out.println("    " + (System.currentTimeMillis()-startTime));
            System.out.println("new resource manager");
        rm = new ResourceManager();
            System.out.println("    " + (System.currentTimeMillis()-startTime));
            startTime = System.currentTimeMillis();
            System.out.println("maxMemory " + Runtime.getRuntime().maxMemory());
            System.out.println("totalMemory " + Runtime.getRuntime().totalMemory());
            System.out.println("load resources");
        rm.initAllResources();
            System.out.println("    " + (System.currentTimeMillis()-startTime));
            startTime = System.currentTimeMillis();
            System.out.println("maxMemory " + Runtime.getRuntime().maxMemory());
            System.out.println("totalMemory " + Runtime.getRuntime().totalMemory());
            System.out.println("new scene loader");
        sl = new SceneLoader(rm);
            System.out.println("    " + (System.currentTimeMillis()-startTime));
            startTime = System.currentTimeMillis();
            System.out.println("load scene menu");

        lm = new ScreenManager(sl);
        ScreenManager.requestLoadScene = "menu";
            System.out.println("    " + (System.currentTimeMillis()-startTime));
            startTime = System.currentTimeMillis();
            System.out.println("end load");
    }

    public void update(float delta)
    {
        // запросы на смену сцены
        lm.update();

        // команды пришедшие из игры
        switch (sl.interanlCommands)
        {
            case NONE:
                break;
            case EXIT:
                screenManager.exit();
                break;
        }
        sl.engine.update(delta);
    }

    public void switchPause(boolean state)
    {
        if(sl != null) sl.engine.getSystem(ActorSystem.class).switchPauseGame(state);
    }
}
