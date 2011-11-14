
public interface iClient 
{
	public String dummyEngineString();
	
	public int randomClientNumber();
	
	public void receiveUpdateFromMaster(String updateValue);
	
	public void setClientId(int id);
}
