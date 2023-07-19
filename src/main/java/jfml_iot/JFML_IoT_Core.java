package jfml_iot;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import jfml_iot.communication.*;
import jfml_iot.device.*;
import jfml_iot.embeddedsystem.*;
import jfml.FuzzyInferenceSystem;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main class to manage the whole system
 * 
 * @author 
 *
 */
public class JFML_IoT_Core implements Debug{
	
	public ArrayList<EmbeddedSystem> embeddeds = new ArrayList<EmbeddedSystem>();
	public Communication communication;
	FuzzyInferenceSystem jfml_system =  new FuzzyInferenceSystem();
	Integer cycleDelay = 1000;
	
	
	/**
	 * Main constructor
	 * 
	 * @param embeddeds
	 * @param communication
	 * @param jfml_system
	 */
	public JFML_IoT_Core(ArrayList<EmbeddedSystem> embeddeds, Communication communication, FuzzyInferenceSystem jfml_system) {
		super();
		this.embeddeds = embeddeds;
		this.communication = communication;
		this.jfml_system = jfml_system;
		
	}
	
	/**
	 * Main constructor, embeddeds, communication and jfml system can be added later.
	 */
	public JFML_IoT_Core() {
		super();
		
	}
	
	/**
	 * Function to set jfml_system.
	 * 
	 * @param jfml_system
	 */
	public void setFuzzyInferenceSystem(FuzzyInferenceSystem jfml_system) {
		this.jfml_system = jfml_system;
	}
	
	/**
	 * Function to set the communication.
	 * 
	 * @param communication
	 */
	public void addCommunication(Communication communication) {
		this.communication = communication;
	}
	
	/**
	 * Function to add an Embedded to its register.
	 * 
	 * @param embedded
	 */
	public void addEmbedded(EmbeddedSystem embedded) {
		embeddeds.add(embedded);
	}
	
	
	/**
	 * Function to build the files woth the code for each embedded.
	 * 
	 * @param path Path where build embedded's code
	 */
	public void buildCode(String path) {
		
		configCommunication();
		
		for(EmbeddedSystem embedded: embeddeds) {
			String extension = embedded.getFileExtension();
			String code = embedded.buildCode();
			String embedded_name = embedded.getName();
			
			File EmbeddedFolder = new File(path+"/"+embedded_name);
			
			if (!EmbeddedFolder.exists()){
				EmbeddedFolder.mkdirs();
			}
			
			try {
				FileWriter writer = new FileWriter(path + "/"+embedded_name +  "/" + embedded_name + "" + extension);
	            writer.write(code);
	            writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Function for the system inference.
	 * - 1. Read the values of each input device and load to JFML
	 * - 2. Makes the inference
	 * - 3. Read the values of each output given by JFML, save the value on its device, and send the information by the communication.
	 * 
	 */
	private void inference() {
		
		for(EmbeddedSystem e: embeddeds) {
		for(Device device: e.getDevices()) {
			if(device instanceof Sensor)
				for(String id: device.getIds()) {
					Float value = device.getValue(id);
					jfml_system.getVariable(id).setValue(value);
				}
		}}
		
		jfml_system.evaluate();
		
		for(EmbeddedSystem e: embeddeds) {
		for(Device device: e.getDevices()) {
			if(device instanceof Actuator)
				for(String id: device.getIds()) {
					Float value = jfml_system.getVariable(id).getValue();
					Float valueOld = device.getValue(id);
					if(Math.abs(value - valueOld) > 0.001f) {
						System.out.println(value);
						System.out.println(valueOld);
						
						device.setValue(id, value);
						communication.setValueComputed(id, value);
						
					}
					
				}
		}}
	}
	
	/**
	 * Add each embedded to the communication register.
	 */
	private void configCommunication() {
		for(EmbeddedSystem embedded : embeddeds) {
			this.communication.addEmbedded(embedded);
		
		}
	}
	
	
	/**
	 * Check the status of each embedded.
	 */
	public void checkEmbeddedsStatus() {
		for(EmbeddedSystem e: embeddeds) {
			if(e.checkStatus()!=0) {
				communication.checkEmbedded(e);
			}
		}
	}
	
	
	/**
	 *	Collect the debug text from each embedded (including its devices).
	 */
	public String debug(){
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatted = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDate = now.format(formatted);
		String debug = "\nSystem update at: "+ formattedDate + "\n";
		for(EmbeddedSystem embedded: embeddeds) {
			debug += "\n" + embedded.debug();
		}
		return debug;
	}
	
	/**
	 * Set the delay time between each cycle, in other word, time between each inference.
	 * 
	 * @param miliseconds
	 */
	public void setDelay(Integer miliseconds) {
		this.cycleDelay = miliseconds;
	}
	
	/**
	 * Configure the communication, and inicialize it.
	 * 
	 * Then get in an infinity loop to set the system alive, where check the embedded and makes the inference.
	 */
	public void run(){
		
		configCommunication();
		communication.connect();
		
		while(true) {
			checkEmbeddedsStatus();
			inference();
			System.out.println(debug());
			try {
				Thread.sleep(cycleDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	}
