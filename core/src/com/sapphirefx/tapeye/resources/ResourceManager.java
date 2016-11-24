package com.sapphirefx.tapeye.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Json;
import com.sapphirefx.tapeye.ashley.factory.prototypes.CompositeComponents;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeCompositeObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.SceneObject;
import com.sapphirefx.tapeye.levelsAndMonsters.ChapterManager;
import com.sapphirefx.tapeye.levelsAndMonsters.MonsterController;
import com.sapphirefx.tapeye.tools.MySkin;
import com.sapphirefx.tapeye.tools.ProjectInfo;
import com.sapphirefx.tapeye.tools.ResolutionEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
/**
 * Created by sapphire on 20.10.15.
 */
public class ResourceManager implements IResourceRetriever, IResourceLoader
{
    /**
     *  Paths (please change if different) this is the default structure exported from editor
     */
    public String packResolutionName = "orig";

    public String scenesPath = "scenes";
    public String particleEffectsPath = "particles";
    public String spriteAnimationsPath = "sprite_animations";
    public String spriterAnimationsPath = "spriter_animations";
    public String fontsPath = "freetypefonts";
    public String shadersPath = "shaders";

    public static SoundManager soundManager = SoundManager.getInstance();
    public static ShaderManager shaderManager = ShaderManager.getInstance();
    public static ChapterManager chapterManager = ChapterManager.getInstance();
    public static MonsterController monsterController = new MonsterController();

    protected float resMultiplier;

    protected ProjectInfo projectVO;

    protected ArrayList<String> preparedSceneNames = new ArrayList<String>();
    protected HashMap<String, SceneObject> loadedSceneVOs = new HashMap<String, SceneObject>();

    protected HashSet<String> particleEffectNamesToLoad = new HashSet<String>();
    protected HashSet<String> spineAnimNamesToLoad = new HashSet<String>();
    protected HashSet<String> spriteAnimNamesToLoad = new HashSet<String>();
    protected HashSet<String> spriterAnimNamesToLoad = new HashSet<String>();
    protected HashSet<FontSizePair> fontsToLoad = new HashSet<FontSizePair>();
    protected HashSet<String> shaderNamesToLoad = new HashSet<String>();

    protected TextureAtlas mainPack;
    protected HashMap<String, ParticleEffect> particleEffects = new HashMap<String, ParticleEffect>();

    protected HashMap<String, TextureAtlas> skeletonAtlases = new HashMap<String, TextureAtlas>();
    protected HashMap<String, FileHandle> skeletonJSON = new HashMap<String, FileHandle>();

    protected HashMap<String, TextureAtlas> spriteAnimations = new HashMap<String, TextureAtlas>();
    protected HashMap<String, FileHandle> spriterAnimations = new HashMap<String, FileHandle>();
    protected HashMap<FontSizePair, BitmapFont> bitmapFonts = new HashMap<FontSizePair, BitmapFont>();
    protected HashMap<String, ShaderProgram> shaderPrograms = new HashMap<String, ShaderProgram>();

    /**
     * Constructor does nothing
     */
    public ResourceManager()
    {
    }

    /**
     * Sets working resolution, please set before doing any loading
     * @param resolution String resolution name, default is "orig" later use resolution names created in editor
     */
    public void setWorkingResolution(String resolution)
    {
        ResolutionEntry resolutionObject = getProjectVO().getResolution("resolutionName");
        if(resolutionObject != null)
        {
            packResolutionName = resolution;
        }
    }

    /**
     * Easy use loader
     * Iterates through all scenes and schedules all for loading
     * Prepares all the assets to be loaded that are used in scheduled scenes
     * finally loads all the prepared assets
     */
    public void initAllResources()
    {
        loadProjectVO();
        for (int i = 0; i < projectVO.scenes.size(); i++)
        {
            loadSceneVO(projectVO.scenes.get(i).sceneName);
            scheduleScene(projectVO.scenes.get(i).sceneName);
        }
        prepareAssetsToLoad();
        loadAssets();
    }

    /**
     * Initializes scene by loading it's VO data object and loading all the assets needed for this particular scene only
     * @param sceneName - scene file name without ".dt" extension
     */
    public void initScene(String sceneName)
    {
        loadSceneVO(sceneName);
        scheduleScene(sceneName);
        prepareAssetsToLoad();
        loadAssets();
    }

    /**
     * Anloads scene from the memory, and clears all the freed assets
     *
     * @param sceneName - scene file name without ".dt" extension
     */
    public void unLoadScene(String sceneName)
    {
        unScheduleScene(sceneName);
        loadedSceneVOs.remove(sceneName);
        loadAssets();
    }

