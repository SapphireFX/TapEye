package com.sapphirefx.tapeye.ashley.factory.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.World;
import com.sapphirefx.tapeye.ashley.components.CompositeTransformComponent;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.LayerMapComponent;
import com.sapphirefx.tapeye.ashley.components.NodeComponent;
import com.sapphirefx.tapeye.ashley.factory.ComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.EntityFactory;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeCompositeObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeLayerObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeObject;
import com.sapphirefx.tapeye.resources.IResourceRetriever;
import com.sapphirefx.tapeye.resources.SceneLoader;

import box2dLight.RayHandler;

/**
 * Created by sapphire on 19.10.15.
 */
public class CompositeComponentFactory extends ComponentFactory
{

    public CompositeComponentFactory(IResourceRetriever rm)
    {
        super(rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, PrototypeObject po)
    {
        createCommonComponents(entity, po, EntityFactory.COMPOSITE_TYPE);
        if(root != null)
        {
            createParentNodeComponent(root, entity);
        }
        createNodeComponent(root, entity);
        createPhysicsComponents(entity, po);
        createCompositeComponents(entity, (PrototypeCompositeObject) po);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, PrototypeObject po)
    {
        DimensionsComponent dimensionsComponent = new DimensionsComponent();
        dimensionsComponent.width = ((PrototypeCompositeObject) po).width;
        dimensionsComponent.height = ((PrototypeCompositeObject) po).height;

        entity.add(dimensionsComponent);
        return dimensionsComponent;
    }

    @Override
    protected void createNodeComponent(Entity root, Entity entity)
    {
        if(root != null)
        {
            super.createNodeComponent(root, entity);
        }

        NodeComponent node = new NodeComponent();
        entity.add(node);
    }

    protected void createCompositeComponents(Entity entity, PrototypeCompositeObject cpo)
    {
        CompositeTransformComponent compositeTransform = new CompositeTransformComponent();

        LayerMapComponent layerMap = new LayerMapComponent();
        if(cpo.composite.layers.size() == 0)
        {
            cpo.composite.layers.add(PrototypeLayerObject.createDefault());
        }
        layerMap.setLayers(cpo.composite.layers);

        entity.add(compositeTransform);
        entity.add(layerMap);
    }
}
