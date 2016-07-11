package com.sapphirefx.tapeye.ashley.factory.prototypes;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.sapphirefx.tapeye.ashley.components.LayerMapComponent;
import com.sapphirefx.tapeye.ashley.components.MainItemComponent;
import com.sapphirefx.tapeye.ashley.components.NodeComponent;
import com.sapphirefx.tapeye.ashley.factory.EntityFactory;
import com.sapphirefx.tapeye.resources.FontSizePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by sapphire on 19.10.15.
 */
public class CompositeComponents
{
    public ArrayList<PrototypeImageObject> sImages = new ArrayList<PrototypeImageObject>(1);
    public ArrayList<Prototype9PatchObject> sImage9patchs = new ArrayList<Prototype9PatchObject>(1);
    public ArrayList<PrototypeTextBox> sTextBox = new ArrayList<PrototypeTextBox>(1);
    public ArrayList<PrototypeLabelObject> sLabels = new ArrayList<PrototypeLabelObject>(1);
    public ArrayList<PrototypeCompositeObject> sComposites = new ArrayList<PrototypeCompositeObject>(1);
    public ArrayList<PrototypeSelectBox> sSelectBoxes = new ArrayList<PrototypeSelectBox>(1);
    public ArrayList<PrototypeParticleEffectObject> sParticleEffects = new ArrayList<PrototypeParticleEffectObject>(1);
    public ArrayList<PrototypeLightObject> sLights = new ArrayList<PrototypeLightObject>(1);
    public ArrayList<PrototypeAnimationObject> sSpriteAnimations = new ArrayList<PrototypeAnimationObject>(1);
    public ArrayList<PrototypeColorPrimitiveObject> sColorPrimitives = new ArrayList<PrototypeColorPrimitiveObject>(1);

    public ArrayList<PrototypeLayerObject> layers = new ArrayList<PrototypeLayerObject>();


    public CompositeComponents()
    {
    }

    public CompositeComponents(CompositeComponents vo)
    {
        if (vo == null) return;
        update(vo);
    }

    public void update(CompositeComponents vo)
    {
        clear();
        for (int i = 0; i < vo.sImages.size(); i++)
        {
            sImages.add(new PrototypeImageObject(vo.sImages.get(i)));
        }
        for (int i = 0; i < vo.sImage9patchs.size(); i++)
        {
            sImage9patchs.add(new Prototype9PatchObject(vo.sImage9patchs.get(i)));
        }
        for (int i = 0; i < vo.sTextBox.size(); i++)
        {
            sTextBox.add(new PrototypeTextBox(vo.sTextBox.get(i)));
        }
        for (int i = 0; i < vo.sLabels.size(); i++)
        {
            sLabels.add(new PrototypeLabelObject(vo.sLabels.get(i)));
        }
        for (int i = 0; i < vo.sComposites.size(); i++)
        {
            sComposites.add(new PrototypeCompositeObject(vo.sComposites.get(i)));
        }
        for (int i = 0; i < vo.sSelectBoxes.size(); i++)
        {
            sSelectBoxes.add(new PrototypeSelectBox(vo.sSelectBoxes.get(i)));
        }

        for (int i = 0; i < vo.sParticleEffects.size(); i++)
        {
            sParticleEffects.add(new PrototypeParticleEffectObject(vo.sParticleEffects.get(i)));
        }

        for (int i = 0; i < vo.sLights.size(); i++)
        {
            sLights.add(new PrototypeLightObject(vo.sLights.get(i)));
        }

        for (int i = 0; i < vo.sSpriteAnimations.size(); i++)
        {
            sSpriteAnimations.add(new PrototypeAnimationObject(vo.sSpriteAnimations.get(i)));
        }

        for (int i = 0; i < vo.sColorPrimitives.size(); i++)
        {
            sColorPrimitives.add(new PrototypeColorPrimitiveObject(vo.sColorPrimitives.get(i)));
        }

        layers.clear();
        for (int i = 0; i < vo.layers.size(); i++)
        {
            layers.add(new PrototypeLayerObject(vo.layers.get(i)));
        }

    }

    public void addItem(PrototypeObject vo) {
        String className = vo.getClass().getSimpleName();

        if (className.equals("PrototypeImageObject"))
        {
            sImages.add((PrototypeImageObject) vo);
        }
        if (className.equals("Prototype9PatchObject"))
        {
            sImage9patchs.add((Prototype9PatchObject) vo);
        }
        if (className.equals("PrototypeTextBox"))
        {
            sTextBox.add((PrototypeTextBox) vo);
        }
        if (className.equals("PrototypeLabelObject"))
        {
            sLabels.add((PrototypeLabelObject) vo);
        }
        if (className.equals("PrototypeCompositeObject"))
        {
            sComposites.add((PrototypeCompositeObject) vo);
        }
        if (className.equals("PrototypeSelectBox"))
        {
            sSelectBoxes.add((PrototypeSelectBox) vo);
        }
        if (className.equals("PrototypeParticleEffectObject"))
        {
            sParticleEffects.add((PrototypeParticleEffectObject) vo);
        }
        if (className.equals("PrototypeLightObject"))
        {
            sLights.add((PrototypeLightObject) vo);
        }
        if (className.equals("PrototypeAnimationObject"))
        {
            sSpriteAnimations.add((PrototypeAnimationObject) vo);
        }
        if(className.equals("PrototypeColorPrimitiveObject"))
        {
            sColorPrimitives.add((PrototypeColorPrimitiveObject) vo);
        }
    }

