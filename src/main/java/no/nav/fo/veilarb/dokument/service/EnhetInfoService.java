package no.nav.fo.veilarb.dokument.service;

import no.nav.common.client.norg2.Enhet;
import no.nav.common.types.identer.EnhetId;
import no.nav.fo.veilarb.dokument.client.api.EnhetClient;
import no.nav.fo.veilarb.dokument.domain.EnhetKontaktinformasjon;
import no.nav.fo.veilarb.dokument.domain.EnhetOrganisering;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class EnhetInfoService {

    private final EnhetClient enhetClient;

    public EnhetInfoService(EnhetClient enhetClient) {
        this.enhetClient = enhetClient;
    }

    public Enhet hentEnhet(EnhetId enhetId) {
        List<Enhet> enheter = enhetClient.hentAktiveEnheter();

        return enheter.stream()
                .filter(enhet -> enhetId.get().equals(enhet.getEnhetNr()))
                .findFirst().orElseThrow(() -> new IllegalStateException("Fant ikke navn for enhet " + enhetId));
    }

    public EnhetKontaktinformasjon utledEnhetKontaktinformasjon(EnhetId enhetId) {
        EnhetKontaktinformasjon enhetKontaktinformasjon = enhetClient.hentKontaktinfo(enhetId);

        if (enhetKontaktinformasjon.getPostadresse() != null) {
            return enhetKontaktinformasjon;
        } else {
            List<EnhetOrganisering> enhetOrganisering = enhetClient.hentEnhetOrganisering(enhetId);
            List<EnhetOrganisering> eiere = enhetOrganisering.stream()
                    .filter(x -> x.getOrgType().equals("EIER")).collect(Collectors.toList());
            List<EnhetOrganisering> gyldigeEiere = eiere.stream().filter(this::erGjeldende).collect(Collectors.toList());
            if (gyldigeEiere.size() == 1) {
                EnhetOrganisering eier = gyldigeEiere.get(0);
                EnhetKontaktinformasjon eierEnhetKontaktinformasjon =
                        enhetClient.hentKontaktinfo(eier.getOrganiserer().getNr());
                if (eierEnhetKontaktinformasjon.getPostadresse() != null) {
                    return eierEnhetKontaktinformasjon;
                } else {
                    throw new RuntimeException(format("Eier-enhet %s for enhet %s mangler adresse",
                            eier.getOrganiserer().getNr(), enhetId));
                }
            } else {
                throw new RuntimeException(
                        format("Fant ikke eier-enhet for enhet %s uten adresse. Årsak: %d gydlige eiere, %d ugyldige eiere.",
                                enhetId, gyldigeEiere.size(), eiere.size() - gyldigeEiere.size()));
            }
        }
    }

    private boolean erGjeldende(EnhetOrganisering x) {
        LocalDate now = LocalDate.now();

        Boolean gyldigFra = Optional.ofNullable(x.getFra()).map(fra -> fra.isBefore(now) || fra.isEqual(now)).orElse(true);
        Boolean gyldigTil = Optional.ofNullable(x.getTil()).map(til -> til.isAfter(now) || til.isEqual(now)).orElse(true);

        return gyldigFra && gyldigTil;
    }
}
