package jfml_iot.communication;

import jfml_iot.embeddedsystem.*;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import jfml_iot.device.*;

/**
 * Java class for the MQTT comunication. 
 * Overrides method for connect, disconnect, send messages and handle for new messages.
 * 
 * @author 
 *
 */
public class MQTT extends Communication {

	public String user;
	public String password;
	public MqttAsyncClient client;
	public MqttConnectOptions options;
	public Integer port;
	public int qos;

	String tmpDir = System.getProperty("java.io.tmpdir");
	MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);
	

	/**
	 * Default constructor
	 * 
	 * @param address IP adress of the MQTT server.
	 * @param port Port number of the MQTT communication.
	 * @param user UserName for authentication.
	 * @param password Passqord for authentication.
	 * @param qos Quality of service for the messages.
	 */
	public MQTT(String address, Integer port, String user, String password, int qos) {
		super(address);
		this.user = user;
		this.password = password;
		this.qos = qos;
		this.port = port;

		// Configuracion de MQTT
		options = new MqttConnectOptions();

		if (user != null) {
			options.setUserName(user);

		}
		if (password != null) {
			options.setPassword(password.toCharArray());

		}

		String complete_adress = address + ":" + port.toString();

		try {
			this.client = new MqttAsyncClient(complete_adress, user, dataStore);
		} catch (MqttException e) {
			System.exit(0);
		}

	}
	
	
	/**
	 * Connect with the MQTT server.
	 */
	@Override
	public void connect() {
		
		try {
			client.connect().waitForCompletion(100);
			System.out.println("Established connection");
		} catch (MqttException e) {
			System.out.println("Connection not established");
			System.exit(0);
		}
		
		for (EmbeddedSystem e : embeddeds) {
			try {
				client.subscribe(e.getName() + "/status", qos);
			} catch (MqttException e1) {
				e1.printStackTrace();
			}
			for (Device device : e.getDevices()) {
				if (device instanceof Sensor) {
					for (String id : device.getIds()) {
						try {
							System.out.println(id);
							client.subscribe(id, qos);
						} catch (MqttException exc) {
							exc.printStackTrace();
						}
					}
				}

			}
		}


		client.setCallback(new MqttCallback() {

			public void connectionLost(Throwable cause) {
				System.out.println("Connection lost ");
				System.exit(0);

			}

			public void messageArrived(String topic, MqttMessage message) throws Exception {
				
				float value = Float.parseFloat(new String(message.getPayload()));
				if(topic.contains("/status")) {
					if(value == 0f) {
						for(EmbeddedSystem e: embeddeds) {
							if(topic.split("/")[0].equals(e.getName())) {
								e.resetTime();
								sendValuesToEmbedded(e);
							}
						}
					}
					
				}else {
					setValueRecived(topic,value);
				}

			}

			public void deliveryComplete(IMqttDeliveryToken token) {}
		});

	}

	/**
	 * Disconnect to the MQTT server.
	 */
	@Override
	public void disconnect() {
		try {
			client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendValue(String id, Float value) {
		
		String text = String.valueOf(value);
		MqttMessage message = new MqttMessage(text.getBytes());
		message.setQos(this.qos);
		if (!client.isConnected()) {
			System.out.println("Server disconnected");
			System.exit(0);
		}
		try {
			client.publish(id, message);
		} catch (Exception exc) {
			System.out.println("Error sending. Topic: " + id + " Value: " + text + exc);
		}
	}
	
	@Override
	public String getSendDataCodeArduino() {
		return "void sendData(float newValue, String id){" 
				+ "\n\t" + "char buffer[10];"
				+ "\n\t" + "dtostrf(newValue, 4, 2, buffer);"
				+ "\n\t" + "client.publish(id.c_str(), buffer);"
				+ "\n" + "}";
		
	}
	
	@Override
	public String getArduinoInitialCode(EmbeddedSystem embedded) {
				
		String arduino_adress = this.adress.replace("tcp://", "");
		String code =  "\n" + "#include <PubSubClient.h>"
					+ "\n" + "PubSubClient client(espClient);"
					+ "\n" + "const char* mqttServer = \""+ arduino_adress +"\";"
					+ "\n" + "const int mqttPort = "+port+";"
					+ "\n\n" + "void MqttCallBack(char topic[], byte payload[], unsigned int length) {"
					+ "\n\t" + "String topic_id = String(topic);"
					+ "\n\t" + "String message = \"\";"
					+ "\n\t" + "for (int i = 0; i < length; i++) {"
					+ "\n\t\t" + "message += String((char)payload[i]);}"
					+ "\n\t";
		
			for(Device device: embedded.getDevices()) {
				if(device instanceof Actuator) {
					for(String id: device.getIds()) {
						code +=  "if (topic_id.equals(\""+id+"\")) {"
								+ "\n\t\t" + id + "_value = message.toFloat();"
								+ "\n\t" + "}else "
								;
					}
				}
			}
			
		
		code += " if(topic_id.equals(\""+embedded.getName()+"/status\") && message.toFloat() > 0){"
		
				+ "\n\t\tsendData(0,\""+embedded.getName()+"/status\");"
				+ "\n\t}else if(topic_id.equals(\""+embedded.getName()+"/status\") && message.toFloat() > 1){";
					for(Device d: embedded.getDevices())
						if(d instanceof Sensor) {
							for(String id: d.getIds())
							code += "\n\t\t" + "sendData("+id+"_value,\""+id+"\");";
						}
					
				code+="\n}}";
			return code;}
	
	@Override
	public String getArduinoSetupCode() {return "\n\t" + "client.setServer(mqttServer, mqttPort);"
			+ "\n\t"+"client.setCallback(MqttCallBack);" + "\n";};
			
	@Override
	public String getArduinoLoopCode() {return "\n\t" + "if (!client.connected()) {connect();}" + "\n"
			+ "\n\t" + "client.loop();";};
	
	
	@Override
	public String getArduinoConnectCode(EmbeddedSystem embedded) {

		
		String code = "\n"+"void connect() {"
				+ "\n\t" + "while (!client.connected()) {"
				+ "\n\t\t" + "client.connect(\""+embedded.getName()+"\");"
						+ "\n\t\t" +"delay(1000);\n\t}"
				;
		
		for(Device device: embedded.getDevices()) {
			if(device instanceof Actuator) {
				for(String id: device.getIds()) {
			code += "\n\t" + "client.subscribe(\""+id+"\");";
		}
			}}
		
		code += "\n\t" + "client.loop();";
		code += "\n\t" + "client.subscribe(\""+embedded.getName()+"/status\");" +"\n}";
		
		return code;
				
	}


	
	
}

