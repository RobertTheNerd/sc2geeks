package api.sc2geeks.service.imp;

import api.sc2geeks.entity.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 8/18/12
 * Time: 12:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerPersonManagerImpTest
{
	@Test
	public void testGetPlayerPersons() throws Exception
	{
		PlayerPersonManagerImp manager = new PlayerPersonManagerImp();

		// search without filter
		SearchInput si = new SearchInput();
		si.setPageSize(9999);
		SearchResult sr = manager.getPlayerPersons(si, false);
		assertTrue(sr.getTotalMatches() > 0);

		// search with refinement
		List<RefinementInfo> refinementInfoList = new ArrayList<RefinementInfo>();
		RefinementInfo info = new RefinementInfo();
		info.setMaxCount(0);
		info.setRefinementField(RefinementField.Team);
		info.setSortMethod(RefinementSortMethod.Name);
		info.setSortOrder(SortOrder.Asc);
		refinementInfoList.add(info);
		si.setRefinementFields(refinementInfoList);
		sr = manager.getPlayerPersons(si, false);
		assertTrue(sr.getNavigationNodes() != null && sr.getNavigationNodes().size() > 0);
	}
}
