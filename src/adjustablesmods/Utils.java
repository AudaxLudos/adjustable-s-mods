package adjustablesmods;

import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;

public class Utils {
    public static String getVariantCustomTag(ShipVariantAPI variant) {
        String tag = "";
        if (!variant.getTags().isEmpty()) {
            for (String t : variant.getTags()) {
                if (t.contains("asm_max_smod_limit")) {
                    tag = t;
                    break;
                }
            }
        }

        if (tag.isEmpty()) {
            tag = "asm_max_smod_limit:0";
            variant.addTag(tag);
        }

        return tag;
    }

    public static int getStoryPointCost(ShipVariantAPI variant) {
        String tag = getVariantCustomTag(variant);
        int maxSModLimit = Integer.parseInt(tag.replaceAll("[^0-9]", ""));
        ++maxSModLimit;

        return Math.round((float) Math.pow(2, maxSModLimit));
    }

    public static void increaseSModLimit(ShipVariantAPI variant) {
        String tag = getVariantCustomTag(variant);
        int maxSModLimit = Integer.parseInt(tag.replaceAll("[^0-9]", ""));
        ++maxSModLimit;

        variant.removeTag(tag);
        variant.addTag("asm_max_smod_limit:" + maxSModLimit);
    }

    public static void installSModTracker(ShipVariantAPI variant) {
        if (!variant.hasHullMod("asm_feature_creep"))
            variant.addPermaMod("asm_feature_creep", false);
    }

    public static void removeSMod(ShipVariantAPI variant, HullModSpecAPI hullMod) {
        if (variant.getSModdedBuiltIns().contains(hullMod.getId())) {
            variant.getSModdedBuiltIns().remove(hullMod.getId());
        } else {
            variant.removePermaMod(hullMod.getId());
        }
    }
}
