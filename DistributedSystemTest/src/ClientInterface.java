import rpc.AnnotatedObject;
import rpc.Asynchronous;
import rpc.Synchronous;


public class ClientInterface extends AnnotatedObject implements iClient
{
	@Synchronous
	public String dummyEngineString()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Synchronous
	public int randomClientNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Asynchronous
	public void receiveUpdateFromMaster(String updateValue) 
	{
		// TODO Auto-generated method stub
	}

	@Asynchronous
	public void setClientId(int id) 
	{
		// TODO Auto-generated method stub
		
	}	
}
