package jfml_iot.device.arduino;

public class ArduinoPIR extends ArduinoSensor{
	

	public ArduinoPIR(String name, String id) {
		
		super(name, id);
	}
	
	
	public String getArduinoInitialCode() {
		
		return "\n" + "// Initial code for " + name +"\n"
		+"\n"	+ "const int " + id1 + "_pin = "+pin1+"; "
		+"\n"	+ "const int " + id1 + "_max_range ="+ maxValues.get(id1) +";"
		+"\n"	+ "const int " + id1 + "_min_range =" + minValues.get(id1) + ";"
		+"\n"	+ "float " + id1 + "_value = 0;"
		+"\n"	+ "float " + id1 + "_value_old = 0;" + "\n\n";}

	public String getArduinoSetupCode() {
		
		return  "\n" + "// Setup code for " + name +"\n"
				+ "\t"+"pinMode("+id1+"_pin,INPUT_PULLUP);" + "\n\n";}
	
	public String getArduinoLoopCode() {
		
		return "\n\t" + "// Loop code for " + name +"\n"
				
				+ "\n\t"+ "if (digitalRead(" + id1 + "_pin)) {" + id1 + "_value = " + id1 + "_max_range;"
				+ "\n\t\t"+ "inputData(" + id1 + "_value, " + id1 + "_value_old, \"" + id1 + "\", first_loop);"
				+ "\n\t\t"+ "" + id1 + "_value_old = " + id1 + "_value;"
				+ "\n\t"+ "} else{"+ id1 + "_value = " + id1 + "_min_range;}"
				+ "\n\t\t"+ "inputData(" + id1 + "_value, " + id1 + "_value_old, \"" + id1 + "\", first_loop);"
				+ "\n\t\t"+ "" + id1 + "_value_old = " + id1 + "_value;";}
	
	
	
	
	
	
	

	
	


}
