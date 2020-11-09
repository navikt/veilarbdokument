package no.nav.fo.veilarb.dokument.localtestapp.stub

import no.nav.fo.veilarb.dokument.client.api.BrevClient

class BrevClientStub : BrevClient {
    override fun genererBrev(brevdataPdf: BrevClient.Brevdata): ByteArray {
         return ByteArray(1)
    }
}