    public void removeItem(PrototypeObject vo)
    {
        String className = vo.getClass().getSimpleName();
        if (className.equals("PrototypeImageObject"))
        {
            sImages.remove((PrototypeImageObject) vo);
        }
        if (className.equals("Prototype9PatchObject"))
        {
            sImage9patchs.remove((Prototype9PatchObject) vo);
        }
        if (className.equals("PrototypeTextBox"))
        {
            sTextBox.remove((PrototypeTextBox) vo);
        }
        if (className.equals("PrototypeLabelObject"))
        {
            sLabels.remove((PrototypeLabelObject) vo);
        }
        if (className.equals("PrototypeCompositeObject"))
        {
            sComposites.remove((PrototypeCompositeObject) vo);
        }
        if (className.equals("PrototypeSelectBox"))
        {
            sSelectBoxes.remove((PrototypeSelectBox) vo);
        }
        if (className.equals("PrototypeParticleEffectObject"))
        {
            sParticleEffects.remove((PrototypeParticleEffectObject) vo);
        }
        if (className.equals("PrototypeLightObject"))
        {
            sLights.remove((PrototypeLightObject) vo);
        }
        if (className.equals("PrototypeAnimationObject"))
        {
            sSpriteAnimations.remove((PrototypeAnimationObject) vo);
        }
        if(className.equals("PrototypeColorPrimitiveObject"))
        {
            sColorPrimitives.remove((PrototypeColorPrimitiveObject) vo);
        }
    }

    public void clear()
    {
        sImages.clear();
        sTextBox.clear();
        sLabels.clear();
        sComposites.clear();
        sSelectBoxes.clear();
        sParticleEffects.clear();
        sLights.clear();
        sSpriteAnimations.clear();
        sColorPrimitives.clear();
    }

    public boolean isEmpty()
    {
        return sComposites.size() == 0 &&
                sImage9patchs.size() == 0 &&
                sImages.size() == 0 &&
                sSpriteAnimations.size() == 0 &&
                sLabels.size() == 0 &&
                sLights.size() == 0 &&
                sParticleEffects.size() == 0 &&
                sSelectBoxes.size() == 0 &&
                sTextBox.size() == 0 &&
                sColorPrimitives.size() == 0;
    }

    public String[] getRecursiveParticleEffectsList()
    {
        HashSet<String> list = new HashSet<String>();
        for (PrototypeParticleEffectObject sParticleEffect : sParticleEffects)
        {
            list.add(sParticleEffect.particleName);
        }
        for (PrototypeCompositeObject sComposite : sComposites)
        {
            String[] additionalList = sComposite.composite.getRecursiveParticleEffectsList();
            Collections.addAll(list, additionalList);
        }
        String[] finalList = new String[list.size()];
        list.toArray(finalList);

        return finalList;
    }

    public String[] getRecursiveSpineAnimationList()
    {
        HashSet<String> list = new HashSet<String>();
        for (PrototypeCompositeObject sComposite : sComposites)
        {
            String[] additionalList = sComposite.composite.getRecursiveSpineAnimationList();
            Collections.addAll(list, additionalList);
        }
        String[] finalList = new String[list.size()];
        list.toArray(finalList);

        return finalList;
    }

    public String[] getRecursiveSpriteAnimationList()
    {
        HashSet<String> list = new HashSet<String>();
        for (PrototypeAnimationObject sSpriteAnimation : sSpriteAnimations)
        {
            list.add(sSpriteAnimation.animationName);
        }
        for (PrototypeCompositeObject sComposite : sComposites)
        {
            String[] additionalList = sComposite.composite.getRecursiveSpriteAnimationList();
            Collections.addAll(list, additionalList);
        }
        String[] finalList = new String[list.size()];
        list.toArray(finalList);

        return finalList;
    }

    public FontSizePair[] getRecursiveFontList()
    {
        HashSet<FontSizePair> list = new HashSet<FontSizePair>();
        for (PrototypeLabelObject sLabel : sLabels)
        {
            list.add(new FontSizePair(sLabel.style.isEmpty() ? "arial" : sLabel.style, sLabel.size == 0 ? 12 : sLabel.size));
        }
        for (PrototypeCompositeObject sComposite : sComposites)
        {
            FontSizePair[] additionalList = sComposite.composite.getRecursiveFontList();
            Collections.addAll(list, additionalList);
        }
        FontSizePair[] finalList = new FontSizePair[list.size()];
        list.toArray(finalList);

        return finalList;
    }

