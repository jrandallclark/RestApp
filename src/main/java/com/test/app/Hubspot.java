package com.test.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.entities.Invitation;
import com.test.entities.Partner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Hit a REST endpoint and get data, do some calculation and post data for a SUCCESS response
 *
 */
public class Hubspot {
    private static final String GET_PARTNERS = "https://candidate.hubteam.com/candidateTest/v3/problem/dataset?userKey=a715314cd6bd0072e727388c6dee";
    private static final String POST_INVITATIONS = "https://candidate.hubteam.com/candidateTest/v3/problem/result?userKey=a715314cd6bd0072e727388c6dee";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static void main(String[] args) {
        List<Partner> partners = getData(MediaType.APPLICATION_JSON, GET_PARTNERS, "partners");
        List<Invitation> invitations = doWork(partners);
        postData(MediaType.APPLICATION_JSON, POST_INVITATIONS, invitations, "countries");
    }

    private static List<Invitation> doWork(List<Partner> partners) {
        Map<String, Map<Date, Collection<Partner>>> countryToDateFeasibilityMap = new HashMap<>();
        for (Partner partner : partners) {
            // find out which partners in a country can attend on a given date
            Map<Date, Collection<Partner>> dateFeasibilityMap = countryToDateFeasibilityMap.computeIfAbsent(partner.getCountry(), k -> new TreeMap<>());

            for (Date availableDate : partner.getAvailableDates()) {
                Collection<Partner> partnersWhoCanAttend = dateFeasibilityMap.computeIfAbsent(availableDate, k -> new HashSet<>());
                partnersWhoCanAttend.add(partner);
            }
        }

        // figure out the best two consecutive dates for a country where the attendance of partners is maximized
        List<Invitation> invitations = new ArrayList<>();
        for (Map.Entry<String, Map<Date, Collection<Partner>>>  countryToDateFeasibilityEntry : countryToDateFeasibilityMap.entrySet()) {
            String country = countryToDateFeasibilityEntry.getKey();
            Map<Date, Collection<Partner>> dateFeasibilityMap = countryToDateFeasibilityEntry.getValue();

            int maxAttendeeCount = Integer.MIN_VALUE;
            Date bestStartDate = null;
            Collection<Partner> maxAttendees = Collections.emptyList();

            List<Map.Entry<Date, Collection<Partner>>> dateFeasibilityEntries = new ArrayList<>(dateFeasibilityMap.entrySet());
            for (int i = 0; i < dateFeasibilityEntries.size() - 1; i++) {
                Date currentDate = dateFeasibilityEntries.get(i).getKey();
                Date nextDate = dateFeasibilityEntries.get(i + 1).getKey();

                // check whether dates are consecutive
                long diffInMillis = Math.abs(nextDate.getTime() - currentDate.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                if (diff != 1 || !dateFeasibilityEntries.get(i).getValue().equals(dateFeasibilityEntries.get(i + 1).getValue())) {
                    continue;
                }

                Set<Partner> attendees = new HashSet<>();
                attendees.addAll(dateFeasibilityEntries.get(i).getValue());

                if (attendees.size() > maxAttendeeCount) {
                    maxAttendeeCount = attendees.size();
                    maxAttendees = attendees;
                    bestStartDate = currentDate;
                }
            }

            String[] attendeeEmails = maxAttendees.stream().map(Partner::getEmail).toArray(String[]::new);
            invitations.add(new Invitation(country, bestStartDate, maxAttendeeCount, attendeeEmails));
        }
        return invitations;
    }

    /**
     * Gets the data from the REST endpoint via GET request
     * @param mediaType the type for the response from the REST endpoint
     * @param getURI the REST endpoint for GET requestd
     * @return a collection of the data from the REST endpoint
     */
    private static List<Partner> getData(final String mediaType, final String getURI, final String mapKey) {
        Client client = null;
        Response response = null;
        try {
            client = ClientBuilder.newClient();
            response = client.target(getURI)
                    .request(mediaType)
                    .get();

            // read JSON response and assert values returned
            String jsonContent = response.readEntity(String.class);

            JSONParser parser = new JSONParser();
            Object jsonObject = parser.parse(jsonContent);

            JSONObject jsonData = (JSONObject) jsonObject;
            JSONArray jsonArray = (JSONArray) jsonData.get(mapKey);
            Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();

            List<Partner> payloadData = new LinkedList<>();
            for (Object jsonArrayItem : jsonArray) {
                JSONObject jsonClassData = (JSONObject) jsonArrayItem;
                Partner partner = gson.fromJson(jsonClassData.toJSONString(), Partner.class);

                payloadData.add(partner);
            }

            return payloadData;

        } catch (ParseException e) {
            e.printStackTrace();
            return Collections.emptyList();

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
     * @param invitations the data for the post request body
     */
    private static void postData(final String mediaType, final String postURI, final List<Invitation> invitations, final String mapKey)  {
        Client client = null;
        Response response = null;
        try {
            client = ClientBuilder.newClient();

            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setDateFormat(dateFormat);
            String arrayToJson = objectMapper.writeValueAsString(invitations);

            // build up the payload string for POST request
            String invitationData = String.format("{ \"%s\": %s }", mapKey, arrayToJson);

            response = client.target(postURI)
                    .request(mediaType)
                    .post(Entity.json(invitationData));

        } catch (JsonProcessingException e) {
            e.printStackTrace();

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
