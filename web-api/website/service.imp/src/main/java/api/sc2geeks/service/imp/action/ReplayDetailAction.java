package api.sc2geeks.service.imp.action;

import api.sc2geeks.service.imp.ReplayManagerImp;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 5/6/12
 * Time: 9:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReplayDetailAction extends ActionBase
{
	String replayId;
	boolean withRelatedInfo = false;
	boolean withAllInfo = false;

	public void setReplayId(String replayId)
	{
		this.replayId = replayId;
	}

	public void setWithRelatedInfo(boolean withRelatedInfo)
	{
		this.withRelatedInfo = withRelatedInfo;
	}

	public boolean isWithAllInfo() {
		return withAllInfo;
	}

	public void setWithAllInfo(boolean withAllInfo) {
		this.withAllInfo = withAllInfo;
	}

	@Override
	protected String doExecute()
	{
		if (StringUtils.isNotBlank(replayId))
		{
			ReplayManagerImp manager = new ReplayManagerImp();
			if (withAllInfo) {
				setValue("returnValue", manager.getReplayWithAllInfo(replayId));
			}
			else if (withRelatedInfo) {
				setValue("returnValue", manager.getReplayWithRelatedInfo(replayId));
			}
			else
			{
				setValue("returnValue", manager.getReplay(replayId));
			}
		}
		return SUCCESS;
	}
}
