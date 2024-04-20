package adjustablesmods.ui.tooltips;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.BaseTooltipCreator;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.Objects;

public class HullmodTooltip extends BaseTooltipCreator {
    private final HullModSpecAPI hullModSpec;
    private final ShipAPI.HullSize hullSize;

    public HullmodTooltip(HullModSpecAPI hullModSpec, ShipAPI.HullSize hullSize) {
        this.hullModSpec = hullModSpec;
        this.hullSize = hullSize;
    }

    @Override
    public float getTooltipWidth(Object tooltipParam) {
        return 380f;
    }

    @Override
    public void createTooltip(TooltipMakerAPI tooltip, boolean expanded, Object tooltipParam) {
        tooltip.addPara(hullModSpec.getDisplayName(), Misc.getBasePlayerColor(), 0f);
        tooltip.addSpacer(10f);

        if (!Objects.equals(hullModSpec.getManufacturer(), "Common")) {
            tooltip.addPara(
                    "Design Type: %s",
                    0f,
                    Misc.getGrayColor(),
                    Misc.getDesignTypeColor(hullModSpec.getManufacturer()),
                    hullModSpec.getManufacturer());
            tooltip.addSpacer(10f);
        }

        tooltip.addPara(hullModSpec.getDescriptionFormat(), 0f, Misc.getTextColor(), Misc.getHighlightColor(),
                hullModSpec.getEffect().getDescriptionParam(0, hullSize),
                hullModSpec.getEffect().getDescriptionParam(1, hullSize),
                hullModSpec.getEffect().getDescriptionParam(2, hullSize),
                hullModSpec.getEffect().getDescriptionParam(3, hullSize),
                hullModSpec.getEffect().getDescriptionParam(4, hullSize),
                hullModSpec.getEffect().getDescriptionParam(5, hullSize),
                hullModSpec.getEffect().getDescriptionParam(6, hullSize),
                hullModSpec.getEffect().getDescriptionParam(7, hullSize),
                hullModSpec.getEffect().getDescriptionParam(8, hullSize),
                hullModSpec.getEffect().getDescriptionParam(9, hullSize));

        hullModSpec.getEffect().addPostDescriptionSection(
                tooltip,
                hullSize,
                null,
                getTooltipWidth(tooltip),
                false);

        if (hullModSpec.getEffect().hasSModEffect()) {
            tooltip.addSpacer(10f);
            if (!hullModSpec.getEffect().isSModEffectAPenalty()) {
                tooltip.addSectionHeading(
                        "S-mod bonus",
                        Misc.getStoryOptionColor(),
                        Misc.getStoryDarkColor(),
                        Alignment.MID,
                        0f);
            } else {
                tooltip.addSectionHeading(
                        "S-mod penalty",
                        Misc.getNegativeHighlightColor(),
                        Misc.setAlpha(Misc.scaleColorOnly(Misc.getNegativeHighlightColor(), 0.4f), 175),
                        Alignment.MID,
                        0f);
            }
            hullModSpec.getEffect().addSModEffectSection(
                    tooltip,
                    hullSize,
                    null,
                    getTooltipWidth(tooltip),
                    false,
                    false);
        }
    }
}
