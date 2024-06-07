package adjustablesmods.ui.plugins;

import adjustablesmods.Utils;
import adjustablesmods.ui.SModRefitButton;
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
            if (!this.refitButton.selectedSMods.contains((HullModSpecAPI) buttonId)) {
                this.refitButton.selectedSMods.add((HullModSpecAPI) buttonId);
            } else {
                this.refitButton.selectedSMods.remove((HullModSpecAPI) buttonId);
            }
        }

        for (ButtonAPI button : this.refitButton.sModButtons) {
            if (this.refitButton.selectedSMods.contains((HullModSpecAPI) button.getCustomData())) {
                button.highlight();
            } else {
                button.unhighlight();
            }
        }

        this.refitButton.removeSModButton.setEnabled(!this.refitButton.selectedSMods.isEmpty() && Utils.canAffordStoryPointsCost(this.refitButton.selectedSMods.size()));
        this.refitButton.increaseSModLimitButton.setEnabled(Utils.canAffordStoryPointsCost(Utils.getStoryPointCost(this.variant)));
    }
}
