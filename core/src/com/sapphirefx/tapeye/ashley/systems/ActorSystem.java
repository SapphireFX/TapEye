package com.sapphirefx.tapeye.ashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sapphirefx.tapeye.ashley.components.ButtonComponent;
import com.sapphirefx.tapeye.ashley.components.DeskComponent;
import com.sapphirefx.tapeye.ashley.components.EnemyComponent;
import com.sapphirefx.tapeye.ashley.components.LabelComponent;
import com.sapphirefx.tapeye.ashley.components.MainItemComponent;
import com.sapphirefx.tapeye.ashley.components.NodeComponent;
import com.sapphirefx.tapeye.ashley.components.ParentNodeComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;
import com.sapphirefx.tapeye.ashley.tools.ComponentRetriever;
import com.sapphirefx.tapeye.screens.ScreenManager;
import com.sapphirefx.tapeye.resources.ResourceManager;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.tools.GameParameters;

import java.util.Set;

/**
 * Created by sapphire on 20.10.15.
 */
public class ActorSystem extends IteratingSystem
{
    private SceneLoader sl;
    protected ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper.getFor(TransformComponent.class);

    private Stage stage;
    private Actor keyInputActor;

    private Array<Actor> pausedActors = null;
    private boolean isPausedWindow = false;
    private float pausedWindowVisible = 0;
    private StringBuilder pausedLabel;

    public ActorSystem(Viewport viewport, final SceneLoader sl)
    {
        super(Family.one(DeskComponent.class, ButtonComponent.class, EnemyComponent.class).get());
        this.sl = sl;
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        keyInputActor = new Actor();
        keyInputActor.addListener(new InputListener()
        {
            @Override
            public boolean keyDown(InputEvent event, int keycode)
            {
                if (keycode == Keys.BACK)
                {
                    if (!GameParameters.isPaused) setVisiblePopupWindow("gamemenu", true);
                }
                return true;
            }
        });
        stage.setKeyboardFocus(keyInputActor);
        stage.addActor(keyInputActor);
        pausedActors = new Array<Actor>();
    }

    @Override
    public void update(float deltaTime)
    {
        if (pausedLabel == null)
        {
            // получаем ссылку на текстовое поле в окне паузы
            ImmutableArray<Entity> allEntities =  sl.engine.getEntities();
            for (int i=0; i<allEntities.size(); i++)
            {
                MainItemComponent mainItemComponent = allEntities.get(i).getComponent(MainItemComponent.class);
                if(mainItemComponent != null && mainItemComponent.tags.contains("pausecounter"))
                {
                    pausedLabel = allEntities.get(i).getComponent(LabelComponent.class).text;
                    break;
                }
            }
        }

        super.update(deltaTime);
        stage.act(deltaTime);
        if (isPausedWindow)
        {
            pausedWindowVisible -= deltaTime;
            pausedLabel.delete(0, pausedLabel.length());
            pausedLabel.append(String.valueOf((int)pausedWindowVisible+1));
            if (pausedWindowVisible < 0) setVisiblePopupWindow("pausedwindow", false);
        }

        // debug actors
        //stage.setDebugAll(true);
        //stage.draw();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        TransformComponent transformComponent =  transformComponentMapper.get(entity);
        EnemyComponent enemyComponent = ComponentRetriever.get(entity, EnemyComponent.class);
        ButtonComponent buttonComponent = ComponentRetriever.get(entity, ButtonComponent.class);
        DeskComponent deskComponent = entity.getComponent(DeskComponent.class);

        Actor actor = null;

        if (deskComponent != null)
        {
            if (!deskComponent.addedInStage)
            {
                stage.addActor(deskComponent.actor);
                deskComponent.actor.toBack();
                deskComponent.addedInStage = true;
            }
        } else if (enemyComponent != null && !GameParameters.isPaused) // для врагов
        {
            actor = enemyComponent.actor;
            if (!enemyComponent.addedInStage && enemyComponent.isAlive)
            {
                stage.addActor(actor);
                enemyComponent.addedInStage = true;
            }
            if (enemyComponent.addedInStage && !enemyComponent.isAlive)
            {// умер, но еще на столе, значит уберем и выключим, все что с ним связано
                ResourceManager.soundManager.stopThisSoundInstance(enemyComponent.sound);
                stage.getActors().removeValue(actor, true);
                enemyComponent.addedInStage = false;
            }
        } else if (buttonComponent != null) // для кнопок
        {
            actor = buttonComponent.actor;
            calculatePositionActors(entity, actor);
            if (!buttonComponent.addedInStage)
            {
                stage.addActor(actor);
                buttonComponent.addedInStage = true;
            }
            if (buttonComponent.isTouched)
            {
                Gdx.input.vibrate(50);
                switch (buttonComponent.typeButton)
                {
                    case NONE:
                        break;
                    case SETTINGS:
                        //TODO проверить надо ли актеров восстанавливать с предыдущей сцены
                        removeAllactors();
                        ScreenManager.requestLoadScene = "settings";
                        break;
                    case LEVELS:
                        //TODO проверить надо ли актеров восстанавливать с предыдущей сцены
                        removeAllactors();
                        ScreenManager.requestLoadScene = "levels";
                        break;
                    case START:
                        removeAllactors();
                        restartGame();
                        ScreenManager.requestLoadScene = "MainScene";
                        break;
                    case EXIT:
                        removeAllactors();
                        sl.interanlCommands = GameParameters.InteranlCommands.EXIT;
                        break;
                    case BACKTOMENU:
                        removeAllactors();
                        ScreenManager.requestLoadScene = "menu";
                        break;
                    case PAUSE:
                        setVisiblePopupWindow("gamemenu", true);
                        break;
                    case BACK:
                        setVisiblePopupWindow("gamemenu", false);
                        break;
                    case MAINMENU:
                        removeAllactors();
                        pausedLabel = null;
                        ScreenManager.requestLoadScene = "menu";
                        break;
                    case MUTE:
                        muteMusic();
                        break;
                }
                buttonComponent.isTouched = false;
            }
        }
    }

