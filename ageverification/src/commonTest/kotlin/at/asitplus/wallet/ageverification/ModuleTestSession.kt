package at.asitplus.wallet.ageverification

import de.infix.testBalloon.framework.core.TestSession

class ModuleTestSession : TestSession() {
    init {
        Initializer.initWithVCK()
    }
}
