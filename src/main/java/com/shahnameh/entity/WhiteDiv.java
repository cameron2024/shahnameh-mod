package com.shahnameh.entity;

import com.shahnameh.ModTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Div-e Sepid, the White Demon, king of the Divs of Mazandaran.
 * He blinded Kay Kavus and his army; Rostam slew him in the seventh labour.
 *
 * Abilities: a ground slam that hurls back everyone nearby, and a roar that blinds.
 */
public class WhiteDiv extends Monster {
    private final ServerBossEvent bossEvent = new ServerBossEvent(
        this.getUUID(),
        Component.translatable("entity.shahnameh.white_div"),
        BossEvent.BossBarColor.WHITE,
        BossEvent.BossBarOverlay.NOTCHED_10);
    private int slamCooldown = 60;
    private int roarCooldown = 200;

    public WhiteDiv(EntityType<? extends WhiteDiv> type, Level level) {
        super(type, level);
        this.xpReward = 120;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 320.0)
            .add(Attributes.MOVEMENT_SPEED, 0.27)
            .add(Attributes.ATTACK_DAMAGE, 14.0)
            .add(Attributes.ATTACK_KNOCKBACK, 1.5)
            .add(Attributes.ARMOR, 12.0)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
            .add(Attributes.FOLLOW_RANGE, 40.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!(this.level() instanceof ServerLevel level)) {
            return;
        }
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

        LivingEntity target = this.getTarget();
        if (target == null) {
            return;
        }
        if (--this.slamCooldown <= 0 && this.distanceToSqr(target) < 25.0) {
            this.slam(level);
            this.slamCooldown = 100 + this.random.nextInt(60);
        }
        if (--this.roarCooldown <= 0) {
            this.roar(level);
            this.roarCooldown = 300 + this.random.nextInt(200);
        }
    }

    /** Strike the ground: damages and throws back everything around him except other Divs. */
    private void slam(ServerLevel level) {
        level.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY() + 0.3, this.getZ(), 6, 2.0, 0.2, 2.0, 0.0);
        level.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY() + 0.2, this.getZ(), 40, 3.0, 0.2, 3.0, 0.05);
        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.IRON_GOLEM_ATTACK, SoundSource.HOSTILE, 2.0F, 0.5F);
        for (LivingEntity victim : level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5.0, 2.0, 5.0),
                e -> e != this && e.isAlive() && !e.getType().is(ModTags.DIVS))) {
            victim.hurtServer(level, this.damageSources().mobAttack(this), 10.0F);
            victim.knockback(1.6, this.getX() - victim.getX(), this.getZ() - victim.getZ());
        }
    }

    /** A roar that blinds those nearby, as the White Div blinded Kay Kavus. */
    private void roar(ServerLevel level) {
        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.RAVAGER_ROAR, SoundSource.HOSTILE, 3.0F, 0.6F);
        for (Player player : level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(16.0))) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 4, 0));
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.RAVAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.RAVAGER_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return 0.6F;
    }
}
