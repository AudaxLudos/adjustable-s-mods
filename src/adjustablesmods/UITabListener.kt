package adjustablesmods

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CoreUITabId
import com.fs.starfarer.api.campaign.listeners.CoreUITabListener
import com.fs.starfarer.api.fleet.FleetMemberAPI


class UITabListener : CoreUITabListener {
    override fun reportAboutToOpenCoreTab(tab: CoreUITabId?, param: Any?) {
        val fleetMembers = Global.getSector().playerFleet.fleetData.membersListCopy
        for (m in fleetMembers) {
            tagVariantsWithOP(m)
        }
    }

    private fun tagVariantsWithOP(fleetMember: FleetMemberAPI) {
        val shipVariant = fleetMember.variant
        if (!shipVariant.hasTag("asm_id_" + fleetMember.id))
            shipVariant.addTag("asm_id_" + fleetMember.id)
        if (shipVariant.moduleSlots != null) {
            for (i in shipVariant.moduleSlots.indices) {
                val moduleVariant = shipVariant.getModuleVariant(shipVariant.moduleSlots[i])
                if (!moduleVariant.hasTag("asm_id_" + fleetMember.id))
                    moduleVariant.addTag("asm_id_" + fleetMember.id)
                if (!moduleVariant.hasTag("asm_slot_id_" + shipVariant.moduleSlots[i]))
                    moduleVariant.addTag("asm_slot_id_" + shipVariant.moduleSlots[i])
            }
        }
    }

    companion object {
        fun register() {
            val listeners = Global.getSector().listenerManager
            if (!listeners.hasListener(UITabListener()))
                listeners.addListener(UITabListener())
        }
    }
}