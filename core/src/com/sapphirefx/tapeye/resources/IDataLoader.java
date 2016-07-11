package com.sapphirefx.tapeye.resources;

import com.sapphirefx.tapeye.ashley.factory.prototypes.SceneObject;
import com.sapphirefx.tapeye.tools.ProjectInfo;

/**
 * Created by sapphire on 19.10.15.
 */
public interface IDataLoader
{
    public SceneObject loadSceneVO(String sceneName);
    public ProjectInfo loadProjectVO();
}
