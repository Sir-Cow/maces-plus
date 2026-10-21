package sircow.macesplus.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.macesplus.enchantment.ModEnchantments;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "die", at = @At("HEAD"))
    private void macesPlus$resetBlusteringOnKill(DamageSource source, CallbackInfo ci) {
        if (!(source.getEntity() instanceof ServerPlayer player)) return;

        Holder<Enchantment> blustering = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.BLUSTERING);

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty()) continue;

            if (EnchantmentHelper.getItemEnchantmentLevel(blustering, stack) > 0) {
                UseCooldown useCooldown = stack.get(DataComponents.USE_COOLDOWN);
                if (useCooldown != null && useCooldown.cooldownGroup().isPresent()) player.getCooldowns().removeCooldown(useCooldown.cooldownGroup().get());
                else player.getCooldowns().removeCooldown(player.getCooldowns().getCooldownGroup(stack));
            }
        }
    }
}
