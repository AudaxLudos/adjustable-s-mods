package adjustablesmods.ui.plugins;

import adjustablesmods.Utils;
import adjustablesmods.ui.SModRefitButton;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import org.lwjgl.util.vector.Vector2f;

public class ConfirmButtonPlugin extends BaseCustomUIPanelPlugin {
    public SModRefitButton refitButton;
    public ShipVariantAPI variant;

    public ConfirmButtonPlugin(SModRefitButton refitButton, ShipVariantAPI variant) {
        this.refitButton = refitButton;
        this.variant = variant;
    }

    @Override
    public void buttonPressed(Object buttonId) {
        if (!(buttonId instanceof String)) return;

        if (buttonId == "remove_smod") {
            if (refitButton.selectedSMods.isEmpty()) return;
            Global.getSoundPlayer().playSound("ui_char_spent_story_point", 1f, 1f, Global.getSoundPlayer().getListenerPos(), new Vector2f());
            Global.getSector().getPlayerStats().spendStoryPoints(refitButton.selectedSMods.size(), true, null, true, 0f, null);
            for (int i = 0; i < refitButton.selectedSMods.size(); i++) {
                Utils.removeSMod(variant, refitButton.selectedSMods.get(i));
            }
            refitButton.refreshVariant();
            refitButton.closePanel();
        } else if (buttonId == "increase_limit") {
            Global.getSoundPlayer().playSound("ui_char_spent_story_point", 1f, 1f, Global.getSoundPlayer().getListenerPos(), new Vector2f());
            Global.getSector().getPlayerStats().spendStoryPoints(Utils.getStoryPointCost(variant), true, null, true, 0f, null);
            Utils.increaseSModLimit(variant);
            Utils.installSModTracker(variant);
            refitButton.refreshVariant();
            refitButton.closePanel();
        }
    }
}
