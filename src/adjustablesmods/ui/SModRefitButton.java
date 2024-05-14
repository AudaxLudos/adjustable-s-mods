package adjustablesmods.ui;

import adjustablesmods.Utils;
import adjustablesmods.ui.plugins.BorderedPanelPlugin;
import adjustablesmods.ui.plugins.ConfirmButtonPlugin;
import adjustablesmods.ui.plugins.SelectButtonPlugin;
import adjustablesmods.ui.tooltips.HullmodTooltip;
import adjustablesmods.ui.tooltips.IncreaseSModLimitTooltip;
import adjustablesmods.ui.tooltips.RemoveSModTooltip;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;
import lunalib.lunaRefit.BaseRefitButton;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SModRefitButton extends BaseRefitButton {
    public final float WIDTH = 380f;
    public final float HEIGHT = 426f;
    public List<HullModSpecAPI> selectedSMods = new ArrayList<>();
    public List<ButtonAPI> sModButtons = new ArrayList<>();
    public ButtonAPI removeSModButton = null;
    public ButtonAPI increaseSModLimitButton = null;

    @Override
    public String getButtonName(FleetMemberAPI member, ShipVariantAPI variant) {
        return "Adjust S-Mods";
    }

    @Override
    public String getIconName(FleetMemberAPI member, ShipVariantAPI variant) {
        return "graphics/icons/skills/technology.png";
    }

    @Override
    public int getOrder(FleetMemberAPI member, ShipVariantAPI variant) {
        return 9999;
    }

    @Override
    public boolean hasTooltip(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        return true;
    }

    @Override
    public float getPanelWidth(FleetMemberAPI member, ShipVariantAPI variant) {
        return WIDTH;
    }

    @Override
    public float getPanelHeight(FleetMemberAPI member, ShipVariantAPI variant) {
        return HEIGHT;
    }

    @Override
    public void addTooltip(TooltipMakerAPI tooltip, FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        tooltip.addPara("Options to increase the max s-mod limit or to remove installed s-mods on a ship", 0f);
    }

    @Override
    public boolean hasPanel(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        return true;
    }

    @Override
    public void initPanel(CustomPanelAPI backgroundPanel, FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        TooltipMakerAPI mElement = backgroundPanel.createUIElement(WIDTH, HEIGHT, false);
        backgroundPanel.addUIElement(mElement);

        float width = WIDTH - 20f;

        CustomPanelAPI headerPanel = backgroundPanel.createCustomPanel(width, 320f, new BorderedPanelPlugin());
        TooltipMakerAPI headerElement = headerPanel.createUIElement(width, 320f, false);
        headerElement.addSectionHeading("Installed S-Mods", Alignment.MID, 0f);

        CustomPanelAPI sModsPanel = backgroundPanel.createCustomPanel(width, 300f, null);
        TooltipMakerAPI sModsElement = sModsPanel.createUIElement(width, 300f, true);

        List<HullModSpecAPI> sMods = new ArrayList<>();
        for (HullModSpecAPI spec : Global.getSettings().getAllHullModSpecs()) {
            if (variant.getSMods().contains(spec.getId()) || variant.getSModdedBuiltIns().contains(spec.getId())) {
                sMods.add(spec);
            }
        }
        Collections.sort(sMods, new Comparator<HullModSpecAPI>() {
            @Override
            public int compare(HullModSpecAPI o1, HullModSpecAPI o2) {
                return o1.getDisplayName().compareTo(o2.getDisplayName());
            }
        });

        if (sMods.isEmpty()) {
            sModsElement.setParaOrbitronVeryLarge();
            LabelAPI sModsText = sModsElement.addPara("No s-mods found", 0f);
            sModsText.getPosition().inTL(width / 2f - sModsText.computeTextWidth(sModsText.getText()) / 2, 300f / 2f - sModsText.computeTextHeight(sModsText.getText()) / 2);
        } else {
            for (HullModSpecAPI sMod : sMods) {
                CustomPanelAPI sModPanel = backgroundPanel.createCustomPanel(width, 44f, new SelectButtonPlugin(this, variant));
                TooltipMakerAPI sModNameElement = sModPanel.createUIElement(width, 40f, false);
                TooltipMakerAPI sModImage = sModNameElement.beginImageWithText(sMod.getSpriteName(), 40f);
                sModImage.addPara(sMod.getDisplayName(), Misc.getTextColor(), 0f);
                sModNameElement.addImageWithText(0f);
                sModNameElement.getPosition().setXAlignOffset(-8f).setYAlignOffset(2f);

                TooltipMakerAPI sModButtonElement = sModPanel.createUIElement(width, 44f, false);
                ButtonAPI sModButton = sModButtonElement.addButton("", sMod, new Color(0, 195, 255, 190), new Color(0, 0, 0, 255), Alignment.MID, CutStyle.NONE, width, 44f, 0f);
                sModButton.setHighlightBrightness(0.6f);
                sModButton.setGlowBrightness(0.56f);
                sModButton.setQuickMode(true);
                sModButtonElement.addTooltipTo(new HullmodTooltip(sMod, variant.getHullSize()), sModPanel, TooltipMakerAPI.TooltipLocation.RIGHT);
                sModButtonElement.getPosition().setXAlignOffset(-10f);

                sModPanel.addUIElement(sModButtonElement);
                sModPanel.addUIElement(sModNameElement);
                sModsElement.addCustom(sModPanel, 0f);

                this.sModButtons.add(sModButton);
            }
        }
        sModsPanel.addUIElement(sModsElement);
        headerElement.addCustom(sModsPanel, 0f).getPosition().setXAlignOffset(-5f);

        headerPanel.addUIElement(headerElement);
        mElement.addCustom(headerPanel, 10f).getPosition().setXAlignOffset(10f);

        CustomPanelAPI footerPanel = backgroundPanel.createCustomPanel(width, 100f, null);
        TooltipMakerAPI footerElement = footerPanel.createUIElement(width, 100f, false);

        CustomPanelAPI storyPointsPanel = backgroundPanel.createCustomPanel(width, 40f, null);
        TooltipMakerAPI storyPointsElement = storyPointsPanel.createUIElement(width, 40f, false);
        storyPointsElement.setParaFontOrbitron();
        storyPointsElement.addLabelledValue("Story points remaining", Global.getSector().getPlayerStats().getStoryPoints() + "", Misc.getBasePlayerColor(), Misc.getStoryOptionColor(), width, 0f);
        storyPointsPanel.addUIElement(storyPointsElement);
        footerElement.addCustom(storyPointsPanel, 0f).getPosition().setXAlignOffset(-10f);

        CustomPanelAPI removeSModPanel = backgroundPanel.createCustomPanel(width / 2f, 25f, new ConfirmButtonPlugin(this, variant));
        TooltipMakerAPI removeSModElement = removeSModPanel.createUIElement(width / 2f, 25f, false);
        removeSModElement.setButtonFontOrbitron20();
        removeSModButton = removeSModElement.addButton("Remove", "remove_smod", Misc.getBasePlayerColor(), Misc.getDarkPlayerColor(), Alignment.MID, CutStyle.ALL, WIDTH / 2f - 40f, 25f, 0f);
        removeSModButton.setEnabled(!selectedSMods.isEmpty());
        removeSModButton.getPosition().inBMid(0f);
        removeSModElement.addTooltipTo(new RemoveSModTooltip(this, member), removeSModButton, TooltipMakerAPI.TooltipLocation.RIGHT);
        removeSModPanel.addUIElement(removeSModElement);
        footerElement.addCustom(removeSModPanel, 0f).getPosition().belowLeft(storyPointsPanel, 10f);

        CustomPanelAPI increaseSModLimitPanel = backgroundPanel.createCustomPanel(width / 2f, 25f, new ConfirmButtonPlugin(this, variant));
        TooltipMakerAPI increaseSModLimitElement = increaseSModLimitPanel.createUIElement(width / 2f, 25f, false);
        increaseSModLimitElement.setButtonFontOrbitron20();
        increaseSModLimitButton = increaseSModLimitElement.addButton("Increase Limit", "increase_limit", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, CutStyle.ALL, WIDTH / 2f - 40f, 25f, 0f);
        increaseSModLimitButton.setEnabled(!(Global.getSector().getPlayerStats().getStoryPoints() < Utils.getStoryPointCost(variant)));
        increaseSModLimitButton.getPosition().inBMid(0f);
        increaseSModLimitElement.addTooltipTo(new IncreaseSModLimitTooltip(variant), increaseSModLimitButton, TooltipMakerAPI.TooltipLocation.RIGHT);
        increaseSModLimitPanel.addUIElement(increaseSModLimitElement);
        footerElement.addCustom(increaseSModLimitPanel, 0f).getPosition().rightOfMid(removeSModPanel, 0f);

        footerPanel.addUIElement(footerElement);
        mElement.addCustom(footerPanel, 10f).getPosition().setXAlignOffset(10f);
    }

    @Override
    public void onPanelClose(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        selectedSMods.clear();
    }
}
