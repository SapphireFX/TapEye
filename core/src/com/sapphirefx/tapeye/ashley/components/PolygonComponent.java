package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by sapphire on 19.10.15.
 */
public class PolygonComponent implements Component
{
    public Vector2[][] vertices;

    public void makeRectangle(float width, float height)
    {
        Vector2[] points = new Vector2[4];
        points[0] = new Vector2(0, 0);
        points[1] = new Vector2(0, height);
        points[2] = new Vector2(width, height);
        points[3] = new Vector2(width, 0);

        vertices = new Vector2[1][4];
        vertices[0] = points;
    }
}
