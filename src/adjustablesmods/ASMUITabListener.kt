package adjustablesmods

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CoreUITabId
import com.fs.starfarer.api.campaign.listeners.CoreUITabListener
import com.fs.starfarer.api.fleet.FleetMemberAPI


class ASMUITabListener : CoreUITabListener {
    override fun reportAboutToOpenCoreTab(tab: CoreUITabId?, param: Any?) {
        val fleetMembers = Global.getSector().playerFleet.fleetData.membersListCopy
        for (m in fleetMembers) {
            tagVariantsWithOP(m)
        }
    }

    private fun tagVariantsWithOP(fleetMember: FleetMemberAPI) {
        val shipVariant = fleetMember.variant
        if (!shipVariant.hasTag("asm_" + fleetMember.id))
            shipVariant.addTag("asm_" + fleetMember.id)
        if (shipVariant.moduleSlots != null) {
            for (i in shipVariant.moduleSlots.indices) {
                val moduleVariant = shipVariant.getModuleVariant(shipVariant.moduleSlots[i])
                if (!moduleVariant.hasTag("asm_" + fleetMember.id))
                    moduleVariant.addTag("asm_" + fleetMember.id)
            }
        }
    }
}