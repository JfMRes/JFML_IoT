package jfml_iot.test;

import java.io.File;

import jfml_iot.JFML_IoT_Core;
import jfml_iot.communication.*;
import jfml_iot.device.arduino.*;
import jfml_iot.embeddedsystem.*;
import jfml.FuzzyInferenceSystem;
import jfml.JFML;

public class JFML_IOT_FirePirAlarm {

	public static void main(String[] args) {
		
		String address = "tcp://192.168.1.31";
		Integer port = 1883;
        String user = "user";
        String password = "password";
        Integer qos = 0;
        
        String SSID = "MiFibra-9504";
        String WiFiPassword = "2HoPcrad";
        
        
        MQTT mqtt = new MQTT(address, port,user,password,qos);
        
		ArduinoFireSensor fire = new ArduinoFireSensor("fire_sensor","fire");
		fire.setPin("12");
		fire.setMinMaxValue("fire",0f,100f);
				
		
		ArduinoBuzzer buzzer = new ArduinoBuzzer("buzzer","buzzer");
		buzzer.setMinMaxValue("fire",0f,100f);
		buzzer.setPin("14");
		
		
		ArduinoRGBLed rgb = new ArduinoRGBLed("rgb","red","green","blue");
		rgb.setPin("2", "0", "4");
		rgb.setMinMaxValue("red",0f,100f);
		rgb.setMinMaxValue("green",0f,100f);
		rgb.setMinMaxValue("blue",0f,100f);
		
		ArduinoPushButton alarm = new ArduinoPushButton("alarm","alarm");
		alarm.setMinMaxReadValue("alarm", 0f, 100f);
		alarm.setPin("13");
		
		
		ArduinoPIR motion = new ArduinoPIR("motion","motion");
		motion.setPin("12");
		
		
		ArduinoNodeMCU arduino1 = new ArduinoNodeMCU("arduino_FirePirAlarm_1",mqtt);
		arduino1.setWifi(SSID, WiFiPassword);
		arduino1.setTimeOut(3000);
		
		arduino1.addDevice(buzzer);
		arduino1.addDevice(rgb);
		arduino1.addDevice(motion);
		
		
		ArduinoNodeMCU arduino2 = new ArduinoNodeMCU("arduino_FirePirAlarm_2",mqtt);
		arduino2.setWifi(SSID, WiFiPassword);
		arduino2.setTimeOut(3000);
		
		arduino2.addDevice(fire);
		arduino2.addDevice(alarm);
		
		
		
		File xml = new File("./testXML/FirePirAlarm.xml");
		
		FuzzyInferenceSystem jfml = JFML.load(xml);

		JFML_IoT_Core IoT_system = new JFML_IoT_Core();
		
		IoT_system.setFuzzyInferenceSystem(jfml);
		
		IoT_system.addCommunication(mqtt);
		IoT_system.addEmbedded(arduino1);
		IoT_system.addEmbedded(arduino2);
		
		
		IoT_system.buildCode("./testEmbeddedsCodes");
		IoT_system.setDelay(1000);
		IoT_system.run();

		
	}

}
