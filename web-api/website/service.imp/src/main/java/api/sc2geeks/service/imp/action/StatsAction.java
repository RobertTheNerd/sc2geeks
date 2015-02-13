package api.sc2geeks.service.imp.action;

import api.sc2geeks.service.imp.MongoConfig;
import api.sc2geeks.service.imp.dal.MongoHelper;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/14/12
 * Time: 7:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatsAction extends ActionSupport
{
	private static Logger logger = Logger.getLogger(StatsAction.class);
	private static MongoHelper mongoHelper = MongoHelper.createHelper(MongoConfig.getStatsConfig());

	public static final String OBJ_TYPE_REPLAY = "Replay";
	public static final String OBJ_TYPE_PROGAMER = "Progamer";

	public static final String ACTION_TYPE_DOWNLOAD = "Download";
	public static final String ACTION_TYPE_VIEW = "View";

	String objType;
	String actionType;
	String replayId;
	String fromIP;
	Date inDate;

	public StatsAction(){
		Calendar cal = Calendar.getInstance();
		cal.set(0, 0, 0);
		inDate = cal.getTime();
	}

	public void setReplayId(String replayId)
	{
		this.replayId = replayId;
	}

	public void setFromIP(String fromIP)
	{
		this.fromIP = fromIP.replace("-", ".");
	}
	public void setInDate(String inDate)
	{
		try
		{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm S");
			this.inDate = format.parse(inDate);
		}
		catch(Exception e){}
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String execute()
	{
		if (OBJ_TYPE_REPLAY.compareToIgnoreCase(objType) == 0) {
			if (ACTION_TYPE_DOWNLOAD.compareToIgnoreCase(actionType) == 0)
				mongoHelper.addReplayDownloadCount(Integer.parseInt(replayId), fromIP, inDate);
			else if (ACTION_TYPE_VIEW.compareToIgnoreCase(actionType) == 0)
				mongoHelper.addReplayViewCount(Integer.parseInt(replayId), fromIP, inDate);
		} else if (OBJ_TYPE_PROGAMER.compareToIgnoreCase(objType) == 0 && ACTION_TYPE_VIEW.compareToIgnoreCase(actionType) == 0)
			mongoHelper.addProgamerViewCount(Integer.parseInt(replayId), fromIP, inDate);

		return SUCCESS;
	}
}
