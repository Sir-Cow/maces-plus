package sircow.macesplus.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.macesplus.gamerule.ModGameRules;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {
    @Inject(method = "createContents", at = @At("RETURN"))
    private static void macesPlus$registerGameRules(CallbackInfo ci) {
        ModGameRules.init();
    }
}
