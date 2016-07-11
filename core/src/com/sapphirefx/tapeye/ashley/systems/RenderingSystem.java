package com.sapphirefx.tapeye.ashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.sapphirefx.tapeye.ashley.components.ButtonComponent;
import com.sapphirefx.tapeye.ashley.components.ColorComponent;
import com.sapphirefx.tapeye.ashley.components.CompositeTransformComponent;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.EnemyComponent;
import com.sapphirefx.tapeye.ashley.components.LayerMapComponent;
import com.sapphirefx.tapeye.ashley.components.MainItemComponent;
import com.sapphirefx.tapeye.ashley.components.NodeComponent;
import com.sapphirefx.tapeye.ashley.components.ParentNodeComponent;
import com.sapphirefx.tapeye.ashley.components.PlaceEnemyRespawnComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;
import com.sapphirefx.tapeye.ashley.components.ViewPortComponent;
import com.sapphirefx.tapeye.ashley.components.ZIndexComponent;
import com.sapphirefx.tapeye.ashley.tools.ComponentRetriever;
import com.sapphirefx.tapeye.renderer.DrawableLogicMapper;

/**
 * Created by sapphire on 19.10.15.
 */
public class RenderingSystem extends IteratingSystem
{
    private final float TIME_STEP = 1f/60;

    private Batch batch; // область отрисовки
    private Camera cam;  // камера
    private DrawableLogicMapper drawableLogicMapper;

    private FrameBuffer fbo;
    private TextureRegion fboRegion;

    private ComponentMapper<ViewPortComponent> viewPortMapper = ComponentMapper.getFor(ViewPortComponent.class);
    private ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<CompositeTransformComponent>  compositeTransformMapper = ComponentMapper.getFor(CompositeTransformComponent.class);
    private ComponentMapper<ParentNodeComponent> parentNodeMapper = ComponentMapper.getFor(ParentNodeComponent.class);
    private ComponentMapper<NodeComponent> nodeMapper = ComponentMapper.getFor(NodeComponent.class);
    private ComponentMapper<MainItemComponent> mainItemComponentMapper = ComponentMapper.getFor(MainItemComponent.class);
    private ComponentMapper<PlaceEnemyRespawnComponent> placeEnemyRespawnComponentComponentMapper = ComponentMapper.getFor(PlaceEnemyRespawnComponent.class);

