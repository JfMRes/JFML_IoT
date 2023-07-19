// Code for ArduinoPotentiometerServo
#include <ESP8266WiFi.h>
const char* WifiSSID = "MiFibra-9504";
const char* Wifipassword = "2HoPcrad";

WiFiClient espClient;

bool first_loop = true;

// Initial code for potentiometer

const int potentiometer_pin = A0; 
const int potentiometer_max_value =1024.0;
const int potentiometer_min_value =0.0;
const int potentiometer_max_range =100.0;
const int potentiometer_min_range =0.0;
float potentiometer_value = 0;
float potentiometer_value_old = 0;



// Initial code for stepper

#include <Servo.h>
const int stepper_pin = 4;
float stepper_value = 0;
const float stepper_max_value = 100.0;
Servo stepper;

#include <PubSubClient.h>
PubSubClient client(espClient);
const char* mqttServer = "192.168.1.31";
const int mqttPort = 1883;

void MqttCallBack(char topic[], byte payload[], unsigned int length) {
	String topic_id = String(topic);
	String message = "";
	for (int i = 0; i < length; i++) {
		message += String((char)payload[i]);}
	if (topic_id.equals("stepper")) {
		stepper_value = message.toFloat();
	}else  if(topic_id.equals("ArduinoPotentiometerServo/status") && message.toFloat() > 0){
		sendData(0,"ArduinoPotentiometerServo/status");
	}else if(topic_id.equals("ArduinoPotentiometerServo/status") && message.toFloat() > 1){
		sendData(potentiometer_value,"potentiometer");
}}

void setup(){

	Serial.begin(9600);
	
	WiFi.begin(WifiSSID, Wifipassword);

	client.setServer(mqttServer, mqttPort);
	client.setCallback(MqttCallBack);



	
// Setup code for potentiometer
	pinMode(potentiometer_pin,INPUT);


	
	// Setup code for stepper

	stepper.attach(stepper_pin);

  stepper.write(0);
  delay(2000);
  stepper.write(180);
  delay(2000);
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


	// Loop code for potentiometer

	potentiometer_value = (float)(analogRead(potentiometer_pin) - potentiometer_min_value) / (float)(potentiometer_max_value - potentiometer_min_value) * (potentiometer_max_range - potentiometer_min_range) + potentiometer_min_range;
	inputData(potentiometer_value,potentiometer_value_old, "potentiometer", first_loop);
	potentiometer_value_old = potentiometer_value;



	// Loop code for stepper

	stepper.write(stepper_value / stepper_max_value * 180);
	first_loop = false;
}
void connect() {
	while (!client.connected()) {
		client.connect("ArduinoPotentiometerServo");
		delay(1000);
	}
	client.subscribe("stepper");
	client.loop();
	client.subscribe("ArduinoPotentiometerServo/status");
}
