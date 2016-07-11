package com.sapphirefx.tapeye.ashley.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sapphirefx.tapeye.ashley.components.EnemyComponent;
import com.sapphirefx.tapeye.ashley.components.LifeCicleComponent;
import com.sapphirefx.tapeye.resources.ResourceManager;
import com.sapphirefx.tapeye.tools.GameParameters;

/**
 * Created by sapphire on 25.02.2016.
 * Эта система должна обновлять все параметря жизни на игровом поле для все монстров
 * Вызывается только когда это необходимо
 */
public class UpdateLifeCicleEnemySystem extends IteratingSystem
{

    /**
     * конструктор
     */
    public UpdateLifeCicleEnemySystem()
    {
        super(Family.all(LifeCicleComponent.class).get());
    }

    @Override
    public void update(float deltaTime)
    {
        if (!ResourceManager.monsterController.needUpdateMonsters) return;

        super.update(deltaTime);

        ResourceManager.monsterController.needUpdateMonsters = false;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        LifeCicleComponent lifeCicleComponent = entity.getComponent(LifeCicleComponent.class);
        EnemyComponent enemyComponent = entity.getComponent(EnemyComponent.class);
        lifeCicleComponent.setTime(ResourceManager.monsterController.getMonster(enemyComponent.getTypeEnemy()).getLifeTime());
    }
}
