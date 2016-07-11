package com.sapphirefx.tapeye.ashley.factory.prototypes;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by sapphire on 19.10.15.
 */
public class ShapeObject
{
    public Vector2[][] polygons;
    public Circle[] circles;

    public ShapeObject clone()
    {
        ShapeObject newVo = new ShapeObject();
        Vector2 [][] target = new Vector2[polygons.length][];

        for (int i = 0; i < polygons.length; i++)
        {
            target[i] = new Vector2[polygons[i].length];
            for(int j=0;j<polygons[i].length;j++)
            {
                target[i][j] = polygons[i][j].cpy();
            }
        }
        newVo.polygons = target;
        return newVo;
    }
}
