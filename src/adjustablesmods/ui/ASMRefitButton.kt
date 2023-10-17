package adjustablesmods.ui

import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.combat.ShipVariantAPI
import com.fs.starfarer.api.fleet.FleetMemberAPI
import com.fs.starfarer.api.ui.Alignment
import com.fs.starfarer.api.ui.CustomPanelAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import lunalib.lunaRefit.BaseRefitButton

class ASMRefitButton : BaseRefitButton() {
    override fun getButtonName(member: FleetMemberAPI?, variant: ShipVariantAPI?): String {
        return "Adjust S-Mods"
    }

    override fun getIconName(member: FleetMemberAPI?, variant: ShipVariantAPI?): String {
        return "graphics/icons/skills/technology.png"
    }

    override fun getOrder(member: FleetMemberAPI?, variant: ShipVariantAPI?): Int {
        return 9999
    }

    override fun hasTooltip(member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?): Boolean {
        return true
    }

    override fun getPanelWidth(member: FleetMemberAPI?, variant: ShipVariantAPI?): Float {
        return 400f
    }

    override fun getPanelHeight(member: FleetMemberAPI?, variant: ShipVariantAPI?): Float {
        return 400f
    }

    override fun addTooltip(
        tooltip: TooltipMakerAPI,
        member: FleetMemberAPI?,
        variant: ShipVariantAPI?,
        market: MarketAPI?
    ) {
        tooltip.addPara("Options to remove s-mods or to increase the max s-mod limit of a ship", 0f)
    }

    override fun hasPanel(member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?): Boolean {
        return true
    }

    override fun initPanel(
        backgroundPanel: CustomPanelAPI?,
        member: FleetMemberAPI?,
        variant: ShipVariantAPI?,
        market: MarketAPI?
    ) {
        val width = getPanelWidth(member, variant)
        val height = getPanelHeight(member, variant)

        val mainPanel = backgroundPanel!!.createCustomPanel(width, height, null)
        backgroundPanel.addComponent(mainPanel)
        mainPanel!!.position.inTL(0f, 0f)

        val headerElement = mainPanel.createUIElement(width, 10f, false)
        mainPanel.addUIElement(headerElement)
        headerElement!!.position.inTL(0f, 0f)
        headerElement.addSectionHeading("Installed S-Mods", Alignment.MID, 0f)
        headerElement.position.inTL(0f, 0f)
    }

    override fun shouldShow(member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?): Boolean {
        return true
    }
}