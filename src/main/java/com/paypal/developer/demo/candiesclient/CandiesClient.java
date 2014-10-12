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
    String broker       = "tcp://localhost:1883";
    String clientId     = "2C:41:38:00:DD:AD";
    MemoryPersistence persistence = new MemoryPersistence();
    MqttAsyncClient client;
    int gpioPort        = 7;
    //MqttClient client;
    
    public CandiesClient() {
        this.initialize(this.broker);
    }
    
    
    public CandiesClient(String parBroker)     {
        
        String temp = "tcp://" + parBroker + ":1883";
        
        this.initialize(temp);
    }
    
    public void publish(String msg, MqttAsyncClient client)  {
        
        System.out.println("Publishing message: "+msg);
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(qos);
        
        try {
            client.publish(topic, message);
            System.out.println("Message published");  
            
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
        
        System.out.println("Publishing message: "+msg);
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(qos);
        
        try {
            client.publish(topic, message);
            System.out.println("Message published");  
            
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
    
    private void initialize(String parBroker)      {
        try {
            client = new MqttAsyncClient(parBroker, this.clientId, this.persistence);
            //client = new MqttClient(this.broker, this.clientId, this.persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            
            System.out.println("Connecting to broker: "+broker);
            IMqttToken conToken = client.connect(connOpts);
            conToken.waitForCompletion();
            //client.connect(connOpts);
            System.out.println("Connected");
                                  
            this.publish("Ei Adriano, ta me ouvindo?", client);
            
            System.out.println("Subscribing to " + this.topic + " ...");
            IMqttToken subToken = client.subscribe(this.topic, this.qos);
            subToken.waitForCompletion();
            System.out.println("Subscribed to " + this.topic + " ...");
            
            System.out.println("Enabling GPIO " + gpioPort);
            final GpioController gpio = GpioFactory.getInstance();
            final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Motor", PinState.LOW);
            
            System.out.println("Defining Listener...");
            client.setCallback(new CandieListener(pin));
            System.out.println("Listener defined. Waiting for orders...");
            
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
}