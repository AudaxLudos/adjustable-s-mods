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
    public static final float WIDTH = 380f;
    public static final float HEIGHT = 425f;
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
        return 1;
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
        this.sModButtons.clear();

        TooltipMakerAPI mElement = backgroundPanel.createUIElement(WIDTH, HEIGHT, false);
        backgroundPanel.addUIElement(mElement);

        float columnWidth = WIDTH - 20f;
        float rowHeight = 320f;

        CustomPanelAPI sModsPanel = backgroundPanel.createCustomPanel(columnWidth, rowHeight, new BorderedPanelPlugin());
        TooltipMakerAPI sModsHeader = sModsPanel.createUIElement(columnWidth, rowHeight, false);
        sModsHeader.addSectionHeading("Installed S-Mods", Alignment.MID, 0f);
        sModsPanel.addUIElement(sModsHeader);

        TooltipMakerAPI sModsBody = sModsPanel.createUIElement(columnWidth, rowHeight - 20f, true);
        List<HullModSpecAPI> removableSMods = new ArrayList<>();
        for (HullModSpecAPI spec : Global.getSettings().getAllHullModSpecs()) {
            if (variant.getSMods().contains(spec.getId()) || variant.getSModdedBuiltIns().contains(spec.getId())) {
                removableSMods.add(spec);
            }
        }
        Collections.sort(removableSMods, new Comparator<HullModSpecAPI>() {
            @Override
            public int compare(HullModSpecAPI o1, HullModSpecAPI o2) {
                return o1.getDisplayName().compareTo(o2.getDisplayName());
            }
        });

        if (removableSMods.isEmpty()) {
            sModsBody.setParaOrbitronVeryLarge();
            LabelAPI sModsText = sModsBody.addPara("No s-mods found", 0f);
            sModsText.getPosition().inTL(columnWidth / 2f - sModsText.computeTextWidth(sModsText.getText()) / 2, (rowHeight - 20f) / 2f - sModsText.computeTextHeight(sModsText.getText()) / 2);
        } else {
            for (HullModSpecAPI sMod : removableSMods) {
                CustomPanelAPI sModPanel = backgroundPanel.createCustomPanel(columnWidth, 44f, new SelectButtonPlugin(this, variant));
                TooltipMakerAPI sModButtonElement = sModPanel.createUIElement(columnWidth, 44f, false);
                ButtonAPI sModButton = sModButtonElement.addButton("", sMod, new Color(0, 195, 255, 190), new Color(0, 0, 0, 255), Alignment.MID, CutStyle.NONE, columnWidth, 44f, 0f);
                sModButton.setHighlightBrightness(0.6f);
                sModButton.setGlowBrightness(0.56f);
                sModButton.setQuickMode(true);
                sModButtonElement.addTooltipTo(new HullmodTooltip(sMod, variant.getHullSize()), sModButton, TooltipMakerAPI.TooltipLocation.RIGHT);
                sModButtonElement.getPosition().setXAlignOffset(-10f);
                sModPanel.addUIElement(sModButtonElement);

                TooltipMakerAPI sModNameElement = sModPanel.createUIElement(columnWidth, 40f, false);
                TooltipMakerAPI sModImage = sModNameElement.beginImageWithText(sMod.getSpriteName(), 40f);
                sModImage.addPara(sMod.getDisplayName(), Misc.getTextColor(), 0f);
                sModNameElement.addImageWithText(0f);
                sModNameElement.getPosition().setXAlignOffset(-8f).setYAlignOffset(2f);
                sModPanel.addUIElement(sModNameElement);

                this.sModButtons.add(sModButton);
                sModsBody.addCustom(sModPanel, 0f);
            }
        }
        sModsPanel.addUIElement(sModsBody);
        mElement.addCustom(sModsPanel, 0f).getPosition().inTMid(10f);

        CustomPanelAPI footerPanel = backgroundPanel.createCustomPanel(columnWidth, 100f, null);
        TooltipMakerAPI footerElement = footerPanel.createUIElement(columnWidth, 100f, false);

        CustomPanelAPI storyPointsPanel = backgroundPanel.createCustomPanel(columnWidth, 40f, null);
        TooltipMakerAPI storyPointsElement = storyPointsPanel.createUIElement(columnWidth, 40f, false);
        storyPointsElement.setParaFontOrbitron();
        storyPointsElement.addLabelledValue("Story points remaining", Global.getSector().getPlayerStats().getStoryPoints() + "", Misc.getBasePlayerColor(), Misc.getStoryOptionColor(), columnWidth, 0f);
        storyPointsPanel.addUIElement(storyPointsElement);
        footerElement.addCustom(storyPointsPanel, 0f).getPosition().setXAlignOffset(-10f);

        CustomPanelAPI removeSModPanel = backgroundPanel.createCustomPanel(columnWidth / 2f, 25f, new ConfirmButtonPlugin(this, variant));
        TooltipMakerAPI removeSModElement = removeSModPanel.createUIElement(columnWidth / 2f, 25f, false);
        removeSModElement.setButtonFontOrbitron20();
        this.removeSModButton = removeSModElement.addButton("Remove", "remove_smod", Misc.getBasePlayerColor(), Misc.getDarkPlayerColor(), Alignment.MID, CutStyle.ALL, WIDTH / 2f - 40f, 25f, 0f);
        this.removeSModButton.setEnabled(!this.selectedSMods.isEmpty() && Utils.canAffordStoryPointsCost(1));
        this.removeSModButton.getPosition().inBMid(0f);
        removeSModElement.addTooltipTo(new RemoveSModTooltip(this, member), this.removeSModButton, TooltipMakerAPI.TooltipLocation.RIGHT);
        removeSModPanel.addUIElement(removeSModElement);
        footerElement.addCustom(removeSModPanel, 0f).getPosition().belowLeft(storyPointsPanel, 10f);

        CustomPanelAPI increaseSModLimitPanel = backgroundPanel.createCustomPanel(columnWidth / 2f, 25f, new ConfirmButtonPlugin(this, variant));
        TooltipMakerAPI increaseSModLimitElement = increaseSModLimitPanel.createUIElement(columnWidth / 2f, 25f, false);
        increaseSModLimitElement.setButtonFontOrbitron20();
        this.increaseSModLimitButton = increaseSModLimitElement.addButton("Increase Limit", "increase_limit", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, CutStyle.ALL, WIDTH / 2f - 40f, 25f, 0f);
        this.increaseSModLimitButton.setEnabled(Utils.canAffordStoryPointsCost(Utils.getStoryPointCost(variant)));
        this.increaseSModLimitButton.getPosition().inBMid(0f);
        increaseSModLimitElement.addTooltipTo(new IncreaseSModLimitTooltip(variant), this.increaseSModLimitButton, TooltipMakerAPI.TooltipLocation.RIGHT);
        increaseSModLimitPanel.addUIElement(increaseSModLimitElement);
        footerElement.addCustom(increaseSModLimitPanel, 0f).getPosition().rightOfMid(removeSModPanel, 0f);

        footerPanel.addUIElement(footerElement);
        mElement.addCustom(footerPanel, 10f).getPosition().setXAlignOffset(10f);
    }

    @Override
    public void onPanelClose(FleetMemberAPI member, ShipVariantAPI variant, MarketAPI market) {
        this.selectedSMods.clear();
    }
}
