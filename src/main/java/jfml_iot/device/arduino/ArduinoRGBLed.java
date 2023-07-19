package jfml_iot.device.arduino;

/**
 * Index in register:
 * 	- 1: RED
 * 	- 2: GREEN
 * 	- 3: BLUE
 * 
 * 
 * 
 * @author juanf
 *
 */
public class ArduinoRGBLed extends ArduinoActuator{
	
	private Float thresholdValueR = null;
	private Float thresholdValueG = null;
	private Float thresholdValueB = null;
	

	public ArduinoRGBLed(String name, String id_RED,String id_GREEN,String id_BLUE) {
		
		super(name, id_RED, id_GREEN, id_BLUE);
	}
	
	
	public void setThresholdValue(Float thresholdValueR,Float thresholdValueG,Float thresholdValueB) {
		this.thresholdValueR = thresholdValueR;
		this.thresholdValueG = thresholdValueG;
		this.thresholdValueB = thresholdValueB;
		
	}	
	
	public String getArduinoInitialCode() {
		return "\n" + "// Initial code for " + name +"\n"
		+  "\n" + "const int " +id1 +"_pin = "+pin1 + ";"
		+  "\n" + "float "+id1+"_value = 0;"
		+  "\n" + "const int " +id2 +"_pin = "+pin2 + ";"
		+  "\n" + "float "+id2+"_value = 0;"
		+  "\n" + "const int " +id3 +"_pin = "+pin3 + ";"
		+  "\n" + "float "+id3+"_value = 0;"
		
		;}

	public String getArduinoSetupCode() {
		return  "\n\t" + "// Setup code for " + name +"\n"
		+"\n\t" + "pinMode("+id1+"_pin,OUTPUT);" 
		+"\n\t" + "pinMode("+id3+"_pin,OUTPUT);" 
		+"\n\t" + "pinMode("+id2+"_pin,OUTPUT);" 
		
		;}
	
	public String getArduinoLoopCode() {
		
		if(thresholdValueR == null) {
			thresholdValueR = (maxValues.get(id1)-minValues.get(id1))/2;
			thresholdValueG = (maxValues.get(id2)-minValues.get(id2))/2;
			thresholdValueB = (maxValues.get(id3)-minValues.get(id3))/2;
			
		}
		
		return "\n\t" + "// Loop code for " + name +"\n"
			+ "\n\t" + "digitalWrite("+id1+"_pin,"+id1+"_value>"+this.thresholdValueR.toString()+");"
			+ "\n\t" + "digitalWrite("+id2+"_pin,"+id2+"_value>"+this.thresholdValueG.toString()+");"
			+ "\n\t" + "digitalWrite("+id3+"_pin,"+id3+"_value>"+this.thresholdValueB.toString()+");"
			;
	}
}
	
	



















