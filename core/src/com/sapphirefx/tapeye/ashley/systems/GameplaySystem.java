package com.sapphirefx.tapeye.ashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.sapphirefx.tapeye.ashley.components.EnemyComponent;
import com.sapphirefx.tapeye.ashley.components.PlaceEnemyRespawnComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;
import com.sapphirefx.tapeye.ashley.components.LifeCicleComponent;
import com.sapphirefx.tapeye.resources.ResourceManager;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.tools.GameParameters;

import java.util.Random;

/**
 * Created by sapphire on 20.12.2015.
 */
public class GameplaySystem extends EntitySystem
{
    // респавны глаз
    private Family respawnDotsFamily;
    private ImmutableArray<Entity> respawnDotsEntities;
    // враги
    private Family enemyFamily;
    private ImmutableArray<Entity> enemyEntities;

    private Random random;

    public GameplaySystem()
    {
        respawnDotsFamily = Family.all(PlaceEnemyRespawnComponent.class).get();
        enemyFamily = Family.all(EnemyComponent.class).get();
        random = new Random();
    }

    @Override
    public void addedToEngine (Engine engine)
    {
        respawnDotsEntities = engine.getEntitiesFor(respawnDotsFamily);
        enemyEntities = engine.getEntitiesFor(enemyFamily);
    }

    @Override
    public void removedFromEngine (Engine engine)
    {
        respawnDotsEntities = null;
        enemyEntities = null;
    }


    @Override
    public void update(float deltaTime)
    {
        if (GameParameters.fails == 0)
        {
            // могу показать popup окно только через actorsystem
            //sl.engine.getSystem(ActorSystem.class).setVisiblePopupWindow("gameover", true);
        }

        if (GameParameters.isPaused) return;
        // пока идет пояление сцены, то ничего больше не должно происходить на экране
        if (GameParameters.onShowWindow) return;
        // пока идет сокрытие сцены, то ничего больше не должно происходить на экране
        if (GameParameters.onHideWindow) return;

        ResourceManager.monsterController.tick2(deltaTime);

        ////////////////////////////////////////
        //      show some game elements
        ////////////////////////////////////////
        // showGoodEye
        if(ResourceManager.monsterController.goodEyeManager.isAvailible())
        {
            if(checkFreeRespawnDots() && ResourceManager.monsterController.goodEyeManager.tryShowOne())
            {
                int pointer = getPointOfRespawnEye();
                respawnEye(respawnDotsEntities.get(pointer).getComponent(PlaceEnemyRespawnComponent.class), ResourceManager.monsterController.goodEyeManager.getType());
            }
        }
        // showEvilEye
        if(ResourceManager.monsterController.evilEyeManager.isAvailible())
        {
            if(checkFreeRespawnDots() && ResourceManager.monsterController.evilEyeManager.tryShowOne())
            {
                int pointer = getPointOfRespawnEye();
                respawnEye(respawnDotsEntities.get(pointer).getComponent(PlaceEnemyRespawnComponent.class), ResourceManager.monsterController.evilEyeManager.getType());
            }
        }
        // showGoodFly
        if(ResourceManager.monsterController.goodFlyManager.isAvailible())
        {
            if(ResourceManager.monsterController.goodFlyManager.tryShowOne())
            {
                respawn(GameParameters.ENEMY.GOODFLY);
            }
        }
        // showEvilFly
        if(ResourceManager.monsterController.evilFlyManager.isAvailible())
        {
            if(ResourceManager.monsterController.evilFlyManager.tryShowOne())
            {
                respawn(GameParameters.ENEMY.EVILFLY);
            }
        }
        // show Cat
        if(ResourceManager.monsterController.catManager.isAvailible())
        {
            if(ResourceManager.monsterController.catManager.tryShowOne())
            {
                respawn(GameParameters.ENEMY.CAT);
            }
        }
        // show Spider
        if(ResourceManager.monsterController.spiderManager.isAvailible())
        {
            if(ResourceManager.monsterController.spiderManager.tryShowOne())
            {
                respawn(GameParameters.ENEMY.SPIDER);
            }
        }


        ////////////////////////////////////////
        //            process game elements
        ////////////////////////////////////////
        for (int i=0; i< enemyEntities.size(); i++)
        {
            processEnemyEntity(enemyEntities.get(i), deltaTime);
        }
    }

