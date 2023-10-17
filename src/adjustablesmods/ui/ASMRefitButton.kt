package adjustablesmods.ui

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.combat.ShipVariantAPI
import com.fs.starfarer.api.fleet.FleetMemberAPI
import com.fs.starfarer.api.ui.Alignment
import com.fs.starfarer.api.ui.CustomPanelAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import lunalib.lunaExtensions.addLunaElement
import lunalib.lunaExtensions.addLunaSpriteElement
import lunalib.lunaRefit.BaseRefitButton
import lunalib.lunaUI.elements.LunaSpriteElement

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

        val sModsPanel = mainPanel.createCustomPanel(width, 305f, null)
        mainPanel.addComponent(sModsPanel)
        sModsPanel.position.inTL(0f, 18f)

        val sModsElement = sModsPanel.createUIElement(width, 305f, true)
        sModsElement.addSpacer(5f)

        var installedSMods =
            Global.getSettings().allHullModSpecs.filter { it.id in variant!!.sMods || it.id in variant.sModdedBuiltIns }
        installedSMods = installedSMods.sortedBy { it.displayName }

        for (sMod in installedSMods) {
            sModsElement.addLunaElement(390f, 40f).apply {
                renderBorder = false
                renderBackground = true
                enableTransparency = true
                backgroundAlpha = 0.3f

                val sprite = innerElement.addLunaSpriteElement(
                    sMod.spriteName,
                    LunaSpriteElement.ScalingTypes.STRETCH_SPRITE,
                    36f,
                    36f
                ).apply {
                    renderBorder = false
                    enableTransparency = true

                    borderAlpha = 0.8f
                    getSprite().alphaMult = 0.8f
                    position.inTL(2f, 2f)

                    onClick {
                        playClickSound()
                    }
                }

                innerElement.addPara(sMod.displayName, 0f).apply {
                    position.rightOfMid(sprite.elementPanel, 10f)
                }

                onHoverEnter {
                    playSound("ui_button_mouseover", 1f, 1f)
                    sprite.getSprite().alphaMult = 1f
                    backgroundAlpha = 1f
                }

                onHoverExit {
                    sprite.getSprite().alphaMult = 0.8f
                    backgroundAlpha = 0.3f
                }

                onClick {
                    playClickSound()
                }
            }
        }
        sModsElement.addSpacer(5f)
        sModsPanel.addUIElement(sModsElement)
    }

    override fun shouldShow(member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?): Boolean {
        return true
    }
}