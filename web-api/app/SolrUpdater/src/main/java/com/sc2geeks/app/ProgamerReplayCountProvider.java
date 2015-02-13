package com.sc2geeks.app;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by robert on 1/5/15.
 */
public class ProgamerReplayCountProvider {
	private Hashtable<String, Integer> replayCountMap;

	public int getCount(String name) {
		if (replayCountMap == null)
			getAllProgamers();
		name = name.toLowerCase().trim();
		if (replayCountMap.containsKey(name))
			return replayCountMap.get(name.toLowerCase().trim());
		return 0;
	}

	public void getAllProgamers() {
		String url = Config.getInstance().getSolrReplayUrl();
		HttpSolrServer server = new HttpSolrServer(url);
		SolrQuery query = new SolrQuery();
		query.setQuery("*");
		query.setFacet(true);
		query.setParam("facet.field", "f_player_person_game_id");
		query.setParam("f.f_player_person_game_id.facet.limit", "10000");
		query.setParam("f.f_player_person_game_id.facet.mincount", "0");
		replayCountMap = new Hashtable<>();
		try {
			QueryResponse response = server.query(query);
			SolrDocumentList progamers = response.getResults();

			// facets
			List<FacetField> facets = (List<FacetField>) response.getFacetFields();
			for (FacetField facet : facets) {
				if (facet.getName().compareToIgnoreCase("f_player_person_game_id") == 0) {
					for (FacetField.Count count : facet.getValues()) {
						replayCountMap.put(count.getName().toLowerCase().trim(), (int)count.getCount());
					}
				}
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public static void main(String[] args) {
		ProgamerReplayCountProvider provider = new ProgamerReplayCountProvider();
		int count = provider.getCount("life");
		System.out.print(count);
	}
}
