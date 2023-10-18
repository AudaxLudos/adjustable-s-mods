package adjustablesmods

import adjustablesmods.ui.ASMRefitButton
import com.fs.starfarer.api.BaseModPlugin
import lunalib.lunaRefit.LunaRefitManager

class ASMModPlugin : BaseModPlugin() {
    override fun onApplicationLoad() {
        LunaRefitManager.addRefitButton(ASMRefitButton())
    }

    override fun onGameLoad(newGame: Boolean) {
        loadData()
    }
}