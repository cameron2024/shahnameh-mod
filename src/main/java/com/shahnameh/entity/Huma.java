package com.shahnameh.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * The Huma: a sacred bird that never lands. Anyone its shadow falls on is blessed;
 * anyone who harms it is cursed with misfortune (see ModEvents).
 */
public class Huma extends MythFlyer {
    public Huma(EntityType<? extends Huma> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20.0)
            .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected double cruiseSpeed() {
        return 0.25;
    }

    @Override
    protected double minAltitude() {
        return 6.0;
    }

    @Override
    protected double maxAltitude() {
        return 14.0;
    }

    @Override
    protected double roamRadius() {
        return 24.0;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!(this.level() instanceof ServerLevel level) || this.tickCount % 40 != 0) {
            return;
        }
        for (Player player : level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(4.0, 24.0, 4.0))) {
            if (player.getY() < this.getY()) {
                if (!player.hasEffect(MobEffects.LUCK)) {
                    player.sendOverlayMessage(Component.translatable("message.shahnameh.huma.shadow"));
                }
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 20 * 60 * 3, 0));
                level.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 2.2, player.getZ(), 5, 0.3, 0.2, 0.3, 0.01);
            }
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PARROT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PARROT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }
}
