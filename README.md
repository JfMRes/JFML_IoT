# JFML IoT: A Java library for IoT using JFML for fuzzy control. 

JFML IoT is an open source Java library designed to facilitate the integration of Fuzzy-Base Systems (FRBS) into Internet of Things (IoT) enviroments. Using Java Fuzzy Markup Language (JFML) as engine. JFML is built on the IEEE 1855-2016 standard, simplifying the representation of fuzzy rule-based systems with a standard XML-based FML language.

## User Manual

### Introduction
In this user manual, you will find detailed information about the basic components of the JFML IoT system. The following sections will cover each of the classes that make up the system, their configuration, and the requirements to make them work. To find more examples of creating the JFML file using JFML and creating the IoT system, you can refer to the project's src/test/java directory.

###  Main Class
This class manages the entire system, including inference, communication management, etc. To create the object with empty parameters, use the following code:
```java
JFML_IoT_Core IoT_system = new JFML_IoT_Core();
```
Next, you need to specify the JFML system that will perform the inference. Assuming you have a JFML file in XML format, you can load it as follows:
File xml = new File("./testXML/FirePirAlarm.xml");
FuzzyInferenceSystem jfml = JFML.load(xml);
IoT_system.setFuzzyInferenceSystem(jfml);

Finally, you can set the refresh rate for the system during execution by specifying the value in milliseconds:
IoT_system.setDelay(1000);

###  Communication Class
This class manages the communication within the system. It includes the MQTT class as a subclass. To create an MQTT object, use the following constructor:
MQTT mqtt = new MQTT(address, port, user, password, qos);

The required parameters for constructing an MQTT object are:
- address: Represents the address of the MQTT broker to connect to.
- port: Specifies the port of the MQTT broker.
- user: Username used for authentication on the MQTT broker.
- password: Password associated with the user for authentication on the MQTT broker.
- qos: Quality of Service level used in MQTT communication.

Once the object is created, you need to add it to the main class:
```java
JFML_IoT_Core IoT_system = new JFML_IoT_Core();
IoT_System.addCommunication(mqtt);
```

###  Device Class
The Device class represents various sensors and actuators in the system. Each device has different configurations, IDs, and pin numbers. To create a device object, you can use the following general example:
Device device = new Device(name, id1, id2, id3);
device.setPin(pin1, pin2, pin3);

The parameters for device creation are as follows:
- name: The name of the device for debugging purposes.
- id1, id2, id3: String identifiers specific to the device. The number of identifiers can vary from 1 to 3.

To select the pins used for the electrical connection of the device, use the following function:
device.setPin(pin1, pin2, pin3);

Each of the parameters pin1, pin2, and pin3 is a String. The number of pins, ranging from 1 to 3, depends on the device's characteristics.

To set the maximum reading and writing levels for the system, use the following methods:
device.setMinMaxValue(id, min, max);
device.setMinMaxReadValue(id, min, max);

The setMinMaxValue function selects the ranges used within JFML, while setMinMaxReadValue sets the maximum range of values the embedded system can read or write to the device. Both functions require the following parameters:
- id: The ID string for the range to modify.
- min, max: The minimum and maximum values of the desired range.

The supported devices include sensors such as SR04, DHT11, LDR, PIR, pushbutton, fire sensor, and sound sensor, as well as actuators such as simple LED, PWM LED, RGB LED, buzzer, and stepper motor.

### Embedded Class
The Embedded class represents each embedded system used in the IoT ensemble. It serves as a container for the input and output devices used. In this case, we will use the EmbeddedSystem class as an example, even though it is an abstract class and cannot be directly used. The constructor for the EmbeddedSystem class is as follows:
EmbeddedSystem embedded = new EmbeddedSystem(name, communication);

The parameters represent:
- name: The name of the device, used for visualization in debug mode (string format).
- communication: An instance of the Communication class (or a subclass such as MQTT) used for communication.

If the device is connected via WiFi, use the following function to specify the communication credentials, where both parameters are strings representing the SSID and the password, respectively:
embedded.setWifi(SSID, WiFiPassword);

To modify the maximum timeout allowed for the embedded system, use the following function, specifying the value in milliseconds:
arduino1.setTimeOut(3000);

Once the embedded systems and their devices are created, you need to add the devices to the respective embedded system using the following function:
embedded.addDevice(buzzer);

After adding all the devices, you need to add the embedded system to the main system:
IoT_System.addEmbedded(embedded);

### Generating Embedded System Codes
After adding all the embedded systems with their respective devices to the main class, you need to generate the codes for each embedded system before starting the system. Use the following function, providing the root path where you want to create the codes:
IoT_system.buildCode("./testEmbeddedsCodes");

### Starting the System
Once the configuration and code generation for each embedded system are complete, you can start the system by running the following command:
IoT_system.run();

This command initiates an infinite loop that manages the system and displays the values on the screen.

### Debugging
During the execution loop, you can monitor the current status of the system. It shows the connection status of each embedded system, indicating whether there is a timeout or if the connection is successful. It also displays the state of each associated device, including the value and the timestamp of the last received value. Each data entry is preceded by a timestamp indicating the last connection time with the system.


