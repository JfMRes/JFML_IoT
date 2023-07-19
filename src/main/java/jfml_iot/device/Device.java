package jfml_iot.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDateTime;
import jfml_iot.Debug;
import jfml_iot.communication.*;

/**
 * Java class for devices. Used as register with the variables, and timestamp
 * for last read messages.
 * 
 * Mewthods and constructor was created for a maximum of three variables per device and three pins per device
 * 
 * @author
 *
 */
public abstract class Device implements Debug {

	protected String name;

	protected HashMap<String, Float> valueId = new HashMap<String, Float>();
	protected HashMap<String, LocalDateTime> dateId = new HashMap<String, LocalDateTime>();
	
	/**
	 * Max value in JFML XML, 100 by default. Its used to compute the max value to send to communication or the max value possible recived
	 */
	protected HashMap<String,Float> maxValues = new HashMap<String,Float>();
	/**
	 * Min value in JFML XML, 0 by default
	 */
	protected HashMap<String,Float> minValues = new HashMap<String,Float>();
	
	/**
	 * Max Read for analog values, 1024 by default
	 */
	protected HashMap<String,Float> maxReadValues = new HashMap<String,Float>();
	/**
	 * Min Read for analog values, 0 by default
	 */
	protected HashMap<String,Float> minReadValues = new HashMap<String,Float>();
	
	protected String pin1 = new String();
	protected String pin2 = new String();
	protected String pin3 = new String();
	
	protected String id1 = new String();
	protected String id2 = new String();
	protected String id3 = new String();
	
	
	
	
	protected Communication communication;

	
	/**
	 * Default constructor for devices with one variable
	 * 
	 * @param name Name of the device, used in debug
	 * @param id ID for the variable
	 */
	public Device(String name, String id) {
		super();
		this.name = name;
		
		this.valueId.put(id,0f);
		
		this.id1 = id;
		
		
		for (String key : valueId.keySet()) {
			dateId.put(key, LocalDateTime.of(2000, 1, 1, 0, 0, 0));
			maxValues.put(key,100f);
			minValues.put(key,0f);
			maxReadValues.put(key,100f);
			minReadValues.put(key,0f);
		}
		

	}
	
	
	/**
	 * Default constructor for devices with two variables
	 * 
	 * @param name Name of the device, used in debug
	 * @param id ID1 for the variable1
	 * @param id ID2 for the variable2
	 */
	public Device(String name, String id1, String id2) {
		super();
		this.name = name;
				
		this.valueId.put(id1,0f);
		this.valueId.put(id2,0f);
		this.id1 = id1;
		this.id2 = id2;
		
		for (String key : valueId.keySet()) {
			dateId.put(key, LocalDateTime.of(2000, 1, 1, 0, 0, 0));
			maxValues.put(key,100f);
			minValues.put(key,100f);
			maxReadValues.put(key,100f);
			minReadValues.put(key,100f);
		}
	}
	
	/**
	 * Default constructor for devices with three variables
	 * 
	 * @param name Name of the device, used in debug
	 * @param id ID1 for the variable1
	 * @param id ID2 for the variable2
	 * @param id ID3 for the variable3
	 */
	public Device(String name, String id1, String id2, String id3) {
		super();
		this.name = name;
		
		this.valueId.put(id1,0f);
		this.valueId.put(id2,0f);
		this.valueId.put(id3,0f);
		
		this.id1 = id1;
		this.id2 = id2;
		this.id3 = id3;
		
		
		for (String key : valueId.keySet()) {
			dateId.put(key, LocalDateTime.of(2000, 1, 1, 0, 0, 0));
			maxValues.put(key,100f);
			minValues.put(key,100f);
			maxReadValues.put(key,100f);
			minReadValues.put(key,100f);
		}
	}
	
	
	
	/**
	 * Method to select pin for device with one pin
	 * 
	 * @param pin1
	 */
	public void setPin(String pin1) {
		this.pin1 = pin1;
	}
	
	/**
	 * * Method to select pins for device with two pins
	 * 
	 * @param pin1
	 * @param pin2
	 */
	public void setPin(String pin1,String pin2) {
		this.pin1 = pin1;
		this.pin2 = pin2;
	}
	
	/**
	 * * Method to select pins for device with three pins
	 * 
	 * @param pin1
	 * @param pin2
	 * @param pin3
	 */
	public void setPin(String pin1,String pin2,String pin3) {
		this.pin1 = pin1;
		this.pin2 = pin2;
		this.pin3 = pin3;
	}
	
	
	/**
	 * Given de id of a variable, set the maximun and minimun value for a variable in JFML
	 * 
	 * @param id
	 * @param min
	 * @param max
	 */
	public void setMinMaxValue(String id, Float min, Float max) {
		if(valueId.keySet().contains(id)) {
			minValues.put(id,min);
			maxValues.put(id,max);
		}
	}
	
	
	/**
	 *  Given de id of a variable, set the maximun and minimun read value 
	 *  
	 * @param id
	 * @param min
	 * @param max
	 */
	public void setMinMaxReadValue(String id, Float min, Float max) {
		if(valueId.keySet().contains(id)) {
			minReadValues.put(id,min);
			maxReadValues.put(id,max);
		}
	}

	/**
	 * @return HashMap containing the register of the variables.
	 */
	public HashMap<String, Float> getValues() {

		for (String id : valueId.keySet()) {
			float value = communication.getValue(id);
			if (valueId.containsKey(id)) {
				valueId.put(id, value);
			}
		}

		return valueId;

	}
	
	/**
	 * Set a new value for a given ID.
	 * 
	 * @param id    ID used in the register.
	 * @param value New value for the ID.
	 */
	public void setValue(String id, Float value) {
		if (valueId.containsKey(id)) {
			valueId.put(id, value);
			dateId.put(id, LocalDateTime.now());
		}
	}

	/**
	 * Set news values for given IDs. It calls setValue.
	 * 
	 * @param values HashMap with new values for the variables
	 */
	public void setValues(HashMap<String, Float> values) {
		for (String id : values.keySet()) {
			Float value = values.get(id);
			setValue(id, value);
		}
	}

	/**
	 * @return ArrayList with the IDs in the register
	 */
	public ArrayList<String> getIds() {

		ArrayList<String> temp_list = new ArrayList<String>();

		for (String id : valueId.keySet()) {
			temp_list.add(id);
		}
		return temp_list;

	}

	/**
	 * Return the value for a a given ID.
	 * 
	 * @param id Given ID
	 * @return Value for its ID
	 */
	public Float getValue(String id) {
		return valueId.get(id);
	}

	/**
	 * Return the object communication.
	 * 
	 * @return
	 */
	public Communication getCommunication() {
		return communication;
	}

	/**
	 * Set the communication.
	 * 
	 * @param communication
	 */
	public void setCommunication(Communication communication) {
		this.communication = communication;
	}


	/**
	 * @return Code for initial code in Arduino
	 */
	public abstract String getArduinoInitialCode();

	/**
	 * @return Code for Setup code in Arduino
	 */
	public abstract String getArduinoSetupCode();

	/**
	 * @return Code for Loop code in Arduino
	 */
	public abstract String getArduinoLoopCode();

	/**
	 * @return Debug text for the device.
	 */
	public abstract String debug();

}