    private void calculatePositionActors(Entity entity, Actor actor)
    {
        if(entity.getComponent(ParentNodeComponent.class) != null)
        {
            Entity parent = entity.getComponent(ParentNodeComponent.class).parentEntity;
            if (parent != null)
            {
                calculatePositionActors(parent, actor);
                actor.setX(actor.getX() + parent.getComponent(TransformComponent.class).x);
                actor.setY(actor.getY() + parent.getComponent(TransformComponent.class).y);
            }
        }
    }

    private void muteMusic()
    {
        GameParameters.musicOn = !GameParameters.musicOn;
        if(GameParameters.musicOn) ResourceManager.soundManager.playLoopMusic("PixelEye");
        else ResourceManager.soundManager.stopMusic("PixelEye");
    }

    public void prepareChangeScene()
    {
        removeAllactors();
        pausedLabel = null;
    }

    private void removeAllactors()
    {
        stage.clear();
        stage.setKeyboardFocus(keyInputActor);
        stage.addActor(keyInputActor);
    }

    public void switchPauseGame(boolean isPaused)
    {
        if (!isPaused && !isPausedWindow && sl.getSceneVO().sceneName.equals("MainScene"))
        {
            setVisiblePopupWindow("pausedwindow", true);
            pausedWindowVisible = 3f;
            isPausedWindow = true;
        } else
        {
            GameParameters.isPaused = isPaused;
            if (pausedWindowVisible < 0) isPausedWindow = false;
        }
    }

    public void setVisiblePopupWindow(String nameWindow, boolean visible)
    {
        if (visible)
        {
            pausedActors.clear();
            for (Actor pausedActor : stage.getActors())
            {
                pausedActors.add(pausedActor);
            }
            removeAllactors();
        }else
        {
            removeAllactors();
            for (Actor pausedActor : pausedActors)
            {
                stage.addActor(pausedActor);
            }
            pausedActors.clear();
        }

        ImmutableArray<Entity> allEntitys = sl.engine.getEntities();
        for(Entity entity: allEntitys)
        {
            Set<String> tags = entity.getComponent(MainItemComponent.class).tags;
            if (tags.contains(nameWindow))
            {
                entity.getComponent(MainItemComponent.class).visible = visible;
                setVisibleRecursiveAllButtons(entity, visible);
            }
        }

        switchPauseGame(visible);
    }

    private void setVisibleRecursiveAllButtons(Entity entity, boolean visible)
    {
        ButtonComponent button = entity.getComponent(ButtonComponent.class);
        if(button != null)
        {
            if (visible) stage.addActor(button.actor); // добавляем
            else  stage.getActors().removeValue(button.actor, true); // удаляем
        }
        if(entity.getComponent(NodeComponent.class) != null && entity.getComponent(NodeComponent.class).children.size > 0)
        {
            for (Entity child: entity.getComponent(NodeComponent.class).children)
            {
                setVisibleRecursiveAllButtons(child, visible);
            }
        }
    }

    private void restartGame()
    {
        GameParameters.fails = 3;
        GameParameters.score = 0;
        GameParameters.timeElapsed = 0;
        GameParameters.isPaused = false;
    }
}

