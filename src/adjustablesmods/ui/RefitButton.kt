package adjustablesmods.ui

import adjustablesmods.ui.tooltips.CustomHullmodToolTip
import adjustablesmods.ui.tooltips.IncreaseSModLimitTooltip
import adjustablesmods.ui.tooltips.RemoveHullmodTooltip
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
import kotlin.math.pow
import kotlin.math.roundToInt

class RefitButton : BaseRefitButton() {
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
        tooltip.addPara("Options to increase the max s-mod limit or to remove installed s-mods on a ship", 0f)
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

        val width = getPanelWidth(member!!, variant!!)
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
            Global.getSettings().allHullModSpecs.filter { it.id in variant.sMods || it.id in variant.sModdedBuiltIns }
        sMods = sMods.sortedBy { it.displayName }

        if (sMods.isEmpty())
            sModsElement.addLunaElement(sModsElement.widthSoFar, 290f).apply {
                renderBorder = false
                renderBackground = false
                addText("Their are no Installed S-Mods found")
                centerText()
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

                val textColor =
                    if (selectedSMod != null && selectedSMod == sMod) Misc.getNegativeHighlightColor() else Misc.getTextColor()
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
            sModsElement!!.addTooltipToPrevious(
                CustomHullmodToolTip(sMod, variant.hullSize),
                TooltipMakerAPI.TooltipLocation.RIGHT
            )
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
            backgroundAlpha = if (selectedSMod != null) 0.3f else 0.2f
            backgroundColor = Misc.getNegativeHighlightColor()
            enableTransparency = true
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

        footerElement!!.addTooltipToPrevious(RemoveHullmodTooltip(), TooltipMakerAPI.TooltipLocation.RIGHT)

        val increaseMaxSModButton = footerElement.addLunaElement(195f, 50f).apply {
            renderBorder = false
            renderBackground = true
            backgroundAlpha = if (canIncreaseMaxSModLimit(variant)) 0.3f else 0.2f
            backgroundColor = Misc.getStoryOptionColor()
            enableTransparency = true

            innerElement.addPara("Increase Max S-Mod Limit", Misc.getStoryOptionColor(), 10f)
                .apply { setAlignment(Alignment.MID) }
            innerElement.addPara(
                "(Cost: %s)",
                0f,
                Misc.getStoryOptionColor(),
                Misc.getStoryOptionColor(),
                "${getStoryPointCost(variant).roundToInt()}"
            )
                .apply { setAlignment(Alignment.MID) }

            onHoverEnter {
                playSound("ui_button_mouseover", 1f, 1f)
                if (canIncreaseMaxSModLimit(variant))
                    backgroundAlpha = 0.5f
            }

            onHoverExit {
                backgroundAlpha = if (canIncreaseMaxSModLimit(variant)) 0.3f else 0.2f
            }

            onClick {
                if (!it.isLMBEvent) return@onClick
                if (!canIncreaseMaxSModLimit(variant)) {
                    playSound("ui_button_disabled_pressed", 1f, 1f)
                    return@onClick
                }

                playSound("ui_char_spent_story_point", 1f, 1f)
                incrementMaxSModLimit(variant)
                installSModTracker(variant)
                selectedSMod = null
                refreshVariant()
                refreshButtonList()
                refreshPanel(member, variant)
            }
        }

        footerElement.addTooltipToPrevious(IncreaseSModLimitTooltip(), TooltipMakerAPI.TooltipLocation.RIGHT)
        increaseMaxSModButton.position.rightOfMid(removeSModButton.elementPanel, 0f)
        footerElement.addSpacer(5f)
        footerPanel.addUIElement(footerElement)
    }

    override fun onPanelClose(member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?) {
        backgroundPanel = null
        mainPanel = null
        selectedSMod = null
    }

    override fun shouldShow(member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?): Boolean {
        return true
    }

    private fun getStoryPointCost(shipVariant: ShipVariantAPI): Float {
        val tag = shipVariant.tags.firstOrNull { it.contains("asm_max_smod_limit:") }
        if (tag == null) {
            return 2f.pow(0 + 1f)
        }

        val maxSModLimit = tag.replace("asm_max_smod_limit:", "")
        return 2f.pow(maxSModLimit.toInt() + 1f)
    }

    private fun incrementMaxSModLimit(shipVariant: ShipVariantAPI) {
        var maxSModLimit = 1
        var tag = shipVariant.tags.firstOrNull { it.contains("asm_max_smod_limit:") }
        if (tag != null) {
            tag = shipVariant.tags?.firstOrNull { it.contains("asm_max_smod_limit:") }
            maxSModLimit = tag!!.replace("asm_max_smod_limit:", "").toInt()
            maxSModLimit += 1

            shipVariant.removeTag(tag)
        }

        shipVariant.addTag("asm_max_smod_limit:$maxSModLimit")
        Global.getSector().playerStats.spendStoryPoints(
            maxSModLimit,
            true,
            null,
            true,
            0f,
            null
        )
    }

    private fun canIncreaseMaxSModLimit(shipVariant: ShipVariantAPI): Boolean {
        return Global.getSector().playerStats.storyPoints >= getStoryPointCost(shipVariant)
    }

    private fun installSModTracker(variant: ShipVariantAPI) {
        if (!variant.hasHullMod("asm_feature_creep"))
            variant.addPermaMod("asm_feature_creep", false)
    }

    private fun removeInstalledSMod(variant: ShipVariantAPI, hullModId: String) {
        if (variant.sModdedBuiltIns.contains(hullModId)) {
            variant.sModdedBuiltIns.remove(hullModId)
        } else {
            variant.removePermaMod(hullModId)
        }
    }
}