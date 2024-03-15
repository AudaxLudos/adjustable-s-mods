package adjustablesmods.hullmods

import com.fs.starfarer.api.combat.BaseHullMod
import com.fs.starfarer.api.combat.MutableShipStatsAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.fleet.FleetMemberAPI
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc

class FeatureCreep : BaseHullMod() {
    override fun applyEffectsBeforeShipCreation(hullSize: ShipAPI.HullSize, stats: MutableShipStatsAPI, id: String) {
        stats.dynamic.getMod(Stats.MAX_PERMANENT_HULLMODS_MOD)
            .modifyFlat(id, getMaxSModLimit(stats.fleetMember).toFloat())
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
            "${getMaxSModLimit(ship.fleetMember)}"
        )
        tooltip.addPara("The default limit and other modifiers are not included", pad)
        tooltip.addPara(
            "Max number of hull mods that can be s-modded is %s",
            pad,
            b,
            "${Misc.getMaxPermanentMods(ship)}"
        )
        tooltip.setBulletedListMode(null)
    }

    private fun getMaxSModLimit(fleetMember: FleetMemberAPI?): Int {
        val tag = fleetMember?.variant?.tags?.firstOrNull { it.contains("asm_max_smod_limit:") }
        val maxSModLimit = tag?.replace("asm_max_smod_limit:", "")?.toInt() ?: 0
        return maxSModLimit
    }
}