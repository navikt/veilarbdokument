package no.nav.fo.veilarb.dokument.localtestapp.stub;

import no.nav.common.client.aktoroppslag.AktorOppslagClient;
import no.nav.common.client.aktoroppslag.BrukerIdenter;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.EksternBrukerId;
import no.nav.common.types.identer.Fnr;

import java.util.List;
import java.util.Map;

import static no.nav.fo.veilarb.dokument.localtestapp.stub.Values.TEST_AKTOR_ID;

public class AktorOppslagClientStub implements AktorOppslagClient{
    @Override
    public Fnr hentFnr(AktorId s) {
        return null;
    }

    @Override
    public AktorId hentAktorId(Fnr s) {
        return TEST_AKTOR_ID;
    }

    @Override
    public Map<AktorId, Fnr> hentFnrBolk(List<AktorId> list) {
        return null;
    }

    @Override
    public Map<Fnr, AktorId> hentAktorIdBolk(List<Fnr> list) {
        return null;
    }

    @Override
    public BrukerIdenter hentIdenter(EksternBrukerId eksternBrukerId) {
        return null;
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckResult.healthy();
    }
}
