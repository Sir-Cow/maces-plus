package sircow.macesplus.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.macesplus.enchantment.ModEnchantments;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void macesPlus$blusteringMaceEnchant(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);

        if (!(stack.getItem() instanceof MaceItem)) return;

        Holder<Enchantment> blustering = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.BLUSTERING);

        int blusteringLevel = EnchantmentHelper.getItemEnchantmentLevel(blustering, stack);

        if (blusteringLevel <= 0) return;

        if (!level.isClientSide()) {
            Vec3 look = player.getLookAngle();
            WindCharge windCharge = new WindCharge(player, level, player.getX(), player.getEyeY() - 0.15, player.getZ());

            windCharge.shoot(look.x(), look.y(), look.z(), 1.5F, 1.0F);
            level.addFreshEntity(windCharge);
            stack.hurtAndBreak(1, player, hand);
        }

        player.getCooldowns().addCooldown(stack, 60 - 25 * (blusteringLevel - 1));
        player.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, true);
        cir.setReturnValue(InteractionResult.SUCCESS);
    }
}
