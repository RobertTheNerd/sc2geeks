package api.sc2geeks.entity;

/**
 * Created by robert on 11/16/14.
 */
public class BuildAbility {
	private String ability;
	private int pid;
	private int frame;

	public String getAbility() {
		return ability;
	}

	public void setAbility(String ability) {
		this.ability = ability;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}
}
