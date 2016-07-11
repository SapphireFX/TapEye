package com.sapphirefx.tapeye.ashley.factory.pool;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sapphire on 23.01.2016.
 */
public class EntityPool
{
    private Engine engine; // ссылка на движок
    private List<Entity> availibleEntities; // доступные и не добавленые еще в движок для обработки
    private List<Entity> blockedEntities; // недоступные и он уже в движке и обрабатываются
    private int sizeOfPool; // размер пула
    private boolean isBusy; // флаг блокировки, что сейчас идет работа с пулом

    public EntityPool(Engine engine, int size)
    {
        this.engine = engine;
        availibleEntities = new LinkedList<Entity>();
        blockedEntities = new LinkedList<Entity>();
        sizeOfPool = size;
        isBusy = false;
    }

    public boolean isFull()
    {
        return availibleEntities.size() + blockedEntities.size() >= sizeOfPool;
    }

    /**
     * заполняем пул клонами
     */
    public boolean add(Entity entity)
    {
        if (availibleEntities.size() + blockedEntities.size() < sizeOfPool)
        {
            availibleEntities.add(entity);
            return true;
        }else return false;

    }

    /**
     * сделать доступным после использоваия
     */
    public boolean free(Entity entity)
    {
        isBusy = true;
        boolean result;

        if (blockedEntities.contains(entity))
        {
            availibleEntities.add(entity);
            blockedEntities.remove(entity);
            result = true;
        }else
        {
            result = false;
        }

        isBusy = false;
        return result;
    }

    /**
     * берем из пула
     */
    public Entity get()
    {
        isBusy = true;
        Entity result = null;
        if(availibleEntities.size()>0)
        {
            result = availibleEntities.get(0);
            availibleEntities.remove(0);
            blockedEntities.add(result);
        }
        isBusy = false;
        return result;
    }

    public void clear()
    {
        isBusy = true;

        for(int i=0; i< availibleEntities.size(); i++) engine.removeEntity(availibleEntities.get(i));
        for(int i=0; i< blockedEntities.size(); i++) engine.removeEntity(blockedEntities.get(i));
        availibleEntities.clear();
        blockedEntities.clear();

        isBusy = false;
    }
}
