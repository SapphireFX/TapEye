package com.sapphirefx.tapeye.screens;

import com.sapphirefx.tapeye.ashley.systems.ActorSystem;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.tools.GameParameters;

/**
 * Created by sapphire on 10.05.2016.
 */
public class ScreenManager
{
    private SceneLoader sl;
    public static String requestLoadScene = ""; // запрос от систем на смену сцены

    public ScreenManager(SceneLoader sl)
    {
        this.sl = sl;
    }

    private boolean changeScreen()
    {
        sl.getEngine().getSystem(ActorSystem.class).prepareChangeScene();
        sl.loadScene(requestLoadScene);
        if (requestLoadScene == "boss1")
        {
            GameParameters.onShowWindow = true;
            sl.getEntityFactory().createEffect(GameParameters.EFFECT.ONSHOWWINDOW, 0, 0, 1, 1, null);
        }
        return true;
    }

    public void update()
    {
        if(requestLoadScene != "")
        {
            changeScreen();
            requestLoadScene = "";
        }
    }
}
