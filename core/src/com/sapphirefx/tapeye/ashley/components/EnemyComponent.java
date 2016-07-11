package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sapphirefx.tapeye.resources.ResourceManager;
import com.sapphirefx.tapeye.tools.GameParameters;

import java.util.Random;

/**
 * Created by sapphire on 23.10.15.
 */
public class EnemyComponent implements Component
{
    public Actor actor = null;
    public long sound;
    private GameParameters.ENEMY typeEnemy;

    public float timerLife = 0;
    public boolean addedInStage = false;
    public boolean isAlive = false;
    public long placeRespawnID = -1;

    public float speed = 0;
    private Vector2 centr; // центр траектории движения
    private boolean isFirstHalfWay = true; // разбил на две половинки для простоты
    private int a; // параметр функции движения мухи
    private int directionX = 1;
    private int directionY = 1;
    private Random random;

    public EnemyComponent (GameParameters.ENEMY typeEnemy)
    {
        this.typeEnemy = typeEnemy;
        actor = new Actor();
        centr = new Vector2(100f, 100f);
        random = new Random();
        switch (typeEnemy)
        {
            case GOODEYE:
                speed = 1;
                break;
            case EVILEYE:
                speed = 1;
                break;
            case GOODFLY:
                a = 200;
                speed = 1f;
                break;
            case EVILFLY:
                a = 400;
                speed = 3;
                break;
            case CAT:
                speed = 1;
                break;
            case SPIDER:
                speed = 1;
                break;
            default:
        }
    }

    /*
    public EnemyComponent (EnemyComponent component)
    {
        timerLife = component.timerLife;
        actor = null;
        addedInStage = component.addedInStage;
        isAlive = component.isAlive;
        placeRespawnID = -1;
        typeEnemy = component.typeEnemy;
    }
    */

    /**
     * расчет скорости исходя из времени, которое враг должен провести на поле
     */
    public void calculateSpeed(float time)
    {
        switch (typeEnemy)
        {
            case GOODEYE:
            case EVILEYE:
            case CAT:
                // скорость движения не важна, т.к. они находятся на одном месте
                break;
            case GOODFLY:
                // t меняется на двух временных отрезках [-0.9;1] и [-1;1]
                // итого тратится на всю анимацию при speed=1 3,9 секунды
                // рассчитаем скорость
                speed = 3.9f / time;
                break;
            case EVILFLY:
                // t меняется на отрезке [0;10]
                // итого тратится на всю анимацию при speed=3 3,33 секунды
                // рассчитаем скорость
                speed = (3.33f*3) / time;
                break;
        }
    }

    /**
     * оживление и запуск врага
     */
    public void launch()
    {
        if(random.nextBoolean()) directionX = 1; else directionX = -1;
        if(random.nextBoolean()) directionY = 1; else directionY = -1;
        centr.set(random.nextInt(150)+100 , random.nextInt(400)+200);
        isFirstHalfWay = true;
        isAlive = true;
        timerLife = 0;

        if (typeEnemy == GameParameters.ENEMY.CAT)
        {
            lounchCat();
            ResourceManager.soundManager.playSoundCatShow();
        }
        if (typeEnemy == GameParameters.ENEMY.GOODFLY)
        {
            sound = ResourceManager.soundManager.playSoundFlyInAir();
        }
        if (typeEnemy == GameParameters.ENEMY.SPIDER)
        {
            centr.set(random.nextInt(720 - (int)actor.getWidth()), 1280 + (int)actor.getHeight());
            actor.setPosition(centr.x, centr.y);
        }
    }

    public void move(float delta)
    {
        timerLife += delta*speed;

        switch (typeEnemy)
        {
            case GOODEYE:
                moveGoodEye();
                break;
            case EVILEYE:
                moveEvilEye();
                break;
            case GOODFLY:
                moveGoodFly();
                break;
            case EVILFLY:
                moveEvilFly();
                break;
            case CAT:
                moveCat();
                // кот там где появился, там и остается
                break;
            case SPIDER:
                moveSpider();
                break;
            default:
                // по умолчанию никуда не двигаемся
        }
    }

    public GameParameters.ENEMY getTypeEnemy()
    {
        return typeEnemy;
    }

    @Override
    public String toString()
    {
        return "[timerLife="+timerLife+"][actor="+actor+"][addedInStage="+addedInStage+"][isAlive="+isAlive+"][placeRespawnID="+placeRespawnID+"]";
    }

    /**
     * движение зеленой мухи
     * петля декарта
     */
    private void moveGoodFly()
    {
        double t;

        if (isFirstHalfWay)
        {
            // изменение t [-0.9;1]
            t = timerLife - 0.9;
            if (t == -1) t = -1.0001;
            if (t > 1)
            {
                isFirstHalfWay = false;
                timerLife = 0;
                return;
            }
            actor.setX((float)( directionX*3*a*t   / (1 + t*t*t) ) + centr.x ) ;
            actor.setY((float)( directionY*3*a*t*t / (1 + t*t*t) ) + centr.y );
        } else
        {
            // изменение t [-1;1]
            t = 1 - timerLife;
            if (t == -1) t = -1.0001;
            if (t < -0.9)
            {
                isFirstHalfWay = true;
                return;
            }
            actor.setY((float)( directionY*3*a*t   / (1 + t*t*t)) + centr.y );
            actor.setX((float)( directionX*3*a*t*t / (1 + t*t*t)) + centr.x );
        }
    }

    /**
     * движение красной мухи
     * спираль
     */
    private void moveEvilFly()
    {
        actor.setX( (centr.x + directionX*(a-a*(timerLife/10))*(float)Math.sin(timerLife)) );
        actor.setY( (centr.y + directionY*(a-a*(timerLife/10))*(float)Math.cos(timerLife)) );
    }

    /**
     * движение зеленого глазика
     */
    private void moveGoodEye()
    {
        // стоит на месте
    }

    /**
     * движение красного глазика
     */
    private void moveEvilEye()
    {
        // стоит на месте
    }

    /**
     * движение котика
     */
    private void moveCat()
    {
        // стоит на месте
        actor.setPosition(centr.x, centr.y);
    }

    /**
     * движение паука
     */
    private void moveSpider()
    {
        actor.setY( (timerLife - 3) * (timerLife - 3)*100 + 300 );
    }

    /**
     * появление котика на игровом поле
     */
    private void lounchCat()
    {
        switch(random.nextInt(3))
        {
            case 0: // появление слева
                actor.setRotation(-90);
                centr.set(0, random.nextInt(1000) + (int)actor.getWidth());
                break;
            case 1: // появление снизу
                actor.setRotation(0);
                centr.set(random.nextInt(720 - (int)actor.getWidth()), 0);
                break;
            case 2: // появление справа
                actor.setRotation(90);
                centr.set(720, random.nextInt(1000));
                break;
        }
    }
}
