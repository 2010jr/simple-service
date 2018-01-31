package com.example.fitnesse.fixtures;

import java.io.IOException;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;

public class DeviceStubCreator {
    private String endPoint;
    private String id;
    private String topicName;
    private String stub;

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void execute() throws AWSIotException, AWSIotTimeoutException, IOException, InterruptedException {
    }

    public String stub() {
        return "DeviceStub1";
    }
}
