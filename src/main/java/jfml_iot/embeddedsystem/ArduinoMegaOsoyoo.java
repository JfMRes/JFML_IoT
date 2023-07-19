package jfml_iot.embeddedsystem;

import java.util.ArrayList;

import jfml_iot.device.Actuator;
import jfml_iot.device.Sensor;
import jfml_iot.device.Device;

public class ArduinoMegaOsoyoo extends EmbeddedSystem {

	public ArduinoMegaOsoyoo(String name, jfml_iot.communication.Communication communication) {
		super(name, communication);
	}

	public ArduinoMegaOsoyoo(String name, ArrayList<Device> devices,
			jfml_iot.communication.Communication communication) {
		super(name, devices, communication);

	}

	@Override
	public String buildCode() {
		
		String code = "// Code for " + name + "\n";

		if(WifiSSID != null) {
			code += "#include <WiFiEsp.h>"
					+ "\n"+ "const char* WifiSSID = \""+ WifiSSID+"\";"
					+ "\n"+ "const char* Wifipassword = \""+Wifipassord+"\";"
					+ "\n\n"+ "WiFiEspClient espClient;"
							+ "\n\n";
					
		}

		code += "bool first_loop = true;";

		for (Device device : devices) {
			code += "\n";
			code += device.getArduinoInitialCode();
		}
		code += "\n";
		
		code += communication.getArduinoInitialCode(this);
		
		// SETUP

		code += "\n\n"+"void setup(){";
		
		code += "\n\n\t" + "Serial.begin(9600);";
		
		if(WifiSSID != null) {
			code +=   "\n\t"+ "Serial1.begin(9600);"
					+ "\n\t" + "WiFi.init(&Serial1);"	
					+ "\n\t"+ "\n\t"+ "WiFi.begin(WifiSSID, Wifipassword);";
					
		}
		
		code += "\n" + this.communication.getArduinoSetupCode();
		
		code += "\n" + "";
		
		
		for (Device device : devices) {
			code += "\n\t";
			code += device.getArduinoSetupCode();
		}

		code += "\n}\n\n";
		
		
		
		code += communication.getSendDataCodeArduino();
		
		code += "\n\n";
		
		code += "void inputData(float newValue, float oldValue, String id, bool force){"
				+ "\n\t" + "if (force || newValue != oldValue) {"
				+ "\n\t\t" + "sendData(newValue, id);"
				+ "\n\t\t" + "}";
		
		
		
		code += "\n"+"}";
		
		
		// LOOP
		

		
		code += "\n\n" + "void loop(){" + "\n";
		
		code += this.communication.getArduinoLoopCode();
		
		for (Device device : devices) {
		
			if (device instanceof Sensor) {
				code += "\n\n";
				code += device.getArduinoLoopCode();
			}
		}
		
		for (Device device : devices) {

			if (device instanceof Actuator) {
				code += "\n\n";
				code += device.getArduinoLoopCode();
			}
		}
		
		code += "\n\tfirst_loop = false;\n}";
		
		
		// Cambiar a outputs_id
		code += communication.getArduinoConnectCode(this);

		return code;
	}

	@Override
	public String getFileExtension() {
		return ".ino";
	}

}
