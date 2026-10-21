package sircow.macesplus.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.macesplus.enchantment.ModEnchantments;
import sircow.macesplus.gamerule.ModGameRules;

import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.world.item.Item.BASE_ATTACK_DAMAGE_ID;
import static net.minecraft.world.item.Item.BASE_ATTACK_SPEED_ID;

@Mixin(MaceItem.class)
public class MaceItemMixin {
    @ModifyArg(method = "createAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;add(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;Lnet/minecraft/world/entity/EquipmentSlotGroup;)Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;", ordinal = 0), index = 1)
    private static AttributeModifier macesPlus$modifyDamage(AttributeModifier modifier) {
        return new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 9.0, AttributeModifier.Operation.ADD_VALUE);
    }

    @ModifyArg(method = "createAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;add(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;Lnet/minecraft/world/entity/EquipmentSlotGroup;)Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;", ordinal = 1), index = 1)
    private static AttributeModifier macesPlus$modifyAttackSpeed(AttributeModifier modifier) {
        return new AttributeModifier(BASE_ATTACK_SPEED_ID, -3.0, AttributeModifier.Operation.ADD_VALUE);
    }

    @Inject(method = "hurtEnemy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/MaceItem;knockback(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;)V"))
    private void macesPlus$collisionMaceEnchant(ItemStack itemStack, LivingEntity mob, LivingEntity attacker, CallbackInfo ci) {
        if (!(attacker.level() instanceof ServerLevel level)) return;

        Holder<Enchantment> collision = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ModEnchantments.COLLISION);

        int enchantLevel = EnchantmentHelper.getItemEnchantmentLevel(collision, itemStack);

        if (enchantLevel <= 0) return;

        double fallDistance = attacker.fallDistance;
        double fallDamageBonus;

        if (fallDistance <= 3.0) fallDamageBonus = 4.0 * fallDistance;
        else if (fallDistance <= 8.0) fallDamageBonus = 12.0 + 2.0 * (fallDistance - 3.0);
        else fallDamageBonus = 22.0 + fallDistance - 8.0;

        double densityBonus = EnchantmentHelper.modifyFallBasedDamage(level, attacker.getWeaponItem(), mob, attacker.damageSources().mace(attacker), 0.0F) * fallDistance;
        double totalDamage = attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) + fallDamageBonus + densityBonus;

        float collisionDamage = (float) (2.0 + 0.15 * enchantLevel * totalDamage);

        Predicate<LivingEntity> predicate = MaceItemAccessor.invokeKnockbackPredicate(attacker, mob);

        for (LivingEntity nearby : level.getEntitiesOfClass(
                LivingEntity.class,
                mob.getBoundingBox().inflate(3.5),
                nearby -> predicate.test(nearby) && !nearby.isInvulnerable() && nearby.isAlive()
        )) {
            nearby.hurt(attacker.damageSources().mace(attacker), collisionDamage);
        }
    }

    @Redirect(method = "knockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"))
    private static List<LivingEntity> macesPlus$playerKnockbackGameRule(Level level, Class<LivingEntity> entityClass, AABB box, Predicate<LivingEntity> predicate) {
        if (level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(ModGameRules.MACE_PLAYER_KNOCKBACK)) {
            return level.getEntitiesOfClass(entityClass, box, entity -> predicate.test(entity) && !(entity instanceof Player));
        }

        return level.getEntitiesOfClass(entityClass, box, predicate);
    }
}
