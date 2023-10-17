package adjustablesmods.hullmods

import com.fs.starfarer.api.combat.BaseHullMod
import com.fs.starfarer.api.combat.MutableShipStatsAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc

class ASMFeatureCreep : BaseHullMod() {
    override fun applyEffectsBeforeShipCreation(hullSize: ShipAPI.HullSize, stats: MutableShipStatsAPI, id: String) {
        stats.dynamic.getMod(Stats.MAX_PERMANENT_HULLMODS_MOD).modifyFlat(id, 1f)
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
        tooltip.addPara("Increases the ship's max s-mod limit by %s", oPad, good, "1")
        tooltip.addPara("The default limit and other modifiers is not included", pad)
        tooltip.addPara("Max number of hull mods that can be s-modded is %s", pad, b, "${Misc.getMaxPermanentMods(ship)}")
        tooltip.setBulletedListMode(null)
    }
}