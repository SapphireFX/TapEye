package com.sapphirefx.tapeye.ashley.factory.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.World;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.TextureComponent;
import com.sapphirefx.tapeye.ashley.factory.ComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.EntityFactory;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeImageObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeObject;
import com.sapphirefx.tapeye.ashley.tools.ComponentRetriever;
import com.sapphirefx.tapeye.resources.IResourceRetriever;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.tools.ProjectInfo;
import com.sapphirefx.tapeye.tools.ResolutionEntry;

import box2dLight.RayHandler;

/**
 * Created by sapphire on 19.10.15.
 */
public class SimpleImageComponentFactory extends ComponentFactory
{
    public SimpleImageComponentFactory(IResourceRetriever rm)
    {
        super(rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, PrototypeObject po)
    {
        createCommonComponents( entity, po, EntityFactory.IMAGE_TYPE);
        createTextureRegionComponent(entity, (PrototypeImageObject) po);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, PrototypeObject po)
    {
        DimensionsComponent component = new DimensionsComponent();

        entity.add(component);
        return component;
    }

    protected TextureComponent createTextureRegionComponent(Entity entity, PrototypeImageObject io)
    {
        TextureComponent component = new TextureComponent();
        component.regionName = io.imageName;
        component.region = rm.getTextureRegion(io.imageName);
        component.isRepeat = io.isRepeat;
        component.isPolygon = io.isPolygon;

        ResolutionEntry resolutionEntry = rm.getLoadedResolution();
        ProjectInfo projectInfoVO = rm.getProjectVO();
        float multiplier = resolutionEntry.getMultiplier(rm.getProjectVO().originalResolution);

        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

        dimensionsComponent.width = (float) component.region.getRegionWidth() * multiplier / projectInfoVO.pixelToWorld;
        dimensionsComponent.height = (float) component.region.getRegionHeight() * multiplier / projectInfoVO.pixelToWorld;


        entity.add(component);

        return component;
    }
}
