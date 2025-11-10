package at.asitplus.wallet.ageverification

import at.asitplus.wallet.lib.data.ConstantIndex
import at.asitplus.wallet.lib.data.ConstantIndex.CredentialRepresentation.*


/**
 * Age verification scheme according to
 * [AV profile](https://ageverification.dev/Technical%20Specification/annexes/annex-A/annex-A-av-profile)
 * from 2025-11-10.
 */
object AgeVerificationScheme : ConstantIndex.CredentialScheme {
    override val schemaUri: String = "https://wallet.a-sit.at/schemas/1.0.0/ageverification.json"
    override val isoNamespace: String = "eu.europa.ec.av.1"
    override val isoDocType: String = "eu.europa.ec.av.1"
    override val supportedRepresentations: Collection<ConstantIndex.CredentialRepresentation> =
        listOf(ISO_MDOC)

    override val claimNames: Collection<String> = listOf(
        Attributes.AGE_OVER_12,
        Attributes.AGE_OVER_13,
        Attributes.AGE_OVER_14,
        Attributes.AGE_OVER_16,
        Attributes.AGE_OVER_18,
        Attributes.AGE_OVER_21,
        Attributes.AGE_OVER_25,
        Attributes.AGE_OVER_60,
        Attributes.AGE_OVER_62,
        Attributes.AGE_OVER_65,
        Attributes.AGE_OVER_68,
    )

    val requiredClaimNames: Collection<String> = listOf(
        Attributes.AGE_OVER_18,
    )

    object Attributes {
        /** Additional current age attestations: Attesting whether the user is currently over 12 years old. */
        const val AGE_OVER_12 = "age_over_12"

        /** Additional current age attestations: Attesting whether the user is currently over 13 years old. */
        const val AGE_OVER_13 = "age_over_13"

        /** Additional current age attestations: Attesting whether the user is currently over 14 years old. */
        const val AGE_OVER_14 = "age_over_14"

        /** Additional current age attestations: Attesting whether the user is currently over 16 years old. */
        const val AGE_OVER_16 = "age_over_16"

        /** Attesting whether the User to whom the data relates is currently an adult (true) or a minor (false). */
        const val AGE_OVER_18 = "age_over_18"

        /** Additional current age attestations: Attesting whether the user is currently over 21 years old. */
        const val AGE_OVER_21 = "age_over_21"

        /** Additional current age attestations: Attesting whether the user is currently over 25 years old. */
        const val AGE_OVER_25 = "age_over_25"

        /** Additional current age attestations: Attesting whether the user is currently over 60 years old. */
        const val AGE_OVER_60 = "age_over_60"

        /** Additional current age attestations: Attesting whether the user is currently over 62 years old. */
        const val AGE_OVER_62 = "age_over_62"

        /** Additional current age attestations: Attesting whether the user is currently over 65 years old. */
        const val AGE_OVER_65 = "age_over_65"

        /** Additional current age attestations: Attesting whether the user is currently over 68 years old. */
        const val AGE_OVER_68 = "age_over_68"

    }

}
