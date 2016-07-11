package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.SnapshotArray;
import com.sapphirefx.tapeye.ashley.components.animation.SpriteAnimationComponent;
import com.sapphirefx.tapeye.ashley.components.animation.SpriteAnimationStateComponent;
import com.sapphirefx.tapeye.ashley.tools.SomeAction;
import com.sapphirefx.tapeye.resources.ResourceManager;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.tools.GameParameters;


/**
 * Created by sapphire on 23.02.2016.
 * Весь жизненый цикл заключается в трех этапах
 * 1) появление
 * 2) существование на экране
 * 3) исчезание
 *    а) само (вышло время)
 *    б) тыкнули на экране
 * на всех этапах надо запускать анимацию, т.к. она бывает разной (простая по кругу, или разовая и т.п.)
 * после окончания одного из этапов цикла, все связано с этим циклом надо скрыть, и показать зависимы от него если есть
 */
public class LifeCicleComponent implements Component
{
    public enum LIFECICLE {SHOW, NORMAL, HIDE, HIDETOUCH}
    public enum KOGDA {BEFORE, AFTER}

    private LifePeriod showPeriod;
    private LifePeriod normalPeriod;
    private LifePeriod hideSimplePeriod;
    private LifePeriod hideTouchPeriod;

    private LifePeriod currentPeriod;
    private float timeLeft;

    /**
     * конструктор
     * @param entity
     */
    public LifeCicleComponent(Entity entity)
    {
        // пробуем создать каждую в отдельности и если не получается, то делаем пустую
        NodeComponent nodeComponent = entity.getComponent(NodeComponent.class);
        if(nodeComponent != null)
        {
            SnapshotArray<Entity> childrens = nodeComponent.children;
            for (int i=0; i<childrens.size; i++)
            {
                ZIndexComponent zIndexComponent = childrens.get(i).getComponent(ZIndexComponent.class);
                if (zIndexComponent.layerName.equals("show"))
                {
                    showPeriod = new LifePeriod(LIFECICLE.SHOW, childrens.get(i));
                }else if (zIndexComponent.layerName.equals("normal"))
                {
                    normalPeriod = new LifePeriod(LIFECICLE.NORMAL, childrens.get(i));
                }else if (zIndexComponent.layerName.equals("hide"))
                {
                    hideSimplePeriod = new LifePeriod(LIFECICLE.HIDE, childrens.get(i));
                }else if (zIndexComponent.layerName.equals("hidetouch"))
                {
                    hideTouchPeriod = new LifePeriod(LIFECICLE.HIDETOUCH, childrens.get(i));
                }
            }
        }
        if (showPeriod == null)
        {
            //System.out.println("нет слоя с SHOW");
            showPeriod = new LifePeriod(LIFECICLE.SHOW);
        }
        if (normalPeriod == null)
        {
            //System.out.println("нет слоя с NORMAL");
            normalPeriod = new LifePeriod(LIFECICLE.NORMAL);
        }
        if (hideSimplePeriod == null)
        {
            //System.out.println("нет слоя с HIDE");
            hideSimplePeriod = new LifePeriod(LIFECICLE.HIDE);
        }
        if (hideTouchPeriod == null)
        {
            //System.out.println("нет слоя с HIDETOUCH");
            hideTouchPeriod = new LifePeriod(LIFECICLE.HIDETOUCH);
        }
    }

    /**
     * увеличиваем таймер жизни
     */
    public void ticktack(float delta)
    {
        timeLeft += delta;
        if(timeLeft > currentPeriod.lifeTime)
        {
            currentPeriod.onEnd();
            switch (currentPeriod.typePeriod)
            {
                case SHOW:
                    timeLeft = 0;
                    if(currentPeriod.action != null && currentPeriod.timingAction == KOGDA.AFTER) currentPeriod.action.doAction();
                    currentPeriod = normalPeriod;
                    if(currentPeriod.action != null && currentPeriod.timingAction == KOGDA.BEFORE) currentPeriod.action.doAction();
                    currentPeriod.onStart();
                    break;
                case NORMAL:
                    timeLeft = 0;
                    if(currentPeriod.action != null && currentPeriod.timingAction == KOGDA.AFTER) currentPeriod.action.doAction();
                    currentPeriod = hideSimplePeriod;
                    if(currentPeriod.action != null && currentPeriod.timingAction == KOGDA.BEFORE) currentPeriod.action.doAction();
                    currentPeriod.onStart();
                    break;
                case HIDE:
                    timeLeft = 0;
                    if(currentPeriod.action != null &&currentPeriod. timingAction == KOGDA.AFTER) currentPeriod.action.doAction();
                    killEnemyByTime();
                    currentPeriod = null;
                    break;
                case HIDETOUCH:
                    break;
            }
        }
    }

