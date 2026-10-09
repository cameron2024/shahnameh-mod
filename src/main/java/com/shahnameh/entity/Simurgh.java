package com.shahnameh.entity;

import com.shahnameh.ModEntities;
import com.shahnameh.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * The Simurgh: the wise bird of the Alborz who raised Zal.
 * A wild Simurgh descends to greet travellers and gives three feathers once.
 * A summoned Simurgh heals the one who burned her feather, then departs.
 */
public class Simurgh extends MythFlyer {
    private static final int SUMMON_DURATION = 20 * 12;

    private boolean gifted;
    /** -1 for a wild Simurgh; otherwise ticks since she was called. */
    private int summonedTicks = -1;
    @Nullable
    private Player summoner;

    public Simurgh(EntityType<? extends Simurgh> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 200.0)
            .add(Attributes.MOVEMENT_SPEED, 0.3)
            .add(Attributes.ARMOR, 10.0)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
            .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    /** Called when a player burns a Simurgh Feather. */
    public static void answerCall(ServerLevel level, Player player) {
        player.removeEffect(MobEffects.POISON);
        player.removeEffect(MobEffects.WITHER);
        player.removeEffect(MobEffects.BLINDNESS);
        player.removeEffect(MobEffects.DARKNESS);
        player.removeEffect(MobEffects.NAUSEA);
        player.removeEffect(MobEffects.WEAKNESS);
        player.removeEffect(MobEffects.SLOWNESS);
        player.removeEffect(MobEffects.HUNGER);
        player.setHealth(player.getMaxHealth());
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 10, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 120, 1));
        player.sendOverlayMessage(Component.translatable("message.shahnameh.simurgh.answers"));

        level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getY() + 1.0, player.getZ(), 60, 0.6, 0.8, 0.6, 0.3);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_FLAP, SoundSource.NEUTRAL, 1.5F, 1.2F);

        Simurgh simurgh = ModEntities.SIMURGH.get().create(level, EntitySpawnReason.MOB_SUMMONED);
        if (simurgh != null) {
            simurgh.setPos(player.getX(), player.getY() + 7.0, player.getZ());
            simurgh.summonedTicks = 0;
            simurgh.summoner = player;
            simurgh.gifted = true;
            level.addFreshEntity(simurgh);
        }
    }

    public boolean isSummoned() {
        return this.summonedTicks >= 0;
    }

    @Override
    protected double cruiseSpeed() {
        return 0.35;
    }

    @Override
    protected double minAltitude() {
        return this.isSummoned() ? 3.0 : 10.0;
    }

    @Override
    protected double maxAltitude() {
        return this.isSummoned() ? 6.0 : 26.0;
    }

    @Override
    protected double roamRadius() {
        return this.isSummoned() ? 6.0 : 40.0;
    }

    @Override
    protected Vec3 pickFlightTarget() {
        if (this.isSummoned() && this.summoner != null && this.summoner.isAlive()) {
            double angle = this.random.nextDouble() * Math.PI * 2.0;
            return this.summoner.position().add(Math.cos(angle) * 5.0, 3.5, Math.sin(angle) * 5.0);
        }
        if (!this.isSummoned() && !this.gifted) {
            // Curious about travellers: come down and hover near the closest player.
            Player nearby = this.level().getNearestPlayer(this, 32.0);
            if (nearby != null) {
                double angle = this.random.nextDouble() * Math.PI * 2.0;
                return nearby.position().add(Math.cos(angle) * 3.5, 1.5, Math.sin(angle) * 3.5);
            }
        }
        return super.pickFlightTarget();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!(this.level() instanceof ServerLevel level) || !this.isSummoned()) {
            return;
        }
        this.summonedTicks++;
        if (this.summonedTicks % 20 == 0) {
            level.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY() + 1.0, this.getZ(), 6, 1.0, 0.6, 1.0, 0.02);
        }
        if (this.summonedTicks >= SUMMON_DURATION) {
            level.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY() + 1.0, this.getZ(), 40, 1.2, 1.0, 1.2, 0.05);
            this.discard();
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isSummoned() || !player.getItemInHand(hand).isEmpty()) {
            return super.mobInteract(player, hand);
        }
        if (!(this.level() instanceof ServerLevel level)) {
            return InteractionResult.SUCCESS;
        }
        if (this.gifted) {
            player.sendOverlayMessage(Component.translatable("message.shahnameh.simurgh.already_gifted"));
            return InteractionResult.SUCCESS;
        }
        ItemStack feathers = new ItemStack(ModItems.SIMURGH_FEATHER.get(), 3);
        if (!player.getInventory().add(feathers)) {
            player.drop(feathers, false);
        }
        this.gifted = true;
        player.sendOverlayMessage(Component.translatable("message.shahnameh.simurgh.gift"));
        level.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY() + 1.2, this.getZ(), 40, 1.2, 0.8, 1.2, 0.05);
        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1.0F, 0.7F);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Gifted", this.gifted);
        output.putInt("SummonedTicks", this.summonedTicks);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.gifted = input.getBooleanOr("Gifted", false);
        this.summonedTicks = input.getIntOr("SummonedTicks", -1);
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

    @Override
    public float getVoicePitch() {
        return 0.45F;
    }
}
