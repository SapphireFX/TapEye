package com.sapphirefx.tapeye.ashley.factory.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.NinePatchComponent;
import com.sapphirefx.tapeye.ashley.factory.ComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.EntityFactory;
import com.sapphirefx.tapeye.ashley.factory.prototypes.Prototype9PatchObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeObject;
import com.sapphirefx.tapeye.resources.IResourceRetriever;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.tools.ProjectInfo;
import com.sapphirefx.tapeye.tools.ResolutionEntry;

import box2dLight.RayHandler;

/**
 * Created by sapphire on 19.10.15.
 */
public class NinePatchComponentFactory extends ComponentFactory
{
    NinePatchComponent ninePatchComponent;

    public NinePatchComponentFactory(IResourceRetriever rm)
    {
        super(rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, PrototypeObject po)
    {
        ninePatchComponent = createNinePatchComponent(entity, (Prototype9PatchObject) po);
		createCommonComponents(entity, po, EntityFactory.NINE_PATCH);
		createParentNodeComponent(root, entity);
		createNodeComponent(root, entity);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, PrototypeObject po)
    {
        DimensionsComponent component = new DimensionsComponent();
		component.height = ((Prototype9PatchObject) po).height;
		component.width = ((Prototype9PatchObject) po).width;
		if(component.width == 0)
        {
			component.width = ninePatchComponent.ninePatch.getTotalWidth();
		}

		if(component.height == 0)
        {
			component.height = ninePatchComponent.ninePatch.getTotalHeight();
		}
		entity.add(component);
		return component;
    }

    private NinePatchComponent createNinePatchComponent(Entity entity, Prototype9PatchObject vo)
    {
		NinePatchComponent ninePatchComponent = new NinePatchComponent();
		TextureAtlas.AtlasRegion atlasRegion = (TextureAtlas.AtlasRegion) rm.getTextureRegion(vo.imageName);
		ninePatchComponent.ninePatch = new NinePatch(atlasRegion, atlasRegion.splits[0], atlasRegion.splits[1], atlasRegion.splits[2], atlasRegion.splits[3]);

		ResolutionEntry resolutionEntryVO = rm.getLoadedResolution();
		ProjectInfo projectInfoVO = rm.getProjectVO();
		float multiplier = resolutionEntryVO.getMultiplier(rm.getProjectVO().originalResolution);

		ninePatchComponent.ninePatch.scale(multiplier/projectInfoVO.pixelToWorld, multiplier/projectInfoVO.pixelToWorld);
		ninePatchComponent.ninePatch.setMiddleWidth(ninePatchComponent.ninePatch.getMiddleWidth()*multiplier/projectInfoVO.pixelToWorld);
		ninePatchComponent.ninePatch.setMiddleHeight(ninePatchComponent.ninePatch.getMiddleHeight()*multiplier/projectInfoVO.pixelToWorld);

		ninePatchComponent.textureRegionName = vo.imageName;
		entity.add(ninePatchComponent);

		return ninePatchComponent;
	}
}
