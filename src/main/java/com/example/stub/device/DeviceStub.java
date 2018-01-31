package com.example.stub.device;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.amazonaws.services.iot.client.sample.sampleUtil.PrivateKeyReader;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil.KeyStorePasswordPair;

public class DeviceStub {
    private AWSIotMqttClient client;
    private String endPoint;
    private String clientId;
    private String certificateFile;
    private String privateKeyFile;
    private Queue<AWSIotMessage> messages;

    public DeviceStub(String endPoint, String clientId, String certificate, String privateKey) {
        this.endPoint = endPoint;
        this.clientId = clientId;
        this.certificateFile = certificate;
        this.privateKeyFile = privateKey;

        this.messages = new ArrayDeque<>();
    }

    /**
     * 
     * @param args
     * @throws IOException
     * @throws InterruptedException 
     * @throws AWSIotException 
     */
    public void start() throws IOException, InterruptedException, AWSIotException {
        // SampleUtil.java and its dependency PrivateKeyReader.java can be copied from the sample source code.
        // Alternatively, you could load key store directly from a file - see the example included in this README.
        PrivateKey privateKey;
        try (DataInputStream stream = new DataInputStream(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(privateKeyFile))) {
            privateKey = PrivateKeyReader.getPrivateKey(stream, null);
        } catch (IOException | GeneralSecurityException e) {
            System.out.println("Failed to load private key from file ");
            return;
        }
        List<Certificate> certificates;
        try (BufferedInputStream stream = new BufferedInputStream(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(certificateFile))) {
            final CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            certificates = (List<Certificate>) certFactory.generateCertificates(stream);
        } catch (IOException | CertificateException e) {
            System.out.println("Failed to load certificate file ");
            return;
        }

        KeyStore keyStore;
        String keyPassword;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            // randomly generated key password for the key in the KeyStore
            keyPassword = new BigInteger(128, new SecureRandom()).toString(32);

            Certificate[] certChain = new Certificate[certificates.size()];
            certChain = certificates.toArray(certChain);
            keyStore.setKeyEntry("alias", privateKey, keyPassword.toCharArray(), certChain);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            System.out.println("Failed to create key store");
            return;
        }

        KeyStorePasswordPair pair = new KeyStorePasswordPair(keyStore, keyPassword);
        client = new AWSIotMqttClient(endPoint, clientId, pair.keyStore, pair.keyPassword);

        // optional parameters can be set before connect()
        client.connect();

    }

    public void subscribe(String topicName) throws AWSIotException, AWSIotTimeoutException {
        AWSIotQos qos = AWSIotQos.QOS0;
        MyTopic topic = new MyTopic(topicName, qos, messages);
        client.subscribe(topic); //Blocking API
    }

    public AWSIotMessage pick() {
        return messages.poll();
    }

    public void publish(String topicName, String payload) throws AWSIotException {
        client.publish(topicName, AWSIotQos.QOS0, payload);
    }

    public void stop() throws AWSIotException {
        client.disconnect();
    }
}
