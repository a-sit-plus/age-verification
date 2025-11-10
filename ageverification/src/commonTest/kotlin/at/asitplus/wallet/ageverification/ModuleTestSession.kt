package at.asitplus.wallet.ageverification

import de.infix.testBalloon.framework.TestSession

class ModuleTestSession : TestSession() {
    init {
        Initializer.initWithVCK()
    }
}
