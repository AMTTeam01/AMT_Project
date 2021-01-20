package ch.heigvd.amt.mvcProject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class GamificationConfig {

    @Inject
    @ConfigProperty(name="port")
    private int port;

    @Inject
    @ConfigProperty(name="address")
    private String address;

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public String getUrl() {
        return "http://" + address + ":" + port;
    }
}
