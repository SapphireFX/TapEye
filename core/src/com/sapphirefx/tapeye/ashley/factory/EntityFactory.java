package com.sapphirefx.tapeye.ashley.factory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sapphirefx.tapeye.ashley.components.CompositeTransformComponent;
import com.sapphirefx.tapeye.ashley.components.DeskComponent;
import com.sapphirefx.tapeye.ashley.components.EnemyComponent;
import com.sapphirefx.tapeye.ashley.components.MainItemComponent;
import com.sapphirefx.tapeye.ashley.components.PlaceEnemyRespawnComponent;
import com.sapphirefx.tapeye.ashley.components.TransformComponent;
import com.sapphirefx.tapeye.ashley.components.ViewPortComponent;
import com.sapphirefx.tapeye.ashley.components.ZIndexComponent;
import com.sapphirefx.tapeye.ashley.factory.components.AnimationComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.components.ColorPrimitiveComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.components.CompositeComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.components.LabelComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.components.NinePatchComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.components.ParticleEffectComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.components.SimpleImageComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.prototypes.CompositeComponents;
import com.sapphirefx.tapeye.ashley.factory.prototypes.Prototype9PatchObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeAnimationObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeColorPrimitiveObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeCompositeObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeImageObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeLabelObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeLightObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeParticleEffectObject;
import com.sapphirefx.tapeye.resources.IResourceRetriever;
import com.sapphirefx.tapeye.resources.ResourceManager;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.tools.GameParameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by sapphire on 19.10.15.
 */
public class EntityFactory
{
    public static final int IMAGE_TYPE 		        = 1;
	public static final int LABEL_TYPE 		        = 2;
	public static final int SPRITE_ANIMATION_TYPE   = 3;
	public static final int COMPOSITE_TYPE 	        = 6;
	public static final int PARTICLE_TYPE 	        = 7;
	public static final int LIGHT_TYPE 		        = 8;
	public static final int NINE_PATCH 		        = 9;
	public static final int COLOR_PRIMITIVE 		= 10;

	public IResourceRetriever rm = null;

	ComponentFactory compositeComponentFactory, lightComponentFactory, particleEffectComponentFactory, simpleImageComponentFactory,
            spriteComponentFactory,labelComponentFactory, ninePatchComponentFactory, gameComponentFactory, colorPrimitiveFactory;

    private HashMap<Integer, ComponentFactory> externalFactories = new HashMap<Integer, ComponentFactory>();

    private HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();

    public ComponentFactory getCompositeComponentFactory()
    {
		return compositeComponentFactory;
	}

    public EntityFactory(IResourceRetriever rm)
    {
        this.rm = rm;

		compositeComponentFactory = new CompositeComponentFactory(rm);

		particleEffectComponentFactory = new ParticleEffectComponentFactory(rm);
		simpleImageComponentFactory = new SimpleImageComponentFactory(rm);
		spriteComponentFactory = new AnimationComponentFactory(rm);
		labelComponentFactory = new LabelComponentFactory(rm);
		ninePatchComponentFactory = new NinePatchComponentFactory(rm);
        gameComponentFactory = new CompositeComponentFactory(rm);
		colorPrimitiveFactory = new ColorPrimitiveComponentFactory(rm);
	}

	public Entity createEntity(Entity root, PrototypeImageObject vo)
    {
		Entity entity = new Entity();
		simpleImageComponentFactory.createComponents(root, entity, vo);
		postProcessEntity(entity, vo);
		return entity;
	}

	public Entity createEntity(Entity root, Prototype9PatchObject vo)
    {
		Entity entity = new Entity();
		ninePatchComponentFactory.createComponents(root, entity, vo);
		postProcessEntity(entity, vo);
		return entity;
	}

	public Entity createEntity(Entity root, PrototypeLabelObject vo)
    {
		Entity entity = new Entity();
		labelComponentFactory.createComponents(root, entity, vo);
		postProcessEntity(entity, vo);
		return entity;
	}

	public Entity createEntity(Entity root, PrototypeParticleEffectObject vo)
    {
		Entity entity = new Entity();
		particleEffectComponentFactory.createComponents(root, entity, vo);
		postProcessEntity(entity, vo);
		return entity;
	}

	public Entity createEntity(Entity root, PrototypeLightObject vo)
    {
		Entity entity = new Entity();
		lightComponentFactory.createComponents(root, entity, vo);
		postProcessEntity(entity, vo);
		return entity;
	}

	public Entity createEntity(Entity root, PrototypeAnimationObject vo)
    {
		Entity entity = new Entity();
		spriteComponentFactory.createComponents(root, entity, vo);
		postProcessEntity(entity, vo);
		return entity;
	}

