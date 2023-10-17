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

        // UI colors
        val gray = Misc.getTextColor()
        val darkGray = Misc.getGrayColor()
        val green = Misc.getStoryOptionColor()
        val darkGreen = Misc.getStoryDarkColor()
        val red = Misc.getNegativeHighlightColor()
        val darkRed = Misc.setAlpha(Misc.scaleColorOnly(Misc.getNegativeHighlightColor(), 0.4f), 175)
        val yellow = Misc.getHighlightColor()
        val cyan = Misc.getBasePlayerColor()

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

        if (sMods.isEmpty()) {
            sModsElement.addLunaElement(sModsElement.widthSoFar, 290f).apply {
                renderBorder = false
                renderBackground = false
                addText("Their are no Installed S-Mods found")
                centerText()
            }
        }

        // Generate list of s-mods
        for (sMod in sMods) {
            // Add clickable s-mod button
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

                val textColor = if (selectedSMod != null && selectedSMod == sMod) red else gray
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

            // Add tooltip to s-mod
            sModsElement!!.addTooltipToPrevious(object : TooltipMakerAPI.TooltipCreator {
                override fun isTooltipExpandable(tooltipParam: Any?): Boolean {
                    return false
                }

                override fun getTooltipWidth(tooltipParam: Any?): Float {
                    return 380f
                }

                override fun createTooltip(tooltip: TooltipMakerAPI?, expanded: Boolean, tooltipParam: Any?) {
                    tooltip!!.addPara(sMod.displayName, cyan, 0f)
                    tooltip.addSpacer(10f)

                    if (sMod.manufacturer != "Common") {
                        tooltip.addPara(
                            "Design Type: %s",
                            0f,
                            darkGray,
                            Misc.getDesignTypeColor(sMod.manufacturer),
                            sMod.manufacturer
                        )
                        tooltip.addSpacer(10f)
                    }

                    val sModParams = arrayOf(
                        sMod.effect.getDescriptionParam(0, variant!!.hullSize),
                        sMod.effect.getDescriptionParam(1, variant.hullSize),
                        sMod.effect.getDescriptionParam(2, variant.hullSize),
                        sMod.effect.getDescriptionParam(3, variant.hullSize),
                        sMod.effect.getDescriptionParam(4, variant.hullSize),
                        sMod.effect.getDescriptionParam(5, variant.hullSize),
                        sMod.effect.getDescriptionParam(6, variant.hullSize),
                        sMod.effect.getDescriptionParam(7, variant.hullSize),
                        sMod.effect.getDescriptionParam(8, variant.hullSize),
                        sMod.effect.getDescriptionParam(9, variant.hullSize)
                    )

                    tooltip.addPara(sMod.descriptionFormat, 0f, gray, yellow, *sModParams)

                    sMod.effect.addPostDescriptionSection(
                        tooltip,
                        variant.hullSize,
                        null,
                        getTooltipWidth(tooltip),
                        false
                    )

                    if (sMod.effect.hasSModEffect()) {
                        tooltip.addSpacer(10f)
                        if (!sMod.effect.isSModEffectAPenalty) {
                            tooltip.addSectionHeading("S-mod bonus", green, darkGreen, Alignment.MID, 0f)
                        } else {
                            tooltip.addSectionHeading("S-mod penalty", red, darkRed, Alignment.MID, 0f)
                        }
                        sMod.effect.addSModEffectSection(
                            tooltip,
                            variant.hullSize,
                            null,
                            getTooltipWidth(tooltip),
                            false,
                            false
                        )
                    }
                }
            }, TooltipMakerAPI.TooltipLocation.RIGHT)
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
            backgroundColor = red
            addText("Remove Installed S-Mod", red)
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

        footerElement!!.addTooltipToPrevious(object : TooltipMakerAPI.TooltipCreator {
            override fun isTooltipExpandable(tooltipParam: Any?): Boolean {
                return false
            }

            override fun getTooltipWidth(tooltipParam: Any?): Float {
                return 380f
            }

            override fun createTooltip(tooltip: TooltipMakerAPI?, expanded: Boolean, tooltipParam: Any?) {
                tooltip!!.addPara("%s spent on s-mods is not refunded when removed", 0f, gray, green, "Story points")
            }
        }, TooltipMakerAPI.TooltipLocation.RIGHT)

        val increaseMaxSModButton = footerElement.addLunaElement(195f, 50f).apply {
            renderBorder = false
            renderBackground = true
            enableTransparency = true
            backgroundAlpha = if (canIncreaseMaxSModLimit(member)) 0.3f else 0.2f
            backgroundColor = green
            innerElement.addPara("Increase Max S-Mod Limit", green, 10f).apply { setAlignment(Alignment.MID) }
            innerElement.addPara("(Cost: %s)", 0f, green, green, "${getStoryPointCost(member).roundToInt()}")
                .apply { setAlignment(Alignment.MID) }

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
                selectedSMod = null
                refreshVariant()
                refreshButtonList()
                refreshPanel(member, variant)
            }
        }

        footerElement.addTooltipToPrevious(object : TooltipMakerAPI.TooltipCreator {
            override fun isTooltipExpandable(tooltipParam: Any?): Boolean {
                return false
            }

            override fun getTooltipWidth(tooltipParam: Any?): Float {
                return 380f
            }

            override fun createTooltip(tooltip: TooltipMakerAPI?, expanded: Boolean, tooltipParam: Any?) {
                tooltip!!.addPara(
                    "No bonus experience is gained when spending %s on this upgrade",
                    0f,
                    gray,
                    green,
                    "story points"
                )
                tooltip.addSpacer(10f)
                tooltip.addPara(
                    "Each upgrade doubles the amount of %s required for the next upgrade",
                    0f,
                    gray,
                    green,
                    "story points"
                )
            }
        }, TooltipMakerAPI.TooltipLocation.RIGHT)

        increaseMaxSModButton.position.rightOfMid(removeSModButton.elementPanel, 0f)
        footerElement.addSpacer(5f)
        footerPanel.addUIElement(footerElement)
    }

    override fun shouldShow(member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?): Boolean {
        return true
    }
}