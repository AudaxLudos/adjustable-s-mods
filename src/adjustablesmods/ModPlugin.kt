package adjustablesmods

import adjustablesmods.ui.RefitButton
import com.fs.starfarer.api.BaseModPlugin
import lunalib.lunaRefit.LunaRefitManager

class ModPlugin : BaseModPlugin() {
    override fun onApplicationLoad() {
        LunaRefitManager.addRefitButton(RefitButton())
    }

    override fun onGameLoad(newGame: Boolean) {
        loadData()
        UITabListener.register()
    }
}