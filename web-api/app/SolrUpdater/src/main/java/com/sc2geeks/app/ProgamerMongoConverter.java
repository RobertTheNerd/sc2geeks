package com.sc2geeks.app;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import org.apache.solr.common.SolrInputDocument;

/**
 * Created by robert on 8/28/14.
 */
public class ProgamerMongoConverter implements MongoObjConverter {

	private ProgamerReplayCountProvider replayCountProvider = new ProgamerReplayCountProvider();

	@Override
	public SolrInputDocument convertToSolrDoc(DBObject progamer) {
		SolrInputDocument doc = new SolrInputDocument();
		int progamerId = (Integer) progamer.get("_id");
		doc.addField("p_person_id", progamerId);
		doc.addField("p_liquipedia_name", progamer.get("name"));
		doc.addField("p_en_fullname", progamer.get("romanized_name"));
		doc.addField("p_native_fullname", progamer.get("native_name"));
		doc.addField("pf_team", progamer.get("team"));
		doc.addField("p_alt_ids", progamer.get("other_ids"));
		doc.addField("p_image", getImage(progamer));
		doc.addField("p_twitter_url", progamer.get("twitter_url"));
		doc.addField("p_wiki_url", progamer.get("link"));
		doc.addField("p_fanpage", progamer.get("pan_page"));
		doc.addField("pf_country", progamer.get("country"));
		doc.addField("p_birthday", progamer.get("birthday"));
		doc.addField("pf_race", progamer.get("race"));
		doc.addField("p_replay_count", replayCountProvider.getCount(progamer.get("name").toString()));
		return doc;
	}

	private static String getImage(DBObject progamer) {
		Object image = progamer.get("image");
		if (image == null)
			return null;

		return new Gson().toJson(image);
	}

	private int getReplayCount(int progamerId) {
		return MongoHelper.getReplayCountForProgamer(progamerId);
	}

	@Override
	public String getUniqueId() {
		return "p_person_id";
	}
}
