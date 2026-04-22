package at.asitplus.wallet.ageverification

import at.asitplus.iso.DeviceKeyInfo
import at.asitplus.iso.IssuerSigned
import at.asitplus.iso.IssuerSignedItem
import at.asitplus.iso.IssuerSignedItemSerializer
import at.asitplus.iso.MobileSecurityObject
import at.asitplus.iso.ValidityInfo
import at.asitplus.iso.ValueDigest
import at.asitplus.iso.ValueDigestList
import at.asitplus.signum.indispensable.CryptoSignature
import at.asitplus.signum.indispensable.cosef.CoseEllipticCurve
import at.asitplus.signum.indispensable.cosef.CoseHeader
import at.asitplus.signum.indispensable.cosef.CoseKey
import at.asitplus.signum.indispensable.cosef.CoseKeyParams
import at.asitplus.signum.indispensable.cosef.CoseKeyType
import at.asitplus.signum.indispensable.cosef.CoseSigned
import at.asitplus.signum.indispensable.cosef.io.coseCompliantSerializer
import at.asitplus.testballoon.invoke
import at.asitplus.wallet.ageverification.AgeVerificationScheme.Attributes.AGE_OVER_12
import at.asitplus.wallet.ageverification.AgeVerificationScheme.Attributes.AGE_OVER_13
import at.asitplus.wallet.ageverification.AgeVerificationScheme.Attributes.AGE_OVER_14
import at.asitplus.wallet.ageverification.AgeVerificationScheme.Attributes.AGE_OVER_16
import at.asitplus.wallet.ageverification.AgeVerificationScheme.Attributes.AGE_OVER_18
import at.asitplus.wallet.ageverification.AgeVerificationScheme.Attributes.AGE_OVER_21
import at.asitplus.wallet.ageverification.AgeVerificationScheme.Attributes.AGE_OVER_25
import at.asitplus.wallet.ageverification.AgeVerificationScheme.Attributes.AGE_OVER_60
import at.asitplus.wallet.ageverification.AgeVerificationScheme.Attributes.AGE_OVER_62
import at.asitplus.wallet.ageverification.AgeVerificationScheme.Attributes.AGE_OVER_65
import at.asitplus.wallet.ageverification.AgeVerificationScheme.Attributes.AGE_OVER_68
import at.asitplus.wallet.lib.agent.SubjectCredentialStore
import at.asitplus.wallet.lib.data.CredentialToJsonConverter
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.assertions.withClue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.json.JsonObject
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.time.Clock


val IsoSerializationTest by testSuite {

    test("Serialization and deserialization") {
        dataMap().entries.forEach {
            withClue("key=${it.key}") {
                val item = it.toIssuerSignedItem()

                val serialized = item.serialize(AgeVerificationScheme.isoNamespace)

                IssuerSignedItem.deserialize(serialized, AgeVerificationScheme.isoNamespace, item.elementIdentifier)
                    .apply {
                        elementValue shouldBe it.value
                    }
            }
        }
    }

    test("Serialization to JSON Element") {
        val mso = MobileSecurityObject(
            version = "1.0",
            digestAlgorithm = "SHA-256",
            valueDigests = mapOf("foo" to ValueDigestList(listOf(ValueDigest(0U, byteArrayOf())))),
            deviceKeyInfo = deviceKeyInfo(),
            docType = "docType",
            validityInfo = ValidityInfo(Clock.System.now(), Clock.System.now(), Clock.System.now())
        )
        val claims = dataMap()
        val namespacedItems: Map<String, List<IssuerSignedItem>> =
            mapOf(AgeVerificationScheme.isoNamespace to claims.map { it.toIssuerSignedItem() }.toList())
        val issuerAuth = CoseSigned.create(
            CoseHeader(), null, mso, CryptoSignature.RSA(byteArrayOf(1, 3, 3, 7)),
            MobileSecurityObject.serializer()
        )
        val credential = SubjectCredentialStore.StoreEntry.Iso(
            IssuerSigned.fromIssuerSignedItems(namespacedItems, issuerAuth),
            AgeVerificationScheme.isoNamespace
        )
        val converted = CredentialToJsonConverter.toJsonElement(credential)
            .shouldBeInstanceOf<JsonObject>()
            .also { println(it) }
        val jsonMap = converted[AgeVerificationScheme.isoNamespace]
            .shouldBeInstanceOf<JsonObject>()

        claims.forEach {
            withClue("Serialization for ${it.key}") {
                jsonMap[it.key].shouldNotBeNull()
            }
        }
    }
}

private fun Map.Entry<String, Any>.toIssuerSignedItem() =
    IssuerSignedItem(Random.nextUInt(), Random.nextBytes(32), key, value)

@Suppress("DEPRECATION")
private fun dataMap(): Map<String, Any> = mapOf(
    AGE_OVER_12 to Random.nextBoolean(),
    AGE_OVER_13 to Random.nextBoolean(),
    AGE_OVER_14 to Random.nextBoolean(),
    AGE_OVER_16 to Random.nextBoolean(),
    AGE_OVER_18 to Random.nextBoolean(),
    AGE_OVER_21 to Random.nextBoolean(),
    AGE_OVER_25 to Random.nextBoolean(),
    AGE_OVER_60 to Random.nextBoolean(),
    AGE_OVER_62 to Random.nextBoolean(),
    AGE_OVER_65 to Random.nextBoolean(),
    AGE_OVER_68 to Random.nextBoolean(),
)

private fun deviceKeyInfo() =
    DeviceKeyInfo(CoseKey(CoseKeyType.EC2, keyParams = CoseKeyParams.EcYBoolParams(CoseEllipticCurve.P256)))

private fun IssuerSignedItem.serialize(namespace: String): ByteArray =
    coseCompliantSerializer.encodeToByteArray(IssuerSignedItemSerializer(namespace, elementIdentifier), this)

private fun IssuerSignedItem.Companion.deserialize(
    serialized: ByteArray,
    namespace: String,
    elementIdentifier: String,
): IssuerSignedItem =
    coseCompliantSerializer.decodeFromByteArray(IssuerSignedItemSerializer(namespace, elementIdentifier), serialized)
