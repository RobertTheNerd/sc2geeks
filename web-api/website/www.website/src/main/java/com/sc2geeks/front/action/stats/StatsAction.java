package com.sc2geeks.front.action.stats;

import api.sc2geeks.client.PlayerPersonManagerProxy;
import api.sc2geeks.client.ReplayManagerProxy;
import com.sc2geeks.front.action.ActionBase;

import java.util.Date;

/**
 * Created by robert on 9/5/14.
 */
public class StatsAction extends ActionBase {
	public static final String OBJ_TYPE_REPLAY = "Replay";
	public static final String OBJ_TYPE_PROGAMER = "Progamer";

	public static final String ACTION_TYPE_DOWNLOAD = "Download";
	public static final String ACTION_TYPE_VIEW = "View";


	private String objType;
	private String actionType;
	private String id;

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	protected String doExecute() {
		try {
			if (OBJ_TYPE_REPLAY.compareToIgnoreCase(objType) == 0 && ACTION_TYPE_VIEW.compareToIgnoreCase(actionType) == 0) {
				new ReplayManagerProxy().saveViewCounter(id, requestContext.getRemoteIP(), new Date());
			} else if (OBJ_TYPE_PROGAMER.compareToIgnoreCase(objType) == 0 && ACTION_TYPE_VIEW.compareToIgnoreCase(actionType) == 0)
				new PlayerPersonManagerProxy().saveViewCounter(id, requestContext.getRemoteIP(), new Date());
		}
		catch(Exception e) {
			logger.error("Error saving status.", e);
		}
		return SUCCESS;
	}
}
