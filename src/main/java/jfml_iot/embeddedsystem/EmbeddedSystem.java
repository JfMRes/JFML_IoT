package jfml_iot.embeddedsystem;
import java.util.ArrayList;
import jfml_iot.communication.*;
import jfml_iot.device.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;


/**
 * Embedded Object. Used to manage devices, test the communication and facilitate debug
 * 
 * @author 
 *
 */
public abstract class EmbeddedSystem {
	
	String name;
	ArrayList<Device> devices = new ArrayList<Device>();

	Communication communication;
	
	String WifiSSID = null;
	String Wifipassord = null;
	
	
	LocalDateTime lastCheck = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
	
	/**
	 * Maximum timeout value, if exceeded, it indicates an error.
	 */
	Integer maxTimeOut = 3000;
	
	
	/**
	 * Time flag, if exceeded, communication is tested.
	 */
	Integer timeOutFlag = maxTimeOut/2;
	
	
	/**
	 * Default constructor
	 * 
	 * @param name
	 * @param devices
	 * @param communication
	 */
	public EmbeddedSystem(String name, ArrayList<Device> devices, jfml_iot.communication.Communication communication) {
		super();
		this.name = name;
		this.devices = devices;
		this.communication = communication;
		
	}
	
	/**
	 * Constructor without devices, they can be added later
	 * 
	 * @param name
	 * @param communication
	 */
	public EmbeddedSystem(String name, jfml_iot.communication.Communication communication) {
		super();
		this.name = name;
		this.communication = communication;
	}
	
	/**
	 * @return Name of this Embedded.
	 */
	public String getName() { return this.name;}
	
	/**
	 * Set the timeout on communication for the embedded
	 * 
	 * @param time
	 */
	public void setTimeOut(Integer time) {
		this.maxTimeOut = time;
		this.timeOutFlag = time/2;
	}
	
	
	/**
	 * Update the time for the last check on this Embedded
	 */
	public void resetTime() {
		this.lastCheck = LocalDateTime.now();
	}
	
	
	/**
	 * Used in debug() function, created to simplify the process
	 * 
	 * @return String with debug text for the device and communication.
	 */
	public String debugStatus() {

		String debugStatus = "";
		
		DateTimeFormatter formatted = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDate = lastCheck.format(formatted);

		debugStatus += formattedDate + " Embedded --> " + name + "\t --> Status --> "; 
		
		Duration duration = Duration.between(lastCheck, LocalDateTime.now());
		long milliseconds = duration.toMillis();
		
		if(milliseconds < maxTimeOut) {
			debugStatus += "OK \t\n";
			return debugStatus;
		}
		
		if(milliseconds < 1000) {  debugStatus += milliseconds + "ms\n";}
		else if(milliseconds < 1000 * 60) {  debugStatus += "TimeOut : " + milliseconds/1000 + "s\n";}
		else if(milliseconds < 1000 * 60 * 60) {  debugStatus += "TimeOut : " + milliseconds/1000/60 + "m\n";}
		else if(milliseconds < 1000 * 60 * 60 * 24) {  debugStatus += "TimeOut : " + milliseconds/1000/60 + "h\n";}
		else {debugStatus +="TimeOut : >1d\n";}
		return debugStatus;
	}

	/**
	 * Collect the debug text in each device, and the debug text for the embedded and communication.
	 * @return String with debug text for embedded and their devices.
	 */
	public String debug(){
		
		String debugString = "";
		debugString += debugStatus();
		
		for(Device device: devices) {
			if(device instanceof Sensor) {
				debugString += device.debug();
			}
		}
		
		for(Device device: devices) {
			if(device instanceof Actuator) {
				debugString += device.debug();
			}
		}
		return debugString;
	}	
	
	
	/**
	 * Used in jfml_iot.core, to check timeOut, error code:
	 * - 0: No flag, no error.
	 * - 1:	Time flag, communication should be tested.
	 * - 2:	Maximum time out has been exceeded, retry connection and show an error.
	 * 
	 * @return error code
	 */
	public int checkStatus() {
		Duration duration = Duration.between(lastCheck, LocalDateTime.now());
		long milliseconds = duration.toMillis();
		if(milliseconds < timeOutFlag) { return 0;}
		if(milliseconds < maxTimeOut) { return 1;}
		else{ return 3;}
	}
	
	
	/**
	 * Add a device associated to this embedded.
	 * 
	 * @param device
	 */
	public void addDevice(Device device) {
		
		device.setCommunication(communication);
		this.devices.add(device);
	}
	
	/**
	 * Add a list of devices associated to this embedded.
	 * 
	 * @param devices
	 */
	public void addDevices(ArrayList<Device> devices) {
		for(Device device: devices) {
			this.devices.add(device);
		}
	}
	
	/**
	 * Set SSID and password for WiFi connection. Used in code.
	 * 
	 * @param WifiSSID
	 * @param Wifipassord
	 */
	public void setWifi(String WifiSSID, String Wifipassord) {
		this.WifiSSID = WifiSSID;
		this.Wifipassord = Wifipassord;
	}
	
	/**
	 * @return devices associated to this embedded.
	 */
	public ArrayList<Device> getDevices() {
		return devices;
	}
	
	/**
	 * Manage code on devices, communication and this device.
	 * 
	 * @return Text with the code for the embedded.
	 */
	public abstract String buildCode();
	
	/**
	 * @return File extension. Example: ".ino"
	 */
	public abstract String getFileExtension();
	

	

	

}