    /**
     * начало жизненого цикла
     */
    public void show()
    {
        endAll();
        timeLeft = 0;
        currentPeriod = showPeriod;
        if(currentPeriod.action != null && currentPeriod.timingAction == KOGDA.BEFORE) currentPeriod.action.doAction();
        currentPeriod.onStart();
    }

    /**
     * цикл касания объекта
     */
    public void touch()
    {
        endAll();
        timeLeft = 0;
        currentPeriod = hideTouchPeriod;
        currentPeriod.onStart();
    }

    /**
     * скрываем все
     */
    private void endAll()
    {
        showPeriod.onEnd();
        normalPeriod.onEnd();
        hideSimplePeriod.onEnd();
        hideTouchPeriod.onEnd();
    }

    /**
     * установка общего времени для всего объекта
     * тут его делим между составляющими жищненого цикла по своим правилам
     * @param fullTime
     */
    public void setTime(float fullTime)
    {
        float showTime = showPeriod.maxAnimationTime; // минимальное время для появления
        float hideTime = hideSimplePeriod.maxAnimationTime; // минимальное время для исчезания
        float normalTime = 0;
        EnemyComponent enemyComponent = null;

        if (showPeriod.entity != null) enemyComponent = showPeriod.entity.getComponent(ParentNodeComponent.class).parentEntity.getComponent(EnemyComponent.class);
        else if (normalPeriod.entity != null) enemyComponent = normalPeriod.entity.getComponent(ParentNodeComponent.class).parentEntity.getComponent(EnemyComponent.class);
        else  if (hideSimplePeriod.entity !=null) enemyComponent = hideSimplePeriod.entity.getComponent(ParentNodeComponent.class).parentEntity.getComponent(EnemyComponent.class); else
        {
            System.out.println("WARNING !!!! нельза задать время т.к. ниодной части жизненго цикла нет.");
            return;
        }

        if (showTime + hideTime < fullTime && normalPeriod.entity != null)
        {
            normalTime = fullTime - showTime - hideTime;
            showPeriod.setAnimationTime(showPeriod.maxAnimationTime);
            hideSimplePeriod.setAnimationTime(hideSimplePeriod.maxAnimationTime);
        }
        else
        {
            if (showTime>0 && hideTime>0)
            {// поидее надо пропорционально разделить, но пока делю ровно пополам
                showTime = hideTime = fullTime / 2;
                showPeriod.setAnimationTime(showTime);
                hideSimplePeriod.setAnimationTime(hideTime);
            }else if (showTime == 0)
            {
                hideTime = fullTime;
                hideSimplePeriod.setAnimationTime(hideTime);
            }else
            {
                showTime = fullTime;
                showPeriod.setAnimationTime(showTime);
            }
        }
        showPeriod.lifeTime = showTime;
        normalPeriod.lifeTime = normalTime;
        hideSimplePeriod.lifeTime = hideTime;

        // расчет скорости движения врага иходя из длительности
        if (enemyComponent != null) enemyComponent.calculateSpeed(fullTime);
    }

    /**
    * задание дествия, которое будет совершаться при активации определенного жизненого периода
     */
    public void setAction(LIFECICLE period, KOGDA kogda, SomeAction action)
    {
        switch (period)
        {
            case SHOW:
                showPeriod.action = action;
                showPeriod.timingAction = kogda;
                break;
            case NORMAL:
                normalPeriod.action = action;
                normalPeriod.timingAction = kogda;
                break;
            case HIDE:
                hideSimplePeriod.action = action;
                hideSimplePeriod.timingAction = kogda;
                break;
            case HIDETOUCH:
                hideTouchPeriod.action = action;
                hideTouchPeriod.timingAction = kogda;
                break;
        }
    }

