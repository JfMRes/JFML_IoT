package jfml_iot.device.arduino;

public class ArduinoServo extends ArduinoActuator{
	

	public ArduinoServo(String name, String id) {
		super(name,id);
	}
	
	
	public String getArduinoInitialCode() {
		
		return "\n" + "// Initial code for " + name +"\n"
		+  "\n" + "#include <Servo.h>"
		+  "\n" + "const int " +id1 +"_pin = "+pin1 + ";"
		+  "\n" + "float "+id1+"_value = 0;"
		+  "\n" + "const float "+id1+"_max_value = "+maxValues.get(id1)+";"
		+  "\n" + "Servo "+id1+";"
			
		;}

	public String getArduinoSetupCode() {
		return  "\n\t" + "// Setup code for " + name +"\n"
		+"\n\t" + id1 +".attach("+id1+"_pin);" 
		;}
	
	public String getArduinoLoopCode() {
		return "\n\t" + "// Loop code for " + name +"\n"
			+ "\n\t" + id1+".write("+id1+"_value / "+id1+"_max_value * 180);";
	}
}
	
	



















