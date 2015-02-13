package api.sc2geeks.entity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/13/12
 * Time: 7:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class NavigationNode
{
	private String nodeName;
	private String displayNodeName;
	private long count;
	List<NavigationNode> children;

	public String getNodeName()
	{
		return nodeName;
	}

	public void setNodeName(String nodeName)
	{
		this.nodeName = nodeName;
	}

	public long getCount()
	{
		return count;
	}

	public void setCount(long count)
	{
		this.count = count;
	}

	public List<NavigationNode> getChildren()
	{
		return children;
	}

	public String getDisplayNodeName() {
		if (displayNodeName == null) {
			displayNodeName = nodeName.replaceAll("\\-[\\d]*$", "");
		}
		return displayNodeName;
	}

	public void setChildren(List<NavigationNode> children)
	{
		this.children = children;
	}
}
