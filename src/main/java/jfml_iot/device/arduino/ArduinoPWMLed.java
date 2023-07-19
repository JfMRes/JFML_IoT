package jfml_iot.device.arduino;

public class ArduinoPWMLed extends ArduinoActuator{
	
	

	public ArduinoPWMLed(String name, String id) {
		
		super(name, id);
	}
	
	
	public String getArduinoInitialCode() {
		
		
		return "\n" + "// Initial code for " + name +"\n"
		+  "\n" + "const int " +id1 +"_pin = "+pin1 + ";"
		+  "\n" + "float "+id1+"_value = 0;"
		+  "\n" + "const float "+id1+"_max_value = "+maxValues.get(id1)+";"
		
		;}

	public String getArduinoSetupCode() {
		return  "\n\t" + "// Setup code for " + name +"\n"
		+"\n\t" + "pinMode("+id1+"_pin,OUTPUT);" 
		;}
	
	public String getArduinoLoopCode() {
		
		return "\n\t" + "// Loop code for " + name +"\n"
			+ "\n\t" + "analogWrite("+id1+"_pin,"+id1+"_value/255*"+id1+"_max_value);";
	}
}
	
	



















