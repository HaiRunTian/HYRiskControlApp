package tianchi.com.risksourcecontrol.config;



public class DefaultDataManager extends DataManager{

	public static final String s_mDefaultServer = "永安新塘.smwu";
	

	public DefaultDataManager()
	{
		setWorkspaceServer(DefaultDataConfig.MapDataPath+ s_mDefaultServer);
	}




}
