package jfml_iot.device.arduino;

public class ArduinoAnalogSensor_base extends ArduinoSensor{
	

	public ArduinoAnalogSensor_base(String name, String id) {
		
		super(name, id);
	}
	
	public String getArduinoInitialCode() {
		
		
		return "\n" + "// Initial code for " + name +"\n"
		+"\n"	+ "const int " + id1 + "_pin = "+pin1+"; "
		+"\n"	+ "const int " + id1 + "_max_value ="+ maxReadValues.get(id1) +";" 
		+"\n"	+ "const int " + id1 + "_min_value =" + minReadValues.get(id1) +";"
		+"\n"	+ "const int " + id1 + "_max_range ="+ maxValues.get(id1) +";"
		+"\n"	+ "const int " + id1 + "_min_range =" + minValues.get(id1) + ";"
		+"\n"	+ "float " + id1 + "_value = 0;"
		+"\n"	+ "float " + id1 + "_value_old = 0;" + "\n\n";}

	public String getArduinoSetupCode() {
		
		return  "\n" + "// Setup code for " + name +"\n"
				+ "\t"+"pinMode("+id1+"_pin,INPUT);" + "\n\n";}
	
	public String getArduinoLoopCode() {
		
		return "\n\t" + "// Loop code for " + name +"\n"
				+"\n\t" + "" + id1 + "_value = (float)(analogRead("+id1+"_pin) - " + id1 + "_min_value) / (float)(" + id1 + "_max_value - " + id1 + "_min_value) * (" + id1 + "_max_range - " + id1 + "_min_range) + " + id1 + "_min_range;"
				+ "\n\t" + "inputData("+id1+"_value,"+id1+"_value_old, \""+id1+"\", first_loop);"
				+ "\n\t" + id1+"_value_old = "+id1+"_value;"
				+ "\n" + "";}
	
	
	
	
	
	
	

	
	


}
