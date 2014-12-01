/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paypal.developer.demo.candiesclient;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author jeffprestes
 */
public class CandiesClient {
    
    String topic        = "jeffprestes/candies/world";   
    int qos             = 2;
    String broker       = "localhost";
    String clientId     = "2C:41:38:00:DD:AD";
    String port         = "1883";
    MemoryPersistence persistence = new MemoryPersistence();
    MqttAsyncClient client;
    int gpioPort        = 7;
    //MqttClient client;
    
    /**
     * Constructor with default parameters to be used in localhost
     */
    public CandiesClient() {
        this.initialize();
    }
    
    /**
     * Constructor with customized Broker Server
     * @param parBroker IP Address or URL of MQTT Broker Server
     */
    public CandiesClient(String parBroker)     {
        
        this.broker = parBroker;
        
        this.initialize();
    }
    
    /**
     * Constructor with customized Broker server and topic (queue)
     * @param parBroker IP Address or URL of MQTT Broker Server
     * @param parTopic Topic (queue name) where is this client must listen to or public messages
     */
    public CandiesClient(String parBroker, String parTopic)     {
        
        this.broker = parBroker;
        this.topic = parTopic;
        
        this.initialize();
    }
    
    /**
     * Constructor with customized Broker server, topic (queue), clientID and Broker Server port
     * @param parBroker IP Address or URL of MQTT Broker Server
     * @param parTopic Topic (queue name) where is this client must listen to or public messages
     * @param parClientId ClientID of this client to identify your messages to Broker
     * @param parPort Port of MQTT Broker Server
     */
    public CandiesClient(String parBroker, String parTopic, String parClientId, String parPort) {
        this.broker = parBroker;
        this.clientId = parClientId;
        this.topic = parTopic;
        this.port = parPort;
        
        this.initialize();
    }
    
    public void publish(String msg, MqttAsyncClient client)  {
        
        System.out.println("+Publishing message: "+msg);
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(qos);
        
        try {
            client.publish(topic, message);
            System.out.println("+Message published");  
            
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
    
    public void publish(String msg, MqttClient client)  {
        
        System.out.println("+Publishing message: "+msg);
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(qos);
        
        try {
            client.publish(topic, message);
            System.out.println("+Message published");  
            
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
    
    public void disconnect(MqttAsyncClient client)    {
        try {
            client.disconnect();
            System.out.println("Disconnected");  
            
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
    
    public void disconnect(MqttClient client)    {
        try {
            client.disconnect();
            System.out.println("Disconnected");  
            
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
    
    
    private MqttAsyncClient getMqttClient()  {
        return client;
    }
    
    /*
    public MqttClient getMqttClient()  {
        return client;
    }
    */
    
    private void initialize()      {
        try {
            
            String temp = "tcp://" + this.broker + ":" + this.port;
            
            client = new MqttAsyncClient(temp, this.clientId, this.persistence);
            //client = new MqttClient(this.broker, this.clientId, this.persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            
            System.out.println("Connecting to broker: "+broker);
            IMqttToken conToken = client.connect(connOpts);
            conToken.waitForCompletion();
            //client.connect(connOpts);
            System.out.println("Connected");
            
            System.out.println("Subscribing to " + this.topic + " ...");
            IMqttToken subToken = client.subscribe(this.topic, this.qos);
            subToken.waitForCompletion();
            System.out.println("Subscribed to " + this.topic + " ...");
            
            System.out.println("Enabling GPIO " + gpioPort);
            final GpioController gpio = GpioFactory.getInstance();
            final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Motor", PinState.LOW);
            
            System.out.println("Defining Listener...");
            client.setCallback(new CandieListener(pin, client, this.topic));
            System.out.println("Listener defined. Waiting for orders...");
            
            InetAddress address = InetAddress.getLocalHost();
            
            this.publish("Machine is initialized in " + CandiesClient.getIp() + " and waiting for orders...", client);
            
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        } catch (UnknownHostException ex) {
            System.err.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
    
    
    private static String getIp()   {
        
        String data = "";
            
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while( b.hasMoreElements()) {

                for ( InterfaceAddress f : b.nextElement().getInterfaceAddresses())     {
                    System.out.println("Machine network interface: " + f.getAddress() + " - " + b.nextElement().getName());
                    data = f.getAddress().getHostAddress();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return data;
    }
}