	public Entity createEntity(Entity root, PrototypeCompositeObject vo)
    {
		Entity entity = new Entity();
		compositeComponentFactory.createComponents(root, entity, vo);
		postProcessEntity(entity, vo);
		return entity;
	}

	public Entity createEntity(Entity root, PrototypeColorPrimitiveObject vo){

		Entity entity = new Entity();

		colorPrimitiveFactory.createComponents(root, entity, vo);

		postProcessEntity(entity, vo);

		return entity;
	}

	public Entity createRootEntity(CompositeComponents compositeObject, Viewport viewport)
    {

        PrototypeCompositeObject vo = new PrototypeCompositeObject();
		vo.composite = compositeObject;

		Entity entity = new Entity();

		compositeComponentFactory.createComponents(null, entity, vo);
		CompositeTransformComponent compositeTransform = new CompositeTransformComponent();
		TransformComponent transform = new TransformComponent();

		ViewPortComponent viewPortComponent = new ViewPortComponent();
		viewPortComponent.viewPort = viewport;

		//TODO: not sure if this line is okay
		viewPortComponent.viewPort.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

		entity.add(transform);
		entity.add(viewPortComponent);

		postProcessEntity(entity, vo);

		return entity;
	}

	public Integer postProcessEntity(Entity entity, PrototypeObject po)
    {
        gameComponentFactory.createGameComponents(entity, po);

		ComponentMapper<MainItemComponent> mainItemComponentComponentMapper = ComponentMapper.getFor(MainItemComponent.class);
		MainItemComponent mainItemComponent = mainItemComponentComponentMapper.get(entity);
		if(mainItemComponent.uniqueId == -1) mainItemComponent.uniqueId = getFreeId();
		entities.put(mainItemComponent.uniqueId, entity);

		return mainItemComponent.uniqueId;
	}

	private int getFreeId()
    {
		if(entities == null || entities.size() == 0) return 1;
		ArrayList<Integer> ids = new ArrayList<Integer>(entities.keySet());
		Collections.sort(ids);
		for(int i = 1; i < ids.size(); i++) {
			if(ids.get(i)-ids.get(i-1) > 1) {
				return ids.get(i-1)+1;
			}
		}
		return ids.get(ids.size()-1)+1;
	}

	public Integer updateMap(Entity entity)
    {
		ComponentMapper<MainItemComponent> mainItemComponentComponentMapper = ComponentMapper.getFor(MainItemComponent.class);
		MainItemComponent mainItemComponent = mainItemComponentComponentMapper.get(entity);
		entities.put(mainItemComponent.uniqueId, entity);

		return mainItemComponent.uniqueId;
	}

	public void initAllChildren(Engine engine, Entity entity, CompositeComponents vo)
    {
		for (int i = 0; i < vo.sImages.size(); i++)
        {
			Entity child = createEntity(entity, vo.sImages.get(i));
			engine.addEntity(child);
		}

		for (int i = 0; i < vo.sImage9patchs.size(); i++)
        {
			Entity child = createEntity(entity, vo.sImage9patchs.get(i));
			engine.addEntity(child);
		}

		for (int i = 0; i < vo.sLabels.size(); i++)
        {
			Entity child = createEntity(entity, vo.sLabels.get(i));
			engine.addEntity(child);
		}

		for (int i = 0; i < vo.sParticleEffects.size(); i++)
        {
			Entity child = createEntity(entity, vo.sParticleEffects.get(i));
			engine.addEntity(child);
		}

		for (int i = 0; i < vo.sLights.size(); i++)
        {
			Entity child = createEntity(entity, vo.sLights.get(i));
			engine.addEntity(child);
		}

		for (int i = 0; i < vo.sSpriteAnimations.size(); i++)
        {
			Entity child = createEntity(entity, vo.sSpriteAnimations.get(i));
			engine.addEntity(child);
		}

		for (int i = 0; i < vo.sColorPrimitives.size(); i++) {
			Entity child = createEntity(entity, vo.sColorPrimitives.get(i));
			engine.addEntity(child);
		}

		for (int i = 0; i < vo.sComposites.size(); i++)
        {
			Entity child = createEntity(entity, vo.sComposites.get(i));
			engine.addEntity(child);
			initAllChildren(engine, child, vo.sComposites.get(i).composite);
		}
	}

	public Entity getEntityByUniqueId(Integer id)
    {
		return entities.get(id);
	}

	public void clean()
	{
		entities.clear();
	}

