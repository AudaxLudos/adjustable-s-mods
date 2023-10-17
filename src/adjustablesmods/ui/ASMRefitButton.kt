package adjustablesmods.ui

import adjustablesmods.*
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.combat.ShipVariantAPI
import com.fs.starfarer.api.fleet.FleetMemberAPI
import com.fs.starfarer.api.loading.HullModSpecAPI
import com.fs.starfarer.api.ui.Alignment
import com.fs.starfarer.api.ui.CustomPanelAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import lunalib.lunaExtensions.addLunaElement
import lunalib.lunaExtensions.addLunaSpriteElement
import lunalib.lunaRefit.BaseRefitButton
import lunalib.lunaUI.elements.LunaSpriteElement
import kotlin.math.roundToInt

class ASMRefitButton : BaseRefitButton() {
    private var backgroundPanel: CustomPanelAPI? = null
    private var mainPanel: CustomPanelAPI? = null
    private var selectedSMod: HullModSpecAPI? = null

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
        this.backgroundPanel = backgroundPanel
        refreshPanel(member, variant)
    }

    private fun refreshPanel(member: FleetMemberAPI?, variant: ShipVariantAPI?) {
        if (backgroundPanel == null) return
        if (mainPanel != null) backgroundPanel!!.removeComponent(mainPanel)

        val width = getPanelWidth(member, variant)
        val height = getPanelHeight(member, variant)

        mainPanel = backgroundPanel!!.createCustomPanel(width, height, null)
        backgroundPanel!!.addComponent(mainPanel)
        mainPanel!!.position.inTL(0f, 0f)

        val headerElement = mainPanel!!.createUIElement(width, 10f, false)
        mainPanel!!.addUIElement(headerElement)
        headerElement!!.position.inTL(0f, 0f)
        headerElement.addSectionHeading("Installed S-Mods", Alignment.MID, 0f)
        headerElement.position.inTL(0f, 0f)

        val sModsPanel = mainPanel!!.createCustomPanel(width, 305f, null)
        mainPanel!!.addComponent(sModsPanel)
        sModsPanel.position.inTL(0f, 18f)

        val sModsElement = sModsPanel.createUIElement(width, 305f, true)
        sModsElement.addSpacer(5f)

        var sMods =
            Global.getSettings().allHullModSpecs.filter { it.id in variant!!.sMods || it.id in variant.sModdedBuiltIns }
        sMods = sMods.sortedBy { it.displayName }

        for (sMod in sMods) {
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

                        selectedSMod = if (selectedSMod == sMod) null else sMod
                        refreshPanel(member, variant)
                    }
                }

                var textColor = Misc.getTextColor()
                if (selectedSMod != null && selectedSMod == sMod) textColor = Misc.getNegativeHighlightColor()
                innerElement.addPara(sMod.displayName, 0f, textColor, textColor).apply {
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

                    selectedSMod = if (selectedSMod == sMod) null else sMod
                    refreshPanel(member, variant)
                }
            }
        }
        sModsElement.addSpacer(5f)
        sModsPanel.addUIElement(sModsElement)

        val footerPanel = mainPanel!!.createCustomPanel(width, 200f, null)
        mainPanel!!.addComponent(footerPanel)
        footerPanel.position.belowMid(sModsPanel, 0f)

        val footerElement = footerPanel.createUIElement(width, 200f, true)
        footerElement.addSectionHeading("Options", Alignment.MID, 10f).apply {
            position.setSize(position.width + 10f, position.height)
        }
        footerElement.addSpacer(5f)

        val removeSModButton = footerElement.addLunaElement(195f, 50f).apply {
            renderBorder = false
            renderBackground = true
            enableTransparency = true
            backgroundAlpha = if (selectedSMod != null) 0.3f else 0.2f
            backgroundColor = Misc.getNegativeHighlightColor()
            addText("Remove Installed S-Mod", Misc.getNegativeHighlightColor())
            centerText()

            onHoverEnter {
                playSound("ui_button_mouseover", 1f, 1f)
                if (selectedSMod != null)
                    backgroundAlpha = 0.5f
            }

            onHoverExit {
                backgroundAlpha = if (selectedSMod != null) 0.3f else 0.2f
            }

            onClick {
                if (!it.isLMBEvent) return@onClick
                if (selectedSMod == null) {
                    playSound("ui_button_disabled_pressed", 1f, 1f)
                    return@onClick
                }

                playSound("ui_char_decrease_skill", 1f, 1f)
                removeInstalledSMod(variant, selectedSMod!!.id)
                selectedSMod = null
                refreshVariant()
                refreshButtonList()
                refreshPanel(member, variant)
            }
        }

        val increaseMaxSModButton = footerElement.addLunaElement(195f, 50f).apply {
            renderBorder = false
            renderBackground = true
            enableTransparency = true
            backgroundAlpha = if (canIncreaseMaxSModLimit(member)) 0.3f else 0.2f
            backgroundColor = Misc.getStoryOptionColor()
            addText("Increase Max S-Mod Limit (%s)",
                Misc.getStoryOptionColor(),
                Misc.getStoryOptionColor(),
                listOf("${getStoryPointCost(member).roundToInt()}")
            ).apply {
                centerText()
            }

            onHoverEnter {
                playSound("ui_button_mouseover", 1f, 1f)
                if (canIncreaseMaxSModLimit(member))
                    backgroundAlpha = 0.5f
            }

            onHoverExit {
                backgroundAlpha = if (canIncreaseMaxSModLimit(member)) 0.3f else 0.2f
            }

            onClick {
                if (!it.isLMBEvent) return@onClick
                if (!canIncreaseMaxSModLimit(member)) {
                    playSound("ui_button_disabled_pressed", 1f, 1f)
                    return@onClick
                }

                playSound("ui_char_spent_story_point", 1f, 1f)
                incrementMaxSModLimit(member)
                installSModTracker(variant)
                refreshVariant()
                refreshButtonList()
                refreshPanel(member, variant)
            }
        }
        increaseMaxSModButton.position.rightOfMid(removeSModButton.elementPanel, 0f)
        footerElement.addSpacer(5f)
        footerPanel.addUIElement(footerElement)
    }

    override fun shouldShow(member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?): Boolean {
        return true
    }
}