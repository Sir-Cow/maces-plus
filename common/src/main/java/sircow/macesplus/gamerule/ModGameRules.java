package sircow.macesplus.gamerule;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

public final class ModGameRules {
    public static GameRule<Boolean> MACE_PLAYER_KNOCKBACK;

    public static void init() {
        if (MACE_PLAYER_KNOCKBACK != null) return;

        MACE_PLAYER_KNOCKBACK = Registry.register(
                BuiltInRegistries.GAME_RULE,
                "mace_player_knockback",
                new GameRule<>(GameRuleCategory.PLAYER, GameRuleType.BOOL, BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean, Codec.BOOL, value -> value ? 1 : 0, false, FeatureFlagSet.of())
        );
    }

    private ModGameRules() {}
}
