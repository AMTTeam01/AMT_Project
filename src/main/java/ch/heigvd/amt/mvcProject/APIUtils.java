package ch.heigvd.amt.mvcProject;

import ch.heigvd.amt.mvcProject.application.gamificationapi.badge.BadgesDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Utils for the gamification API service
 */
public class APIUtils {

    private static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();
    private static final boolean DEBUG = true;

    // API Values (todo : in env. variables)
    private static final String BASE_URL = "http://localhost:8080";
    private static final String API_KEY = "";

    // POST EVENTS
    private static final String EVENT_COMMENT = "COMMENT";
    private static final String EVENT_UPVOTE = "UPVOTE";
    private static final String EVENT_DOWNVOTE = "DOWNVOTE";
    private static final String EVENT_POST_QUESTION = "POST_QUESTION";
    private static final ArrayList<String> POST_EVENT_TYPES = new ArrayList<>(Arrays.asList(
            EVENT_COMMENT, EVENT_UPVOTE, EVENT_DOWNVOTE, EVENT_POST_QUESTION
    ));

    /**
     * Get all badges from the API
     * @return list of all badges
     * @throws Exception
     */
    public static ArrayList<BadgesDTO.BadgeDTO> getBadges() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BadgesDTO.BadgeDTO[] badges = mapper.readValue(getBadgesApiCall(), BadgesDTO.BadgeDTO[].class);
        return new ArrayList<>(Arrays.asList(badges));
    }

    /**
     * Inform the api that a user gave an upvote
     * @param userId : id of the user
     * @return api response
     * @throws Exception
     */
    public static String postUpvoteEvent(String userId) throws Exception {
        return postEvent(EVENT_UPVOTE, userId);
    }

    /**
     * Inform the api that a user gave an upvote
     * @param userId : id of the user
     * @return api response
     * @throws Exception
     */
    public static String postDownvoteEvent(String userId) throws Exception {
        return postEvent(EVENT_DOWNVOTE, userId);
    }

    /**
     * Inform the api that a user asked a question
     * @param userId : id of the user
     * @return api response
     * @throws Exception
     */
    public static String postAskedAQuestionEvent(String userId) throws Exception {
        return postEvent(EVENT_POST_QUESTION, userId);
    }

    /**
     * Inform the api that a user wrote a comment
     * @param userId : id of the user
     * @return api response
     * @throws Exception
     */
    public static String postCommentEvent(String userId) throws Exception {
        return postEvent(EVENT_COMMENT, userId);
    }

    /**
     * Post an event to the api
     * @param type : type of the event
     * @param userId : id of the user
     * @return api response
     * @throws Exception
     */
    private static String postEvent(String type, String userId) throws Exception {
        if(API_KEY.isEmpty()) {
            throw new Exception("This application is not registered.");
        }

        if(type.isEmpty() || !POST_EVENT_TYPES.contains(type)) {
            throw new Exception("Invalid type");
        }

        // Make post request with parameters
        HttpPost request = makePostRequest("/events", new ArrayList<>(Arrays.asList(
                new BasicNameValuePair("userId", userId),
                new BasicNameValuePair("timestamp", new Date().toString()),
                new BasicNameValuePair("type", type)
        )), true);

        // Get response
        HttpResponse response = HTTP_CLIENT.execute(request);

        if(response != null) {
            switch(response.getStatusLine().getStatusCode()) {
                case 201:
                    if(DEBUG) System.out.println("Successfully created a new event of type " + type + " for user " + userId);
                    return getJsonFromResponse(response).toString();
                case 401:
                    if(DEBUG) System.out.println("The API Key is missing.");
                    throw new Exception("The API Key is missing.");
                default:
                    if(DEBUG) System.out.println("Unknown status code : " + response.getStatusLine().getStatusCode());
                    throw new Exception("Unknown status code : " + response.getStatusLine().getStatusCode());
            }
        }

        // No response from the api
        return "";
    }

    private static String getBadgesApiCall() throws Exception {
        if(API_KEY.isEmpty()) {
            throw new Exception("This application is not registered.");
        }

        // Make get request with no parameters
        HttpGet request = makeGetRequest("/badges", new ArrayList<>(), true);

        // Get response
        HttpResponse response = HTTP_CLIENT.execute(request);

        if(response != null) {
            switch(response.getStatusLine().getStatusCode()) {
                case 200:
                    if(DEBUG) System.out.println("Successfully loaded all badges.");
                    return getJsonFromResponse(response).toString();
                case 401:
                    if(DEBUG) System.out.println("The API Key is missing.");
                    throw new Exception("The API Key is missing.");
                default:
                    if(DEBUG) System.out.println("Unknown status code : " + response.getStatusLine().getStatusCode());
                    throw new Exception("Unknown status code : " + response.getStatusLine().getStatusCode());
            }
        }

        // No response from the api
        return "";
    }

    /**
     * Make an HTTP post request
     * @param endpoint : endpoint for the request
     * @return http post request
     */
    private static HttpPost makePostRequest(String endpoint, ArrayList<NameValuePair> postParameters,
                                            boolean registered) {
        HttpPost result = new HttpPost(BASE_URL + endpoint);

        // Add header for authorization
        if(registered)
            result.setHeader("X-API-KEY", API_KEY);

        // Add parameters if there are any
        StringEntity entityParams = null;
        if(postParameters != null && !postParameters.isEmpty()) {
            try {
                entityParams = new StringEntity(getJsonFromParams(postParameters));
                result.setHeader("Content-type", "application/json");
                result.setEntity(entityParams);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if(DEBUG)  {
            System.out.println("POST Request : " + BASE_URL + endpoint);
            for(Header header : result.getAllHeaders())
                System.out.println("\t\tHeader : " + header.getName() + " : " + header.getValue());
            if(entityParams != null) {
                System.out.println("\t\t" + entityParams);
                try {
                    System.out.println("\t\t" + entityParams.getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * Make an HTTP get request
     * @param endpoint : endpoint for the request
     * @return http get request
     */
    private static HttpGet makeGetRequest(String endpoint, ArrayList<NameValuePair> getParameters,
                                            boolean registered) {
        StringBuilder getUrl = new StringBuilder(BASE_URL + endpoint);

        // Add parameters if there are any
        if (getParameters != null && !getParameters.isEmpty()) {
            getUrl.append("?");
            for (int i = 0; i < getParameters.size(); i++) {

                getUrl.append(getParameters.get(i).getName())
                        .append("=")
                        .append(getParameters.get(i).getValue());

                if(i < getParameters.size() - 1) {
                    getUrl.append(",");
                }
            }
        }

        HttpGet result = new HttpGet(getUrl.toString());

        // Add header for authorization
        if(registered)
            result.setHeader("X-API-KEY", API_KEY);

        if(DEBUG)  {
            System.out.println("POST Request : " + getUrl.toString());
            for(Header header : result.getAllHeaders())
                System.out.println("\t\tHeader : " + header.getName() + " : " + header.getValue());
        }

        return result;
    }

    /**
     * Get the JSON object from an http response
     * @param response : http response
     * @return json object from response
     * @throws IOException
     */
    private static JSONObject getJsonFromResponse(HttpResponse response) throws IOException {
        StringWriter writer = new StringWriter();
        String encoding = StandardCharsets.UTF_8.name();
        IOUtils.copy(response.getEntity().getContent(), writer, encoding);
        return new JSONObject(writer.toString());
    }

    /**
     * Get the JSON Format for parameters
     * @param params : parameters
     * @return json for the parameter
     */
    private static String getJsonFromParams(ArrayList<NameValuePair> params) {

        StringBuilder jsonParams = new StringBuilder("{");
        for(int i = 0; i < params.size(); ++i) {
            NameValuePair param = params.get(i);
            jsonParams.append("\"")
                      .append(param.getName())
                      .append("\":\"")
                      .append(param.getValue())
                      .append("\"");
            if(i < params.size() - 1) jsonParams.append(",");
        }
        jsonParams.append("}");

        return jsonParams.toString();
    }
}
