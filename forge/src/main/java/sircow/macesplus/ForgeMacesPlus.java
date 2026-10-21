package sircow.macesplus;

import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sircow.macesplus.platform.Services;

@Mod(Constants.MOD_ID)
public class ForgeMacesPlus {
    public ForgeMacesPlus(FMLJavaModLoadingContext context) {
        if (Services.PLATFORM.isModLoaded("pinferno")) {
            throw new IllegalStateException(String.valueOf(Component.translatable("info.macesplus.conflict", Constants.MOD_ID, "Preserved: Inferno")));
        }

        CommonClass.init();
    }
}
