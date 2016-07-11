package com.sapphirefx.tapeye.ashley.factory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.sapphirefx.tapeye.ashley.components.ButtonComponent;
import com.sapphirefx.tapeye.ashley.components.ColorComponent;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.EffectComponent;
import com.sapphirefx.tapeye.ashley.components.EnemyComponent;
import com.sapphirefx.tapeye.ashley.components.FailsComponent;
import com.sapphirefx.tapeye.ashley.components.LabelComponent;
import com.sapphirefx.tapeye.ashley.components.LoggingComponent;
import com.sapphirefx.tapeye.ashley.components.MainItemComponent;
import com.sapphirefx.tapeye.ashley.components.MessageComponent;
import com.sapphirefx.tapeye.ashley.components.MovementComponent;
import com.sapphirefx.tapeye.ashley.components.MovementMenuComponent;
import com.sapphirefx.tapeye.ashley.components.NodeComponent;
import com.sapphirefx.tapeye.ashley.components.ParentNodeComponent;
import com.sapphirefx.tapeye.ashley.components.PhysicBodyComponent;
import com.sapphirefx.tapeye.ashley.components.PlaceEnemyRespawnComponent;
import com.sapphirefx.tapeye.ashley.components.PolygonComponent;
import com.sapphirefx.tapeye.ashley.components.ScoreComponent;
import com.sapphirefx.tapeye.ashley.components.ShaderComponent;
import com.sapphirefx.tapeye.ashley.components.TimerLabelComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;
import com.sapphirefx.tapeye.ashley.components.ZIndexComponent;
import com.sapphirefx.tapeye.ashley.components.LifeCicleComponent;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeObject;
import com.sapphirefx.tapeye.ashley.tools.SomeAction;
import com.sapphirefx.tapeye.resources.IResourceRetriever;
import com.sapphirefx.tapeye.resources.ResourceManager;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.tools.GameParameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by sapphire on 19.10.15.
 */
public abstract class ComponentFactory
{
    protected IResourceRetriever rm;
    protected ComponentMapper<NodeComponent> nodeComponentMapper;

    public ComponentFactory(IResourceRetriever rm)
    {
        this.rm = rm;
        nodeComponentMapper = ComponentMapper.getFor(NodeComponent.class);
    }

    public abstract void createComponents(Entity root, Entity entity, PrototypeObject po);

    protected void createCommonComponents(Entity entity, PrototypeObject po, int entityType)
    {
        DimensionsComponent dimensionsComponent = createDimensionsComponent(entity, po);
        createMainItemComponent(entity, po, entityType);
        createTransformComponent(entity, po, dimensionsComponent);
        createColorComponent(entity, po);
        createZindexComponent(entity, po);
        createMeshComponent(entity, po);
        createPhysicsComponents(entity, po);
        createShaderComponent(entity, po);
    }

    protected abstract DimensionsComponent createDimensionsComponent(Entity entity, PrototypeObject po);

    protected ShaderComponent createShaderComponent(Entity entity, PrototypeObject po)
    {
        if (po.shaderName == null || po.shaderName.equals(""))
        {
            return null;
        }
        ShaderComponent component = new ShaderComponent();
        component.setShader(po.shaderName, rm.getShaderProgram(po.shaderName));
        entity.add(component);
        return component;
    }

    protected MainItemComponent createMainItemComponent(Entity entity, PrototypeObject po, int entityType)
    {
        MainItemComponent component = new MainItemComponent();
        component.customVars = po.customVars;
        component.uniqueId = po.uniqueId;
        component.itemIdentifier = po.itemIdentifier;
        component.libraryLink = po.itemName;
        if (po.tags != null)
        {
            component.tags = new HashSet<String>(Arrays.asList(po.tags));
        }
        component.entityType = entityType;

        entity.add(component);

        return component;
    }

    protected TransformComponent createTransformComponent(Entity entity, PrototypeObject po, DimensionsComponent dimensionsComponent)
    {
        TransformComponent component = new TransformComponent();
        component.rotation = po.rotation;
        component.scaleX = po.scaleX;
        component.scaleY = po.scaleY;
        component.x = po.x;
        component.y = po.y;

        if (Float.isNaN(po.originX)) component.originX = dimensionsComponent.width / 2f;
        else component.originX = po.originX;

        if (Float.isNaN(po.originY)) component.originY = dimensionsComponent.height / 2f;
        else component.originY = po.originY;

        entity.add(component);

        return component;
    }

