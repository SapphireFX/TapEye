package com.sapphirefx.tapeye.ashley.factory.prototypes;

import com.badlogic.ashley.core.Entity;
import com.sapphirefx.tapeye.ashley.components.ColorComponent;
import com.sapphirefx.tapeye.ashley.components.MainItemComponent;
import com.sapphirefx.tapeye.ashley.components.PhysicBodyComponent;
import com.sapphirefx.tapeye.ashley.components.PolygonComponent;
import com.sapphirefx.tapeye.ashley.components.ShaderComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;
import com.sapphirefx.tapeye.ashley.components.ZIndexComponent;

import java.util.Arrays;

/**
 * Created by sapphire on 19.10.15.
 */
public class PrototypeObject
{
    public int uniqueId = -1;
	public String itemIdentifier = "";
	public String itemName = "";
    public String[] tags = null;
    public String customVars = "";
	public float x;
	public float y;
	public float scaleX	=	1f;
	public float scaleY	=	1f;
	public float originX	=	Float.NaN;
	public float originY	=	Float.NaN;
	public float rotation;
	public int zIndex = 0;
	public String layerName = "";
	public float[] tint = {1, 1, 1, 1};

    public ShapeObject shape = null;

	public String shaderName = "";

    public PrototypePhysics physics = null;


    public PrototypeObject()
    {
    }

    public PrototypeObject(PrototypeObject obj)
    {
        uniqueId = obj.uniqueId;
        itemIdentifier = new String(obj.itemIdentifier);
        itemName = new String(obj.itemName);
        if(obj.tags != null) tags = Arrays.copyOf(obj.tags, obj.tags.length);
        customVars = new String(obj.customVars);
        x = obj.x;
        y = obj.y;
        scaleX = obj.scaleX;
        scaleY = obj.scaleY;
        originX = obj.originX;
        originY = obj.originY;
        rotation = obj.rotation;
        zIndex = obj.zIndex;
        layerName = new String(obj.layerName);
        if(obj.tint != null) tint = Arrays.copyOf(obj.tint, obj.tint.length);
        shaderName = new String(obj.shaderName);

        if(physics != null) physics = new PrototypePhysics(obj.physics);
        if(obj.shape != null) shape = obj.shape.clone();
    }

    public void loadFromEntity(Entity entity)
    {
        MainItemComponent mainItemComponent = entity.getComponent(MainItemComponent.class);
        uniqueId = mainItemComponent.uniqueId;
        itemIdentifier = mainItemComponent.itemIdentifier;
        itemName = mainItemComponent.libraryLink;
        tags = new String[mainItemComponent.tags.size()];
        tags = mainItemComponent.tags.toArray(tags);
        customVars = mainItemComponent.customVars;

        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
        x = transformComponent.x;
        y = transformComponent.y;
        scaleX = transformComponent.scaleX;
        scaleY = transformComponent.scaleY;
        originX = transformComponent.originX;
        originY = transformComponent.originY;
        rotation = transformComponent.rotation;

        ColorComponent colorComponent = entity.getComponent(ColorComponent.class);
        tint = new float[4];
		tint[0] = colorComponent.color.r;
		tint[1] = colorComponent.color.g;
		tint[2] = colorComponent.color.b;
		tint[3] = colorComponent.color.a;

        ZIndexComponent ZIndexComponent = entity.getComponent(ZIndexComponent.class);
        zIndex = ZIndexComponent.getZIndex();
        layerName = ZIndexComponent.layerName;

        PhysicBodyComponent physicBodyComponent = entity.getComponent(PhysicBodyComponent.class);
        if(physicBodyComponent != null)
        {
            physics = new PrototypePhysics();
            physics.loadFromComponent(physicBodyComponent);
        }

        ShaderComponent shaderComponent = entity.getComponent(ShaderComponent.class);
        if(shaderComponent != null && shaderComponent.shaderName != null)
        {
            shaderName = shaderComponent.shaderName;
        }

        PolygonComponent polygonComponent = entity.getComponent(PolygonComponent.class);
		if(polygonComponent != null && polygonComponent.vertices != null)
        {
			shape = new ShapeObject();
			shape.polygons = polygonComponent.vertices;
		}
    }
}
