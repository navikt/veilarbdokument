package no.nav.fo.veilarb.dokument.service;

import no.nav.fo.veilarb.dokument.client.ArenaClient;
import no.nav.fo.veilarb.dokument.domain.ArenaOppfolgingssak;
import no.nav.fo.veilarb.dokument.domain.Bruker;
import no.nav.fo.veilarb.dokument.domain.Sak;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class OppfolgingssakServiceTest {

    private Bruker bruker = new Bruker("fnr", "aktorId");
    private SakClient sakClient = mock(SakClient.class);
    private ArenaClient arenaClient = mock(ArenaClient.class);
    private OppfolgingssakService oppfolgingssakService = new OppfolgingssakService(sakClient, arenaClient);

    @Test
    public void hentOppfolgingssak__henter_sak_fra_gsak_dersom_en_oppfolgingssak() {
        Sak forventetSak = new Sak(0, null, null, null, null);

        when(sakClient.hentOppfolgingssaker(bruker.getAktorId())).thenReturn(Collections.singletonList(forventetSak));

        assertThat(oppfolgingssakService.hentOppfolgingssak(bruker)).isEqualTo(forventetSak);

        verify(sakClient, times(1)).hentOppfolgingssaker(bruker.getAktorId());
        verify(arenaClient, times(0)).oppfolgingssak(any());
        verify(sakClient, times(0)).opprettOppfolgingssak(any(), any());
    }

    @Test
    public void hentOppfolgingssak__henter_sak_fra_arena_og_oppretter_i_gsak_dersom_ingen_oppfolgingssak_i_gsak() {
        Sak forventetSak = new Sak(0, null, null, "fagsakNr", null);

        when(sakClient.hentOppfolgingssaker(bruker.getAktorId())).thenReturn(Collections.emptyList());
        when(arenaClient.oppfolgingssak(bruker.getFnr())).thenReturn(
                Optional.of(new ArenaOppfolgingssak(forventetSak.fagsakNr()))
        );
        when(sakClient.opprettOppfolgingssak(bruker.getAktorId(), forventetSak.fagsakNr())).thenReturn(forventetSak);

        assertThat(oppfolgingssakService.hentOppfolgingssak(bruker)).isEqualTo(forventetSak);

        verify(sakClient, times(1)).hentOppfolgingssaker(bruker.getAktorId());
        verify(arenaClient, times(1)).oppfolgingssak(bruker.getFnr());
        verify(sakClient, times(1)).opprettOppfolgingssak(bruker.getAktorId(), forventetSak.fagsakNr());
    }

    @Test(expected = IllegalStateException.class)
    public void hentOppfolgingssak__feiler_dersom_ingen_oppfolgingssak_verken_i_arena_eller_gsak() {
        when(sakClient.hentOppfolgingssaker(bruker.getAktorId())).thenReturn(Collections.emptyList());
        when(arenaClient.oppfolgingssak(bruker.getFnr())).thenReturn(Optional.empty());

        oppfolgingssakService.hentOppfolgingssak(bruker);

        verify(sakClient, times(1)).hentOppfolgingssaker(bruker.getAktorId());
        verify(arenaClient, times(1)).oppfolgingssak(bruker.getFnr());
        verify(sakClient, times(0)).opprettOppfolgingssak(any(), any());
    }

    @Test
    public void hentOppfolgingssak__finner_riktig_sak_fra_arena_dersom_flere_oppfolgingssaker_i_gsak() {
        Sak forventetSak = new Sak(0, null, null, "forventetFagsakNr", null);
        Sak uforventetSak = new Sak(0, null, null, "uforventetFagsakNr", null);

        when(sakClient.hentOppfolgingssaker(bruker.getAktorId())).thenReturn(Arrays.asList(forventetSak, uforventetSak));
        when(arenaClient.oppfolgingssak(bruker.getFnr())).thenReturn(
                Optional.of(new ArenaOppfolgingssak(forventetSak.fagsakNr()))
        );

        assertThat(oppfolgingssakService.hentOppfolgingssak(bruker)).isEqualTo(forventetSak);

        verify(sakClient, times(1)).hentOppfolgingssaker(bruker.getAktorId());
        verify(arenaClient, times(1)).oppfolgingssak(bruker.getFnr());
        verify(sakClient, times(0)).opprettOppfolgingssak(any(), any());
    }

    @Test(expected = IllegalStateException.class)
    public void hentOppfolgingssak__feiler_dersom_flere_oppfolgingssaker_i_gsak_men_ingen_i_arena() {
        Sak sak1 = new Sak(0, null, null, null, null);
        Sak sak2 = new Sak(0, null, null, null, null);

        when(sakClient.hentOppfolgingssaker(bruker.getAktorId())).thenReturn(Arrays.asList(sak1, sak2));
        when(arenaClient.oppfolgingssak(bruker.getFnr())).thenReturn(Optional.empty());

        oppfolgingssakService.hentOppfolgingssak(bruker);

        verify(sakClient, times(1)).hentOppfolgingssaker(bruker.getAktorId());
        verify(arenaClient, times(1)).oppfolgingssak(bruker.getFnr());
        verify(sakClient, times(0)).opprettOppfolgingssak(any(), any());
    }

    @Test
    public void hentOppfolgingssak__oppretter_sak_i_gsak_dersom_flere_oppfolgingssaker_i_gsak_men_ikke_samme_som_i_arena() {
        Sak forventetSak = new Sak(0, null, null, "forventetFagsakNr", null);
        Sak uforventetSak1 = new Sak(0, null, null, "uforventetFagsakNr1", null);
        Sak uforventetSak2 = new Sak(0, null, null, "uforventetFagsakNr2", null);

        when(sakClient.hentOppfolgingssaker(bruker.getAktorId())).thenReturn(Arrays.asList(uforventetSak1, uforventetSak2));
        when(arenaClient.oppfolgingssak(bruker.getFnr())).thenReturn(
                Optional.of(new ArenaOppfolgingssak(forventetSak.fagsakNr()))
        );
        when(sakClient.opprettOppfolgingssak(bruker.getAktorId(), forventetSak.fagsakNr())).thenReturn(forventetSak);

        assertThat(oppfolgingssakService.hentOppfolgingssak(bruker)).isEqualTo(forventetSak);

        verify(sakClient, times(1)).hentOppfolgingssaker(bruker.getAktorId());
        verify(arenaClient, times(1)).oppfolgingssak(bruker.getFnr());
        verify(sakClient, times(1)).opprettOppfolgingssak(bruker.getAktorId(), forventetSak.fagsakNr());
    }
}
