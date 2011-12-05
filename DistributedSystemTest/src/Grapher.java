import gui.GUI;
import monitor.Graph;
import api.DistributedSystemFactory;
import rpc.AnnotatedObject;
import communication.Recipient;
import communication.iPortal;


public class Grapher extends Recipient implements Runnable
{
	//private GUI gui = new GUI(null);
	
	public static void main(String [] args)
	{
		Thread t = new Thread(new Grapher());
		t.start();
	}
	
	private final iPortal p;
	
	public Grapher()
	{
		p = DistributedSystemFactory.newClient(this, new TestConstants());
	}
	
	@Override
	public void run() 
	{	GUI ui = null;
		
		
		while(true)
		{
			Graph allNodes = p.getNetworkGraph();
			
			if(ui == null)
			{	
				ui = new GUI(allNodes);
			 	ui.setVisible(true);
			}
			
			ui.setTree(allNodes);
			System.out.println("Obtained a new graph with " + allNodes.getAllNodes().size() + " nodes");
			
			try 
			{
				Thread.sleep(10000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void newObjectHasConnected(AnnotatedObject newObject) 
	{
		// Not important.
	}

	@Override
	public String getResourceName() 
	{
		// TODO Auto-generated method stub
		return "None";
	}



}
