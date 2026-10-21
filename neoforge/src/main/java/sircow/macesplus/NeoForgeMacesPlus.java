package sircow.macesplus;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import sircow.macesplus.platform.Services;

@Mod(Constants.MOD_ID)
public class NeoForgeMacesPlus {
    public NeoForgeMacesPlus(IEventBus modEventBus, ModContainer container) {
        if (Services.PLATFORM.isModLoaded("pinferno")) {
            throw new IllegalStateException(String.valueOf(Component.translatable("info.macesplus.conflict", Constants.MOD_ID, "Preserved: Inferno")));
        }

        CommonClass.init();
    }
}
