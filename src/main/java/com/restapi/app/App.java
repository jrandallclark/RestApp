package com.restapi.app;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Hit a REST endpoint and get data
 *
 */
public class App 
{
    public static void main(String[] args) {
        Client client = null;
        Response response = null;
        try {
            client = ClientBuilder.newClient();
            response = client.target("https://api.nexchange.io/en/api/v1/currency/")
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            System.out.println("status: " + response.getStatus());
            System.out.println("headers: " + response.getHeaders());
            System.out.println("body:" + response.readEntity(String.class));


        } finally {
            if (response != null) {
                response.close();
            }

            if (client != null) {
                client.close();
            }
        }
    }
}
