package no.nav.fo.veilarb.dokument.service;

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
    private SakService sakService = mock(SakService.class);
    private ArenaService arenaService = mock(ArenaService.class);
    private OppfolgingssakService oppfolgingssakService = new OppfolgingssakService(sakService, arenaService);

    @Test
    public void hentOppfolgingssak__henter_sak_fra_gsak_dersom_en_oppfolgingssak() {
        Sak forventetSak = new Sak(0, null, null, null, null);

        when(sakService.finnOppfolgingssaker(bruker.getAktorId())).thenReturn(Collections.singletonList(forventetSak));

        assertThat(oppfolgingssakService.hentOppfolgingssak(bruker)).isEqualTo(forventetSak);

        verify(sakService, times(1)).finnOppfolgingssaker(bruker.getAktorId());
        verify(arenaService, times(0)).oppfolgingssak(any());
        verify(sakService, times(0)).opprettOppfolgingssak(any(), any());
    }

    @Test
    public void hentOppfolgingssak__henter_sak_fra_arena_og_oppretter_i_gsak_dersom_ingen_oppfolgingssak_i_gsak() {
        Sak forventetSak = new Sak(0, null, null, "fagsakNr", null);

        when(sakService.finnOppfolgingssaker(bruker.getAktorId())).thenReturn(Collections.emptyList());
        when(arenaService.oppfolgingssak(bruker.getFnr())).thenReturn(
                Optional.of(new ArenaOppfolgingssak(forventetSak.fagsakNr()))
        );
        when(sakService.opprettOppfolgingssak(bruker.getAktorId(), forventetSak.fagsakNr())).thenReturn(forventetSak);

        assertThat(oppfolgingssakService.hentOppfolgingssak(bruker)).isEqualTo(forventetSak);

        verify(sakService, times(1)).finnOppfolgingssaker(bruker.getAktorId());
        verify(arenaService, times(1)).oppfolgingssak(bruker.getFnr());
        verify(sakService, times(1)).opprettOppfolgingssak(bruker.getAktorId(), forventetSak.fagsakNr());
    }

    @Test(expected = IllegalStateException.class)
    public void hentOppfolgingssak__feiler_dersom_ingen_oppfolgingssak_verken_i_arena_eller_gsak() {
        when(sakService.finnOppfolgingssaker(bruker.getAktorId())).thenReturn(Collections.emptyList());
        when(arenaService.oppfolgingssak(bruker.getFnr())).thenReturn(Optional.empty());

        oppfolgingssakService.hentOppfolgingssak(bruker);

        verify(sakService, times(1)).finnOppfolgingssaker(bruker.getAktorId());
        verify(arenaService, times(1)).oppfolgingssak(bruker.getFnr());
        verify(sakService, times(0)).opprettOppfolgingssak(any(), any());
    }

    @Test
    public void hentOppfolgingssak__finner_riktig_sak_fra_arena_dersom_flere_oppfolgingssaker_i_gsak() {
        Sak forventetSak = new Sak(0, null, null, "forventetFagsakNr", null);
        Sak uforventetSak = new Sak(0, null, null, "uforventetFagsakNr", null);

        when(sakService.finnOppfolgingssaker(bruker.getAktorId())).thenReturn(Arrays.asList(forventetSak, uforventetSak));
        when(arenaService.oppfolgingssak(bruker.getFnr())).thenReturn(
                Optional.of(new ArenaOppfolgingssak(forventetSak.fagsakNr()))
        );

        assertThat(oppfolgingssakService.hentOppfolgingssak(bruker)).isEqualTo(forventetSak);

        verify(sakService, times(1)).finnOppfolgingssaker(bruker.getAktorId());
        verify(arenaService, times(1)).oppfolgingssak(bruker.getFnr());
        verify(sakService, times(0)).opprettOppfolgingssak(any(), any());
    }

    @Test(expected = IllegalStateException.class)
    public void hentOppfolgingssak__feiler_dersom_flere_oppfolgingssaker_i_gsak_men_ingen_i_arena() {
        Sak sak1 = new Sak(0, null, null, null, null);
        Sak sak2 = new Sak(0, null, null, null, null);

        when(sakService.finnOppfolgingssaker(bruker.getAktorId())).thenReturn(Arrays.asList(sak1, sak2));
        when(arenaService.oppfolgingssak(bruker.getFnr())).thenReturn(Optional.empty());

        oppfolgingssakService.hentOppfolgingssak(bruker);

        verify(sakService, times(1)).finnOppfolgingssaker(bruker.getAktorId());
        verify(arenaService, times(1)).oppfolgingssak(bruker.getFnr());
        verify(sakService, times(0)).opprettOppfolgingssak(any(), any());
    }

    @Test
    public void hentOppfolgingssak__oppretter_sak_i_gsak_dersom_flere_oppfolgingssaker_i_gsak_men_ikke_samme_som_i_arena() {
        Sak forventetSak = new Sak(0, null, null, "forventetFagsakNr", null);
        Sak uforventetSak1 = new Sak(0, null, null, "uforventetFagsakNr1", null);
        Sak uforventetSak2 = new Sak(0, null, null, "uforventetFagsakNr2", null);

        when(sakService.finnOppfolgingssaker(bruker.getAktorId())).thenReturn(Arrays.asList(uforventetSak1, uforventetSak2));
        when(arenaService.oppfolgingssak(bruker.getFnr())).thenReturn(
                Optional.of(new ArenaOppfolgingssak(forventetSak.fagsakNr()))
        );
        when(sakService.opprettOppfolgingssak(bruker.getAktorId(), forventetSak.fagsakNr())).thenReturn(forventetSak);

        assertThat(oppfolgingssakService.hentOppfolgingssak(bruker)).isEqualTo(forventetSak);

        verify(sakService, times(1)).finnOppfolgingssaker(bruker.getAktorId());
        verify(arenaService, times(1)).oppfolgingssak(bruker.getFnr());
        verify(sakService, times(1)).opprettOppfolgingssak(bruker.getAktorId(), forventetSak.fagsakNr());
    }
}
