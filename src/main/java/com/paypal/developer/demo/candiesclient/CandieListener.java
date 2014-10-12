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
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author jeffprestes
 */
public class CandieListener implements MqttCallback {

    private GpioPinDigitalOutput pin;
    
    public CandieListener(GpioPinDigitalOutput parPin)  {
        this.pin = parPin;
    }
    
    @Override
    public void connectionLost(Throwable th) {
        System.out.println("Connection is closed");
        th.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage mm) throws Exception {
        System.out.println("Message arrived: " + mm.toString() + " at " + topic);
        if (mm.toString().equals("release"))  {
            pin.high();
            System.out.println("--> GPIO state should be: ON");
        
            Thread.sleep(5000);

            // turn off gpio pin #01
            pin.low();
            System.out.println("--> GPIO state should be: OFF");
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        
        if (imdt == null)     {
            System.out.println("DeliveryToke is null");
            return;
        }
        
        try {
            System.out.println("A message was deliveried into " + imdt.getTopics()[0]);
            
        } catch (NullPointerException npe)  {
            System.out.println("msg "+ npe.getMessage());
            System.out.println("loc "+ npe.getLocalizedMessage());
            System.out.println("cause "+ npe.getCause());
            System.out.println("excep "+ npe);
            npe.printStackTrace();
        }
    }
}
