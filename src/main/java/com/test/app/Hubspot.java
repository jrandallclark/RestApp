package com.test.app;

import com.google.gson.Gson;
import com.test.entities.Currency;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Hit a REST endpoint and get data, do some calculation and post data for a SUCCESS response
 *
 */
public class Hubspot {
    private static final String GET_CURRENCIES = "https://api.nexchange.io/en/api/v1/currency/";
    private static final String POST_CURRENCIES = "http://ptsv2.com/t/3u39w-1536889576/post"; // TODO: replace this!

    public static void main(String[] args) {
        Currency[] currencies = getData(MediaType.APPLICATION_JSON, GET_CURRENCIES, Currency[].class);
        List<Currency> currency = doWork(currencies);
        postData(MediaType.APPLICATION_JSON, POST_CURRENCIES, currency);
    }

    private static <T extends Currency> List<T> doWork(T[] data) {
        // TODO: implement this!

        System.out.println("no. of currencies: " + data.length + "\n");
        for (T currency : data) {
            System.out.println("Code:" + currency.getCode());
            System.out.println("Name:" + currency.getName());
            System.out.println("Minimal Amount:" + currency.getMinimalAmount());

            System.out.println("Minimum Confirmations:" + currency.getMinConfirmations());

            System.out.println("Is Crypto:" + currency.getIs_Crypto());
            System.out.println("Is base of enabled pair:" + currency.getIs_Base_Of_Enabled_Pair());
            System.out.println("Is quote of enabled pair:" + currency.getIs_Quote_Of_Enabled_Pair());
            System.out.println("Has enabled pairs:" + currency.getHas_Enabled_Pairs());

            System.out.println("\n");
        }

        List<T> results = new ArrayList<>();
        results.add((T) new Currency("CKN", "CarlCash", "1.00000000", 5, true, false, false, false));
        return results;
    }

    /**
     * Gets the data from the REST endpoint via GET request
     * @param mediaType the type for the response from the REST endpoint
     * @param getURI the REST endpoint for GET request
     * @param clazz the return type for the data in the response
     * @param <T> the generic type for the the object being returned by the REST endpoint
     * @return a collection of the data from the REST endpoint
     */
    private static <T> T[] getData(final String mediaType, final String getURI, final Class<T[]> clazz) {
        Client client = null;
        Response response = null;
        try {
            client = ClientBuilder.newClient();
            response = client.target(getURI)
                    .request(mediaType)
                    .get();

            assert response.getStatus() == HttpURLConnection.HTTP_OK : " GET unsuccessful";
            assert response.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0).equals(mediaType) : " content type for respose doesn't match expected";

            // read JSON response and assert values returned
            String jsonContent = response.readEntity(String.class);
            Gson gson = new Gson();
            return gson.fromJson(jsonContent, clazz);

        } finally {
            if (response != null) {
                response.close();
            }

            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * Posts the data to the REST endpoint via a POST request
     * @param mediaType the type for the response from the REST endpoint
     * @param postURI the rest endpoint for POST request
     * @param payload the data for the post request body
     * @param <T> the generic type for the the object being sent to the REST endpoint
     */
    private static <T> void postData(final String mediaType, final String postURI, final List<T> payload) {
        Client client = null;
        Response response = null;
        try {
            client = ClientBuilder.newClient();

            response = client.target(postURI)
                    .request(mediaType)
                    .post(Entity.json(payload));

            assert response.getStatus() == HttpURLConnection.HTTP_OK : " POST unsuccessful";
            assert response.readEntity(String.class).equals("Thank you for this dump. I hope you have a lovely day!") : " POST unsuccessful"; // TODO: remove this!

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