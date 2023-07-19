// Code for arduino_FirePirAlarm_2
#include <ESP8266WiFi.h>
const char* WifiSSID = "MiFibra-9504";
const char* Wifipassword = "2HoPcrad";

WiFiClient espClient;

bool first_loop = true;

// Initial code for fire_sensor

const int fire_pin = 12; 
const int fire_max_range =100.0;
const int fire_min_range =0.0;
float fire_value = 0;
float fire_value_old = 0;



// Initial code for alarm

const int alarm_pin = 13; 
const int alarm_max_range =100.0;
const int alarm_min_range =0.0;
float alarm_value = 0;
float alarm_value_old = 0;



#include <PubSubClient.h>
PubSubClient client(espClient);
const char* mqttServer = "192.168.1.31";
const int mqttPort = 1883;

void MqttCallBack(char topic[], byte payload[], unsigned int length) {
	String topic_id = String(topic);
	String message = "";
	for (int i = 0; i < length; i++) {
		message += String((char)payload[i]);}
	 if(topic_id.equals("arduino_FirePirAlarm_2/status") && message.toFloat() > 0){
		sendData(0,"arduino_FirePirAlarm_2/status");
	}else if(topic_id.equals("arduino_FirePirAlarm_2/status") && message.toFloat() > 1){
		sendData(fire_value,"fire");
		sendData(alarm_value,"alarm");
}}

void setup(){

	Serial.begin(9600);
	
	WiFi.begin(WifiSSID, Wifipassword);

	client.setServer(mqttServer, mqttPort);
	client.setCallback(MqttCallBack);


	
// Setup code for fire_sensor
	pinMode(fire_pin,INPUT_PULLUP);


	
// Setup code for alarm
	pinMode(alarm_pin,INPUT_PULLUP);


}

void sendData(float newValue, String id){
	char buffer[10];
	dtostrf(newValue, 4, 2, buffer);
	client.publish(id.c_str(), buffer);
}

void inputData(float newValue, float oldValue, String id, bool force){
	if (force || newValue != oldValue) {
		sendData(newValue, id);
		}
}

void loop(){

	if (!client.connected()) {connect();}

	client.loop();


	// Loop code for fire_sensor

	if (digitalRead(fire_pin)) {fire_value = fire_min_range;
		inputData(fire_value, fire_value_old, "fire", first_loop);
		fire_value_old = fire_value;
	} else{fire_value = fire_max_range;}
		inputData(fire_value, fire_value_old, "fire", first_loop);
		fire_value_old = fire_value;


	// Loop code for alarm

	if (digitalRead(alarm_pin)) {alarm_value = alarm_min_range;
		inputData(alarm_value, alarm_value_old, "alarm", first_loop);
		alarm_value_old = alarm_value;
	} else{alarm_value = alarm_max_range;}
		inputData(alarm_value, alarm_value_old, "alarm", first_loop);
		alarm_value_old = alarm_value;
	first_loop = false;
}
void connect() {
	while (!client.connected()) {
		client.connect("arduino_FirePirAlarm_2");
		delay(1000);
	}
	client.loop();
	client.subscribe("arduino_FirePirAlarm_2/status");
}