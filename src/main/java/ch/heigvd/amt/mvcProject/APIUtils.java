package ch.heigvd.amt.mvcProject;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import javax.json.JsonObject;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Utils for the gamification API service
 */
public class APIUtils {

    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();
    private static final boolean DEBUG = true;

    private static String API_KEY = "";

    /**
     * Registers this application to the gamification service
     */
    public static void register() {

        HttpPost request = makePostRequest("/registration");

        try {
            HttpResponse response = HTTP_CLIENT.execute(request);
            JSONObject result = getJsonFromResponse(response);
            API_KEY = result.getString("value");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(DEBUG) System.out.println("Successfully registered : " + API_KEY);
    }

    public static void rewardBadge() {
        if(API_KEY.isEmpty()) {
            System.out.println("This application is not registered.");
            return;
        }

        
    }

    /**
     * Make an HTTP post request
     * @param endpoint : endpoint for the request
     * @return http post request
     */
    private static HttpPost makePostRequest(String endpoint) {
        HttpPost result = new HttpPost(BASE_URL + endpoint);
        if(DEBUG) System.out.println("POST Request : " + BASE_URL + endpoint);
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
}
