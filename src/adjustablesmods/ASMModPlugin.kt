package adjustablesmods

import com.fs.starfarer.api.BaseModPlugin
import java.lang.RuntimeException

class ASMModPlugin : BaseModPlugin() {
    override fun onApplicationLoad() {
        super.onApplicationLoad()

        throw RuntimeException("Adjustable S-Mods Mod Loaded")
    }
}