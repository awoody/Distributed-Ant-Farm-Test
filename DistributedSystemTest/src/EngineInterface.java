import rpc.AnnotatedObject;
import rpc.Synchronous;


public class EngineInterface extends AnnotatedObject implements iEngine
{
	@Synchronous
	public int dummyEngineString(int clientId) 
	{
		// TODO Auto-generated method stub
		return -5;
	}	
}
