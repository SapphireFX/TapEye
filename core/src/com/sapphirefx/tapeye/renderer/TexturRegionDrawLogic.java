package com.sapphirefx.tapeye.renderer;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sapphirefx.tapeye.ashley.components.ColorComponent;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.EffectComponent;
import com.sapphirefx.tapeye.ashley.components.EnemyComponent;
import com.sapphirefx.tapeye.ashley.components.MainItemComponent;
import com.sapphirefx.tapeye.ashley.components.ShaderComponent;
import com.sapphirefx.tapeye.ashley.components.TextureComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;
import com.sapphirefx.tapeye.ashley.factory.ComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.EntityFactory;

/**
 * Created by sapphire on 19.10.15.
 */
public class TexturRegionDrawLogic implements Drawable
{
    private ComponentMapper<ColorComponent> tintComponentComponentMapper;
    private ComponentMapper<TextureComponent> textureRegionMapper;
    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<DimensionsComponent> dimensionsComponentComponentMapper;
    private ComponentMapper<ShaderComponent> shaderComponentMapper;

    public TexturRegionDrawLogic()
    {
        tintComponentComponentMapper = ComponentMapper.getFor(ColorComponent.class);
        textureRegionMapper = ComponentMapper.getFor(TextureComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
        dimensionsComponentComponentMapper = ComponentMapper.getFor(DimensionsComponent.class);
        shaderComponentMapper = ComponentMapper.getFor(ShaderComponent.class);
    }

    @Override
    public void draw(Batch batch, Entity entity, float parentAlpha)
    {

        TextureComponent entityTextureRegionComponent = textureRegionMapper.get(entity);
        ShaderComponent shaderComponent = entity.getComponent(ShaderComponent.class);
        MainItemComponent mainItemComponent = entity.getComponent(MainItemComponent.class);
        EffectComponent effectComponent = entity.getComponent(EffectComponent.class);
        if (effectComponent != null)
        {
            System.out.println(effectComponent.nameEffect);
        }
        if(shaderComponent != null)
        {
            //System.out.println("WOW i try use shader");
            if(shaderComponent.getShader() != null)
            {
                batch.setShader(shaderComponent.getShader());
                GL20 gl = Gdx.gl20;
                int error;
                if ((error = gl.glGetError()) != GL20.GL_NO_ERROR)
                {
                    Gdx.app.log("opengl", "Error: " + error);
                    Gdx.app.log("opengl", shaderComponent.getShader().getLog());
                    throw new RuntimeException( ": glError " + error);
                }
                shaderComponent.SetUniforms();
            }
        }
/*
        if(shaderComponentMapper.has(entity))
        {
            System.out.println("WOW i try use shader");
            ShaderComponent shaderComponent = shaderComponentMapper.get(entity);
            if(shaderComponent.getShader() != null)
            {
                System.out.println("WOW i use shader");
                batch.setShader(shaderComponent.getShader());

                GL20 gl = Gdx.gl20;
                int error;
                if ((error = gl.glGetError()) != GL20.GL_NO_ERROR)
                {
                    Gdx.app.log("opengl", "Error: " + error);
                    Gdx.app.log("opengl", shaderComponent.getShader().getLog());
                    throw new RuntimeException( ": glError " + error);
                }
            }
        }
*/
        if(entityTextureRegionComponent.polygonSprite != null)
        {
            drawTiledPolygonSprite(batch, entity);
        } else
        {
            drawSprite(batch, entity, parentAlpha);
        }
        drawSprite(batch, entity, parentAlpha);

        if(shaderComponent != null)
        {
            batch.setShader(null);
        }
/*
        if(shaderComponentMapper.has(entity))
        {
            batch.setShader(null);
        }
*/
    }

    public void drawSprite(Batch batch, Entity entity, float parentAlpha)
    {
        ColorComponent tintComponent = tintComponentComponentMapper.get(entity);
        TransformComponent entityTransformComponent = transformMapper.get(entity);
        TextureComponent entityTextureRegionComponent = textureRegionMapper.get(entity);
        DimensionsComponent dimensionsComponent = dimensionsComponentComponentMapper.get(entity);



        batch.setColor(tintComponent.color.r, tintComponent.color.g, tintComponent.color.b, tintComponent.color.a * parentAlpha);
        batch.draw(entityTextureRegionComponent.region,
                entityTransformComponent.x, entityTransformComponent.y,
                entityTransformComponent.originX, entityTransformComponent.originY,
                dimensionsComponent.width, dimensionsComponent.height,
                entityTransformComponent.scaleX, entityTransformComponent.scaleY,
                entityTransformComponent.rotation);
    }

    public void drawPolygonSprite(Batch batch, Entity entity)
    {
        ColorComponent colorComponent = tintComponentComponentMapper.get(entity);
        TransformComponent entityTransformComponent = transformMapper.get(entity);
        TextureComponent entityTextureRegionComponent = textureRegionMapper.get(entity);

        DimensionsComponent dimensionsComponent = dimensionsComponentComponentMapper.get(entity);

        entityTextureRegionComponent.polygonSprite.setPosition(entityTransformComponent.x, entityTransformComponent.y);
        entityTextureRegionComponent.polygonSprite.setRotation(entityTransformComponent.rotation);
        entityTextureRegionComponent.polygonSprite.setOrigin(entityTransformComponent.originX, entityTransformComponent.originY);
        entityTextureRegionComponent.polygonSprite.setColor(colorComponent.color);
        entityTextureRegionComponent.polygonSprite.draw((PolygonSpriteBatch) batch);
    }

    public void drawTiledPolygonSprite(Batch batch, Entity entity)
    {
        batch.flush();
        ColorComponent colorComponent = tintComponentComponentMapper.get(entity);
        TransformComponent entityTransformComponent = transformMapper.get(entity);
        TextureComponent entityTextureRegionComponent = textureRegionMapper.get(entity);

        DimensionsComponent dimensionsComponent = dimensionsComponentComponentMapper.get(entity);
        float ppwu = dimensionsComponent.width/entityTextureRegionComponent.region.getRegionWidth();

        Vector2 atlasCoordsVector = new Vector2(entityTextureRegionComponent.region.getU(), entityTextureRegionComponent.region.getV());
        Vector2 atlasSizeVector = new Vector2(entityTextureRegionComponent.region.getU2()-entityTextureRegionComponent.region.getU(), entityTextureRegionComponent.region.getV2()-entityTextureRegionComponent.region.getV());

        batch.getShader().setUniformi("isRepeat", 1);
        batch.getShader().setUniformf("atlasCoord", atlasCoordsVector);
        batch.getShader().setUniformf("atlasSize", atlasSizeVector);

        batch.setColor(colorComponent.color);
        entityTextureRegionComponent.polygonSprite.setOrigin(entityTransformComponent.originX * ppwu, entityTransformComponent.originY * ppwu);
        entityTextureRegionComponent.polygonSprite.setPosition(entityTransformComponent.x, entityTransformComponent.y);
        entityTextureRegionComponent.polygonSprite.setRotation(entityTransformComponent.rotation);
        entityTextureRegionComponent.polygonSprite.setScale(ppwu);
        entityTextureRegionComponent.polygonSprite.draw((PolygonSpriteBatch) batch);
        batch.flush();
        batch.getShader().setUniformi("isRepeat", 0);

    }
}
