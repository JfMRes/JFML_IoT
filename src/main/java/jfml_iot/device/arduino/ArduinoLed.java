package jfml_iot.device.arduino;

public class ArduinoLed extends ArduinoActuator{
	
	
	
	private Float thresholdValue = 0f;

	public ArduinoLed(String name, String id) {
		
		super(name, id);
	}
	

	public void setThresholdValue(Float thresholdValue) {
		this.thresholdValue = thresholdValue;
	}	
	
	
	
	public String getArduinoInitialCode() {
		
		return "\n" + "// Initial code for " + name +"\n"
		+  "\n" + "const int " +id1 +"_pin = "+pin1 + ";"
		+  "\n" + "float "+id1+"_value = 0;"
		;}

	public String getArduinoSetupCode() {
		return  "\n\t" + "// Setup code for " + name +"\n"
		+"\n\t" + "pinMode("+id1+"_pin,OUTPUT);" 
		;}
	
	public String getArduinoLoopCode() {
		
		return "\n\t" + "// Loop code for " + name +"\n"
			+ "\n\t" + "digitalWrite("+id1+"_pin,"+id1+"_value>"+this.thresholdValue.toString()+");";
	}
}
	
	



















