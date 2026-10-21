package sircow.macesplus.enchantment;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import sircow.macesplus.Constants;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> BLUSTERING = key("blustering");
    public static final ResourceKey<Enchantment> COLLISION = key("collision");

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, Constants.id(name));
    }

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        register(context, BLUSTERING, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(1, 11),
                        Enchantment.dynamicCost(12, 11),
                        1,
                        EquipmentSlotGroup.MAINHAND
                )
        ));
        register(context, COLLISION, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(1, 11),
                        Enchantment.dynamicCost(12, 11),
                        1,
                        EquipmentSlotGroup.MAINHAND
                )
        ));
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.identifier()));
    }
}
