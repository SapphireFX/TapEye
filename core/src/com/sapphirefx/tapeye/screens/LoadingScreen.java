package com.sapphirefx.tapeye.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.sapphirefx.tapeye.TapEye;

/**
 * Created by sapphire on 06.04.2016.
 */
public class LoadingScreen implements Screen
{

    private Screen gameScreen;
    private TapEye screenManager;
    private boolean loadedGameScreen = false;

    private SpriteBatch batch;
    private Camera camera;
    private BitmapFont font;
    private Sprite spriteLogo;

    long startTime;

    FrameBuffer fbo;
    ShaderProgram shaderprogram;
    TextureRegion fboTexRegion;

    public LoadingScreen(TapEye screenManager, Screen gameScreen)
    {
        this.gameScreen = gameScreen;
        this.screenManager = screenManager;
    }

    @Override
    public void show()
    {
        loadedGameScreen = false;

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);

        Texture textureLogo = new Texture(Gdx.files.internal("img/BrokenEyeLogo.png"));
        spriteLogo = new Sprite(textureLogo);
        spriteLogo.setOrigin(0, 0);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // test shader
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        fboTexRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboTexRegion.flip(false, true);
        // shader
        String FRAG = Gdx.files.internal("shaders/lavalamp.frag").readString();
        String VERT = Gdx.files.internal("shaders/default.vert").readString();
        shaderprogram = new ShaderProgram(VERT,FRAG);
        System.out.println(shaderprogram.getLog());

        startTime = System.currentTimeMillis();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl20.glClearColor(0f , 0f , 0f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        fbo.begin();
        batch.begin();
        spriteLogo.draw(batch);
        batch.end();
        fbo.end();
        Texture texture = fbo.getColorBufferTexture();
        Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
        texture.bind();


        batch.begin();
        Gdx.gl20.glClearColor(0.3f , 0f , 0.6f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        ShaderProgram.pedantic = false;
        batch.setShader(shaderprogram);

        shaderprogram.setUniformi("u_texture", 0);
        shaderprogram.setUniformf("u_time", (float)(System.currentTimeMillis()-startTime)/100);
        shaderprogram.setUniformf("u_resolution", Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
        batch.draw(texture, 0, 0);
        batch.setShader(null);
        font.draw(batch, "loading......", 200, 300);
        batch.end();


        if (loadedGameScreen)screenManager.setGameScreen(gameScreen);

        if (!loadedGameScreen)
        {
            gameScreen = new GameScreen(screenManager);
            loadedGameScreen = true;
        }

    }

    @Override
    public void resize(int width, int height)
    {
        float posX, posY;
        spriteLogo.setScale(1);
        float scaleX = width/spriteLogo.getWidth();
        posY = (height - spriteLogo.getHeight() * scaleX) / 2;
        spriteLogo.setScale(scaleX);
        spriteLogo.setPosition( 0, posY );
        //camera.update();
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
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
