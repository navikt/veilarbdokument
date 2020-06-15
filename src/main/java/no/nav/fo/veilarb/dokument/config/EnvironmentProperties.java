package no.nav.fo.veilarb.dokument.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app.env")
public class EnvironmentProperties {
    private String dokumentproduksjonUrl;
    private String aktorregisterUrl;
    private String cxfStsUrl;
    private String stsDiscoveryUrl;
    private String sakUrl;
    private String veilarbarenaUrl;
    private String veilarbveilederUrl;
    private String norg2Url;
    private String openAmDiscoveryUrl;
    private String abacUrl;
    private String openAmClientId;
    private String refreshUrl;
}
