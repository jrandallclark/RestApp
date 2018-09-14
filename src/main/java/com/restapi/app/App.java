package com.restapi.app;

import com.google.gson.Gson;
import com.sun.tools.javac.util.Assert;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.parser.ParseException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Hit a REST endpoint and get data
 *
 */
public class App {
    private static final String GET_CURRENCIES = "https://api.nexchange.io/en/api/v1/currency/";
    private static final String POST_CURRENCIES = "http://ptsv2.com/t/3u39w-1536889576/post";

    public static void main(String[] args) throws IOException {
        Client client = null;
        Response response = null;
        try {
            client = ClientBuilder.newClient();
            response = client.target(GET_CURRENCIES)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            Assert.check(response.getStatus() == HttpURLConnection.HTTP_OK);
            Assert.check(response.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0).equals(MediaType.APPLICATION_JSON));

            // read JSON response and assert values returned
            String jsonContent = response.readEntity(String.class);
            Gson gson = new Gson();
            Currency[] currencies = gson.fromJson(jsonContent, Currency[].class);

            System.out.println("no. of currencies: " + currencies.length + "\n");
            for (Currency currency : currencies) {
                System.out.println("Code:" + currency.getCode());
                System.out.println("Name:" + currency.getName());
                System.out.println("Minimal Amount:" + currency.getMinimalAmount());

                System.out.println("Minimum Confirmations:" + currency.getMinConfirmations());

                System.out.println("Is Crypto:" + currency.isCrypto());
                System.out.println("Is base of enabled pair:" + currency.isBaseOfEnabledPair());
                System.out.println("Is quote of enabled pair:" + currency.isQuoteOfEnabledPair());
                System.out.println("Has enabled pairs:" + currency.hasEnabledPairs());

                System.out.println("\n");
            }
            response.close();

            // post JSON object to uri and assert values returned
            Currency currency = new Currency("CKN", "CarlCash", "1.00000000", 5, true, false, false, false);

            ObjectMapper objectMapper = new ObjectMapper();
            jsonContent = objectMapper.writeValueAsString(currency);
            GenericEntity<Currency> entity = new GenericEntity<>(currency, Currency.class);

            response = client.target(POST_CURRENCIES)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(currency, MediaType.APPLICATION_XML));

            Assert.check(response.getStatus() == HttpURLConnection.HTTP_OK);
            Assert.check(response.readEntity(String.class).equals("Thank you for this dump. I hope you have a lovely day!"));

        } finally {
            if (response != null) {
                response.close();
            }

            if (client != null) {
                client.close();
            }
        }
    }

    @XmlRootElement
    private static class Currency {
        private String code, name, minimal_amount;
        private int min_confirmations;
        private boolean is_crypto, is_base_of_enabled_pair, is_quote_of_enabled_pair, has_enabled_pairs;

        public Currency() { }

        public Currency(String code, String name, String minimaAmount, int minConfirmations, boolean isCrypto, boolean isBaseOfEnabledPair, boolean isQuoteOfEnabledPair, boolean hasEnabledPairs) {
            this.code = code;
            this.name = name;
            minimal_amount = minimaAmount;
            min_confirmations = minConfirmations;
            is_crypto = isCrypto;
            is_base_of_enabled_pair = isBaseOfEnabledPair;
            is_quote_of_enabled_pair = isQuoteOfEnabledPair;
            has_enabled_pairs = hasEnabledPairs;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMinimalAmount() {
            return minimal_amount;
        }

        public void setMinimalAmount(String minimal_amount) {
            this.minimal_amount = minimal_amount;
        }

        public int getMinConfirmations() {
            return min_confirmations;
        }

        public void setMinConfirmations(int min_confirmations) {
            this.min_confirmations = min_confirmations;
        }

        public boolean isCrypto() {
            return is_crypto;
        }

        public void setIsCrypto(boolean is_crypto) {
            this.is_crypto = is_crypto;
        }

        public boolean isBaseOfEnabledPair() {
            return is_base_of_enabled_pair;
        }

        public void setIsBaseOfEnabledPair(boolean is_base_of_enabled_pair) {
            this.is_base_of_enabled_pair = is_base_of_enabled_pair;
        }

        public boolean isQuoteOfEnabledPair() {
            return is_quote_of_enabled_pair;
        }

        public void setIs_quote_of_enabled_pair(boolean is_quote_of_enabled_pair) {
            this.is_quote_of_enabled_pair = is_quote_of_enabled_pair;
        }

        public boolean hasEnabledPairs() {
            return has_enabled_pairs;
        }

        public void setHasEnabledPairs(boolean has_enabled_pairs) {
            this.has_enabled_pairs = has_enabled_pairs;
        }
    }
}