    protected ColorComponent createColorComponent(Entity entity, PrototypeObject vo)
    {
        ColorComponent component = new ColorComponent();
        component.color.set(vo.tint[0], vo.tint[1], vo.tint[2], vo.tint[3]);

        entity.add(component);

        return component;
    }

    protected ZIndexComponent createZindexComponent(Entity entity, PrototypeObject po)
    {
        ZIndexComponent component = new ZIndexComponent();

        if(po.layerName == "" || po.layerName == null) po.layerName = "Default";

        component.layerName = po.layerName;
        component.setZIndex(po.zIndex);
        component.needReOrder = false;
        entity.add(component);

        return component;
    }

    protected void createNodeComponent(Entity root, Entity entity)
    {
        NodeComponent component = nodeComponentMapper.get(root);
        component.children.add(entity);
    }

    protected void createPhysicsComponents(Entity entity, PrototypeObject po)
    {
        if(po.physics == null)
        {
            return;
        }
        createPhysicsBodyPropertiesComponent(entity, po);
    }

    protected PhysicBodyComponent createPhysicsBodyPropertiesComponent(Entity entity, PrototypeObject po)
    {
        PhysicBodyComponent component = new PhysicBodyComponent();
        component.allowSleep = po.physics.allowSleep;
        component.awake = po.physics.awake;
        component.bodyType = po.physics.bodyType;
        component.bullet = po.physics.bullet;
        component.centerOfMass = po.physics.centerOfMass;
        component.damping = po.physics.damping;
        component.density = po.physics.density;
        component.friction = po.physics.friction;
        component.gravityScale = po.physics.gravityScale;
        component.mass = po.physics.mass;
        component.restitution = po.physics.restitution;
        component.rotationalInertia = po.physics.rotationalInertia;

        entity.add(component);

        return component;
    }

    protected ParentNodeComponent createParentNodeComponent(Entity root, Entity entity)
    {
        ParentNodeComponent component = new ParentNodeComponent();
        component.parentEntity = root;
        entity.add(component);

        //set visible to true depending on parent
        // TODO: I do not likes this part
        //MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
        //LayerMapComponent layerMapComponent = ComponentRetriever.get(root, LayerMapComponent.class);
        //ZIndexComponent zIndexComponent = ComponentRetriever.get(root, ZIndexComponent.class);
        //mainItemComponent.visible = layerMapComponent.isVisible(zIndexComponent.layerName);

        return component;
    }

    protected PolygonComponent createMeshComponent(Entity entity, PrototypeObject vo)
    {
        PolygonComponent component = new PolygonComponent();
        if(vo.shape != null)
        {
            component.vertices = vo.shape.polygons.clone();
            entity.add(component);

            return component;
        }
        return null;
    }