	public Entity createEnemy(GameParameters.ENEMY typeEnemy)
	{
		Entity enemyEntity = new Entity();
        // TODO неправильно я корень подменяю. надо бы поработать над этим
        Entity root = SceneLoader.rootEntity;
		PrototypeObject po;
        String nameOfEnemy;
        String nameOfLayer;
		switch (typeEnemy)
		{
            case GOODEYE:
                nameOfEnemy = "evilGreenEye";
                nameOfLayer = "playground";
                //root = SceneLoader.rootEntity.getComponent(ParentNodeComponent.class).parentEntity;
                break;
            case EVILEYE:
                nameOfEnemy = "evilOrangeEye";
                nameOfLayer = "playground";
                //root = SceneLoader.rootEntity.getComponent(ParentNodeComponent.class).parentEntity;
                break;
			case GOODFLY:
				nameOfEnemy = "fly";
                nameOfLayer = "flying";
				break;
			case EVILFLY:
				nameOfEnemy = "evilFly";
                nameOfLayer = "flying";
				break;
            case CAT:
                nameOfEnemy = "cat";
                nameOfLayer = "flying";
                break;
			case SPIDER:
				nameOfEnemy = "spider";
				nameOfLayer = "flying";
				break;
			default:
				return null;
		}

        po = rm.getProjectVO().libraryItems.get(nameOfEnemy).clone();

		// корректировка
		switch (typeEnemy)
		{
			case GOODEYE:
			case EVILEYE:
				break;
			case CAT:
				//po.scaleX = 2f;
				//po.scaleY = 2f;
				po.x = -1000;
				po.y = -1000;
				break;
			case GOODFLY:
			case EVILFLY:
				po.x = -1000;
				po.y = -1000;
				break;
			case SPIDER:
				po.x = -1000;
				po.y = -1000;
				//po.scaleX = 0.42f;
				//po.scaleY = 0.42f;
		}

		compositeComponentFactory.createComponents(root, enemyEntity, po);
		initAllChildren(SceneLoader.engine, enemyEntity, rm.getProjectVO().libraryItems.get(nameOfEnemy).composite);
		MainItemComponent mainItemComponent = enemyEntity.getComponent(MainItemComponent.class);
		if(mainItemComponent.uniqueId == -1) mainItemComponent.uniqueId = getFreeId();
		ZIndexComponent zIndexComponent = enemyEntity.getComponent(ZIndexComponent.class);
		zIndexComponent.layerName = nameOfLayer;

		TransformComponent tc = enemyEntity.getComponent(TransformComponent.class);
		compositeComponentFactory.createEnemyComponent(enemyEntity, tc, typeEnemy);
        compositeComponentFactory.createLifeCycle(enemyEntity, typeEnemy);

		return enemyEntity;
	}

	public Entity createEye(GameParameters.ENEMY typeEye, Entity respawn)
    {
        if (typeEye == GameParameters.ENEMY.GOODEYE || typeEye == GameParameters.ENEMY.EVILEYE)
        {
            Entity eyeEntity = createEnemy(typeEye);
            // перекрестные ссылки друг на друга
            respawn.getComponent(PlaceEnemyRespawnComponent.class).enemyes.put(typeEye, eyeEntity);
            eyeEntity.getComponent(EnemyComponent.class).placeRespawnID = respawn.getId();

            // пропорции и позиция
            TransformComponent transformComponentRespawn = respawn.getComponent(TransformComponent.class);
            TransformComponent transformComponentEye = eyeEntity.getComponent(TransformComponent.class);
            EnemyComponent enemyComponent = eyeEntity.getComponent(EnemyComponent.class);
            transformComponentEye.scaleX = transformComponentRespawn.scaleX;
            transformComponentEye.scaleY = transformComponentRespawn.scaleY;
            enemyComponent.actor.setPosition(transformComponentRespawn.x, transformComponentRespawn.y);

            return eyeEntity;
        }
        System.out.println("WTF неправльный тип глаза");
        return null;
    }

