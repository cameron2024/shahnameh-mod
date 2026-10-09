package com.shahnameh.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * A Peri: a small winged spirit, neither good nor evil.
 * Every few minutes a Peri near a player either plays a prank
 * (swapping two of their inventory slots) or leaves a blessing of luck.
 */
public class Peri extends MythFlyer {
    private static final int MAIN_INVENTORY_SLOTS = 36;
    private int mischiefCooldown = 600;

    public Peri(EntityType<? extends Peri> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 8.0)
            .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected double cruiseSpeed() {
        return 0.28;
    }

    @Override
    protected double minAltitude() {
        return 1.2;
    }

    @Override
    protected double maxAltitude() {
        return 3.5;
    }

    @Override
    protected double roamRadius() {
        return 6.0;
    }

    @Override
    protected Vec3 roamCenter() {
        Player nearby = this.level().getNearestPlayer(this, 20.0);
        return nearby != null ? nearby.position() : this.position();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!(this.level() instanceof ServerLevel level)) {
            return;
        }
        if (this.tickCount % 10 == 0) {
            level.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY() + 0.5, this.getZ(), 1, 0.2, 0.2, 0.2, 0.0);
        }
        if (--this.mischiefCooldown > 0) {
            return;
        }
        Player player = level.getNearestPlayer(this, 6.0);
        if (player == null || player.isSpectator()) {
            this.mischiefCooldown = 100;
            return;
        }
        if (this.random.nextBoolean()) {
            this.prank(level, player);
        } else {
            player.addEffect(new MobEffectInstance(MobEffects.LUCK, 20 * 60 * 2, 0));
            player.sendOverlayMessage(Component.translatable("message.shahnameh.peri.blessing"));
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1.0, player.getZ(), 12, 0.4, 0.6, 0.4, 0.0);
        }
        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, SoundSource.NEUTRAL, 1.0F, 1.4F);
        this.mischiefCooldown = 20 * 60 * 2 + this.random.nextInt(20 * 60 * 2);
    }

    /** Swap two inventory slots, at least one of which holds something. */
    private void prank(ServerLevel level, Player player) {
        Inventory inventory = player.getInventory();
        int a = -1;
        for (int tries = 0; tries < 20 && a < 0; tries++) {
            int slot = this.random.nextInt(MAIN_INVENTORY_SLOTS);
            if (!inventory.getItem(slot).isEmpty()) {
                a = slot;
            }
        }
        if (a < 0) {
            return;
        }
        int b = this.random.nextInt(MAIN_INVENTORY_SLOTS);
        if (b == a) {
            b = (a + 1 + this.random.nextInt(MAIN_INVENTORY_SLOTS - 1)) % MAIN_INVENTORY_SLOTS;
        }
        ItemStack first = inventory.getItem(a).copy();
        ItemStack second = inventory.getItem(b).copy();
        inventory.setItem(a, second);
        inventory.setItem(b, first);
        player.sendOverlayMessage(Component.translatable("message.shahnameh.peri.prank"));
        level.sendParticles(ParticleTypes.WITCH, player.getX(), player.getY() + 1.0, player.getZ(), 12, 0.4, 0.6, 0.4, 0.0);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return distanceToClosestPlayer > 96.0 * 96.0;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("MischiefCooldown", this.mischiefCooldown);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.mischiefCooldown = input.getIntOr("MischiefCooldown", 600);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ALLAY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ALLAY_DEATH;
    }
}