    protected void createGameComponents(Entity entity, PrototypeObject po)
    {
        String customVars = po.customVars;
        if (!customVars.equals(""))
        {
            List<String> listVars = Arrays.asList(customVars.split(";"));
            Map<String, String> setVars = new HashMap<String, String>();
            for (String keyVars: listVars)
            {
                setVars.put(keyVars.split(":")[0], keyVars.split(":")[1]);
            }
            if(!setVars.get("button").equals(""))
            {
                createButton(entity, setVars.get("button"), po);
            }
        }
        String[] tags = po.tags;
        if (tags != null)
        {
            for (int i = 0; i < tags.length; i++)
            {
                // текстовые метки
                if (tags[i].equals("score"))
                {
                    ScoreComponent scoreComponent = new ScoreComponent();
                    scoreComponent.scoreLabel = entity.getComponent(LabelComponent.class);
                    entity.add(scoreComponent);
                } else if (tags[i].equals("fails"))
                {
                    FailsComponent failsComponent = new FailsComponent();
                    failsComponent.failsLabel = entity.getComponent(LabelComponent.class);
                    entity.add(failsComponent);
                } else if (tags[i].equals("message"))
                {
                    MessageComponent messageComponent = new MessageComponent();
                    messageComponent.messageLabel = entity.getComponent(LabelComponent.class);
                    entity.add(messageComponent);
                } else if (tags[i].equals("timer"))
                {
                    TimerLabelComponent timerLabelComponent = new TimerLabelComponent();
                    timerLabelComponent.timerLabel = entity.getComponent(LabelComponent.class);
                    entity.add(timerLabelComponent);
                } else if (tags[i].equals("logging"))
                {
                    LoggingComponent loggingComponent = new LoggingComponent();
                    loggingComponent.loggingLabel = entity.getComponent(LabelComponent.class);
                    entity.add(loggingComponent);
                } else if (tags[i].equals("pausecounter"))
                {
                    //
                }

                // метки для окошек
                else if (tags[i].equals("gameover"))
                {
                    entity.getComponent(MainItemComponent.class).visible = false;
                }else if (tags[i].equals("gamemenu"))
                {
                    entity.getComponent(MainItemComponent.class).visible = false;
                }else if (tags[i].equals("pausedwindow"))
                {
                    entity.getComponent(MainItemComponent.class).visible = false;
                }

                // игровые метки
                else if (tags[i].equals("respawn"))
                {
                    createPlacesForRespawn(entity, po, entity.getComponent(DimensionsComponent.class));
                }
                else if (tags[i].equals("positionScore"))
                {
                    // запоминание позиции поля для очков
                    GameParameters.positionScore = new Vector2(po.x, po.y);
                }
                else if (tags[i].equals("positionFails"))
                {
                    // запоминание позиции поля для жизни
                    GameParameters.positionFail = new Vector2(po.x, po.y);
                }
            }
        }
    }

    protected PlaceEnemyRespawnComponent createPlacesForRespawn(Entity entity, PrototypeObject po, DimensionsComponent dimensionsComponent)
    {
        final PlaceEnemyRespawnComponent enemyRespawnComponent = new PlaceEnemyRespawnComponent();
        enemyRespawnComponent.x = po.x;
        enemyRespawnComponent.y = po.y;
        entity.add(enemyRespawnComponent);
        return enemyRespawnComponent;
    }

