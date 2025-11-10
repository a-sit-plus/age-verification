package at.asitplus.wallet.ageverification

import at.asitplus.wallet.lib.LibraryInitializer
import kotlinx.serialization.builtins.serializer
import kotlin.time.Instant

/**
 * Age verification scheme according to
 * [AV profile](https://ageverification.dev/Technical%20Specification/annexes/annex-A/annex-A-av-profile)
 * from 2025-11-10.
 */
object Initializer {

    /**
     * A reference to this class is enough to trigger the init block
     */
    init {
        initWithVCK()
    }

    /**
     * This has to be called first, before anything first, to load the
     * relevant classes of this library into the base implementations of VC-K
     */
    fun initWithVCK() {
        LibraryInitializer.registerExtensionLibrary(
            credentialScheme = AgeVerificationScheme,
            jsonValueEncoder = { null },
            itemValueSerializerMap = mapOf(
                AgeVerificationScheme.Attributes.AGE_OVER_12 to Boolean.serializer(),
                AgeVerificationScheme.Attributes.AGE_OVER_13 to Boolean.serializer(),
                AgeVerificationScheme.Attributes.AGE_OVER_14 to Boolean.serializer(),
                AgeVerificationScheme.Attributes.AGE_OVER_16 to Boolean.serializer(),
                AgeVerificationScheme.Attributes.AGE_OVER_18 to Boolean.serializer(),
                AgeVerificationScheme.Attributes.AGE_OVER_21 to Boolean.serializer(),
                AgeVerificationScheme.Attributes.AGE_OVER_25 to Boolean.serializer(),
                AgeVerificationScheme.Attributes.AGE_OVER_60 to Boolean.serializer(),
                AgeVerificationScheme.Attributes.AGE_OVER_62 to Boolean.serializer(),
                AgeVerificationScheme.Attributes.AGE_OVER_65 to Boolean.serializer(),
                AgeVerificationScheme.Attributes.AGE_OVER_68 to Boolean.serializer(),
            )
        )
    }

}
