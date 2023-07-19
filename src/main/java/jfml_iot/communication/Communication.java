package jfml_iot.communication;

import java.util.ArrayList;
import java.util.HashMap;

import jfml_iot.device.Actuator;
import jfml_iot.device.Device;
import jfml_iot.embeddedsystem.EmbeddedSystem;

/**
 * Abstract class for handling communications
 *
 */
public abstract class Communication {
	
	public String adress;
	public ArrayList<EmbeddedSystem> embeddeds = new ArrayList<EmbeddedSystem>();

	
	/**
	 * Default constructor 
	 * 
	 * @param adress
	 */
	public Communication(String adress) {
		super();
		this.adress = adress;
	}
	
	/**
	 * Add object EmbeddedSystem to it's register
	 * @param embedded
	 */
	public void addEmbedded(EmbeddedSystem embedded) {
		embeddeds.add(embedded);
	}
	
	
	/**
	 * Add a list of objects EmbeddedSystem to it's register
	 * @param embeddedList
	 */
	public void addEmbeddeds(ArrayList<EmbeddedSystem> embeddedList) {
		for(EmbeddedSystem e: embeddedList) {
			addEmbedded(e);
		}
		
	}
	
	
	/**
	 * Establish the connection
	 */
	public abstract void connect();
	
	/**
	 * Disestablish the connection
	 */
	public abstract void disconnect();
	
	/**
	 * Return the value of a variable given its id
	 * @param id name of the variable in the system
	 * @return value associated to its id
	 */
	public float getValue(String topic) {
		Float value = 0f;
		
		for(EmbeddedSystem e: embeddeds) {
			for(Device device: e.getDevices()) {
				if(device.getIds().contains(topic)) {
					value = 11f;
					value = device.getValue(topic);
				}
			}
		}
		return value;
	}
	
	/**
	 * Send a message with the new value for the id. 
	 * @param id name of the variable in the system
	 * @param value new value for this id
	 */
	public abstract void sendValue(String id, Float value);
	
	
	
	/**
	 * Save the computed value on its correspondent device and send and call sendValue to send the message. 
	 * @param id name of the variable in the system
	 * @param value new value for this id
	 */
	public void setValueComputed(String id, Float value) { //sacar

		for (EmbeddedSystem e : embeddeds) {
			for (Device dev : e.getDevices()) {
				if (dev.getIds().contains(id)) {
					if (!dev.getValue(id).equals(value)) {
						dev.setValue(id, value);
						sendValue(id,value);
					}
				}
			}
		}

	}
	
	/**
	 * Save the recived value on its correspondent device.
	 * @param id name of the variable in the system
	 * @param value new value for this id
	 */
	public void setValueRecived(String id, Float value) {
		for (EmbeddedSystem e : embeddeds) {
			for (Device device : e.getDevices()) {
				if (device.getIds().contains(id)) {
					device.setValue(id, value);
					return;
				}
				
			}
		}
	}
	
	
	/**
	 * Return the record of IDs and their corresponding value.
	 * 
	 * @return HashMap with ID and value
	 */
	public HashMap<String, Float> getValues() { 
		HashMap<String,Float> values = new HashMap<String,Float>();
		for(EmbeddedSystem e: embeddeds) {
			for(Device device: e.getDevices()) {
				for(String id: device.getIds()) {
					values.put(id, device.getValue(id));
				}
			}
		}
		return values;
	}
	
	/**
	 * Sends messages containing IDs and their corresponding values to all output devices of the embedded systems.
	 * This method is used when the embedded systems have a timeout.
	 *
	 * @param embeddedSystems The embedded systems to send the messages from.
	 */
	public void sendValuesToEmbedded(EmbeddedSystem e) {
		for (Device device : e.getDevices()) {
			if (device instanceof Actuator) {
				for (String topic : device.getIds()) {
					Float value = device.getValue(topic);
					sendValue(topic, value);
				}
			}
		}
	}

	/**
	 * 
	 * @return Code for the communication from Arduino.
	 */
	public abstract String getSendDataCodeArduino();
		
	/**
	 * Return the initial code in Arduino for a given EmbeddedSystem
	 * 
	 * @param embedded given EmbeddedSystem.
	 * @return Initial code in Arduino.
	 */
	public abstract String getArduinoInitialCode(EmbeddedSystem embedded);
	
	/**
	 * @return Setup code in Arduino
	 */
	public abstract String getArduinoSetupCode();
	
	/**
	 * @return Loop code in Arduino
	 */
	public abstract String getArduinoLoopCode();
	
	/**
	 *  Return the connect code in Arduino for a given EmbeddedSystem
	 * 
	 * @param embedded given EmbeddedSystem.
	 * @return Connection code with this communication
	 */
	public abstract String getArduinoConnectCode(EmbeddedSystem embedded);

	/**
	 * Send a check flag to a given embedded
	 * 
	 * @param embedded given embedded
	 */
	public void checkEmbedded(EmbeddedSystem embedded) {
		String id = embedded.getName() + "/status";
		sendValue(id, 1f);
	}
	
	
	
	
	
}