    /**
     * обработка каждого в отдельности
     */
    private void processEnemyEntity(Entity entity, float deltaTime)
    {
        LifeCicleComponent lifeCicleComponent = entity.getComponent(LifeCicleComponent.class);
        EnemyComponent enemyComponent = entity.getComponent(EnemyComponent.class);
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        if (enemyComponent.isAlive)
        {
            // время жизни
            lifeCicleComponent.ticktack(deltaTime);

            // двигаем во времени
            enemyComponent.move(deltaTime);

            // двигаем в пространстве согласно расположению актера
            transform.x = enemyComponent.actor.getX();
            transform.y = enemyComponent.actor.getY();
            transform.rotation = enemyComponent.actor.getRotation();
        }
    }

    /**
     * проверка есть ли мета для респавна глазиков
     */
    private boolean checkFreeRespawnDots()
    {
        for(int i=0; i<respawnDotsEntities.size(); i++)
        {
            if (!respawnDotsEntities.get(i).getComponent(PlaceEnemyRespawnComponent.class).isBusy) return true;
        }
        return false;
    }

    /**
     * получаем свободное место для респавна
     */
    private int getPointOfRespawnEye()
    {
        int choosen = random.nextInt(respawnDotsEntities.size()) + 1;
        int pointer = -1;
        while (pointer < 0)
        {
            for (int i=0; i<respawnDotsEntities.size(); i++)
            {
                if (!respawnDotsEntities.get(i).getComponent(PlaceEnemyRespawnComponent.class).isBusy) choosen--;
                if (choosen == 0)
                {
                    pointer = i;
                    break;
                }
            }
        }
        return pointer;
    }

    /**
     * респавним глазик в нужной точке
     */
    private void respawnEye(PlaceEnemyRespawnComponent respawnComponent, GameParameters.ENEMY type)
    {
        Entity enemyForRespawn = respawnComponent.enemyes.get(type);
        EnemyComponent enemyComponent = enemyForRespawn.getComponent(EnemyComponent.class);
        enemyComponent.isAlive = true; // оживляем врага
        enemyComponent.timerLife = 0; // обновляем счетчик жизни
        enemyForRespawn.getComponent(LifeCicleComponent.class).setTime(
                ResourceManager.monsterController.getMonster(
                        enemyForRespawn.getComponent(EnemyComponent.class).getTypeEnemy()).getLifeTime());
        enemyForRespawn.getComponent(LifeCicleComponent.class).show(); // запускаем жизненый цикл

        respawnComponent.isBusy = true; // делаем это место респы занятым
    }

    private void respawn(GameParameters.ENEMY type)
    {
        int numberAvailible = -1;
        for(int i=0; i<enemyEntities.size(); i++)
        {
            if(enemyEntities.get(i).getComponent(EnemyComponent.class).getTypeEnemy() == type)
            {
                if (!enemyEntities.get(i).getComponent(EnemyComponent.class).isAlive && !enemyEntities.get(i).getComponent(EnemyComponent.class).addedInStage)
                {
                    numberAvailible = i;
                    break;
                }
            }
        }
        if (numberAvailible > 0)
        {
            enemyEntities.get(numberAvailible).getComponent(EnemyComponent.class).launch();
            enemyEntities.get(numberAvailible).getComponent(LifeCicleComponent.class).setTime(
                    ResourceManager.monsterController.getMonster(
                            enemyEntities.get(numberAvailible).getComponent(EnemyComponent.class).getTypeEnemy()).getLifeTime());
            enemyEntities.get(numberAvailible).getComponent(LifeCicleComponent.class).show();
        }
    }
}
