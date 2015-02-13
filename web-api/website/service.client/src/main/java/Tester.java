import api.sc2geeks.client.ReplayManagerProxy;
import api.sc2geeks.entity.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/5/12
 * Time: 10:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tester
{
	public static void main(String[] args) throws IOException
	{
		// testLinkedMap();
		// testSplit();

		testParam();
		System.in.read();
		return;
	}

	private static void testParam()
	{
		variable();
	}
	private static void variable(String ... input)
	{
		for (String s : input)
			System.out.println();
	}
	private static void testSplit()
	{
		String input;
		input = "a=";
		String [] parts = input.split("=");
		System.out.println(parts);

		input = "a";
		parts = input.split("=");

		System.out.println(parts);
		return;
	}
	
	private static void testLinkedMap() throws IOException
	{
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("one", "one");
		map.put("two", "two");
		map.put("three", "three");
		map.put("String", "String");
		map.put("never", "never");

		for (String key : map.keySet())
		{
			System.out.println(key + ":" + map.get(key));
		}
		System.in.read();
	}
	private static void testSearch()
	{

		SearchInput input = new SearchInput();
		input.setSearchTerms("ogsmc");
		input.setSearchType(SearchType.WithRefinement);
		List<RefinementInfo> refinementInfos = new ArrayList<RefinementInfo>();

		RefinementInfo info = new RefinementInfo();
		info.setRefinementField(RefinementField.MatchUpType);
		info.setMaxCount(5);
		info.setSortMethod(RefinementSortMethod.Count);
		info.setSortOrder(SortOrder.Desc);
		refinementInfos.add(info);

		input.setRefinementFields(refinementInfos);


		ReplayManagerProxy proxy = new ReplayManagerProxy();
		SearchResult<Replay> result = proxy.search(input);
		System.out.println(result);
	}
}
