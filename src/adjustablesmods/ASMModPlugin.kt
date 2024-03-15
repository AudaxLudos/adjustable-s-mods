package adjustablesmods

import adjustablesmods.ui.ASMRefitButton
import com.fs.starfarer.api.BaseModPlugin
import com.fs.starfarer.api.Global
import lunalib.lunaRefit.LunaRefitManager

class ASMModPlugin : BaseModPlugin() {
    override fun onApplicationLoad() {
        LunaRefitManager.addRefitButton(ASMRefitButton())
    }

    override fun onGameLoad(newGame: Boolean) {
        loadData()
        val listeners = Global.getSector().listenerManager
        if (!listeners.hasListener(ASMUITabListener()))
            listeners.addListener(ASMUITabListener())
    }
}