package adjustablesmods.ui.tooltips

import com.fs.starfarer.api.ui.BaseTooltipCreator
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc

class RemoveHullmodTooltip : BaseTooltipCreator() {
    override fun isTooltipExpandable(tooltipParam: Any?): Boolean {
        return false
    }

    override fun getTooltipWidth(tooltipParam: Any?): Float {
        return 380f
    }

    override fun createTooltip(tooltip: TooltipMakerAPI?, expanded: Boolean, tooltipParam: Any?) {
        tooltip!!.addPara(
            "%s spent on s-mods are not refunded when removed",
            0f,
            Misc.getTextColor(),
            Misc.getStoryOptionColor(),
            "Story points"
        )
    }
}