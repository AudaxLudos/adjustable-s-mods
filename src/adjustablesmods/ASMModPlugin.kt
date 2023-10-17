package adjustablesmods

import adjustablesmods.ui.ASMRefitButton
import com.fs.starfarer.api.BaseModPlugin
import lunalib.lunaRefit.LunaRefitManager

class ASMModPlugin : BaseModPlugin() {
    override fun onApplicationLoad() {
        super.onApplicationLoad()

        LunaRefitManager.addRefitButton(ASMRefitButton())
    }
}