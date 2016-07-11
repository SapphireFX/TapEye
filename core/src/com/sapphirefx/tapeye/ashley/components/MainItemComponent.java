package com.sapphirefx.tapeye.ashley.components;

import com.badlogic.ashley.core.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sapphire on 19.10.15.
 */
public class MainItemComponent implements Component
{
    public int uniqueId = 0;
	public String itemIdentifier = "";
	public String libraryLink = "";
    public Set<String> tags = new HashSet<String>();
    public String customVars = "";
	public int entityType;
	public boolean visible = true;

	@Override
	public String toString()
	{
		return "[uniqueId="+uniqueId+"][itemIdentifier="+itemIdentifier+"][libraryLink="+libraryLink+"][tags=tags" +
				"][customVars="+customVars+"][entityType="+entityType+"][visible="+visible+"]";
	}
}
