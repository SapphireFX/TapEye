package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sapphirefx.tapeye.ashley.factory.prototypes.ShapeObject;
import com.sapphirefx.tapeye.ashley.tools.PolygonUtils;

/**
 * Created by sapphire on 19.10.15.
 */
public class DimensionsComponent implements Component
{
    public float width = 0;
    public float height = 0;

    public Rectangle boundBox;
    public Polygon polygon;

    @Override
    public String toString()
    {
        return "[width="+width+"][height="+height+"][boundBox="+boundBox+"][polygon="+polygon+"]";
    }

    public void setPolygon(PolygonComponent polygonComponent)
    {
        Vector2[] verticesArray = PolygonUtils.mergeTouchingPolygonsToOne(polygonComponent.vertices);
        float[] vertices = new float[verticesArray.length*2];
        for(int i  = 0; i < verticesArray.length; i++)
        {
            vertices[i*2] = (verticesArray[i].x);
            vertices[i*2+1] = (verticesArray[i].y);
        }
        polygon = new Polygon(vertices);
    }

    public void setFromShape(ShapeObject shape)
    {
        Vector2 minPoint = new Vector2();
        Vector2 maxPoint = new Vector2();
        if(shape.polygons != null)
        {
            for(int i = 0; i < shape.polygons.length; i++)
            {
                for(int j = 0; j < shape.polygons[i].length; j++)
                {
                    if(i == 0 && j == 0)
                    {
                        minPoint.x = shape.polygons[i][j].x;
                        minPoint.y = shape.polygons[i][j].y;
                        maxPoint.x = shape.polygons[i][j].x;
                        maxPoint.y = shape.polygons[i][j].y;
                    }
                    if(minPoint.x > shape.polygons[i][j].x) minPoint.x = shape.polygons[i][j].x;
                    if(minPoint.y > shape.polygons[i][j].y) minPoint.y = shape.polygons[i][j].y;
                    if(maxPoint.x < shape.polygons[i][j].x) maxPoint.x = shape.polygons[i][j].x;
                    if(maxPoint.y < shape.polygons[i][j].y) maxPoint.y = shape.polygons[i][j].y;
                }
            }
            width = maxPoint.x - minPoint.x;
            height = maxPoint.y - minPoint.y;
        }
    }
}
