import com.sc2geeks.replay.dao.ReplayDAO;
import org.apache.log4j.BasicConfigurator;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/11/12
 * Time: 8:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class Tester
{

	public static void main(String[] args)
	{
		BasicConfigurator.configure();
		// GameEntity game = ReplayDAO.getGame(70);
		// ReplayParser.parse(game);
		List<String> list = ReplayDAO.getMapUrlsWithoutImage();
		for (String name : list)
			System.out.println(name);
	}
}