    public RenderingSystem(Batch batch)
    {
        super(Family.all(ViewPortComponent.class).get());
        this.batch = batch;
        drawableLogicMapper = new DrawableLogicMapper();

        fbo = new FrameBuffer(Pixmap.Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        fboRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboRegion.flip(false, true);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        ViewPortComponent viewPortComponent = entity.getComponent(ViewPortComponent.class);
        cam = viewPortComponent.viewPort.getCamera();


        //fbo.begin();

        Gdx.gl20.glClearColor(77f / 255f, 46f / 255f, 97f / 255f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        drawRecursively(entity, 1f);
        batch.end();
        //fbo.end();
/*
        batch.begin();
        //batch.setShader();
        batch.setProjectionMatrix(cam.combined);
        batch.draw(fboRegion, 0, 0);
        batch.end();
        // TODO: постобработку можно сюда попробовать впихнуть
*/
    }

    private void drawRecursively(Entity rootEntity, float parentAlpha)
    {
        //currentComposite = rootEntity;
        CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);

        PlaceEnemyRespawnComponent respawn = placeEnemyRespawnComponentComponentMapper.get(rootEntity);
        if(respawn != null) return;// если есть компонент респы врагов, то его самого не надо показывать (он только метка)

        if (curCompositeTransformComponent.transform)
        {
            computeTransform(rootEntity);
            applyTransform(rootEntity, batch);
        }

        // проверка "живые" объекты
        if(rootEntity.getComponent(EnemyComponent.class) != null && !rootEntity.getComponent(EnemyComponent.class).isAlive)
        {
            return;
        }

        ColorComponent colorComponent = ComponentRetriever.get(rootEntity, ColorComponent.class);
        parentAlpha *= colorComponent.color.a;

        drawChildren(rootEntity, batch, curCompositeTransformComponent, parentAlpha);

        // подгоняем actor под размеры композит еомпонентаи его позицию
        ButtonComponent buttonComponent = rootEntity.getComponent(ButtonComponent.class);
        if(buttonComponent != null)
        {
            TransformComponent transformComponent = rootEntity.getComponent(TransformComponent.class);
            DimensionsComponent dimensionsComponent = rootEntity.getComponent(DimensionsComponent.class);
            buttonComponent.actor.setBounds(transformComponent.x, transformComponent.y,
                    dimensionsComponent.width * transformComponent.scaleX, dimensionsComponent.height * transformComponent.scaleY);
        }
        EnemyComponent enemyComponent = rootEntity.getComponent(EnemyComponent.class);
        if(enemyComponent != null)
        {
            TransformComponent transformComponent = rootEntity.getComponent(TransformComponent.class);
            DimensionsComponent dimensionsComponent = rootEntity.getComponent(DimensionsComponent.class);
            enemyComponent.actor.setBounds(transformComponent.x, transformComponent.y,
                    dimensionsComponent.width * transformComponent.scaleX, dimensionsComponent.height * transformComponent.scaleY);
        }

        if (curCompositeTransformComponent.transform) resetTransform(rootEntity, batch);
    }

    private void drawChildren(Entity rootEntity, Batch batch, CompositeTransformComponent curCompositeTransformComponent, float parentAlpha)
    {
        NodeComponent nodeComponent = nodeMapper.get(rootEntity);
        Entity[] children = nodeComponent.children.begin();
        if (curCompositeTransformComponent.transform)
        {
            for (int i = 0, n = nodeComponent.children.size; i < n; i++)
            {
                Entity child = children[i];

                LayerMapComponent rootLayers = ComponentRetriever.get(rootEntity, LayerMapComponent.class);
                ZIndexComponent childZIndexComponent = ComponentRetriever.get(child, ZIndexComponent.class);
                if(!rootLayers.isVisible(childZIndexComponent.layerName))
                {
                    continue;
                }

                MainItemComponent childMainItemComponent = mainItemComponentMapper.get(child);
                if(!childMainItemComponent.visible)
                {
                    continue;
                }
                int entityType = childMainItemComponent.entityType;

                NodeComponent childNodeComponent = nodeMapper.get(child);

                if(childNodeComponent == null)
                {
                    //Find logic from the mapper and draw it
                    drawableLogicMapper.getDrawable(entityType).draw(batch, child, parentAlpha);
                }else
                {

                    //Step into Composite
                    drawRecursively(child, parentAlpha);
                }
            }
        } else
        {
            // No transform for this group, offset each child.
            TransformComponent compositeTransform = transformM.get(rootEntity);

            float offsetX = compositeTransform.x, offsetY = compositeTransform.y;

			if(viewPortMapper.has(rootEntity))
            {
				offsetX = 0;
				offsetY = 0;
			}

            for (int i = 0, n = nodeComponent.children.size; i < n; i++)
            {
                Entity child = children[i];

                TransformComponent childTransformComponent = transformM.get(child);
                float cx = childTransformComponent.x, cy = childTransformComponent.y;
                childTransformComponent.x = cx + offsetX;
                childTransformComponent.y = cy + offsetY;

                NodeComponent childNodeComponent = nodeMapper.get(child);
                int entityType = mainItemComponentMapper.get(child).entityType;

                if(childNodeComponent == null)
                {
                    //Finde the logic from mapper and draw it
                    drawableLogicMapper.getDrawable(entityType).draw(batch, child, parentAlpha);
                }else
                {
                    //Step into Composite
                    drawRecursively(child, parentAlpha);
                }
                childTransformComponent.x = cx;
                childTransformComponent.y = cy;

                if(childNodeComponent != null)
                {
                    drawRecursively(child, parentAlpha);
                }
            }
        }
        nodeComponent.children.end();
    }

    /** Returns the transform for this group's coordinate system.
     * @param rootEntity */
    protected Matrix4 computeTransform (Entity rootEntity)
    {
        CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
        NodeComponent nodeComponent = nodeMapper.get(rootEntity);
        ParentNodeComponent parentNodeComponent = parentNodeMapper.get(rootEntity);
        TransformComponent curTransform = transformM.get(rootEntity);
        Affine2 worldTransform = curCompositeTransformComponent.worldTransform;
        //TODO origin thing
        float originX = 0;
        float originY = 0;
        float x = curTransform.x;
        float y = curTransform.y;
        float rotation = curTransform.rotation;
        float scaleX = curTransform.scaleX;
        float scaleY = curTransform.scaleY;

        worldTransform.setToTrnRotScl(x + originX, y + originY, rotation, scaleX, scaleY);
        if (originX != 0 || originY != 0) worldTransform.translate(-originX, -originY);

        // Find the first parent that transforms.

        CompositeTransformComponent parentTransformComponent = null;
        //NodeComponent parentNodeComponent;

        Entity parentEntity = null;
        if(parentNodeComponent != null)
        {
            parentEntity = parentNodeComponent.parentEntity;
        }

        if (parentEntity != null)
        {
            parentTransformComponent = compositeTransformMapper.get(parentEntity);
            worldTransform.preMul(parentTransformComponent.worldTransform);
        }

        curCompositeTransformComponent.computedTransform.set(worldTransform);
        return curCompositeTransformComponent.computedTransform;
    }

    protected void applyTransform (Entity rootEntity, Batch batch)
    {
        CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
        curCompositeTransformComponent.oldTransform.set(batch.getTransformMatrix());
        batch.setTransformMatrix(curCompositeTransformComponent.computedTransform);
    }

    protected void resetTransform (Entity rootEntity, Batch batch)
    {
        CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
        batch.setTransformMatrix(curCompositeTransformComponent.oldTransform);
    }
}
