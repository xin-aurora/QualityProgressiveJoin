package framework;


public class MachineInfo {
	
	private String mWIP;
	private String mWPort;
	private int mWId;
	
	public MachineInfo(String ip, String port, int id) {
		this.mWIP = ip;
		this.mWPort = port;
		this.mWId = id;
	}
	
	@Override
	public final String toString() {
		return "IP Addr = " + this.mWIP + ":" + this.mWPort + ", id = " + this.mWId;
	}

}
