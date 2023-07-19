package jfml_iot.device.arduino;

public class ArduinoDHT11 extends ArduinoSensor{
	
	public ArduinoDHT11(String name, String id_temp, String id_hum) {
		
		super(name, id_temp, id_hum);
	
	}
	
	
	public void setMaxTempValue(Float maxTempValue) {
		this.maxValues.put(id1, maxTempValue);
	}

	public void setMinTempValue(Float minTempValue) {
		this.minValues.put(id1, minTempValue);
	}

	public void setMaxTempReadValue(Float maxTempReadValue) {
		this.maxReadValues.put(id1, maxTempReadValue);
	}

	public void setMinTempReadValue(Float minTempReadValue) {
		this.minReadValues.put(id1, minTempReadValue);
	}

	public void setMaxHumValue(Float maxHumValue) {
		this.maxReadValues.put(id2, maxHumValue);
	}

	public void setMinHumValue(Float minHumValue) {
		this.minValues.put(id2, minHumValue);
	}

	public void setMaxHumReadValue(Float maxHumReadValue) {
		this.maxReadValues.put(id2, maxHumReadValue);
	}

	public void setMinHumReadValue(Float minHumReadValue) {
		this.minReadValues.put(id2, minHumReadValue);
	}

	public String getArduinoInitialCode() {
		
		
		return "\n" + "// Initial code for " + name +"\n"
		+"\n"	+ "//https://github.com/adafruit/DHT-sensor-library"
		+"\n"	+ "#include \"DHT.h\""
		
		+"\n"	+ "#define " + name + "_pin  "+pin1+""
		+"\n"	+ "#define " + name + "_type  DHT11"
		+"\n"	+ "DHT " + name + "_dht("+ name + "_pin  "+", "+ name + "_type"+") ; "
		
		
		+"\n"	+ "const int " + id1 + "_max_value ="+ maxReadValues.get(id1) +";" 
		+"\n"	+ "const int " + id1 + "_min_value =" + minReadValues.get(id1) +";"
		+"\n"	+ "const int " + id1 + "_max_range ="+ maxValues.get(id1) +";"
		+"\n"	+ "const int " + id1 + "_min_range =" + minValues.get(id1) + ";"
		+"\n"	+ "float " + id1 + "_value = 0;"
		+"\n"	+ "float " + id1 + "_value_old = 0;" + "\n"
		
		+"\n"	+ "const int " + id2 + "_max_value ="+ maxReadValues.get(id2) +";" 
		+"\n"	+ "const int " + id2 + "_min_value =" + minReadValues.get(id2) +";"
		+"\n"	+ "const int " + id2 + "_max_range ="+ maxValues.get(id2) +";"
		+"\n"	+ "const int " + id2 + "_min_range =" + minValues.get(id2) + ";"
		+"\n"	+ "float " + id2 + "_value = 0;"
		+"\n"	+ "float " + id2 + "_value_old = 0;" + "\n";
		
	
	}

	public String getArduinoSetupCode() {
		
		return  "\n" + "// Setup code for " + name +"\n"
				+ "\t"+ name + "_dht  "+".begin();";}
	
	public String getArduinoLoopCode() {
		
		return "\n\t" + "// Loop code for " + name +"\n"
				// id_max_value -> max read value
				// id_max_value_range -> max output value
				+"\n\t" + "" + id1 + "_value = (float)("+name+"_dht.readTemperature() - " + id1 + "_min_value) / (float)(" + id1 + "_max_value - " + id1 + "_min_value) * (" + id1 + "_max_range - " + id1 + "_min_range) + " + id1 + "_min_range;"
				+"\n\t" + "if(" + id1 + "_value>"+id1+"_max_range){"+id1+"_value = "+id1+"_max_range;}"
				+ "\n\t" + "inputData("+id1+"_value,"+id1+"_value_old, \""+id1+"\", first_loop);"
				+ "\n\t" + id1+"_value_old = "+id1+"_value;"
				+ "\n"
				+"\n\t" + "" + id2 + "_value = (float)("+name+"_dht.readHumidity() - " + id2 + "_min_value) / (float)(" + id2 + "_max_value - " + id2 + "_min_value) * (" + id2 + "_max_range - " + id2 + "_min_range) + " + id2 + "_min_range;"
				+"\n\t" + "if(" + id2 + "_value>"+id2+"_max_range){"+id2+"_value = "+id2+"_max_range;}"
				+ "\n\t" + "inputData("+id2+"_value,"+id2+"_value_old, \""+id2+"\", first_loop);"
				+ "\n\t" + id2+"_value_old = "+id2+"_value;"
				+ "\n" + "";}
	
	
	
	
	
	
	

	
	


}
