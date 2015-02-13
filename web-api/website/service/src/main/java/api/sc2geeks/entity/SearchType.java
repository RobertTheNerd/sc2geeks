package api.sc2geeks.entity;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 4/5/12
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */
public enum SearchType
{

	/**
	 * Normal replay search, returns replays as well as related navigation structure
	 */
	WithRefinement,

	/**
	 * Only returns navigation structure, no replays are included.
	 */
	RefinementOnly,

	/**
	 * Only returns replays, no navigation structure is included
	 */
	NoRefinement,
}
