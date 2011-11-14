import java.util.HashMap;
import java.util.Map;

import communication.NodeId;
import constants.iConstants;


public class TestConstants implements iConstants
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4045879537830411617L;

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Class> getAnnotatedObjectsByString() 
	{
		HashMap<String, Class> retVal = new HashMap<String, Class>();
		
		retVal.put("TestClient", ClientInterface.class);
		retVal.put("TestEngine", EngineInterface.class);
		
		return retVal;
	}

	@Override
	public int getDefaultDistributorPort()
	{
		// TODO Auto-generated method stub
		return 10000;
	}

	@Override
	public String getDefaultDistributorHostName()
	{
		// TODO Auto-generated method stub
		return "192.168.1.102";
		//return "localhost";
	}

	@Override
	public NodeId getDistributorNodeId()
	{
		// TODO Auto-generated method stub
		return new NodeId(0,0);
	}
	
}
