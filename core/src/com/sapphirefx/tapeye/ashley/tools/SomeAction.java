package com.sapphirefx.tapeye.ashley.tools;

/**
 * Created by sapphire on 04.03.2016.
 */
public abstract class SomeAction
{
    /**
     * обязательно для задания
     */
    public abstract void setAction();

    /**
     * выполнение заданного действия
     */
    public void doAction()
    {
        setAction();
    }
}
