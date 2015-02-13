package api.sc2geeks.service.imp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by robert on 8/28/14.
 */
public class MongoConfig {

	private static MongoConfig statsInstance, replayInstance;
	private MongoConfig(){}

	static {
		ApplicationContext context = null;

		String configFile = System.getProperty("api.sc2geeks.service.imp.mongoConfig");
		if (StringUtils.isNotBlank(configFile)) {
			try {
				context = new FileSystemXmlApplicationContext(configFile);
			}catch(Exception e){
				context = null;
			}
		}
		if (context == null)
			context	= new ClassPathXmlApplicationContext("config.xml");
		statsInstance = context.getBean("mongoStatsConfig", MongoConfig.class);
		replayInstance = context.getBean("mongoReplayConfig", MongoConfig.class);
	}

	public static MongoConfig getStatsConfig() { return statsInstance; }
	public static MongoConfig getReplayConfig(){ return replayInstance; }

	private String mongoHost;
	private int mongoPort;
	private String mongoDBName;

	public String getMongoHost() {
		return mongoHost;
	}

	public void setMongoHost(String mongoHost) {
		this.mongoHost = mongoHost;
	}

	public int getMongoPort() {
		return mongoPort;
	}

	public void setMongoPort(int mongoPort) {
		this.mongoPort = mongoPort;
	}

	public String getMongoDBName() {
		return mongoDBName;
	}

	public void setMongoDBName(String mongoDBName) {
		this.mongoDBName = mongoDBName;
	}
}
