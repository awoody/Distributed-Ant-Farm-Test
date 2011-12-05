import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import rpc.AnnotatedObject;
import server.Server;
import utilities.A;

import communication.Recipient;
import communication.iPortal;


public class CentralEngineTest extends Recipient implements iEngine, Runnable
{
	private List<iClient> connectedClients;
	private ArrayList<Integer> clientList;
	private iPortal portal;
	private int nextClientId;
	
	public static void main(String [] args)
	{		
		CentralEngineTest test = new CentralEngineTest();
		Thread engineThread = new Thread(test);
		engineThread.start();
	}

	public CentralEngineTest()
	{
		TestConstants constants = new TestConstants();
		portal = new Server(this, 10002, constants);	
		connectedClients = new ArrayList<iClient>();
		clientList = new ArrayList<Integer>();
		nextClientId = 0;
	}
	
	@Override
	public void run() 
	{
		//System.out.println("Starting central engine run loop.");
		
		while(true)
		{			
			long currentTime = System.currentTimeMillis();
			
			int broadcasts = 0;
			
			for(int i = 0; i < connectedClients.size(); i++)
			{
				iClient client = connectedClients.get(i);
				client.receiveUpdateFromMaster("Current time is: " + currentTime);
				broadcasts++;
			}
			
			A.error("Broadcast " + broadcasts + " updates to clients.");
			
			try
			{
				Thread.sleep(ClientTest.serverSleepTime);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
 
	@Override
	public int dummyEngineString(int clientId)
	{
		iClient connectedClient = connectedClients.get(clientId);
		
		if(connectedClient == null)
			return -1;
		
		int value = connectedClient.randomClientNumber();
		
		return value;
	}

	@Override
	public void newObjectHasConnected(AnnotatedObject newObject) 
	{
		if(newObject instanceof ClientInterface)
		{	
			iClient newClient = (iClient) newObject;
			
			int id = nextClientId++;
			newClient.setClientId(id);
						
			connectedClients.add(newClient);		
		}
	}

	@Override
	public String getResourceName() 
	{
		return "TestEngine";
	}
	
}
