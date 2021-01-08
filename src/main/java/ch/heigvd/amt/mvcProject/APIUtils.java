package ch.heigvd.amt.mvcProject;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utils for the gamification API service
 */
public class APIUtils {

    private static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();
    private static final boolean DEBUG = true;

    // API Values (todo : in env. variables)
    private static final String BASE_URL = "http://localhost:8080";
    private static String API_KEY = "86d1b80d-126a-4396-8886-72f8e083b858";

    // EVENTS
    private static final String EVENT_COMMENT = "COMMENT";
    private static final String EVENT_UPVOTE = "UPVOTE";
    private static final String EVENT_DOWNVOTE = "DOWNVOTE";
    private static final String EVENT_POST_QUESTION = "questionPosted";
    private static final ArrayList<String> EVENT_TYPES = new ArrayList<>(Arrays.asList(
            EVENT_COMMENT, EVENT_UPVOTE, EVENT_DOWNVOTE, EVENT_POST_QUESTION
    ));

    // Date formatter
    private static final TimeZone tz = TimeZone.getTimeZone("UTC");
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");


    public static String postUpvoteEvent(String userId) throws ApiFailException, IOException {
        return postEvent(EVENT_UPVOTE, userId);
    }

    public static String postDownvoteEvent(String userId) throws ApiFailException, IOException {
        return postEvent(EVENT_DOWNVOTE, userId);
    }

    public static String postAskedAQuestionEvent(String userId) throws ApiFailException, IOException {
        return postEvent(EVENT_POST_QUESTION, userId);
    }

    public static String postCommentEvent(String userId) throws ApiFailException, IOException {
        return postEvent(EVENT_COMMENT, userId);
    }

    private static String postEvent(String type, String userId) throws ApiFailException, IOException {
        if (API_KEY.isEmpty()) {
            throw new ApiFailException("This application is not registered.");
        }

        if (type.isEmpty() || !EVENT_TYPES.contains(type)) {
            throw new ApiFailException("Invalid type");
        }

        // set timezone
        df.setTimeZone(tz);
        // Make post request with parameters
        HttpPost request = makePostRequest("/events", new ArrayList<>(Arrays.asList(
                new BasicNameValuePair("userId", userId),
                new BasicNameValuePair("timestamp", df.format(new Date())),
                new BasicNameValuePair("type", type)
        )), true);

        // Get response
        HttpResponse response = HTTP_CLIENT.execute(request);


        if (response != null) {
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    if (DEBUG)
                        System.out.println("Successfully created a new event of type " + type + " for user " + userId);
                    return "";
                case 201:
                    if (DEBUG)
                        System.out.println("Successfully created a new event of type " + type + " for user " + userId);
                    return getJsonFromResponse(response).toString();
                case 401:
                    if (DEBUG) System.out.println("The API Key is missing.");
                    throw new ApiFailException("The API Key is missing.");
                default:
                    if (DEBUG) System.out.println("Unknown status code : " + response.getStatusLine()
                            .getStatusCode() + "\n" + getJsonFromResponse(response).toString());
                    throw new ApiFailException("Unknown status code : " + response.getStatusLine().getStatusCode());
            }
        }

        return "";
    }

    /**
     * Make an HTTP post request
     *
     * @param endpoint : endpoint for the request
     * @return http post request
     */
    private static HttpPost makePostRequest(String endpoint, ArrayList<NameValuePair> postParameters,
                                            boolean registered) {
        HttpPost result = new HttpPost(BASE_URL + endpoint);

        // Add header for authorization
        if (registered)
            result.setHeader("X-API-KEY", API_KEY);

        // Add parameters if there are any
        StringEntity entityParams = null;
        if (postParameters != null && !postParameters.isEmpty()) {
            try {
                entityParams = new StringEntity(getJsonFromParams(postParameters));
                result.setHeader("Content-type", "application/json");
                result.setEntity(entityParams);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (DEBUG) {
            System.out.println("POST Request : " + BASE_URL + endpoint);
            for (Header header : result.getAllHeaders())
                System.out.println("\t\tHeader : " + header.getName() + " : " + header.getValue());
            if (entityParams != null) {
                System.out.println("\t\t" + entityParams);
                try {
                    System.out.println("\t\t" + IOUtils.toString(entityParams.getContent()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * Get the JSON object from an http response
     *
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
     *
     * @param params : parameters
     * @return json for the parameter
     */
    private static String getJsonFromParams(ArrayList<NameValuePair> params) {

        StringBuilder jsonParams = new StringBuilder("{");
        for (int i = 0; i < params.size(); ++i) {
            NameValuePair param = params.get(i);
            jsonParams.append("\"")
                    .append(param.getName())
                    .append("\":\"")
                    .append(param.getValue())
                    .append("\"");
            if (i < params.size() - 1) jsonParams.append(",");
        }
        jsonParams.append("}");

        return jsonParams.toString();
    }
}
