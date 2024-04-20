package adjustablesmods.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class FeatureCreep extends BaseHullMod {
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getDynamic().getMod(Stats.MAX_PERMANENT_HULLMODS_MOD).modifyFlat(id, getMaxSModLimit(stats.getFleetMember()));
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float oPad = 10f;
        Color good = Misc.getPositiveHighlightColor();
        Color b = Misc.getHighlightColor();

        tooltip.setBulletedListMode(" - ");
        tooltip.addPara("Increases the ship's maximum s-mod limit by %s", oPad, good, getMaxSModLimit(ship.getFleetMember()) + "");
        tooltip.addPara("A maximum of %s hullmods can be s-modded", pad, b, Misc.getMaxPermanentMods(ship) + "");
        tooltip.setBulletedListMode(null);
    }

    private int getMaxSModLimit(FleetMemberAPI fleetMember) {
        String tag = "";
        if (fleetMember == null) return 0;
        if (!fleetMember.getVariant().getTags().isEmpty()) {
            for (String t : fleetMember.getVariant().getTags()) {
                if (t.contains("asm_max_smod_limit")) {
                    tag = t;
                    break;
                }
            }
        }

        int maxSModLimit = 0;
        if (!tag.isEmpty()) {
            maxSModLimit = Integer.parseInt(tag.replaceAll("[^0-9]", ""));
        }

        return maxSModLimit;
    }
}
