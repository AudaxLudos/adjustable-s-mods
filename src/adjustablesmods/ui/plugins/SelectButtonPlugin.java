package adjustablesmods.ui.plugins;

import adjustablesmods.Utils;
import adjustablesmods.ui.SModRefitButton;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.ui.ButtonAPI;

public class SelectButtonPlugin extends BaseCustomUIPanelPlugin {
    public SModRefitButton refitButton;
    public ShipVariantAPI variant;

    public SelectButtonPlugin(SModRefitButton refitButton, ShipVariantAPI variant) {
        this.refitButton = refitButton;
        this.variant = variant;
    }

    @Override
    public void buttonPressed(Object buttonId) {
        if (buttonId instanceof HullModSpecAPI) {
            refitButton.selectedSMod = (HullModSpecAPI) buttonId;
        }

        boolean isHighlighted = false;
        for (ButtonAPI button : refitButton.sModButtons) {
            if (button.getCustomData() == buttonId && !button.isHighlighted()) {
                button.highlight();
                isHighlighted = true;
                continue;
            }
            if (button.isHighlighted()) {
                button.unhighlight();
            }
        }

        if (!isHighlighted) {
            refitButton.selectedSMod = null;
        }

        if (refitButton.removeSModButton != null)
            refitButton.removeSModButton.setEnabled(refitButton.selectedSMod != null);
        if (refitButton.increaseSModLimitButton != null)
            refitButton.increaseSModLimitButton.setEnabled(!(Global.getSector().getPlayerStats().getStoryPoints() < Utils.getStoryPointCost(variant)));
    }
}