    public String[] getRecursiveShaderList()
    {
    	HashSet<String> list = new HashSet<String>();
    	for (PrototypeObject item : getAllItems())
        {
            if(item.shaderName != null && !item.shaderName.isEmpty())
            {
            	list.add(item.shaderName);
            }
        }
    	String[] finalList = new String[list.size()];
        list.toArray(finalList);
    	return finalList;
    }

    public ArrayList<PrototypeObject> getAllItems()
    {
        ArrayList<PrototypeObject> itemsList = new ArrayList<PrototypeObject>();
        itemsList = getAllItemsRecursive(itemsList, this);

        return itemsList;
    }

    private ArrayList<PrototypeObject> getAllItemsRecursive(ArrayList<PrototypeObject> itemsList, CompositeComponents compositeVo)
    {
        for(PrototypeObject vo: compositeVo.sImage9patchs)
        {
            itemsList.add(vo);
        }
        for(PrototypeObject vo: compositeVo.sImages)
        {
            itemsList.add(vo);
        }
        for(PrototypeObject vo: compositeVo.sLabels)
        {
            itemsList.add(vo);
        }
        for(PrototypeObject vo: compositeVo.sLights)
        {
            itemsList.add(vo);
        }
        for(PrototypeObject vo: compositeVo.sParticleEffects)
        {
            itemsList.add(vo);
        }
        for(PrototypeObject vo: compositeVo.sSelectBoxes)
        {
            itemsList.add(vo);
        }
        for(PrototypeObject vo: compositeVo.sSpriteAnimations)
        {
            itemsList.add(vo);
        }
        for(PrototypeObject vo: compositeVo.sTextBox)
        {
            itemsList.add(vo);
        }
        for(PrototypeObject vo: compositeVo.sColorPrimitives)
        {
            itemsList.add(vo);
        }
        for(PrototypeCompositeObject vo: compositeVo.sComposites)
        {
            itemsList = getAllItemsRecursive(itemsList,vo.composite);
            itemsList.add(vo);
        }

        return itemsList;
    }

    public void loadFromEntity(Entity compositeEntity)
    {
        NodeComponent nodeComponent = compositeEntity.getComponent(NodeComponent.class);
        ComponentMapper<MainItemComponent> mainItemComponentMapper = ComponentMapper.getFor(MainItemComponent.class);
        ComponentMapper<LayerMapComponent> layerMainItemComponentComponentMapper = ComponentMapper.getFor(LayerMapComponent.class);

        if(nodeComponent == null) return;
        for(Entity entity: nodeComponent.children)
        {
        	int entityType = mainItemComponentMapper.get(entity).entityType;
            if(entityType == EntityFactory.COMPOSITE_TYPE)
            {
                PrototypeCompositeObject vo = new PrototypeCompositeObject();
                vo.loadFromEntity(entity);
                sComposites.add(vo);
            }
            if(entityType == EntityFactory.IMAGE_TYPE)
            {
                PrototypeImageObject vo = new PrototypeImageObject();
                vo.loadFromEntity(entity);
                sImages.add(vo);
            }
            if(entityType == EntityFactory.NINE_PATCH)
            {
                Prototype9PatchObject vo = new Prototype9PatchObject();
                vo.loadFromEntity(entity);
                sImage9patchs.add(vo);
            }
            if(entityType == EntityFactory.LABEL_TYPE)
            {
                PrototypeLabelObject vo = new PrototypeLabelObject();
                vo.loadFromEntity(entity);
                sLabels.add(vo);
            }
            if(entityType == EntityFactory.PARTICLE_TYPE)
            {
                PrototypeParticleEffectObject vo = new PrototypeParticleEffectObject();
                vo.loadFromEntity(entity);
                sParticleEffects.add(vo);
            }
            if(entityType == EntityFactory.SPRITE_ANIMATION_TYPE)
            {
                PrototypeAnimationObject vo = new PrototypeAnimationObject();
                vo.loadFromEntity(entity);
                sSpriteAnimations.add(vo);
            }
            if(entityType == EntityFactory.LIGHT_TYPE)
            {
                PrototypeLightObject vo = new PrototypeLightObject();
                vo.loadFromEntity(entity);
                sLights.add(vo);
            }
            if(entityType == EntityFactory.COLOR_PRIMITIVE) {
                PrototypeColorPrimitiveObject vo = new PrototypeColorPrimitiveObject();
                vo.loadFromEntity(entity);
                sColorPrimitives.add(vo);
            }
        }

        LayerMapComponent layerMapComponent = layerMainItemComponentComponentMapper.get(compositeEntity);
        layers = layerMapComponent.getLayers();
    }
}
