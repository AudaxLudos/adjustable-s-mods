package adjustablesmods.hullmods

import adjustablesmods.getShipData
import com.fs.starfarer.api.combat.BaseHullMod
import com.fs.starfarer.api.combat.MutableShipStatsAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.fleet.FleetMemberAPI
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import kotlin.math.roundToInt

class FeatureCreep : BaseHullMod() {
    override fun applyEffectsBeforeShipCreation(hullSize: ShipAPI.HullSize, stats: MutableShipStatsAPI, id: String) {
        stats.dynamic.getMod(Stats.MAX_PERMANENT_HULLMODS_MOD).modifyFlat(id, getMaxSModModifier(stats.fleetMember))
    }

    override fun addPostDescriptionSection(
        tooltip: TooltipMakerAPI,
        hullSize: ShipAPI.HullSize,
        ship: ShipAPI,
        width: Float,
        isForModSpec: Boolean
    ) {
        val pad = 3f
        val oPad = 10f
        val good = Misc.getPositiveHighlightColor()
        val b = Misc.getHighlightColor()

        tooltip.setBulletedListMode(" - ")
        tooltip.addPara(
            "Increases the ship's max s-mod limit by %s",
            oPad,
            good,
            "${getMaxSModModifier(ship.fleetMember).roundToInt()}"
        )
        tooltip.addPara("The default limit and other modifiers is not included", pad)
        tooltip.addPara(
            "Max number of hull mods that can be s-modded is %s",
            pad,
            b,
            "${Misc.getMaxPermanentMods(ship)}"
        )
        tooltip.setBulletedListMode(null)
    }

    private fun getMaxSModModifier(fleetMember: FleetMemberAPI?): Float {
        val asmShipData = getShipData(fleetMember)

        val tag = fleetMember?.variant?.tags?.firstOrNull { it.contains("asm_slot_id_") }
        if (tag != null) {
            val slotId = tag.replace("asm_slot_id_", "")
            val moduleData = asmShipData?.modules?.firstOrNull { it.moduleSlot == slotId }
            return moduleData?.maxSModModifier ?: 0f
        }
        return asmShipData?.maxSModModifier ?: 0f
    }
}