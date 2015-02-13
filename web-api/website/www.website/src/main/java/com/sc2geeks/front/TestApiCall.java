package com.sc2geeks.front;

import api.sc2geeks.client.PlayerPersonManagerProxy;
import api.sc2geeks.entity.*;
import api.sc2geeks.entity.playerperson.PlayerPerson;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 8/1/12
 * Time: 9:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestApiCall
{
	public static void main(String[] args)
	{
		testApiCall();
	}

	private static void testApiCall()
	{
		PlayerPersonManagerProxy manager = new PlayerPersonManagerProxy();
		SearchInput searchInput = new SearchInput();
		RefinementInfo refinementInfo = new RefinementInfo();
		refinementInfo.setRefinementField(RefinementField.Country);
		refinementInfo.setMaxCount(0);
		refinementInfo.setSortMethod(RefinementSortMethod.Name);
		refinementInfo.setSortOrder(SortOrder.Asc);
		searchInput.addRefinementInfo(refinementInfo);
		SearchResult<PlayerPerson>  searchResult = manager.getPlayerPersons(searchInput, false);
		System.out.println(searchResult);
	}
}
