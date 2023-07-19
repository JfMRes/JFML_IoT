package jfml_iot.device.arduino;

import jfml_iot.device.Actuator;

public class ArduinoActuator extends Actuator implements Arduino{

	

	public ArduinoActuator(String name, String id) {
		super(name, id);
	}

	public ArduinoActuator(String name, String id1, String id2) {
		super(name, id1, id2);
	}

	public ArduinoActuator(String name, String id1, String id2, String id3) {
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
