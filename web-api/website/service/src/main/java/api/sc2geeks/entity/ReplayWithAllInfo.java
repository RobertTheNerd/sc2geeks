package api.sc2geeks.entity;

import java.util.List;

/**
 * Created by robert on 11/16/14.
 */
public class ReplayWithAllInfo extends ReplayWithRelatedInfo {
	List<BuildAbility> abilityEvents;

	public List<BuildAbility> getAbilityEvents() {
		return abilityEvents;
	}

	public void setAbilityEvents(List<BuildAbility> abilityEvents) {
		this.abilityEvents = abilityEvents;
	}
}
