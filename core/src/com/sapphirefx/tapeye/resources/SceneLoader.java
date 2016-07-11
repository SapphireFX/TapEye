package com.sapphirefx.tapeye.resources;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sapphirefx.tapeye.ashley.factory.EntityFactory;
import com.sapphirefx.tapeye.ashley.factory.prototypes.CompositeComponents;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeCompositeObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.SceneObject;
import com.sapphirefx.tapeye.ashley.components.MainItemComponent;
import com.sapphirefx.tapeye.ashley.systems.ActorSystem;
import com.sapphirefx.tapeye.ashley.systems.AnimationSystem;
import com.sapphirefx.tapeye.ashley.systems.EffectSystem;
import com.sapphirefx.tapeye.ashley.systems.GameplaySystem;
import com.sapphirefx.tapeye.ashley.systems.LabelSystem;
import com.sapphirefx.tapeye.ashley.systems.LayerSystem;
import com.sapphirefx.tapeye.ashley.systems.LightSystem;
import com.sapphirefx.tapeye.ashley.systems.MovementMenuSystem;
import com.sapphirefx.tapeye.ashley.systems.ParticleSystem;
import com.sapphirefx.tapeye.ashley.systems.RenderingSystem;
import com.sapphirefx.tapeye.ashley.systems.ResizeCompositeSystem;
import com.sapphirefx.tapeye.ashley.systems.SaveSystem;
import com.sapphirefx.tapeye.ashley.systems.ScoreCalculateSystem;
import com.sapphirefx.tapeye.ashley.systems.ShaderSystem;
import com.sapphirefx.tapeye.ashley.systems.TimerSystem;
import com.sapphirefx.tapeye.ashley.systems.UpdateLifeCicleEnemySystem;
import com.sapphirefx.tapeye.ashley.tools.ComponentRetriever;
import com.sapphirefx.tapeye.tools.GameParameters.*;
import com.sapphirefx.tapeye.tools.*;

/**
 * Created by sapphire on 20.10.15.
 */
public class SceneLoader
{
    private String curResolution = "orig";
	private static SceneObject sceneVO;
    private static ResolutionEntry resolution;
	private IResourceRetriever rm = null;

    public static Engine engine;
	public static EntityFactory entityFactory;
    public static Entity rootEntity;

	public Viewport viewport;
	private float pixelsPerWU = 1f;

    public InteranlCommands interanlCommands = InteranlCommands.NONE; // команды от систем на различные действия

	public SceneLoader()
    {
		ResourceManager rm = new ResourceManager();
        rm.initAllResources();

		this.rm = rm;

		engine = new Engine();
		initSceneLoader();
    }

    public SceneLoader(IResourceRetriever rm)
    {
        engine = new Engine();
		this.rm = rm;
		initSceneLoader();
    }

	/**
	 * this method is called when rm has loaded all data
     */
    private void initSceneLoader()
    {
        ProjectInfo projectVO = rm.getProjectVO();
        viewport = new ScalingViewport(Scaling.stretch, (float)projectVO.originalResolution.width/ pixelsPerWU,
                (float)projectVO.originalResolution.height / pixelsPerWU, new OrthographicCamera());
        resolution = rm.getLoadedResolution();
		addSystems();
        entityFactory = new EntityFactory(rm);
    }

	public void setResolution(String resolutionName)
    {
		ResolutionEntry resolution = getRm().getProjectVO().getResolution(resolutionName);
		if(resolution != null)
		{
			curResolution = resolutionName;
		}
	}

	public SceneObject getSceneVO()
	{
		return sceneVO;
	}

	public SceneObject loadScene(String sceneName)
	{
        ResourceManager.monsterController.resetAll();
        ResourceManager.monsterController.needUpdateMonsters = true;
		pixelsPerWU = rm.getProjectVO().pixelToWorld;
		engine.removeAllEntities();

		sceneVO = rm.getSceneVO(sceneName);

		if(sceneVO.composite == null)
		{
			sceneVO.composite = new CompositeComponents();
		}

        rootEntity = entityFactory.createRootEntity(sceneVO.composite, viewport);
		engine.addEntity(rootEntity);

		if(sceneVO.composite != null)
		{
			entityFactory.initAllChildren(engine, rootEntity, sceneVO.composite);
		}

        ResourceManager.monsterController.createEnemyes();

		return sceneVO;
	}

