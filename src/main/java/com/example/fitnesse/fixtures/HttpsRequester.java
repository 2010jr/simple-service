package com.example.fitnesse.fixtures;

import java.util.Arrays;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class HttpsRequester implements Requester {
    private String host;
    private String method;
    private String path;
    private String response;

    private MultivaluedMap<String, String> requestParameter;

    public void request() {
        Entity<Form> entity = Entity.entity(new Form(requestParameter), MediaType.APPLICATION_JSON);
        Client client = ClientBuilder.newClient();

        if ("GET".equals(method)) {
            response = client.target(host).path(path).request().get(String.class);
        } else {
            response = client.target(host).path(path).request().post(entity, String.class);
        }
        System.out.println(response);
    }

    public Object get(String header) {
        switch (header) {
        case "Status":
            return Integer.valueOf(200);
        default:
            return response;
        }
    }

    public void set(String header, Object value) {
        switch (header) {
        case "HOST":
            setHost((String) value);
            break;
        case "PATH":
            setPath((String) value);
            break;
        case "METHOD":
            setMethod((String) value);
            break;
        default:
            String[] parameters = header.split("param:");
            if (parameters.length == 2) {
                setParameter(parameters[1], (String) value);
            }
            break;
        }
    }

    protected void setParameter(String key, String value) {
        this.requestParameter.put(key, Arrays.asList(value));
    }

    protected void setHost(String host) {
        this.host = host;
    }

    protected void setMethod(String method) {
        this.method = method;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    public void reset() {
        this.requestParameter = new MultivaluedHashMap<>();
    }

    public void execute() {
        request();
    }

    public void beginTable() {
        this.requestParameter = new MultivaluedHashMap<>();
    }

    public void endTable() {

    }
}
