package adjustablesmods.ui.tooltips

import com.fs.starfarer.api.combat.ShipAPI.HullSize
import com.fs.starfarer.api.loading.HullModSpecAPI
import com.fs.starfarer.api.ui.Alignment
import com.fs.starfarer.api.ui.BaseTooltipCreator
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc

class CustomHullmodToolTip(private var sMod: HullModSpecAPI, private var hullSize: HullSize) : BaseTooltipCreator() {
    override fun isTooltipExpandable(tooltipParam: Any?): Boolean {
        return false
    }

    override fun getTooltipWidth(tooltipParam: Any?): Float {
        return 380f
    }

    override fun createTooltip(tooltip: TooltipMakerAPI?, expanded: Boolean, tooltipParam: Any?) {
        tooltip!!.addPara(sMod.displayName, Misc.getBasePlayerColor(), 0f)
        tooltip.addSpacer(10f)

        if (sMod.manufacturer != "Common") {
            tooltip.addPara(
                "Design Type: %s",
                0f,
                Misc.getGrayColor(),
                Misc.getDesignTypeColor(sMod.manufacturer),
                sMod.manufacturer
            )
            tooltip.addSpacer(10f)
        }

        val sModParams = arrayOf(
            sMod.effect.getDescriptionParam(0, hullSize),
            sMod.effect.getDescriptionParam(1, hullSize),
            sMod.effect.getDescriptionParam(2, hullSize),
            sMod.effect.getDescriptionParam(3, hullSize),
            sMod.effect.getDescriptionParam(4, hullSize),
            sMod.effect.getDescriptionParam(5, hullSize),
            sMod.effect.getDescriptionParam(6, hullSize),
            sMod.effect.getDescriptionParam(7, hullSize),
            sMod.effect.getDescriptionParam(8, hullSize),
            sMod.effect.getDescriptionParam(9, hullSize)
        )

        tooltip.addPara(sMod.descriptionFormat, 0f, Misc.getTextColor(), Misc.getHighlightColor(), *sModParams)

        sMod.effect.addPostDescriptionSection(
            tooltip,
            hullSize,
            null,
            getTooltipWidth(tooltip),
            false
        )

        if (sMod.effect.hasSModEffect()) {
            tooltip.addSpacer(10f)
            if (!sMod.effect.isSModEffectAPenalty) {
                tooltip.addSectionHeading(
                    "S-mod bonus",
                    Misc.getStoryOptionColor(),
                    Misc.getStoryDarkColor(),
                    Alignment.MID,
                    0f
                )
            } else {
                tooltip.addSectionHeading(
                    "S-mod penalty",
                    Misc.getNegativeHighlightColor(),
                    Misc.setAlpha(Misc.scaleColorOnly(Misc.getNegativeHighlightColor(), 0.4f), 175),
                    Alignment.MID,
                    0f
                )
            }
            sMod.effect.addSModEffectSection(
                tooltip,
                hullSize,
                null,
                getTooltipWidth(tooltip),
                false,
                false
            )
        }
    }
}