	private void addSystems()
	{
		AnimationSystem animationSystem = new AnimationSystem();
        RenderingSystem renderer = new RenderingSystem(new PolygonSpriteBatch(2000, createDefaultShader()));
        LayerSystem layerSystem = new LayerSystem();
        ParticleSystem particleSystem = new ParticleSystem();
        LightSystem lightSystem = new LightSystem();
		LabelSystem labelSystem = new LabelSystem();
        ResizeCompositeSystem resizeCompositeSystem = new ResizeCompositeSystem();
        ActorSystem actorSystem = new ActorSystem(viewport, this);
        ScoreCalculateSystem scoreCalculateSystem = new ScoreCalculateSystem();
		GameplaySystem gameplaySystem = new GameplaySystem();
		SaveSystem saveSystem = new SaveSystem(10);
        EffectSystem effectSystem = new EffectSystem();
		UpdateLifeCicleEnemySystem updateLifeCicleEnemySystem = new UpdateLifeCicleEnemySystem();
		MovementMenuSystem movementMenuSystem = new MovementMenuSystem();
		ShaderSystem shaderSystem = new ShaderSystem();
		TimerSystem timerSystem = new TimerSystem();

        engine.addSystem(layerSystem);
        engine.addSystem(animationSystem);
        engine.addSystem(particleSystem);
        engine.addSystem(lightSystem);
        engine.addSystem(labelSystem);
        engine.addSystem(resizeCompositeSystem);
		engine.addSystem(renderer);
		engine.addSystem(actorSystem);
        engine.addSystem(scoreCalculateSystem);
		engine.addSystem(gameplaySystem);
        engine.addSystem(saveSystem);
        engine.addSystem(effectSystem);
		engine.addSystem(updateLifeCicleEnemySystem);
		engine.addSystem(movementMenuSystem);
		engine.addSystem(shaderSystem);
		engine.addSystem(timerSystem);
	}

	public Entity loadFromLibrary(String libraryName)
	{
		ProjectInfo projectInfoVO = getRm().getProjectVO();
		PrototypeCompositeObject compositeItemVO = projectInfoVO.libraryItems.get(libraryName);

		if(compositeItemVO != null)
		{
			Entity entity = entityFactory.createEntity(null, compositeItemVO);
			return entity;
		}
		return null;
	}

    public PrototypeCompositeObject loadVoFromLibrary(String libraryName)
	{
        ProjectInfo projectInfoVO = getRm().getProjectVO();
		PrototypeCompositeObject compositeItemVO = projectInfoVO.libraryItems.get(libraryName);

       return compositeItemVO;
    }

    public void addComponentsByTagName(String tagName, Class componentClass)
	{
        ImmutableArray<Entity> entities = engine.getEntities();
        for(Entity entity: entities)
		{
            MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
            if(mainItemComponent.tags.contains(tagName))
			{
                try
				{
                    entity.add(ClassReflection.<Component>newInstance(componentClass));
                } catch (ReflectionException e)
				{
                    e.printStackTrace();
                }
            }
        }
    }


	public EntityFactory getEntityFactory() {
		return entityFactory;
	}

	public IResourceRetriever getRm() {
	 	return rm;
	 }

    public Engine getEngine() {
        return engine;
    }

	public Entity getRoot() {
		return rootEntity;
	}

    public static String getSceneName()
    {
        return sceneVO.sceneName;
    }

    public static ResolutionEntry getResolution()
    {
        return resolution;
    }
	/** Returns a new instance of the default shader used by SpriteBatch for GL2 when no shader is specified. */
	static public ShaderProgram createDefaultShader ()
	{
		String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
			+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
			+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
			+ "uniform mat4 u_projTrans;\n" //
			+ "varying vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "\n" //
			+ "void main()\n" //
			+ "{\n" //
			+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
			+ "   v_color.a = v_color.a * (255.0/254.0);\n" //
			+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
			+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
			+ "}\n";
		String fragmentShader = "#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n" //
			+ "varying LOWP vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "uniform sampler2D u_texture;\n" //
			+ "uniform vec2 atlasCoord;\n" //
			+ "uniform vec2 atlasSize;\n" //
			+ "uniform int isRepeat;\n" //
			+ "void main()\n"//
			+ "{\n" //
			+ "vec4 textureSample = vec4(0.0,0.0,0.0,0.0);\n"//
			+ "if(isRepeat == 1)\n"//
			+ "{\n"//
			+ "textureSample = v_color * texture2D(u_texture, atlasCoord+mod(v_texCoords, atlasSize));\n"//
			+ "}\n"//
			+ "else\n"//
			+ "{\n"//
			+ "textureSample = v_color * texture2D(u_texture, v_texCoords);\n"//
			+ "}\n"//
			+ "  gl_FragColor = textureSample;\n" //
			+ "}";

		ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
		if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
		return shader;
	}
}
