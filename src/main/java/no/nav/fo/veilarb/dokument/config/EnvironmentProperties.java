package no.nav.fo.veilarb.dokument.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.env")
public class EnvironmentProperties {
    private String dokumentproduksjonUrl;
    private String aktorregisterUrl;
    private String cxfStsUrl;
    private String stsDiscoveryUrl;
    private String norg2Url;
    private String abacUrl;
    private String openAmDiscoveryUrl;
    private String veilarbloginOpenAmClientId;
    private String aadDiscoveryUrl;
    private String veilarbloginAadClientId;
}
