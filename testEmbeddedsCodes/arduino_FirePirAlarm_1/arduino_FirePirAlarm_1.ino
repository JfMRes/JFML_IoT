// Code for arduino_FirePirAlarm_1
#include <ESP8266WiFi.h>
const char* WifiSSID = "MiFibra-9504";
const char* Wifipassword = "2HoPcrad";

WiFiClient espClient;

bool first_loop = true;

// Initial code for buzzer

const int buzzer_pin = 14;
float buzzer_value = 0;

// Initial code for rgb

const int red_pin = 2;
float red_value = 0;
const int green_pin = 0;
float green_value = 0;
const int blue_pin = 4;
float blue_value = 0;

// Initial code for motion

const int motion_pin = 12; 
const int motion_max_range =100.0;
const int motion_min_range =0.0;
float motion_value = 0;
float motion_value_old = 0;



#include <PubSubClient.h>
PubSubClient client(espClient);
const char* mqttServer = "192.168.1.31";
const int mqttPort = 1883;

void MqttCallBack(char topic[], byte payload[], unsigned int length) {
	String topic_id = String(topic);
	String message = "";
	for (int i = 0; i < length; i++) {
		message += String((char)payload[i]);}
	if (topic_id.equals("buzzer")) {
		buzzer_value = message.toFloat();
	}else if (topic_id.equals("red")) {
		red_value = message.toFloat();
	}else if (topic_id.equals("green")) {
		green_value = message.toFloat();
	}else if (topic_id.equals("blue")) {
		blue_value = message.toFloat();
	}else  if(topic_id.equals("arduino_FirePirAlarm_1/status") && message.toFloat() > 0){
		sendData(0,"arduino_FirePirAlarm_1/status");
	}else if(topic_id.equals("arduino_FirePirAlarm_1/status") && message.toFloat() > 1){
		sendData(motion_value,"motion");
}}

void setup(){

	Serial.begin(9600);
	
	WiFi.begin(WifiSSID, Wifipassword);

	client.setServer(mqttServer, mqttPort);
	client.setCallback(MqttCallBack);


	
	// Setup code for buzzer

	pinMode(buzzer_pin,OUTPUT);
	
	// Setup code for rgb

	pinMode(red_pin,OUTPUT);
	pinMode(blue_pin,OUTPUT);
	pinMode(green_pin,OUTPUT);
	
// Setup code for motion
	pinMode(motion_pin,INPUT_PULLUP);


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


	// Loop code for motion

	if (digitalRead(motion_pin)) {motion_value = motion_max_range;
		inputData(motion_value, motion_value_old, "motion", first_loop);
		motion_value_old = motion_value;
	} else{motion_value = motion_min_range;}
		inputData(motion_value, motion_value_old, "motion", first_loop);
		motion_value_old = motion_value;


	// Loop code for buzzer

	digitalWrite(buzzer_pin,buzzer_value>50.0);


	// Loop code for rgb

	digitalWrite(red_pin,red_value>50.0);
	digitalWrite(green_pin,green_value>50.0);
	digitalWrite(blue_pin,blue_value>50.0);
	first_loop = false;
}
void connect() {
	while (!client.connected()) {
		client.connect("arduino_FirePirAlarm_1");
		delay(1000);
	}
	client.subscribe("buzzer");
	client.subscribe("red");
	client.subscribe("green");
	client.subscribe("blue");
	client.loop();
	client.subscribe("arduino_FirePirAlarm_1/status");
}