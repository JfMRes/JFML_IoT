package jfml_iot.device.arduino;

/**
 * Pin 1 -> Echo
 * Pin 2 -> Trigger
 * 
 * @author juanf
 *
 */
public class ArduinoSR04 extends ArduinoSensor{
	

	public ArduinoSR04(String name, String id) {
		super(name, id);
	}
	
	

	public void setPinEchoTrigger(String pinEcho, String pinTrigger) {
		this.pin1 = pinEcho;
		this.pin2 = pinTrigger;
	}
	
	
	
	public String getArduinoInitialCode() {
		
		return "\n" + "// Initial code for " + name +"\n"
		+"\n"	+ "// https://github.com/gamegine/HCSR04-ultrasonic-sensor-lib" 
		+"\n"	+ "#include <HCSR04.h>" 
		+"\n"	+ "const int " + id1 + "_pin_echo = " + pin1 + "; "
		+"\n"	+ "const int " + id1 + "_pin_trigger = " + pin2 + "; "
		+"\n"	+ "const int " + id1 + "_max_value ="+ maxReadValues.get(id1) +";" 
		+"\n"	+ "const int " + id1 + "_min_value =" + minReadValues.get(id1) +";"
		+"\n"	+ "const int " + id1 + "_max_range ="+ maxValues.get(id1) +";"
		+"\n"	+ "const int " + id1 + "_min_range =" + minValues.get(id1) + ";"
		+"\n"	+ "float " + id1 + "_value = 0;"
		+"\n"	+ "float " + id1 + "_value_old = 0;" + "\n\n"
		+"\n"	+ "HCSR04 hc("+id1+"_pin_trigger, "+id1+"_pin_echo); //HCSR04 (trig pin , echo pin); "
		;}
	
	

	public String getArduinoSetupCode() {
		return  "\n" + "// Setup code for " + name +"\n"
				+ "\t"+"pinMode("+id1+"_pin,INPUT);" + "\n\n";}
	
	public String getArduinoLoopCode() {
		
		return "\n\t" + "// Loop code for " + name +"\n"
				+"\n\t" + id1 + "_value = (float)(hc.dist() - " + id1 + "_min_value) / (float)(" + id1 + "_max_value - " + id1 + "_min_value) * (" + id1 + "_max_range - " + id1 + "_min_range) + " + id1 + "_min_range;"
				+"\n\t" + "if(" + id1 + "_value>"+id1+"_max_range){"+id1+"_value = "+id1+"_max_range;}"
				+ "\n\t" + "inputData("+id1+"_value,"+id1+"_value_old, \""+id1+"\", first_loop);"
				+ "\n\t" + id1+"_value_old = "+id1+"_value;"
				+ "\n" + "";}
	
	
	
	
	
	
	

	
	


}
