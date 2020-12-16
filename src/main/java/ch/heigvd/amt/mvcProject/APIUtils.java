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

public class APIUtils {

    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();

    public static String register() {

        String apiKey = "";
        HttpPost request = new HttpPost(BASE_URL+ "/registration");

        try {
            HttpResponse response = HTTP_CLIENT.execute(request);
            HttpEntity entity = response.getEntity();

            StringWriter writer = new StringWriter();
            String encoding = StandardCharsets.UTF_8.name();
            IOUtils.copy(entity.getContent(), writer, encoding);

            JSONObject result = new JSONObject(writer.toString());
            apiKey = result.getString("value");

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(apiKey);

        return apiKey;
    }


}
