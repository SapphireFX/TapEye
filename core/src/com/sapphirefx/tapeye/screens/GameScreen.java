package com.sapphirefx.tapeye.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Json;
import com.sapphirefx.tapeye.TapEye;
import com.sapphirefx.tapeye.tools.GameParameters;
import com.sapphirefx.tapeye.tools.SaveParameters;

/**
 * Created by sapphire on 19.10.15.
 */
public class GameScreen implements Screen
{
    private TapEye screenManager; // link
    private Overlap2Dlvl lvl;

    public GameScreen(TapEye supirGame)
    {
        this.screenManager = supirGame;
    }

    @Override
    public void show()
    {
        lvl = new Overlap2Dlvl(screenManager);
    }

    @Override
    public void render(float delta)
    {
        lvl.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height)
    {
    }

    @Override
    public void pause()
    {
        /*
        // TODO сохранить игровое состояние
        lvl.switchPause(true);
        String param;
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        param = json.toJson(GameParameters.saveParametersParameters());
        System.out.println(param);
        FileHandle file = Gdx.files.local("files/parameters.json");
        file.writeString(param, false);
        */
    }

    @Override
    public void resume()
    {

        // TODO восстановить игровое состояние
        Json json = new Json();
        GameParameters.loadParameters( json.fromJson(SaveParameters.class, Gdx.files.local("files/parameters.json")) );
        lvl.switchPause(false);

    }

    @Override
    public void hide()
    {
    }

    @Override
    public void dispose()
    {
    }
}
