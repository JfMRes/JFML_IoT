package jfml_iot.test;

import java.io.File;

import jfml_iot.JFML_IoT_Core;
import jfml_iot.communication.*;
import jfml_iot.device.arduino.ArduinoRGBLed;
import jfml_iot.device.arduino.ArduinoSR04;
import jfml_iot.embeddedsystem.*;
import jfml.FuzzyInferenceSystem;
import jfml.JFML;

public class JFML_IOT_SR04_RGB {

	public static void main(String[] args) {
		
		String address = "tcp://******";
		Integer port = 1883;
        String user = "user";
        String password = "password";
        Integer qos = 0;
        
        String SSID = "****";
        String WiFiPassword = "****";
        
        MQTT communication = new MQTT(address, port,user,password,qos);
        
		ArduinoSR04 sr04 = new ArduinoSR04("SR04","distance");
		sr04.setPinEchoTrigger("0","1");
		sr04.setMinMaxReadValue("distance",0f,30f);
		
		
		
		ArduinoRGBLed led_rgb = new ArduinoRGBLed("rgb","R","G","B");
		led_rgb.setThresholdValue(50f,50f,50f);
		led_rgb.setPin("2", "3", "4");
		
		ArduinoMegaOsoyoo arduino = new ArduinoMegaOsoyoo("Arduino_SR04_RGB",communication);
		arduino.setWifi(SSID, WiFiPassword);
		
		arduino.addDevice(sr04);
		arduino.addDevice(led_rgb);
		
		
	   File xml = new File("./testXML/SRO04_RGB.xml");
	   FuzzyInferenceSystem jfml = JFML.load(xml);
		
		
		JFML_IoT_Core sistema = new JFML_IoT_Core();
		
		sistema.setFuzzyInferenceSystem(jfml);
		
		sistema.addCommunication(communication);
		sistema.addEmbedded(arduino);

		sistema.buildCode("./testEmbeddedsCodes");
		sistema.run();
		
		
		
	}

}
