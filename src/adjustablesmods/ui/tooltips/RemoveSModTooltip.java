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
        if (!refitButton.selectedSMods.isEmpty() && Global.getSector().getPlayerStats().getStoryPoints() >= refitButton.selectedSMods.size()) {
            tooltip.addPara("Removing selected s-mods will cost a total of %s", 0f, Misc.getHighlightColor(), Misc.getStoryOptionColor(), refitButton.selectedSMods.size() + " story points");
            tooltip.addSpacer(10f);
        }
        if (refitButton.selectedSMods.isEmpty() && !member.getVariant().getSMods().isEmpty()) {
            tooltip.addPara("Select s-mods to remove", Misc.getHighlightColor(), 0f);
            tooltip.addSpacer(10f);
        }
        if (member.getVariant().getSMods().isEmpty()) {
            tooltip.addPara("No s-mods found", Misc.getHighlightColor(), 0f);
            tooltip.addSpacer(10f);
        }
        if (!refitButton.selectedSMods.isEmpty() && Global.getSector().getPlayerStats().getStoryPoints() < 1) {
            tooltip.addPara("Not enough skill points", Misc.getNegativeHighlightColor(), 0f);
            tooltip.addSpacer(10f);
        }
        tooltip.addPara("Removing an s-mod will cost %s", 0f, Misc.getTextColor(), Misc.getStoryOptionColor(), "1 story point");
        tooltip.addSpacer(10f);
        tooltip.addPara("%s spent on s-mods are not refunded when removed", 0f, Misc.getTextColor(), Misc.getStoryOptionColor(), "Story points");
        tooltip.addSpacer(10f);
        tooltip.addPara("No bonus experience is gained when removing hullmods with %s", 0f, Misc.getTextColor(), Misc.getStoryOptionColor(), "story points");
    }
}