    /**
     * Создание эффектов
     * @param effect
     * @return
     */
	public Entity createEffect(GameParameters.EFFECT effect, float PosX, float PosY, float scaleX, float scaleY, String text)
	{
		Entity effectEntity = new Entity();

        String nameEffect;
		String nameOfLayer = "playground";
		float time = 2;
		switch (effect)
		{
			case SMASHEDGREENEYE:
				nameEffect = "deadEye";
				break;
            case SMASHEDORANGEEYE:
                nameEffect = "deadEvilEye";
                break;
			case DEADEVILFLY:
				nameEffect = "deadEvilFly";
				break;
			case DEADGOODFLY:
				nameEffect = "deadFly";
				break;
			case DEADSPIDER:
				nameEffect = "deadSpider";
				break;
			case ADDEYE:
				nameEffect = "addEyeEffect";
				time = 0.2f;
				break;
			case DELEYE:
				nameEffect = "delEyeEffect";
				nameOfLayer = "effects";
				time = 0.2f;
				break;
			case GREENTEXT:
				nameEffect = "greenText";
				nameOfLayer = "ui";
				compositeComponentFactory.createMovementComponent(effectEntity, PosX, PosY);
				break;
			case REDTEXT:
				nameOfLayer = "ui";
				nameEffect = "redText";
				compositeComponentFactory.createMovementComponent(effectEntity, PosX, PosY);
				break;
            case BLOWEVILEYE:
                nameEffect = "blowEvilEye";
                break;
			case ONSHOWWINDOW:
				nameOfLayer = "gameover";
				nameEffect = "onShowBoss1";
				break;
			case ONHIDEWINDOW:
				//TODO убрать если не понадобится
				nameEffect = "greenText";
				nameOfLayer = "ui";
				break;
			case NEXTLVL:
				nameEffect = "greenText";
				nameOfLayer = "ui";
				break;
			default:
                System.out.println("WTF Where effect");
                nameEffect = "deadEye";
                break;
		}

		PrototypeObject po = rm.getProjectVO().libraryItems.get(nameEffect).clone();

        if(effect == GameParameters.EFFECT.SMASHEDGREENEYE)
        {
            //po.rotation = 180;
            po.originX = (float)Math.random() * 2000;
            po.originY = (float)Math.random() * 2000;
        }

		po.x = PosX;
		po.y = PosY;
		po.scaleX = scaleX;
		po.scaleY = scaleY;

		compositeComponentFactory.createComponents(SceneLoader.rootEntity, effectEntity, po);
		initAllChildren(SceneLoader.engine, effectEntity, rm.getProjectVO().libraryItems.get(nameEffect).composite);
		MainItemComponent mainItemComponent = effectEntity.getComponent(MainItemComponent.class);
		if(mainItemComponent.uniqueId == -1) mainItemComponent.uniqueId = getFreeId();
		ZIndexComponent zIndexComponent = effectEntity.getComponent(ZIndexComponent.class);
		zIndexComponent.layerName = nameOfLayer;
		compositeComponentFactory.createEffectComponent(effectEntity, time, effect);

		switch (effect)
		{
			case SMASHEDGREENEYE:
				compositeComponentFactory.createShaderComponent(effectEntity, "rotation", time, (float)(360*Math.random()));
				break;
			case SMASHEDORANGEEYE:
				break;
			case DEADEVILFLY:
				break;
			case DEADGOODFLY:
				break;
			case DEADSPIDER:
				break;
			case ADDEYE:
				break;
			case DELEYE:
				break;
			case GREENTEXT:
				break;
			case REDTEXT:
				break;
			case BLOWEVILEYE:
				break;
			case ONSHOWWINDOW:
				break;
			default:
				break;
		}


		/*
		SnapshotArray<Entity> children = effectEntity.getComponent(NodeComponent.class).children;
		for (int i=0; i<children.size; i++)
		{
			if (children.get(i).getComponent(AnimationComponent.class) != null)
			{
				System.out.println("add shader");

			}
			if (children.get(i).getComponent(ParticleComponent.class) != null)
			{
				// включаем анимацию частиц
				children.get(i).getComponent(ParticleComponent.class).particleEffect.reset();
			}
			// если есть текстовое поле, то заполняем его текстом, кторый прилетел в конструкторе
			if (children.get(i).getComponent(LabelComponent.class) != null && text != null)
			{
				StringBuilder stringBuilder = children.get(i).getComponent(LabelComponent.class).text;
				stringBuilder.delete(0, stringBuilder.length);
				stringBuilder.append(text);
			}
		}
		*/
        SceneLoader.engine.addEntity(effectEntity);
		return effectEntity;
	}

    /**
     * Создание игровой доски
     * @param width
     * @param height
     * @return
     */
	public Entity createGameDesk(float width, float height)
	{
		Entity deskEntity = new Entity();

        DeskComponent deskComponent = new DeskComponent();
        deskComponent.actor = new Actor();
        deskComponent.actor.setBounds(0, 0, width, height);
        deskComponent.actor.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                ResourceManager.soundManager.playSoundTableTouch();
                return true;
            }
        });
        deskEntity.add(deskComponent);

        MainItemComponent mainItemComponent = new MainItemComponent();
        mainItemComponent.uniqueId = getFreeId();
        deskEntity.add(mainItemComponent);

		return deskEntity;
	}
}
