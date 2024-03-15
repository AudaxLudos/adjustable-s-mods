package adjustablesmods.ui.tooltips

import com.fs.starfarer.api.ui.BaseTooltipCreator
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc

class IncreaseSModLimitTooltip : BaseTooltipCreator() {
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
            Misc.getTextColor(),
            Misc.getStoryOptionColor(),
            "story points"
        )
        tooltip.addSpacer(10f)
        tooltip.addPara(
            "Each upgrade doubles the amount of %s required for the next upgrade",
            0f,
            Misc.getTextColor(),
            Misc.getStoryOptionColor(),
            "story points"
        )
    }
}