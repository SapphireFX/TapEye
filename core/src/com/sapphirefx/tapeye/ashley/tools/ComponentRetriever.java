package com.sapphirefx.tapeye.ashley.tools;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.sapphirefx.tapeye.ashley.components.animation.SpriteAnimationComponent;
import com.sapphirefx.tapeye.ashley.components.animation.SpriteAnimationStateComponent;
import com.sapphirefx.tapeye.ashley.components.ButtonComponent;
import com.sapphirefx.tapeye.ashley.components.CompositeTransformComponent;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.EnemyComponent;
import com.sapphirefx.tapeye.ashley.components.LabelComponent;
import com.sapphirefx.tapeye.ashley.components.LayerMapComponent;
import com.sapphirefx.tapeye.ashley.components.LightObjectComponent;
import com.sapphirefx.tapeye.ashley.components.MainItemComponent;
import com.sapphirefx.tapeye.ashley.components.NinePatchComponent;
import com.sapphirefx.tapeye.ashley.components.NodeComponent;
import com.sapphirefx.tapeye.ashley.components.ParentNodeComponent;
import com.sapphirefx.tapeye.ashley.components.ParticleComponent;
import com.sapphirefx.tapeye.ashley.components.PhysicBodyComponent;
import com.sapphirefx.tapeye.ashley.components.PolygonComponent;
import com.sapphirefx.tapeye.ashley.components.ScissorComponent;
import com.sapphirefx.tapeye.ashley.components.ShaderComponent;
import com.sapphirefx.tapeye.ashley.components.ColorComponent;
import com.sapphirefx.tapeye.ashley.components.TextureComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;
import com.sapphirefx.tapeye.ashley.components.ViewPortComponent;
import com.sapphirefx.tapeye.ashley.components.ZIndexComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sapphire on 19.10.15.
 */
public class ComponentRetriever
{
    /**
     * single static instance of this class
     */
    private static ComponentRetriever instance;

    /**
     * Unique map of mappers that can be accessed by component class
     */
    private Map<Class, ComponentMapper<? extends Component>> mappers = new HashMap<Class, ComponentMapper<? extends Component>>();

    /**
     * Private constructor
     */
    private ComponentRetriever() {

    }

    /**
     * This is called only during first initialisation and populates map of mappers of all known Component mappers
     * it might be a good idea to use Reflections library later to create this list from all classes in components package of runtime,
     * all in favour?
     */
    private void init()
    {
    	mappers.put(LightObjectComponent.class, ComponentMapper.getFor(LightObjectComponent.class));
    	mappers.put(ParticleComponent.class, ComponentMapper.getFor(ParticleComponent.class));
        mappers.put(LabelComponent.class, ComponentMapper.getFor(LabelComponent.class));
    	mappers.put(PolygonComponent.class, ComponentMapper.getFor(PolygonComponent.class));
    	mappers.put(PhysicBodyComponent.class, ComponentMapper.getFor(PhysicBodyComponent.class));
        mappers.put(SpriteAnimationComponent.class, ComponentMapper.getFor(SpriteAnimationComponent.class));
        mappers.put(SpriteAnimationStateComponent.class, ComponentMapper.getFor(SpriteAnimationStateComponent.class));
        mappers.put(CompositeTransformComponent.class, ComponentMapper.getFor(CompositeTransformComponent.class));
        mappers.put(DimensionsComponent.class, ComponentMapper.getFor(DimensionsComponent.class));
        mappers.put(LayerMapComponent.class, ComponentMapper.getFor(LayerMapComponent.class));
        mappers.put(MainItemComponent.class, ComponentMapper.getFor(MainItemComponent.class));
        mappers.put(NinePatchComponent.class, ComponentMapper.getFor(NinePatchComponent.class));
        mappers.put(NodeComponent.class, ComponentMapper.getFor(NodeComponent.class));
        mappers.put(ParentNodeComponent.class, ComponentMapper.getFor(ParentNodeComponent.class));
        mappers.put(ScissorComponent.class, ComponentMapper.getFor(ScissorComponent.class));
        mappers.put(TextureComponent.class, ComponentMapper.getFor(TextureComponent.class));
        mappers.put(ColorComponent.class, ComponentMapper.getFor(ColorComponent.class));
        mappers.put(TransformComponent.class, ComponentMapper.getFor(TransformComponent.class));
        mappers.put(ViewPortComponent.class, ComponentMapper.getFor(ViewPortComponent.class));
        mappers.put(ZIndexComponent.class, ComponentMapper.getFor(ZIndexComponent.class));
        mappers.put(EnemyComponent.class, ComponentMapper.getFor(EnemyComponent.class));
        mappers.put(ShaderComponent.class, ComponentMapper.getFor(ShaderComponent.class));
        mappers.put(ButtonComponent.class, ComponentMapper.getFor(ButtonComponent.class));
    }

    /**
     * Short version of getInstance singleton variation, but with private access,
     * as there is no reason to get instance of this class, but only use it's public methods
     *
     * @return ComponentRetriever only instance
     */
    private static synchronized ComponentRetriever self()
    {
        if(instance == null)
        {
            instance = new ComponentRetriever();

            // Important to initialize during first creation, to populate mappers map
            instance.init();
        }

        return instance;
    }

    /**
     * @return returns Map of mappers, for internal use only
     */
    private Map<Class, ComponentMapper<? extends Component>> getMappers()
    {
        return mappers;
    }

    /**
     * Retrieves Component of provided type from a provided entity
     * @param entity of type Entity to retrieve component from
     * @param type of the component
     * @param <T>
     *
     * @return Component subclass instance
     */
    @SuppressWarnings("unchecked")
    public static <T extends Component> T get(Entity entity, Class<T> type)
    {
        return (T)self().getMappers().get(type).get(entity);
    }


    @SuppressWarnings("unchecked")
    public static Collection<Component> getComponents(Entity entity)
    {
        Collection<Component> components = new ArrayList<Component>();
        for (ComponentMapper<? extends Component> mapper : self().getMappers().values())
        {
            if(mapper.get(entity) != null) components.add(mapper.get(entity));
        }

        return components;
    }

    /**
     * This is to add a new mapper type externally, in case of for example implementing the plugin system,
     * where components might be initialized on the fly
     *
     * @param type
     */
    @SuppressWarnings("unchecked")
    public static void addMapper(Class type)
    {
        self().getMappers().put(type, ComponentMapper.getFor(type));
    }
}
