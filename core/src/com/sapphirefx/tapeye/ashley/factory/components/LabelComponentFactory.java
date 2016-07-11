package com.sapphirefx.tapeye.ashley.factory.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.sapphirefx.tapeye.ashley.components.DimensionsComponent;
import com.sapphirefx.tapeye.ashley.components.LabelComponent;
import com.sapphirefx.tapeye.ashley.factory.ComponentFactory;
import com.sapphirefx.tapeye.ashley.factory.EntityFactory;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeLabelObject;
import com.sapphirefx.tapeye.ashley.factory.prototypes.PrototypeObject;
import com.sapphirefx.tapeye.resources.IResourceRetriever;
import com.sapphirefx.tapeye.resources.SceneLoader;
import com.sapphirefx.tapeye.tools.ProjectInfo;

import box2dLight.RayHandler;

/**
 * Created by sapphire on 19.10.15.
 */
public class LabelComponentFactory extends ComponentFactory
{
    private static int labelDefaultSize = 12;

    public LabelComponentFactory(IResourceRetriever rm)
    {
        super(rm);
    }

    @Override
    public void createComponents(Entity root, Entity entity, PrototypeObject po)
    {
        createCommonComponents(entity, po, EntityFactory.LABEL_TYPE);
		createParentNodeComponent(root, entity);
		createNodeComponent(root, entity);
		createLabelComponent(entity, (PrototypeLabelObject) po);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, PrototypeObject po)
    {
        DimensionsComponent component = new DimensionsComponent();
        component.height = ((PrototypeLabelObject) po).height;
        component.width = ((PrototypeLabelObject) po).width;

        entity.add(component);
        return component;
    }

    protected LabelComponent createLabelComponent(Entity entity, PrototypeLabelObject lo)
        {
    	LabelComponent component = new LabelComponent(lo.text, generateStyle(rm, lo.style, lo.size));
        component.fontName = lo.style;
        component.fontSize = lo.size;
        component.setAlignment(lo.align);

        ProjectInfo projectInfoVO = rm.getProjectVO();
        component.setFontScale(1f / projectInfoVO.pixelToWorld);

        entity.add(component);
        return component;
    }


    public static Label.LabelStyle generateStyle(IResourceRetriever rManager, String fontName, int size)
    {

        if (size == 0) {
            size = labelDefaultSize;
        }
        return new Label.LabelStyle(rManager.getBitmapFont(fontName, size), null);
    }
}
