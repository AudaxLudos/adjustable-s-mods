package adjustablesmods

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.ShipHullSpecAPI
import com.fs.starfarer.api.combat.ShipVariantAPI
import com.fs.starfarer.api.fleet.FleetMemberAPI
import com.fs.starfarer.api.impl.campaign.ids.HullMods
import kotlin.math.pow


class ShipData(fleetMemberId: String) {
    var id: String = fleetMemberId
    var maxSModModifier: Float = 0f
    var modules: MutableList<ModuleData> = mutableListOf()
}

class ModuleData(moduleSlotId: String) {
    var moduleSlot: String = moduleSlotId
    var maxSModModifier: Float = 0f
}

var SHIP_DATA_LIST = mutableListOf<ShipData?>()

fun loadData() {
    if (!Global.getSector().persistentData.containsKey("ASMShipDataList")) {
        Global.getSector().persistentData["ASMShipDataList"] = SHIP_DATA_LIST
    } else {
        SHIP_DATA_LIST = Global.getSector().persistentData["ASMShipDataList"] as MutableList<ShipData?>
    }
}

fun getShipData(fleetMember: FleetMemberAPI?): ShipData? {
    var fm = getFleetMemberFromTag(fleetMember)
    if (fm == null) fm = fleetMember
    var data = SHIP_DATA_LIST.find { it?.id == fm?.id }
    if (data == null) {
        data = fm?.id?.let { ShipData(it) }
        if (data == null) return null

        data.modules.addAll(createModuleDataList(fm))
        SHIP_DATA_LIST.add(data)
    }
    return data
}

fun getFleetMemberFromTag(fleetMember: FleetMemberAPI?): FleetMemberAPI? {
    val tag = fleetMember?.variant?.tags?.find { it.contains("asm_id_") }
    val id = tag?.replace("asm_id_", "")
    return Global.getSector().playerFleet.fleetData.membersListCopy.find { it.id == id }
}

fun createModuleDataList(fleetMember: FleetMemberAPI?): MutableList<ModuleData> {
    val moduleDataList: MutableList<ModuleData> = mutableListOf()
    val shipVariant = fleetMember?.variant
    if (shipVariant?.moduleSlots != null) {
        for (i in shipVariant.moduleSlots.indices) {
            val moduleVariant: ShipVariantAPI = shipVariant.getModuleVariant(shipVariant.moduleSlots[i])
            if (moduleVariant.hullSpec.getOrdnancePoints(null) <= 0)
                continue

            moduleDataList.add(ModuleData(shipVariant.moduleSlots[i]))
        }
    }

    return moduleDataList
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

fun isVariantModule(variant: ShipVariantAPI?): Boolean {
    return (variant!!.hints.contains(ShipHullSpecAPI.ShipTypeHints.UNBOARDABLE) &&
            variant.hints.contains(ShipHullSpecAPI.ShipTypeHints.HIDE_IN_CODEX) &&
            !variant.hasHullMod(HullMods.AUTOMATED))
}