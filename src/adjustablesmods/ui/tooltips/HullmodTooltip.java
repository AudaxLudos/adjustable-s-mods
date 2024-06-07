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
        tooltip.addPara(this.hullModSpec.getDisplayName(), Misc.getBasePlayerColor(), 0f);
        tooltip.addSpacer(10f);

        if (!Objects.equals(this.hullModSpec.getManufacturer(), "Common")) {
            tooltip.addPara(
                    "Design Type: %s",
                    0f,
                    Misc.getGrayColor(),
                    Misc.getDesignTypeColor(this.hullModSpec.getManufacturer()),
                    this.hullModSpec.getManufacturer());
            tooltip.addSpacer(10f);
        }

        tooltip.addPara(this.hullModSpec.getDescriptionFormat(), 0f, Misc.getTextColor(), Misc.getHighlightColor(),
                this.hullModSpec.getEffect().getDescriptionParam(0, this.hullSize),
                this.hullModSpec.getEffect().getDescriptionParam(1, this.hullSize),
                this.hullModSpec.getEffect().getDescriptionParam(2, this.hullSize),
                this.hullModSpec.getEffect().getDescriptionParam(3, this.hullSize),
                this.hullModSpec.getEffect().getDescriptionParam(4, this.hullSize),
                this.hullModSpec.getEffect().getDescriptionParam(5, this.hullSize),
                this.hullModSpec.getEffect().getDescriptionParam(6, this.hullSize),
                this.hullModSpec.getEffect().getDescriptionParam(7, this.hullSize),
                this.hullModSpec.getEffect().getDescriptionParam(8, this.hullSize),
                this.hullModSpec.getEffect().getDescriptionParam(9, this.hullSize));

        this.hullModSpec.getEffect().addPostDescriptionSection(
                tooltip,
                this.hullSize,
                null,
                getTooltipWidth(tooltip),
                false);

        if (this.hullModSpec.getEffect().hasSModEffect()) {
            tooltip.addSpacer(10f);
            if (!this.hullModSpec.getEffect().isSModEffectAPenalty()) {
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
            this.hullModSpec.getEffect().addSModEffectSection(
                    tooltip,
                    this.hullSize,
                    null,
                    getTooltipWidth(tooltip),
                    false,
                    false);
        }
    }
}
