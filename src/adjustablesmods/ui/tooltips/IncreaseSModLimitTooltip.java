package adjustablesmods.ui.tooltips;

import adjustablesmods.Utils;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.BaseTooltipCreator;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class IncreaseSModLimitTooltip extends BaseTooltipCreator {
    private final ShipVariantAPI variant;

    public IncreaseSModLimitTooltip(ShipVariantAPI variant) {
        this.variant = variant;
    }

    @Override
    public float getTooltipWidth(Object tooltipParam) {
        return 380f;
    }

    @Override
    public void createTooltip(TooltipMakerAPI tooltip, boolean expanded, Object tooltipParam) {
        if (Global.getSector().getPlayerStats().getStoryPoints() < Utils.getStoryPointCost(variant)) {
            tooltip.addPara("Not enough story points", Misc.getNegativeHighlightColor(), 0f);
            tooltip.addSpacer(10f);
        }
        tooltip.addPara("This upgrade currently costs %s", 0f, Misc.getTextColor(), Misc.getStoryOptionColor(), Utils.getStoryPointCost(variant) + " story points");
        tooltip.addSpacer(10f);
        tooltip.addPara("Increases the ship's max s-mod limit by %s", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "1");
        tooltip.addSpacer(10f);
        tooltip.addPara("No bonus experience is gained when spending %s on this upgrade", 0f, Misc.getTextColor(), Misc.getStoryOptionColor(), "story points");
        tooltip.addSpacer(10f);
        tooltip.addPara("Each upgrade made to the ship doubles the amount of %s required for the next upgrade", 0f, Misc.getTextColor(), Misc.getStoryOptionColor(), "story points");
    }
}
