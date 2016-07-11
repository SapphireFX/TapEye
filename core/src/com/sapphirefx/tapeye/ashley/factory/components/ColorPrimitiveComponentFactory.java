package com.sapphirefx.tapeye.ashley.factory.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.PolygonComponent;
import com.sapphirefx.tapeye.ashley.components.TextureComponent;
import com.sapphirefx.tapeye.ashley.factory.ComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.EntityFactory;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeObject;
import com.sapphirefx.tapeye.ashley.tools.ComponentRetriever;
import com.sapphirefx.tapeye.resources.IResourceRetriever;
import com.sapphirefx.tapeye.resources.SceneLoader;

import box2dLight.RayHandler;

/**
 * Created by sapphire on 22.12.2015.
 */
public class ColorPrimitiveComponentFactory extends ComponentFactory
{
    public ColorPrimitiveComponentFactory(IResourceRetriever rm)
    {
        super(rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, PrototypeObject vo)
    {
        createCommonComponents(entity, vo, EntityFactory.COLOR_PRIMITIVE);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);

        createTextureRegionComponent(entity, vo);

        TextureComponent textureRegionComponent = ComponentRetriever.get(entity, TextureComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        PolygonComponent polygonComponent = ComponentRetriever.get(entity, PolygonComponent.class);
        dimensionsComponent.setPolygon(polygonComponent);
        float ppwu = dimensionsComponent.width/textureRegionComponent.region.getRegionWidth();
        textureRegionComponent.setPolygonSprite(polygonComponent, 1f/ppwu);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, PrototypeObject vo)
    {
        DimensionsComponent component = new DimensionsComponent();
        component.setFromShape(vo.shape);

        entity.add(component);

        return component;
    }

    protected TextureComponent createTextureRegionComponent(Entity entity, PrototypeObject vo)
    {
        TextureComponent component = new TextureComponent();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion textureRegion = new TextureRegion(texture);
        component.region = textureRegion;
        component.isRepeat = false;
        component.isPolygon = true;
        entity.add(component);

        return component;
    }
}
