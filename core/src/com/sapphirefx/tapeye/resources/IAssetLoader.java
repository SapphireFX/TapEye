package com.sapphirefx.tapeye.resources;

/**
 * Created by sapphire on 19.10.15.
 */
public interface IAssetLoader
{
    public void loadAtlasPack();
    public void loadParticleEffects();
    public void loadSpriteAnimations();
    public void loadSpineAnimations();
    public void loadFonts();
    public void loadShaders();
	void loadSpriterAnimations();
}
