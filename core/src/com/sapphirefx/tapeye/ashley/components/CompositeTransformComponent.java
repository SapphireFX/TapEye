package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;

/**
 * Created by sapphire on 19.10.15.
 */
public class CompositeTransformComponent implements Component
{
    public boolean transform = true;
	public final Affine2 worldTransform = new Affine2();
	public final Matrix4 computedTransform = new Matrix4();
	public final Matrix4 oldTransform = new Matrix4();

	@Override
	public String toString()
	{
		return "[transform="+transform+"][worldTransform="+worldTransform+"][computedTransform="+computedTransform+"][oldTransform="+oldTransform+"]";
	}
}
