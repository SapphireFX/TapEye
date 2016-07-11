package com.sapphirefx.tapeye.ashley.factory.prototypes;

import java.util.ArrayList;

/**
 * Created by sapphire on 19.10.15.
 */
public class PrototypeSelectBox extends PrototypeObject
{
    public ArrayList<String> list	=	new ArrayList<String>();
	public String	style	=  "";
	public float width = 0;
	public float height = 0;

	public PrototypeSelectBox()
    {
		super();
	}

	public PrototypeSelectBox(PrototypeSelectBox vo)
    {
		super(vo);
		width 		= vo.width;
		height 		= vo.height;
		style = new String(vo.style);
	}
}
