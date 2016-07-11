package com.sapphirefx.tapeye.resources;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.sapphirefx.tapeye.ashley.factory.prototypes.SceneObject;
import com.sapphirefx.tapeye.tools.MySkin;
import com.sapphirefx.tapeye.tools.ProjectInfo;
import com.sapphirefx.tapeye.tools.ResolutionEntry;

/**
 * Created by sapphire on 19.10.15.
 */
public interface IResourceRetriever
{
    public TextureRegion getTextureRegion(String name);
    public ParticleEffect getParticleEffect(String name);
    public TextureAtlas getSkeletonAtlas(String name);
    public FileHandle getSkeletonJSON(String name);
    public FileHandle getSCMLFile(String name);
    public TextureAtlas getSpriteAnimation(String name);
    public BitmapFont getBitmapFont(String name, int size);
    public MySkin getSkin();

    public SceneObject getSceneVO(String sceneName);
    public ProjectInfo getProjectVO();

    public ResolutionEntry getLoadedResolution();
    public ShaderProgram getShaderProgram(String shaderName);
}
