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
     * Registers a new application to the gamification service
     * @return api key used for identification
     */
    public static void register() {

        HttpPost request = makePostRequest("/registration");

        try {
            HttpResponse response = HTTP_CLIENT.execute(request);
            HttpEntity entity = response.getEntity();

            StringWriter writer = new StringWriter();
            String encoding = StandardCharsets.UTF_8.name();
            IOUtils.copy(entity.getContent(), writer, encoding);

            JSONObject result = new JSONObject(writer.toString());
            API_KEY = result.getString("value");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(DEBUG) System.out.println("Successfully registered : " + API_KEY);
    }

    public static void rewardBadge() {



    }

    private static HttpPost makePostRequest(String endpoint) {
        HttpPost result = new HttpPost(BASE_URL + endpoint);
        if(DEBUG) System.out.println("POST Request : " + BASE_URL + endpoint);
        return result;
    }
}