    public void createEnemyComponent(final Entity entity, final TransformComponent po, GameParameters.ENEMY typeEnemy)
    {
        DimensionsComponent dimensionsComponent = entity.getComponent(DimensionsComponent.class);
        final EnemyComponent enemyComponent = new EnemyComponent(typeEnemy);
        enemyComponent.timerLife = -1;
        enemyComponent.actor = new Actor();
        enemyComponent.actor.setBounds(po.x, po.y, dimensionsComponent.width, dimensionsComponent.height);
        switch (typeEnemy)
        {
            case GOODEYE:
                enemyComponent.actor.addListener(new ClickListener()
                {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                    {
                        ResourceManager.soundManager.playSoundEyePop();
                        GameParameters.score++;
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.ADDEYE, GameParameters.positionScore.x, GameParameters.positionScore.y, 1, 1, null);
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.GREENTEXT, enemyComponent.actor.getX()+(float)Math.random()*100, enemyComponent.actor.getY()+(float)Math.random()*100, 1, 1, "+1");
                        enemyComponent.isAlive = false;
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.SMASHEDGREENEYE, enemyComponent.actor.getX(), enemyComponent.actor.getY(), po.scaleX, po.scaleY, null);
                        SceneLoader.engine.getEntity(enemyComponent.placeRespawnID).getComponent(PlaceEnemyRespawnComponent.class).isBusy = false;
                        ResourceManager.monsterController.hideOne(GameParameters.ENEMY.GOODEYE);
                        return true;
                    }
                });
                break;
            case EVILEYE:
                enemyComponent.actor.addListener(new ClickListener()
                {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                    {
                        ResourceManager.soundManager.playSoundBadEyePop();
                        GameParameters.fails--;
                        //SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.DELEYE, GameParameters.positionFail.x, GameParameters.positionFail.y, null);
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.DELEYE, 0, 0, 1, 1, null);
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.REDTEXT, enemyComponent.actor.getX()+(float)Math.random()*100,
enemyComponent.actor.getY()+(float)Math.random()*100, 1, 1, "-1");
                        Gdx.input.vibrate(200);
                        enemyComponent.isAlive = false;
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.SMASHEDORANGEEYE, enemyComponent.actor.getX(), enemyComponent.actor.getY(), po.scaleX, po.scaleY, null);
                        SceneLoader.engine.getEntity(enemyComponent.placeRespawnID).getComponent(PlaceEnemyRespawnComponent.class).isBusy = false;
                        ResourceManager.monsterController.hideOne(GameParameters.ENEMY.EVILEYE);
                        return true;
                    }
                });
                break;
            case GOODFLY:
                enemyComponent.actor.addListener(new ClickListener()
                {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                    {
                        ResourceManager.soundManager.stopThisSoundInstance(enemyComponent.sound);
                        ResourceManager.soundManager.playSoundFlyPop();
                        // TODO
                        // take bonus
                        GameParameters.score += 10;
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.ADDEYE, GameParameters.positionScore.x, GameParameters.positionScore.y, 1, 1,null);
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.GREENTEXT, enemyComponent.actor.getX()+(float)Math.random()*100, enemyComponent.actor.getY()+(float)Math.random()*100,1, 1, "+10");
                        enemyComponent.isAlive = false;
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.DEADGOODFLY, enemyComponent.actor.getX(), enemyComponent.actor.getY(),1, 1, null);
                        entity.getComponent(TransformComponent.class).x = -1000;
                        entity.getComponent(TransformComponent.class).y = -1000;
                        ResourceManager.monsterController.hideOne(GameParameters.ENEMY.GOODFLY);
                        return true;
                    }
                });
                break;
            case EVILFLY:
                enemyComponent.actor.addListener(new ClickListener()
                {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                    {
                        ResourceManager.soundManager.playSoundFlyPop();
                        GameParameters.score += 15;
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.ADDEYE, GameParameters.positionScore.x, GameParameters.positionScore.y,1, 1, null);
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.GREENTEXT, enemyComponent.actor.getX()+(float)Math.random()*100, enemyComponent.actor.getY()+(float)Math.random()*100,1, 1, "+15");
                        enemyComponent.isAlive = false;
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.DEADEVILFLY, enemyComponent.actor.getX(), enemyComponent.actor.getY(),1, 1,
 null);
                        entity.getComponent(TransformComponent.class).x = -1000;
                        entity.getComponent(TransformComponent.class).y = -1000;
                        ResourceManager.monsterController.hideOne(GameParameters.ENEMY.EVILFLY);
                        return true;
                    }
                });
                break;
            case CAT:
                enemyComponent.actor.addListener(new ClickListener()
                {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                    {
                        ResourceManager.soundManager.playSoundCatTouch();
                        GameParameters.score += 15;
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.ADDEYE, GameParameters.positionScore.x, GameParameters.positionScore.y,1, 1, null);
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.GREENTEXT, enemyComponent.actor.getX()+(float)Math.random()*100, enemyComponent.actor.getY()+(float)Math.random()*100, 1, 1,"+15");
                        enemyComponent.isAlive = false;
                        entity.getComponent(TransformComponent.class).x = -1000;
                        entity.getComponent(TransformComponent.class).y = -1000;
                        ResourceManager.monsterController.hideOne(GameParameters.ENEMY.CAT);
                        return true;
                    }
                });
                break;
            case SPIDER:
                enemyComponent.actor.addListener(new ClickListener()
                {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                    {
                        ResourceManager.soundManager.playSoundFlyPop();
                        GameParameters.score += 15;
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.ADDEYE, GameParameters.positionScore.x, GameParameters.positionScore.y, 1, 1,null);
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.GREENTEXT, enemyComponent.actor.getX()+(float)Math.random()*100, enemyComponent.actor.getY()+(float)Math.random()*100, 1, 1,"+15");
                        enemyComponent.isAlive = false;
                        SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.DEADSPIDER, enemyComponent.actor.getX(), enemyComponent.actor.getY(), 1, 1,
null);
                        entity.getComponent(TransformComponent.class).x = -1000;
                        entity.getComponent(TransformComponent.class).y = -1000;
                        ResourceManager.monsterController.hideOne(GameParameters.ENEMY.SPIDER);
                        return true;
                    }
                });
        }
        entity.add(enemyComponent);
    }

    private void createButton(Entity entity, String type, PrototypeObject po)
    {
        DimensionsComponent dimensionsComponent = entity.getComponent(DimensionsComponent.class);
        GameParameters.Buttons typeButton;
        if(type.equals("none")) typeButton = GameParameters.Buttons.NONE;
        else if(type.equals("start"))
        {
            typeButton = GameParameters.Buttons.START;
            entity.add(new MovementMenuComponent(po.x, po.y));
        }
        else if(type.equals("exit"))
        {
            typeButton = GameParameters.Buttons.EXIT;
            entity.add(new MovementMenuComponent(po.x, po.y));
            entity.getComponent(MovementMenuComponent.class).changeDirection();
        }
        else if(type.equals("settings"))
        {
            typeButton = GameParameters.Buttons.SETTINGS;
            entity.add(new MovementMenuComponent(po.x, po.y));
            entity.getComponent(MovementMenuComponent.class).changeDirection();
        }
        else if(type.equals("levels"))
        {
            typeButton = GameParameters.Buttons.LEVELS;
            entity.add(new MovementMenuComponent(po.x, po.y));
        }
        else if(type.equals("backtomenu")) typeButton = GameParameters.Buttons.BACKTOMENU;
        else if(type.equals("back")) typeButton = GameParameters.Buttons.BACK;
        else if(type.equals("mainmenu")) typeButton = GameParameters.Buttons.MAINMENU;
        else if(type.equals("pause")) typeButton = GameParameters.Buttons.PAUSE;
        else if(type.equals("mute")) typeButton = GameParameters.Buttons.MUTE;
        else
        {
            typeButton = GameParameters.Buttons.NONE;
            System.out.println("cant find button [" + type + "]");
        }
        final ButtonComponent buttonComponent = new ButtonComponent();
        buttonComponent.typeButton = typeButton;
        buttonComponent.actor = new Actor();
        buttonComponent.actor.setBounds(po.x, po.y, dimensionsComponent.width, dimensionsComponent.height);
        buttonComponent.actor.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                ResourceManager.soundManager.playSoundButtonTouch();
                buttonComponent.isTouched = true;
                return true;
            }
        });
        entity.add(buttonComponent);
    }

    public void createEffectComponent(Entity entity, float time, GameParameters.EFFECT nameEffect)
    {
        EffectComponent effectComponent = new EffectComponent(time);
        effectComponent.nameEffect = nameEffect;
        entity.add(effectComponent);
    }

    public void createMovementComponent(Entity entity, float posX, float posY)
    {
        MovementComponent movementComponent = new MovementComponent(posX, posY);
        entity.add(movementComponent);
    }

    public void createLifeCycle(final Entity entity, GameParameters.ENEMY typeEnemy)
    {
        LifeCicleComponent lifeCicleComponent = new LifeCicleComponent(entity);
        // добавление скриптов для жизненого цикла
        switch (typeEnemy)
        {
            case EVILFLY:
                lifeCicleComponent.setAction(LifeCicleComponent.LIFECICLE.NORMAL, LifeCicleComponent.KOGDA.AFTER, new SomeAction()
                    {
                        @Override
                        public void setAction()
                        {
                            System.out.println("booom badabooom");
                            EnemyComponent enemyComponent = entity.getComponent(EnemyComponent.class);
                            Actor actor = enemyComponent.actor;
                            SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.BLOWEVILEYE, actor.getX(), actor.getY(),1, 1, null);
                            GameParameters.fails--;
                            SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.DELEYE, 0, 0,1, 1, null);
                            SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.REDTEXT, enemyComponent.actor.getX()+(float)Math.random()*100, enemyComponent.actor.getY()+(float)Math.random()*100,1, 1, "-1");
                        }
                    });
                break;
        }
        entity.add(lifeCicleComponent);
    }

    public void createShaderComponent(Entity entity, String nameShader, float timer, float u_float)
    {
        ShaderComponent shaderComponent;
        if(u_float == 0) shaderComponent = ResourceManager.shaderManager.createShader(nameShader);
        else shaderComponent = ResourceManager.shaderManager.createShader(nameShader, u_float);
        if(timer != 0)
        {
            shaderComponent.setTimer(timer);
            entity.add(shaderComponent.timerComponent);
        }

        if (entity.getComponent(NodeComponent.class) != null)
        {
            SnapshotArray<Entity> children = entity.getComponent(NodeComponent.class).children;
            for (int i=0; i<children.size; i++)
            {
                if (children.get(i).getComponent(NodeComponent.class) != null) createShaderComponent(children.get(i), nameShader, timer, u_float);
                else children.get(i).add(shaderComponent);
            }
        }else entity.add(shaderComponent);
    }
}
