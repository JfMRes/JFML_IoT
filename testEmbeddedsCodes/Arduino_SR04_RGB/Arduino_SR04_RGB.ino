// Code for Arduino_SR04_RGB
#include <WiFiEsp.h>
const char* WifiSSID = "****";
const char* Wifipassword = "****";

WiFiEspClient espClient;

bool first_loop = true;

// Initial code for SR04

// https://github.com/gamegine/HCSR04-ultrasonic-sensor-lib
#include <HCSR04.h>
const int distance_pin_echo = 0; 
const int distance_pin_trigger = 1; 
const int distance_max_value =30.0;
const int distance_min_value =0.0;
const int distance_max_range =100.0;
const int distance_min_range =0.0;
float distance_value = 0;
float distance_value_old = 0;


HCSR04 hc(distance_pin_trigger, distance_pin_echo); //HCSR04 (trig pin , echo pin); 

// Initial code for rgb

const int R_pin = 2;
float R_value = 0;
const int G_pin = 3;
float G_value = 0;
const int B_pin = 4;
float B_value = 0;

#include <PubSubClient.h>
PubSubClient client(espClient);
const char* mqttServer = "******";
const int mqttPort = 1883;

void MqttCallBack(char topic[], byte payload[], unsigned int length) {
	String topic_id = String(topic);
	String message = "";
	for (int i = 0; i < length; i++) {
		message += String((char)payload[i]);}
	if (topic_id.equals("R")) {
		R_value = message.toFloat();
	}else if (topic_id.equals("B")) {
		B_value = message.toFloat();
	}else if (topic_id.equals("G")) {
		G_value = message.toFloat();
	}else  if(topic_id.equals("Arduino_SR04_RGB/status") && message.toFloat() > 0){
		sendData(0,"Arduino_SR04_RGB/status");
	}else if(topic_id.equals("Arduino_SR04_RGB/status") && message.toFloat() > 1){
		sendData(distance_value,"distance");
}}

void setup(){

	Serial.begin(9600);
	Serial1.begin(9600);
	WiFi.init(&Serial1);
	
	WiFi.begin(WifiSSID, Wifipassword);

	client.setServer(mqttServer, mqttPort);
	client.setCallback(MqttCallBack);


	
// Setup code for SR04
	pinMode(distance_pin,INPUT);


	
	// Setup code for rgb

	pinMode(R_pin,OUTPUT);
	pinMode(B_pin,OUTPUT);
	pinMode(G_pin,OUTPUT);
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


	// Loop code for SR04

	distance_value = (float)(hc.dist() - distance_min_value) / (float)(distance_max_value - distance_min_value) * (distance_max_range - distance_min_range) + distance_min_range;
	if(distance_value>distance_max_range){distance_value = distance_max_range;}
	inputData(distance_value,distance_value_old, "distance", first_loop);
	distance_value_old = distance_value;



	// Loop code for rgb

	digitalWrite(R_pin,R_value>50.0);
	digitalWrite(G_pin,G_value>50.0);
	digitalWrite(B_pin,B_value>50.0);
	first_loop = false;
}
void connect() {
	while (!client.connected()) {
		client.connect("Arduino_SR04_RGB");
		delay(1000);
	}
	client.subscribe("R");
	client.subscribe("B");
	client.subscribe("G");
	client.loop();
	client.subscribe("Arduino_SR04_RGB/status");
}