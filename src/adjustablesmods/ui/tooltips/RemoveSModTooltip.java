package adjustablesmods.ui.tooltips;

import adjustablesmods.ui.SModRefitButton;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.BaseTooltipCreator;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class RemoveSModTooltip extends BaseTooltipCreator {
    private final SModRefitButton refitButton;
    private final FleetMemberAPI member;

    public RemoveSModTooltip(SModRefitButton refitButton, FleetMemberAPI member) {
        this.refitButton = refitButton;
        this.member = member;
    }

    @Override
    public float getTooltipWidth(Object tooltipParam) {
        return 380f;
    }

    @Override
    public void createTooltip(TooltipMakerAPI tooltip, boolean expanded, Object tooltipParam) {
        tooltip.addPara("Removes the selected hullmod for %s", 0f, Misc.getTextColor(), Misc.getStoryOptionColor(), "1 story point");
        tooltip.addSpacer(10f);
        tooltip.addPara("%s spent on s-mods are not refunded when removed", 0f, Misc.getTextColor(), Misc.getStoryOptionColor(), "Story points");
        tooltip.addSpacer(10f);
        tooltip.addPara("No bonus experience is gained when removing hullmods with %s", 0f, Misc.getTextColor(), Misc.getStoryOptionColor(), "story points");
        if (refitButton.selectedSMod == null && !member.getVariant().getSMods().isEmpty()) {
            tooltip.addSpacer(10f);
            tooltip.addPara("Select a hullmod to remove", Misc.getHighlightColor(), 0f);
        }
        if (member.getVariant().getSMods().isEmpty()) {
            tooltip.addSpacer(10f);
            tooltip.addPara("No s-mods found", Misc.getHighlightColor(), 0f);
        }
        if (refitButton.selectedSMod != null && Global.getSector().getPlayerStats().getStoryPoints() < 1) {
            tooltip.addSpacer(10f);
            tooltip.addPara("Not enough skill points", Misc.getNegativeHighlightColor(), 0f);
        }
    }
}
