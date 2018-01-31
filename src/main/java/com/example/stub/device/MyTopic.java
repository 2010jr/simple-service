package com.example.stub.device;

import java.util.Queue;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

public class MyTopic extends AWSIotTopic {

    private Queue<AWSIotMessage> iotMessages;

    public MyTopic(String topic, AWSIotQos qos, Queue<AWSIotMessage> messages) {
        super(topic, qos);
        iotMessages = messages;
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        System.out.println("topic is subscribed");
        iotMessages.add(message);
    }
}
