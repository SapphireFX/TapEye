package com.sapphirefx.tapeye;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sapphirefx.tapeye.screens.GameScreen;
import com.sapphirefx.tapeye.screens.LoadingScreen;

public class TapEye extends Game
{
	private Screen gameScreen;
	private Screen loadingScreen;

	@Override
	public void create ()
	{
        loadingScreen = new LoadingScreen(this, gameScreen);
        setScreen(loadingScreen);
	}

    public void setGameScreen(Screen gameScreen)
    {
        getScreen().pause();
        this.gameScreen = gameScreen;
        setScreen(gameScreen);
    }

	@Override
	public void dispose()
	{
		super.dispose();
	}

	public void exit()
	{
		Gdx.app.exit();
	}

}