    /**
     * Schedules scene for later loading
     * if later prepareAssetsToLoad function will be called it will only prepare assets that are used in scheduled scene
     *
     * @param name - scene file name without ".dt" extension
     */
    public void scheduleScene(String name)
    {
        if (loadedSceneVOs.containsKey(name))
        {
            preparedSceneNames.add(name);
        } else {
            //TODO: Throw exception that scene was not loaded to be prepared for asseting
        }
    }


    /**
     * Unschedule scene from later loading
     *
     * @param name
     */
    public void unScheduleScene(String name)
    {
        preparedSceneNames.remove(name);
    }


    /**
     * Creates the list of uniqe assets used in all of the scheduled scenes,
     * removes all the duplicates, and makes list of assets that are only needed.
     *
     */
    public void prepareAssetsToLoad()
    {
        particleEffectNamesToLoad.clear();
        spineAnimNamesToLoad.clear();
        spriteAnimNamesToLoad.clear();
        spriterAnimNamesToLoad.clear();
        fontsToLoad.clear();
        shaderPrograms.clear();

        for (String preparedSceneName : preparedSceneNames)
        {
            CompositeComponents composite = loadedSceneVOs.get(preparedSceneName).composite;
            if (composite == null)
            {
                continue;
            }
            //
            String[] particleEffects = composite.getRecursiveParticleEffectsList();
            String[] spineAnimations = composite.getRecursiveSpineAnimationList();
            String[] spriteAnimations = composite.getRecursiveSpriteAnimationList();
            String[] shaderNames = composite.getRecursiveShaderList();
            FontSizePair[] fonts = composite.getRecursiveFontList();
            for(PrototypeCompositeObject library : projectVO.libraryItems.values())
            {
                FontSizePair[] libFonts = library.composite.getRecursiveFontList();
                Collections.addAll(fontsToLoad, libFonts);

                // loading particle effects used in library items
                String[] libEffects = library.composite.getRecursiveParticleEffectsList();
                Collections.addAll(particleEffectNamesToLoad, libEffects);
            }
            //
            Collections.addAll(particleEffectNamesToLoad, particleEffects);
            Collections.addAll(spineAnimNamesToLoad, spineAnimations);
            Collections.addAll(spriteAnimNamesToLoad, spriteAnimations);
            Collections.addAll(fontsToLoad, fonts);
            Collections.addAll(shaderNamesToLoad, shaderNames);
        }
    }

    /**
     * Loads all the scheduled assets into memory including
     * main atlas pack, particle effects, sprite animations, spine animations and fonts
     */
    public void loadAssets()
    {
        loadAtlasPack();
        loadParticleEffects();
        loadSpineAnimations();
        loadSpriteAnimations();
        loadSpriterAnimations();
        loadFonts();
        loadShaders();
    }

    @Override
    public void loadAtlasPack()
    {
        FileHandle packFile = Gdx.files.internal(packResolutionName + File.separator + "pack.atlas");
        if (!packFile.exists())
        {
            return;
        }
        mainPack = new TextureAtlas(packFile);
    }

    @Override
    public void loadParticleEffects()
    {
        // empty existing ones that are not scheduled to load
        for (String key : particleEffects.keySet())
        {
            if (!particleEffectNamesToLoad.contains(key))
            {
                particleEffects.remove(key);
            }
        }

        // load scheduled
        for (String name : particleEffectNamesToLoad)
        {
            ParticleEffect effect = new ParticleEffect();
            effect.load(Gdx.files.internal(particleEffectsPath + File.separator + name), mainPack, "");
            particleEffects.put(name, effect);
        }
    }

    @Override
    public void loadSpriteAnimations()
    {
        // empty existing ones that are not scheduled to load
        for (String key : spriteAnimations.keySet())
        {
            if (!spriteAnimNamesToLoad.contains(key))
            {
                spriteAnimations.remove(key);
            }
        }

        for (String name : spriteAnimNamesToLoad)
        {
            TextureAtlas animAtlas = new TextureAtlas(Gdx.files.internal(packResolutionName + File.separator + spriteAnimationsPath + File.separator + name + File.separator + name + ".atlas"));
            spriteAnimations.put(name, animAtlas);
        }
    }

    @Override
    public void loadSpineAnimations()
    {
        // TODO
    }

    @Override
    public void loadSpriterAnimations()
    {
    	// empty existing ones that are not scheduled to load
    	for (String key : spriterAnimations.keySet())
        {
    		if (!spriterAnimNamesToLoad.contains(key))
            {
    			spriterAnimations.remove(key);
    		}
    	}
    	for (String name : spriterAnimNamesToLoad)
        {
    		FileHandle animFile = Gdx.files.internal("orig" + File.separator + spriterAnimationsPath + File.separator + name + File.separator + name + ".scml");
    		spriterAnimations.put(name, animFile);
    	}
    }

