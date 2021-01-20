package ch.heigvd.amt.mvcProject;

import ch.heigvd.amt.mvcProject.application.gamificationapi.badge.BadgesDTO;
import ch.heigvd.amt.mvcProject.application.gamificationapi.profile.UsersProfileDTO;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
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
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;


/**
 * Utils for the gamification API service
 */
@ApplicationScoped
@Named("APIUtils")
public class APIUtils {

    @Inject
    @Named("GamificationConfig")
    private GamificationConfig gamificationConfig;

    private static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();
    private static final boolean DEBUG = true;

    // EVENTS
    private static final String EVENT_COMMENT = "commentPosted";
    private static final String EVENT_POST_QUESTION = "questionPosted";
    private static final String EVENT_POST_FIRST_QUESTION = "firstPost";
    private static final String EVENT_POST_OPEN_QUESTION = "openAQuestion";

    private static final String EVENT_POPULAR_COMMENT_QUESTION = "popularCommentOrQuestion";

    //TODO: Not used yet, need to defined in the api
    private static final String EVENT_UPVOTE = "UPVOTE";
    private static final String EVENT_DOWNVOTE = "DOWNVOTE";

    private static final ArrayList<String> EVENT_TYPES = new ArrayList<>(Arrays.asList(
            EVENT_COMMENT, EVENT_POST_QUESTION, EVENT_POST_OPEN_QUESTION, EVENT_POPULAR_COMMENT_QUESTION,
            EVENT_UPVOTE, EVENT_DOWNVOTE, EVENT_POST_FIRST_QUESTION
    ));

    // Date formatter
    private static final TimeZone tz = TimeZone.getTimeZone("UTC");
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

    public APIUtils() {
    }

