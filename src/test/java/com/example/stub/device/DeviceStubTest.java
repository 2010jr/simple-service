package com.example.stub.device;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;

public class DeviceStubTest {

    @Test
    public void test() throws IOException, InterruptedException, AWSIotException, AWSIotTimeoutException {
        DeviceStub stub = new DeviceStub("a33hoix1t55rlh.iot.us-east-1.amazonaws.com", "device_stub1",
                "device/c12732a6b6-certificate.pem.crt.txt", "device/c12732a6b6-private.pem.key");
        stub.start();
        stub.subscribe("mytopic/device_stub1");

        TimeUnit.SECONDS.sleep(100);

        stub.stop();
    }
}