    @Override
    public void loadFonts()
    {
    	//resolution related stuff
    	ResolutionEntry curResolution = getProjectVO().getResolution(packResolutionName);
        resMultiplier = 1;
    	if(!packResolutionName.equals("orig"))
        {
    		if(curResolution.base == 0)
            {
                resMultiplier = (float) curResolution.width / (float) getProjectVO().originalResolution.width;
    		} else{
                resMultiplier = (float) curResolution.height / (float) getProjectVO().originalResolution.height;
    		}
    	}

        // empty existing ones that are not scheduled to load
        for (FontSizePair pair : bitmapFonts.keySet())
        {
            if (!fontsToLoad.contains(pair))
            {
                bitmapFonts.remove(pair);
            }
        }

        for (FontSizePair pair : fontsToLoad)
        {
            loadFont(pair);
        }
    }

    public void loadFont(FontSizePair pair)
    {
        FileHandle fontFile;
        fontFile = Gdx.files.internal(fontsPath + File.separator + pair.fontName + ".ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Math.round(pair.fontSize * resMultiplier);
        BitmapFont font = generator.generateFont(parameter);
        bitmapFonts.put(pair, font);
    }

    @Override
    public SceneObject loadSceneVO(String sceneName)
    {
        FileHandle file = Gdx.files.internal(scenesPath + File.separator + sceneName + ".dt");
        Json json = new Json();
        SceneObject sceneVO = json.fromJson(SceneObject.class, file.readString());

        //System.out.println(sceneVO);
        loadedSceneVOs.put(sceneName, sceneVO);

        return sceneVO;
    }

    public void unLoadSceneVO(String sceneName)
    {
        loadedSceneVOs.remove(sceneName);
    }

    @Override
    public ProjectInfo loadProjectVO()
    {

        FileHandle file = Gdx.files.internal("project.dt");
        Json json = new Json();
        projectVO = json.fromJson(ProjectInfo.class, file.readString());

        return projectVO;
    }

    @Override
	public void loadShaders()
    {
    	// empty existing ones that are not scheduled to load
        for (String key : shaderPrograms.keySet())
        {
            if (!shaderNamesToLoad.contains(key))
            {
            	shaderPrograms.get(key).dispose();
            	shaderPrograms.remove(key);
            }
        }

        for (String name : shaderNamesToLoad)
        {
            ShaderProgram shaderProgram = new ShaderProgram(Gdx.files.internal(shadersPath + File.separator + name + ".vert"), Gdx.files.internal(shadersPath + File.separator + name + ".frag"));
            shaderPrograms.put(name, shaderProgram);
        }
	}

    /**
     * Following methods are for retriever interface, which is intended for runtime internal use
     * to retrieve any already loaded into memory asset for rendering
     */




    @Override
    public TextureRegion getTextureRegion(String name)
    {
        return mainPack.findRegion(name);
    }

    @Override
    public ParticleEffect getParticleEffect(String name)
    {
        return new ParticleEffect(particleEffects.get(name));
    }

    @Override
    public TextureAtlas getSkeletonAtlas(String name)
    {
        return skeletonAtlases.get(name);
    }

    @Override
    public FileHandle getSkeletonJSON(String name)
    {
        return skeletonJSON.get(name);
    }

    @Override
    public TextureAtlas getSpriteAnimation(String name)
    {
        return spriteAnimations.get(name);
    }

    @Override
    public BitmapFont getBitmapFont(String name, int size)
    {
        return bitmapFonts.get(new FontSizePair(name, size));
    }

    @Override
    public MySkin getSkin()
    {
        return null;
    }

    @Override
    public SceneObject getSceneVO(String sceneName)
    {
        return loadedSceneVOs.get(sceneName);
    }

    @Override
    public ProjectInfo getProjectVO()
    {
        return projectVO;
    }

    @Override
    public ResolutionEntry getLoadedResolution()
    {
        if(packResolutionName.equals("orig"))
        {
            return getProjectVO().originalResolution;
        }
        return getProjectVO().getResolution(packResolutionName);
    }

    public void dispose()
    {
        mainPack.dispose();
    }

	@Override
	public FileHandle getSCMLFile(String name)
    {
		return spriterAnimations.get(name);
	}

	@Override
	public ShaderProgram getShaderProgram(String shaderName)
    {
		// TODO Auto-generated method stub
		return null;
	}
}