    /**
     * Get all badges from the API
     *
     * @return list of all badges
     * @throws Exception
     */
    public ArrayList<BadgesDTO.BadgeDTO> getBadges() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BadgesDTO.BadgeDTO[] badges = mapper.readValue(getBadgesApiCall(), BadgesDTO.BadgeDTO[].class);
        return new ArrayList<>(Arrays.asList(badges));
    }

    public UsersProfileDTO.UserProfileDTO getProfil(UserId id) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        UsersProfileDTO.UserProfileDTO profile = mapper.readValue(getProfileAPI(id), UsersProfileDTO.UserProfileDTO.class);
        return profile;
    }

    /**
     * Get one badge from the api
     *
     * @param id : id of the badge
     * @return the requested badge
     * @throws Exception
     */
    public BadgesDTO.BadgeDTO getBadge(long id) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(getBadgeApiCall(id), BadgesDTO.BadgeDTO.class);
    }

    /**
     * Inform the api that a user gave an upvote
     *
     * @param userId : id of the user
     * @return api response
     * @throws Exception
     */
    public String postUpvoteEvent(String userId) throws Exception {
        return postEvent(EVENT_UPVOTE, userId);
    }

    /**
     * Inform the api that a user gave an upvote
     *
     * @param userId : id of the user
     * @return api response
     * @throws Exception
     */
    public String postDownvoteEvent(String userId) throws Exception {
        return postEvent(EVENT_DOWNVOTE, userId);
    }

    /**
     * Inform the api that a user asked a question
     *
     * @param userId : id of the user
     * @return api response
     * @throws Exception
     */
    public String postAskedAQuestionEvent(String userId) throws ApiFailException, IOException {
        return postEvent(EVENT_POST_QUESTION, userId);
    }

    /**
     * Inform the api that a user wrote a comment
     *
     * @param userId : id of the user
     * @return api response
     * @throws Exception
     */
    public String postCommentEvent(String userId) throws ApiFailException, IOException {
        return postEvent(EVENT_COMMENT, userId);
    }

    public String postFirstQuestionEvent(String userId) throws ApiFailException, IOException {
        return postEvent(EVENT_POST_FIRST_QUESTION, userId);
    }

    /**
     * Inform the api that a user open a question page
     *
     * @param userId : id of the user
     * @return api response
     * @throws Exception
     */
    public String postOpenAQuestion(String userId) throws Exception {
        return postEvent(EVENT_POST_OPEN_QUESTION, userId);
    }

    /**
     * Post an event to the api
     *
     * @param type   : type of the event
     * @param userId : id of the user
     * @return api response
     * @throws Exception
     */
    private String postEvent(String type, String userId) throws ApiFailException, IOException {
        System.out.println("post event type :" + type);
        if (gamificationConfig.getApiKey().isEmpty()) {
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
        )));

        // Get response
        HttpResponse response = HTTP_CLIENT.execute(request);


        if (response != null) {
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    if (DEBUG)
                        System.out.println(
                                "Successfully created a new event of type " + type + " for user " + userId);
                    return "";
                case 201:
                    if (DEBUG)
                        System.out.println(
                                "Successfully created a new event of type " + type + " for user " + userId);
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

        // No response from the api
        return "";
    }

    private String getProfileAPI(UserId id) throws Exception {
        if (gamificationConfig.getApiKey().isEmpty()) {
            throw new Exception("This application is not registered.");
        }

        // Make get request with no parameters
        HttpGet request = makeGetRequest("/users/" + id.asString(), new ArrayList<>());

        // Get response
        HttpResponse response = HTTP_CLIENT.execute(request);

        if (response != null) {
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    if (DEBUG) System.out.println("Successfully loaded the profile of the userId " + id.toString());
                    return getJsonFromResponse(response).toString();
                case 401:
                    if (DEBUG) System.out.println("The API Key is missing.");
                    throw new Exception("The API Key is missing.");
                case 404:
                    if (DEBUG) System.out.println("User is not found");
                    throw new Exception("User is not found");
                default:
                    if (DEBUG)
                        System.out.println("Unknown status code : " + response.getStatusLine().getStatusCode());
                    throw new Exception("Unknown status code : " + response.getStatusLine().getStatusCode());
            }
        }

        // No response from the api
        return "";
    }

    private String getBadgesApiCall() throws Exception {
        if (gamificationConfig.getApiKey().isEmpty()) {
            throw new Exception("This application is not registered.");
        }

        // Make get request with no parameters
        HttpGet request = makeGetRequest("/badges", new ArrayList<>());

        // Get response
        HttpResponse response = HTTP_CLIENT.execute(request);

        if (response != null) {
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    if (DEBUG) System.out.println("Successfully loaded all badges.");
                    return getJsonFromResponse(response).toString();
                case 401:
                    if (DEBUG) System.out.println("The API Key is missing.");
                    throw new Exception("The API Key is missing.");
                default:
                    if (DEBUG)
                        System.out.println("Unknown status code : " + response.getStatusLine().getStatusCode());
                    throw new Exception("Unknown status code : " + response.getStatusLine().getStatusCode());
            }
        }

        // No response from the api
        return "";
    }

    private String getBadgeApiCall(long id) throws Exception {
        if (gamificationConfig.getApiKey().isEmpty()) {
            throw new Exception("This application is not registered.");
        }

        // Make get request with no parameters
        HttpGet request = makeGetRequest("/badges/" + id, new ArrayList<>());

        // Get response
        HttpResponse response = HTTP_CLIENT.execute(request);

        if (response != null) {
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    if (DEBUG) System.out.println("Successfully loaded one badge.");
                    return getJsonFromResponse(response).toString();
                case 401:
                    if (DEBUG) System.out.println("The API Key is missing.");
                    throw new Exception("The API Key is missing.");
                default:
                    if (DEBUG)
                        System.out.println("Unknown status code : " + response.getStatusLine().getStatusCode());
                    throw new Exception("Unknown status code : " + response.getStatusLine().getStatusCode());
            }
        }

        // No response from the api
        return "";
    }

    /**
     * Make an HTTP post request
     *
     * @param endpoint : endpoint for the request
     * @return http post request
     */
    private HttpPost makePostRequest(String endpoint, ArrayList<NameValuePair> postParameters) {
        String url = gamificationConfig.getUrl();
        String addr = gamificationConfig.getAddress();
        int port = gamificationConfig.getPort();
        HttpPost result = new HttpPost(gamificationConfig.getUrl() + endpoint);

        // Add header for authorization
        result.setHeader("X-API-KEY", gamificationConfig.getApiKey());

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
            System.out.println("POST Request : " + gamificationConfig.getUrl() + endpoint);
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
     * Make an HTTP get request
     *
     * @param endpoint : endpoint for the request
     * @return http get request
     */
    private HttpGet makeGetRequest(String endpoint, ArrayList<NameValuePair> getParameters) {
        StringBuilder getUrl = new StringBuilder(gamificationConfig.getUrl() + endpoint);

        // Add parameters if there are any
        if (getParameters != null && !getParameters.isEmpty()) {
            getUrl.append("?");
            for (int i = 0; i < getParameters.size(); i++) {

                getUrl.append(getParameters.get(i).getName())
                        .append("=")
                        .append(getParameters.get(i).getValue());

                if (i < getParameters.size() - 1) {
                    getUrl.append(",");
                }
            }
        }

        HttpGet result = new HttpGet(getUrl.toString());

        // Add header for authorization
        result.setHeader("X-API-KEY", gamificationConfig.getApiKey());

        if (DEBUG) {
            System.out.println("POST Request : " + getUrl.toString());
            for (Header header : result.getAllHeaders())
                System.out.println("\t\tHeader : " + header.getName() + " : " + header.getValue());
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
