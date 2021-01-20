package ch.heigvd.amt.mvcProject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Named("GamificationConfig")
public class GamificationConfig {

    @Inject
    @ConfigProperty(name = "port")
    private int port;

    @Inject
    @ConfigProperty(name = "address")
    private String address;

    @Inject
    @ConfigProperty(name = "apikey")
    private String apiKey;

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public String getApiKey(){
        return apiKey;
    }

    public String getUrl() {
        return "http://" + getAddress() + ":" + getPort();
    }
}