    public void killEnemyByTime()
    {
        EnemyComponent enemyComponent = null;
        if (showPeriod.entity != null) enemyComponent = showPeriod.entity.getComponent(ParentNodeComponent.class).parentEntity.getComponent(EnemyComponent.class);
        else if (normalPeriod.entity != null) enemyComponent = normalPeriod.entity.getComponent(ParentNodeComponent.class).parentEntity.getComponent(EnemyComponent.class);
        else  if (hideSimplePeriod.entity !=null) enemyComponent = hideSimplePeriod.entity.getComponent(ParentNodeComponent.class).parentEntity.getComponent(EnemyComponent.class);
        if (enemyComponent == null)
        {
            System.out.println("WTF where enemy component ???");
            return;
        }

        enemyComponent.isAlive = false;
        switch (enemyComponent.getTypeEnemy())
        {
            case GOODEYE:
                // точка респы стала свободна
                SceneLoader.engine.getEntity(enemyComponent.placeRespawnID).getComponent(PlaceEnemyRespawnComponent.class).isBusy = false;
                // если этот глаз необходимо было раздавить, то вычитаем очки жизни
                GameParameters.fails--;
                SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.DELEYE, 0, 0, 1, 1, null);
                SceneLoader.entityFactory.createEffect(GameParameters.EFFECT.REDTEXT, enemyComponent.actor.getX()+(float)Math.random()*100, enemyComponent.actor.getY()+(float)Math.random()*100, 1, 1, "-1");
                Gdx.input.vibrate(200);
                break;
            case EVILEYE:
                // точка респы стала свободна
                SceneLoader.engine.getEntity(enemyComponent.placeRespawnID).getComponent(PlaceEnemyRespawnComponent.class).isBusy = false;
                break;
            case GOODFLY:
                break;
            case EVILFLY:
                break;
            case CAT:
                break;
            case SPIDER:
                break;
        }
        ResourceManager.monsterController.hideOne(enemyComponent.getTypeEnemy());
    }

    /**
     * класс переиода жизни
     */
    private class LifePeriod
    {
        public KOGDA timingAction;
        public SomeAction action;
        public LIFECICLE typePeriod;
        public float lifeTime;
        public float maxAnimationTime;

        private Entity entity;

        /**
         * конструктор
         * @param type
         * @param entity
         */
        public LifePeriod(LIFECICLE type, Entity entity)
        {
            typePeriod = type;
            this.entity = entity;

            maxAnimationTime = getMaxTimeAnimation();
        }

        /**
         * можно сказать пустое задание состояния
         */
        public LifePeriod(LIFECICLE type)
        {
            typePeriod = type;
            entity = null;
        }

        /**
         * возвращает максимальное время анимации
         */
        private float getMaxTimeAnimation()
        {
            if (entity == null) return 0; // вылетаем если запускать нечего

            float maxTime = 0;
            if (entity.getComponent(NodeComponent.class) != null)
            {
                SnapshotArray<Entity> children = entity.getComponent(NodeComponent.class).children;
                for (int i = 0; i < children.size; i++)
                {
                    // анимашки
                    if (children.get(i).getComponent(SpriteAnimationStateComponent.class) != null &&
                            (children.get(i).getComponent(SpriteAnimationStateComponent.class).currentAnimation.getPlayMode() == Animation.PlayMode.NORMAL ||
                                    children.get(i).getComponent(SpriteAnimationStateComponent.class).currentAnimation.getPlayMode() == Animation.PlayMode.REVERSED))
                    {
                        if (children.get(i).getComponent(SpriteAnimationStateComponent.class).currentAnimation.getAnimationDuration() > maxTime)
                        {
                            maxTime = children.get(i).getComponent(SpriteAnimationStateComponent.class).currentAnimation.getAnimationDuration();
                        }
                    }
                }
            } else
            {
                if (entity.getComponent(SpriteAnimationStateComponent.class) != null &&
                        (entity.getComponent(SpriteAnimationStateComponent.class).currentAnimation.getPlayMode() == Animation.PlayMode.NORMAL ||
                                entity.getComponent(SpriteAnimationStateComponent.class).currentAnimation.getPlayMode() == Animation.PlayMode.REVERSED))
                {
                    Animation animation = entity.getComponent(SpriteAnimationStateComponent.class).currentAnimation;
                    if (entity.getComponent(SpriteAnimationStateComponent.class).currentAnimation.getAnimationDuration() > maxTime)
                    {
                        maxTime = entity.getComponent(SpriteAnimationStateComponent.class).currentAnimation.getAnimationDuration();
                    }
                }
            }
            return maxTime;
        }

        /**
         * естанавливаем время для анимации
         */
        public void setAnimationTime(float time)
        {
            if (entity == null) return; // вылетаем если запускать нечего

            if (entity.getComponent(NodeComponent.class) != null)
            {
                SnapshotArray<Entity> children = entity.getComponent(NodeComponent.class).children;
                for (int i = 0; i < children.size; i++)
                {
                    // анимашки
                    if (children.get(i).getComponent(SpriteAnimationStateComponent.class) != null &&
                            (children.get(i).getComponent(SpriteAnimationStateComponent.class).currentAnimation.getPlayMode() == Animation.PlayMode.NORMAL ||
                                    children.get(i).getComponent(SpriteAnimationStateComponent.class).currentAnimation.getPlayMode() == Animation.PlayMode.REVERSED))
                    {
                        int countFrames = children.get(i).getComponent(SpriteAnimationStateComponent.class).currentAnimation.getKeyFrames().length;
                        Animation animation = children.get(i).getComponent(SpriteAnimationStateComponent.class).currentAnimation;
                        children.get(i).getComponent(SpriteAnimationComponent.class).fps = Math.round(1/(time / countFrames));
                        //animation.setFrameDuration(time / countFrames);
                        //System.out.println(animation.getKeyFrameIndex(time-0.01f));
                    }
                }
            } else
            {
                if (entity.getComponent(SpriteAnimationStateComponent.class) != null &&
                        (entity.getComponent(SpriteAnimationStateComponent.class).currentAnimation.getPlayMode() == Animation.PlayMode.NORMAL ||
                                entity.getComponent(SpriteAnimationStateComponent.class).currentAnimation.getPlayMode() == Animation.PlayMode.REVERSED))
                {
                    int countFrames = entity.getComponent(SpriteAnimationStateComponent.class).currentAnimation.getKeyFrames().length;
                    Animation animation = entity.getComponent(SpriteAnimationStateComponent.class).currentAnimation;
                    entity.getComponent(SpriteAnimationComponent.class).fps = Math.round(1/(time / countFrames));
                    //animation.setFrameDuration(time / countFrames);
                    //System.out.println(animation.getKeyFrameIndex(time-0.01f));
                }
            }
        }

        /**
         * показывания текущего состояния
         * делаем активными анимашки (сбрасывая счетчик анимации, чтобы они начались сначала)
         * делаем активными частички (сбрасывая счетчик анимации, чтобы они начались сначала)
         * делаем видимыми все элементы
         */
        public void onStart()
        {
            if (entity == null) return; // вылетаем если запускать нечего

            if (entity.getComponent(NodeComponent.class) != null)
            {
                SnapshotArray<Entity> children = entity.getComponent(NodeComponent.class).children;
                for (int i = 0; i < children.size; i++)
                {
                    // частички
                    if (children.get(i).getComponent(ParticleComponent.class) != null)
                    {
                        // включаем анимацию частиц
                        children.get(i).getComponent(ParticleComponent.class).particleEffect.reset();
                    }
                    // анимашки
                    if (children.get(i).getComponent(SpriteAnimationStateComponent.class) != null)
                    {
                        // включаем анимацию
                        children.get(i).getComponent(SpriteAnimationStateComponent.class).time = 0f;
                    }
                    // делаем видимыми все скрытое
                    children.get(i).getComponent(MainItemComponent.class).visible = true;
                }
            }else
            {
                // если есть анимашки
                if(entity.getComponent(SpriteAnimationStateComponent.class) != null)
                    entity.getComponent(SpriteAnimationStateComponent.class).time = 0;
                entity.getComponent(MainItemComponent.class).visible = true;
            }
        }

        /**
         * убираем со сцены
         */
        public void onEnd()
        {
            if (entity == null) return; // вылетаем если нет
            if (entity.getComponent(NodeComponent.class) != null)
            {
                SnapshotArray<Entity> children = entity.getComponent(NodeComponent.class).children;
                for (int i = 0; i < children.size; i++)
                {
                    // делаем невидимыми
                    children.get(i).getComponent(MainItemComponent.class).visible = false;
                }
            }else entity.getComponent(MainItemComponent.class).visible = false;
        }
    }
}
