package adjustablesmods

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CoreUITabId
import com.fs.starfarer.api.combat.ShipVariantAPI
import com.fs.starfarer.api.fleet.FleetMemberAPI
import kotlin.math.pow

class ShipData(fleetMemberId: String) {
    var id: String = fleetMemberId
    var maxSModModifier: Float = 0f
}

var SHIP_DATA_LIST = mutableListOf<ShipData?>()

fun loadData() {
    if (!Global.getSector().persistentData.containsKey("ASMShipDataList")) {
        Global.getSector().persistentData["ASMShipDataList"] = SHIP_DATA_LIST
    } else {
        @Suppress("UNCHECKED_CAST")
        SHIP_DATA_LIST = Global.getSector().persistentData["ASMShipDataList"] as MutableList<ShipData?>
    }
}

fun getShipData(fleetMember: FleetMemberAPI?): ShipData? {
    var data = SHIP_DATA_LIST.find { it?.id == fleetMember?.id }
    if (data == null) {
        data = fleetMember?.id?.let { ShipData(it) }
        if (data == null) return null
        SHIP_DATA_LIST.add(data)
    }
    return data
}

fun getStoryPointCost(fleetMember: FleetMemberAPI?): Float {
    val data = getShipData(fleetMember!!)
    return 2f.pow(data!!.maxSModModifier + 1f)
}

fun incrementMaxSModLimit(fleetMember: FleetMemberAPI?) {
    val data = getShipData(fleetMember!!)
    Global.getSector().playerStats.spendStoryPoints(
        getStoryPointCost(fleetMember).toInt(),
        true,
        null,
        true,
        0f,
        null
    )
    data!!.maxSModModifier++
}

fun canIncreaseMaxSModLimit(fleetMember: FleetMemberAPI?): Boolean {
    return Global.getSector().playerStats.storyPoints >= getStoryPointCost(fleetMember!!)
}

fun installSModTracker(variant: ShipVariantAPI?) {
    if (!variant!!.hasHullMod("ASMFeatureCreep"))
        variant.addPermaMod("ASMFeatureCreep", false)
}

fun removeInstalledSMod(variant: ShipVariantAPI?, hullModId: String) {
    if (variant!!.sModdedBuiltIns.contains(hullModId)) {
        variant.sModdedBuiltIns.remove(hullModId)
    } else {
        variant.removePermaMod(hullModId)
    }
}