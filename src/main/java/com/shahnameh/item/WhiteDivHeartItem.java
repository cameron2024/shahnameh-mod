package com.shahnameh.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * The blood of the White Div's heart restored Kay Kavus's sight.
 * Using it cures blindness and darkness and grants long night vision.
 */
public class WhiteDivHeartItem extends LoreItem {
    public WhiteDivHeartItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            player.removeEffect(MobEffects.BLINDNESS);
            player.removeEffect(MobEffects.DARKNESS);
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60 * 8, 0));
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 8, 1));
            serverLevel.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1.6, player.getZ(), 30, 0.4, 0.3, 0.4, 0.02);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8F, 0.6F);
            stack.consume(1, player);
        }
        return InteractionResult.SUCCESS;
    }
}
