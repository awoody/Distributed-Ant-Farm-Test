import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rpc.AnnotatedObject;
import utilities.A;
import client.Client;

import communication.Recipient;
import communication.iPortal;

import constants.iConstants;



public class ClientTest extends Recipient implements iClient, Runnable
{	
	private EngineInterface engine;
	Integer myId;
	int iAm;
	private iPortal portal;
	private static Map<Integer, Long> clientToServerRegister;
	private static Map<Integer, Long> serverToClientRegister;
	private static int totalClients = 30;
	static long clientSleepTime = 200;
	static long serverSleepTime = 500;
	static long testSleepTime = 5000;
	static long timeoutThreshold = 10000;
	private int lastIntGenerated;
	
	public static void main(String [] args)
	{		
		clientToServerRegister = new HashMap<Integer, Long>();
		serverToClientRegister = new HashMap<Integer, Long>();
		
		long now = System.currentTimeMillis() + 5000;
				
		for(int i=0; i < totalClients; i++)
		{
			ClientTest c = new ClientTest(i);
			
			//fixedThreadPool.submit(c);
			
			Thread clientThread = new Thread(c);
			clientThread.start();
			clientToServerRegister.put(i, now);
			serverToClientRegister.put(i, now);
		}
		
		while(true)
		{
			long currentTime = System.currentTimeMillis();
			
			boolean isBroken = false;
			
			int [] clientToServerLag = new int[totalClients];
			int [] serverToClientLag = new int[totalClients];
			
			for(int i=0; i < totalClients; i++)
			{
				Long lastCtoSComm;
				Long lastStoCComm;
				
				synchronized(clientToServerRegister)
				{
					lastCtoSComm = clientToServerRegister.get(i);
				}
				
				synchronized(serverToClientRegister)
				{
					lastStoCComm = serverToClientRegister.get(i);
				}
				
				if(lastCtoSComm == null || lastStoCComm == null)
				{
					isBroken = true;
					break;
				}
				
////				if(currentTime - lastCtoSComm >= clientSleepTime + 500)
////					System.out.println("There is some lag between client: " + i + " and the server.");
//				
//				long time = (currentTime - lastCtoSComm);
//				
//				if(time > clientSleepTime) time -= clientSleepTime;
//				
//				clientToServerLag[i] = (int) time;
				
				if(currentTime - lastCtoSComm >= timeoutThreshold)
				{
					A.error("Client " + i + " has not heard back from the server in: " + (currentTime - lastCtoSComm) + " miliseconds.");
					isBroken = true;
				}
				
////				if(currentTime - lastStoCComm >= serverSleepTime + 500)
////					System.out.println("There is some lag between the server and client " + i);
//				
//				time = (currentTime - lastCtoSComm);		
//				if(time > serverSleepTime) time -= serverSleepTime;
//				
//				serverToClientLag[i] = (int) time;
				
				
				if(currentTime - lastStoCComm >= timeoutThreshold)
				{
					A.error("The server has not broadcast to client: " + i + " in " + (currentTime - lastStoCComm) + " miliseconds.");
					isBroken = true;
				}
				
				
			}
			
			if(isBroken)
				A.fatalError("The client and server communication has broken down during the last " + testSleepTime + " miliseconds.");	
			
			double averageCtoS = A.average(clientToServerLag);
			double averageStoC = A.average(serverToClientLag);
			
			double CtoSvar = A.standardDeviation(clientToServerLag, averageCtoS);
			double StoCvar = A.standardDeviation(serverToClientLag, averageStoC);
			
			A.error("Client passed assertion tests normally");
//			A.error("Average StoC lag: " + averageStoC + " and variance: " + StoCvar);
//			A.error("Average CtoS lag: " + averageCtoS + " and variance: " + CtoSvar);
//			System.out.println();
			
			try
			{
				Thread.sleep(testSleepTime);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public ClientTest(int iAm)
	{
		iConstants testConstants = new TestConstants();
		portal = new Client(this, testConstants);
		engine = (EngineInterface) portal.makeNewConnection("TestEngine");
		this.iAm = iAm;
	}
	
	@Override
	public String dummyEngineString() 
	{
		// TODO Auto-generated method stub
		return "This string came from the client";
	}

	@Override
	public void run() 
	{
		
		
		while(true)
		{
			
//				continue;
			
			if(myId != null)
			{	
				int testString = engine.dummyEngineString(myId);		
				//A.error("Client " + iAm + " got a test string from the engine: " + testString);
				
				if(testString != lastIntGenerated)
					A.fatalError("Client " + iAm + " failed to receive a matching test int from the server.");
				
				
				
				clientToServerRegister.put(iAm, System.currentTimeMillis());
				
			}
			
			try 
			{
				Thread.sleep(clientSleepTime);
			} 
			catch (InterruptedException e) 
			{
				//System.out.println("Client thread was interrupted.");
				//e.printStackTrace();
			}
		}
		
	}

	@Override
	public void newObjectHasConnected(AnnotatedObject newObject) 
	{
		System.out.println("This should never happen on the client.");
	}

	@Override
	public String getResourceName() 
	{
		// TODO Auto-generated method stub
		return "TestClient";
	}

	@Override
	public int randomClientNumber() 
	{
		// TODO Auto-generated method stub
		int value = A.randomIntFromZeroToBound(2000);
		//System.out.println("Generated number: " + value + " on the client.");
		lastIntGenerated = value;
		return value;
	}

	@Override
	public void receiveUpdateFromMaster(String updateValue)
	{
		//System.out.println("Client " + iAm + " received an update from the server: " + updateValue);
		
		
		serverToClientRegister.put(iAm, System.currentTimeMillis());
		
	}

	@Override
	public void setClientId(int id)
	{
		System.out.println("Set the clientId to: " + id);
		this.myId = id;	
	}
}
