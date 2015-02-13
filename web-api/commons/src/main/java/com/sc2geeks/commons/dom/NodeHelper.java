package com.sc2geeks.commons.dom;

import org.w3c.dom.Node;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 6/17/12
 * Time: 12:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class NodeHelper
{
	public static Node getFirstChildByNodeName(Node node, String name)
	{
		if (node == null || node.getChildNodes() == null)
			return null;

		for (int i = 0; i < node.getChildNodes().getLength(); i ++)
		{
			Node child = node.getChildNodes().item(i);
			if (child.getNodeName().compareTo(name) == 0)
				return child;
		}
		return null;
	}

	public static Node getLastChildByNodeName(Node node, String name)
	{
		if (node == null || node.getChildNodes() == null)
			return null;

		Node child = null;
		for (int i = 0; i < node.getChildNodes().getLength(); i ++)
		{
			child = node.getChildNodes().item(i);
			if (child.getNodeName().compareTo(name) == 0)
				return child;
		}
		return child;
	}

}
