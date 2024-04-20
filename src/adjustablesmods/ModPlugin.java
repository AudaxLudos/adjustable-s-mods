package adjustablesmods;

import adjustablesmods.ui.SModRefitButton;
import com.fs.starfarer.api.BaseModPlugin;
import lunalib.lunaRefit.LunaRefitManager;

public class ModPlugin extends BaseModPlugin {
    @Override
    public void onApplicationLoad() {
        LunaRefitManager.addRefitButton(new SModRefitButton());
    }
}
