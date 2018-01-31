package com.example.fitnesse.fixtures;

import java.io.IOException;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;

public class DeviceStubSubscriber {
    private String stub;
    private String input;
    private String topicName;
    private AWSIotMessage message;

    public DeviceStubSubscriber(String stub) {
        this.stub = stub;
    }

    public void setTopic(String topicName) {
        this.topicName = topicName;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String time() {
        return message.getStringPayload();
    }

    public String message() {
        return "{ time: 2018-02-01T10:00:00 }";
    }

    public void beginTable() throws IOException, InterruptedException, AWSIotException {
    }

    public void execute() throws AWSIotException, AWSIotTimeoutException {

    }
}
