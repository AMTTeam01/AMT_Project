package ch.heigvd.amt.mvcProject.ui.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

// Default page : https://stackoverflow.com/questions/20455442/java-servlet-specify-start-page-wih-webservlet-annotation

@WebServlet(name = "HomePageRenderer", urlPatterns = "/index.html")
public class HomePageRenderer extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // test api call at init of home page
        ObjectMapper mapper = new ObjectMapper();

        OkHttpClient client = new OkHttpClient();
        RequestBody emptyRequestBody = RequestBody.create(null, new byte[0]);

        Request request = new Request.Builder()
                .url("http://localhost:8080/registration")
                .post(emptyRequestBody)
                .build(); // defaults to GET

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //APOD apod = mapper.readValue(response.body().byteStream(), APOD.class);

        System.out.println(response);
        System.out.println(response.body());
        System.out.println(response.message());
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException,
            IOException {

        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}
