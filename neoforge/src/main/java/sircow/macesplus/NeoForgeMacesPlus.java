package sircow.macesplus;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class NeoForgeMacesPlus {
    public NeoForgeMacesPlus(IEventBus eventBus) {
        CommonClass.init();
    }
}
