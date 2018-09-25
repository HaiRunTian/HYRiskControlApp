package tianchi.com.risksourcecontrol.config;

import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;

import java.io.File;
import java.util.ArrayList;

public class DataManager {

	private String m_WorkspaceServer = "";
	
	private Datasets m_OpenedDatasets = null;
	
	private String m_DisplayMapName = "";
	
	protected Workspace m_Workspace = null;
	private int indexOfServer = -1;
	private boolean isDataOpen = false;
	private ArrayList<String> m_WorkspaceServerList = null;
	private ArrayList<Workspace> m_WorkspaceList = null;
	/**
	 * 构造函数
	 */
	public DataManager() {
		m_Workspace = new Workspace();
		m_WorkspaceServerList = new ArrayList<String>();
		m_WorkspaceList = new ArrayList<Workspace>();
	}
	
	/**
	 * 获取工作空间
	 * @return
	 */
	public Workspace getWorkspace(){
		if(indexOfServer>-1)
			m_Workspace = m_WorkspaceList.get(indexOfServer);
		return m_Workspace;
	}
	
	/**
	 * 打开数据源
	 * @param udb
	 * @return
	 */
	public boolean openUDB(String udb){
		Datasource _ds = null;
		boolean hasOpened = false;
		//先看看有没有再里边
		for(int i = m_Workspace.getDatasources().getCount()-1; i>=0; i--){
			_ds  = m_Workspace.getDatasources().get(i);
			if(_ds.getConnectionInfo().getServer().equals(udb)){
				hasOpened = true;
				break;
			}
		}
		if(!hasOpened){
			DatasourceConnectionInfo _dsInfo = new DatasourceConnectionInfo();
			_dsInfo.setServer(udb);
			String alias = udb.replace(".udb", "")+"_temp";
			_dsInfo.setAlias(alias);
			_dsInfo.setEngineType(EngineType.UDB);
			_ds = m_Workspace.getDatasources().open(_dsInfo);
		}
		if(_ds==null){
			return false;
		}
		m_OpenedDatasets = _ds.getDatasets();
		return true;
	}
	
	/**
	 * 获取数据集集合
	 * @return
	 */
	public Datasets getOpenedDatasets(){
		return m_OpenedDatasets;
	}

	/**
	 * 获取工作空间的路径
	 * @return
	 */
	public String getWorkspaceServer() {
		return m_WorkspaceServer;
	}

	/**
	 * 设置工作空间的路径
	 * @param WorkspaceServer
	 */
	public void setWorkspaceServer(String WorkspaceServer) {
		if(!m_WorkspaceServer.equals(WorkspaceServer)){
			this.m_WorkspaceServer = WorkspaceServer;
			isDataOpen = false;
		}
	}

	/**
	 * 获取当前打开的工作空间的名称
	 * @return
	 */
	public String getDisplayMapName() {
		if(isDataOpen){
			return m_DisplayMapName;
		}
		return "Workspace unOpen";
	}

	/**
	 * 设置打开的工作空间的名称
	 * @param DisplayMapName
	 */
	public void setDisplayMapName(String DisplayMapName) {
		if(isDataOpen){
			this.m_DisplayMapName = DisplayMapName;
		}
	}
	
	/**
	 * 打开工作空间
	 * @return
	 */
	public boolean open(){
		indexOfServer = m_WorkspaceServerList.indexOf(m_WorkspaceServer);
		if(indexOfServer>-1)
			return true;
		if(isDataOpen){
			return true;
		}
		File _wksFile = new File(m_WorkspaceServer);
		if(!_wksFile.exists()){
			return false;
		}
		WorkspaceType _type = null;
		if(m_WorkspaceServer.endsWith(".SMWU")|| m_WorkspaceServer.endsWith(".smwu"))
		{
			_type = WorkspaceType.SMWU;
		}else if(m_WorkspaceServer.endsWith(".SXWU")|| m_WorkspaceServer.endsWith(".sxwu"))
		{
			_type = WorkspaceType.SXWU;
		}
		WorkspaceConnectionInfo _info = new WorkspaceConnectionInfo();
		_info.setServer(m_WorkspaceServer);
		_info.setType(_type);
		m_Workspace.close();
		isDataOpen = m_Workspace.open(_info);
		if(isDataOpen){
			if(getMapCount()>=1){
				setDisplayMapName(getMapName(0));
			}
		}
		_info.dispose();
		_info = null;
		return isDataOpen;
	}
	
	/**
	 * 检测工作空间是否打开
	 * @return
	 */
	public boolean isDataOpen()
	{
		return isDataOpen;
	}
	
	public void close(){
		m_Workspace.close();
		isDataOpen = false;
	}
	
	/**
	 * 获取数据源数目
	 * @return
	 */
	public int getDatasourceCount(){
		if(isDataOpen){
			return m_Workspace.getDatasources().getCount();
		}
		return 0;
	}
	
	/**
	 * 获取指定序号的数据源
	 * @param index
	 * @return
	 */
	public Datasource getDatasource(int index){
		if(isDataOpen){
			return m_Workspace.getDatasources().get(index);
		}
		return null;
	}
	
	/**
	 * 获取指定名称的数据源
	 * @param name
	 * @return
	 */
	public Datasource getDatasource(String name){
		if(isDataOpen){
			return m_Workspace.getDatasources().get(name);
		}
		return null;
	}
	
	/**
	 * 获取地图数量
	 * @return
	 */
	public int getMapCount(){
		if(isDataOpen){
			return m_Workspace.getMaps().getCount();
		}
		return 0;
	}
	
	/**
	 * 获取指定序号的名称
	 * @param index
	 * @return
	 */
	public String getMapName(int index){
		if(isDataOpen){
			return m_Workspace.getMaps().get(index);
		}
		return null;
	}

	/**
	 * 初始化工作空间列表
	 * @param serverPath
	 */
	public void initWorkspace(String serverPath){
		if(serverPath == null)
			return;
		File _wksFile = new File(serverPath);
		if(!_wksFile.exists()){
			return;
		}
		if(m_WorkspaceServerList.contains(serverPath)){
		   return;
		}
		 m_WorkspaceServerList.add(serverPath);
		 Workspace _workspace = openWorkspace(serverPath);
		 m_WorkspaceList.add(_workspace);
	}
	private Workspace openWorkspace(String serverPath){
		Workspace _workspace = new Workspace();
		WorkspaceType type = null;
		if(serverPath.endsWith(".SMWU")||serverPath.endsWith(".smwu"))
		{
			type = WorkspaceType.SMWU;
		}else if(serverPath.endsWith(".SXWU")||serverPath.endsWith(".sxwu"))
		{
			type = WorkspaceType.SXWU;
		}
		WorkspaceConnectionInfo _info = new WorkspaceConnectionInfo();
		_info.setServer(serverPath);
		_info.setType(type);
		boolean isOpened = _workspace.open(_info);
		if(!isOpened){
			_workspace.dispose();
			return null;
		}
		_info.dispose();
		_info = null;
		return _workspace;
	}
}
