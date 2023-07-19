package jfml_iot.test;

import java.io.File;

import jfml_iot.JFML_IoT_Core;
import jfml_iot.communication.*;
import jfml_iot.device.arduino.ArduinoPotentiometer;
import jfml_iot.device.arduino.ArduinoServo;
import jfml_iot.embeddedsystem.*;
import jfml.FuzzyInferenceSystem;
import jfml.JFML;

public class JFML_IOT_PotentiometerStepper {

	public static void main(String[] args) {
		
	String address = "tcp://******";
	address = "tcp://192.168.1.31";
	
	Integer port = 1883;
    String user = "user";
    String password = "password";
    Integer qos = 0;
    
    String SSID = "****";
    SSID = "MiFibra-9504";
    
    String WiFiPassword = "****";
    WiFiPassword = "2HoPcrad";
    
    
    
    MQTT communication = new MQTT(address, port,user,password,qos);
    
	ArduinoPotentiometer potentiometer = new ArduinoPotentiometer("potentiometer","potentiometer");
	potentiometer.setPin("A0");
	potentiometer.setMinMaxReadValue("potentiometer", 0f, 1024f);
	potentiometer.setMinMaxValue("potentiometer", 0f, 100f);
	
	
	
	ArduinoServo servo = new ArduinoServo("stepper","stepper");
	servo.setPin("4");
	
	ArduinoNodeMCU arduino = new ArduinoNodeMCU("ArduinoPotentiometerServo",communication);
	arduino.setWifi(SSID, WiFiPassword);
	
	arduino.addDevice(potentiometer);
	arduino.addDevice(servo);
	
	
   File xml = new File("./testXML/PotentiometerStepper.xml");
   FuzzyInferenceSystem jfml = JFML.load(xml);
	
	
	JFML_IoT_Core iotSystsem = new JFML_IoT_Core();
	iotSystsem.setDelay(3000);
	
	iotSystsem.setFuzzyInferenceSystem(jfml);
	
	iotSystsem.addCommunication(communication);
	iotSystsem.addEmbedded(arduino);

	iotSystsem.buildCode("./testEmbeddedsCodes");
	iotSystsem.run();
		
		
	}

}
