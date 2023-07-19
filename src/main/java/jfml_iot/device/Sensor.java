package jfml_iot.device;

import java.time.format.DateTimeFormatter;

public abstract class Sensor extends Device{



	public Sensor(String name, String id) {
		super(name, id);
	}

	public Sensor(String name, String id1, String id2) {
		super(name, id1, id2);
	}

	public Sensor(String name, String id1, String id2, String id3) {
		super(name, id1, id2, id3);
	}

	
	public String debug() {
		DateTimeFormatter formatted = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String debugString = "";
		for(String id : valueId.keySet()) {
			String formattedDate = dateId.get(id).format(formatted);
			String value = Float.toString(valueId.get(id));
			debugString +=  formattedDate + " Device --> " + name +" --> " + id + " --> " + value + " --> (INPUT)\n" ;
		}
		return debugString;
	}



}
