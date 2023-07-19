package jfml_iot.device.arduino;

import jfml_iot.device.Sensor;

public class ArduinoSensor extends Sensor implements Arduino{

	public ArduinoSensor(String name, String id) {
		super(name, id);
	}

	public ArduinoSensor(String name, String id1, String id2) {
		super(name, id1, id2);
	}

	public ArduinoSensor(String name, String id1, String id2, String id3) {
		super(name, id1, id2, id3);
	}

	@Override
	public String getArduinoInitialCode() {
		return null;
	}

	@Override
	public String getArduinoSetupCode() {
		return null;
	}

	@Override
	public String getArduinoLoopCode() {
		return null;
	}
	

}
