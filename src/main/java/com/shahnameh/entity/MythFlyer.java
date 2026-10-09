package com.shahnameh.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Shared flight for the mod's birds and spirits. Instead of vanilla flying
 * navigation, each flyer picks a point in the air and glides toward it,
 * which keeps the behaviour simple and predictable.
 */
public abstract class MythFlyer extends PathfinderMob {
    @Nullable
    protected Vec3 flightTarget;
    protected int retargetTimer;

    protected MythFlyer(EntityType<? extends MythFlyer> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    /** Cruising speed in blocks per tick. */
    protected abstract double cruiseSpeed();

    /** Height range above the ground to fly at. */
    protected abstract double minAltitude();

    protected abstract double maxAltitude();

    /** How far from its current spot it wanders. */
    protected abstract double roamRadius();

    /** The point the flyer roams around. Defaults to where it is now. */
    protected Vec3 roamCenter() {
        return this.position();
    }

    protected Vec3 pickFlightTarget() {
        Vec3 center = this.roamCenter();
        double angle = this.random.nextDouble() * Math.PI * 2.0;
        double dist = this.random.nextDouble() * this.roamRadius();
        double x = center.x + Math.cos(angle) * dist;
        double z = center.z + Math.sin(angle) * dist;
        int ground = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING, Mth.floor(x), Mth.floor(z));
        double y = ground + this.minAltitude() + this.random.nextDouble() * (this.maxAltitude() - this.minAltitude());
        return new Vec3(x, y, z);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide()) {
            return;
        }
        if (this.flightTarget == null || --this.retargetTimer <= 0 || this.position().distanceToSqr(this.flightTarget) < 4.0) {
            this.flightTarget = this.pickFlightTarget();
            this.retargetTimer = 60 + this.random.nextInt(100);
        }
        Vec3 toTarget = this.flightTarget.subtract(this.position());
        double distance = toTarget.length();
        if (distance > 0.05) {
            Vec3 desired = toTarget.scale(Math.min(this.cruiseSpeed(), distance * 0.2) / distance);
            Vec3 velocity = this.getDeltaMovement();
            this.setDeltaMovement(velocity.add(desired.subtract(velocity).scale(0.1)));
            float yaw = (float) (Mth.atan2(toTarget.z, toTarget.x) * (180.0 / Math.PI)) - 90.0F;
            this.setYRot(Mth.rotLerp(0.2F, this.getYRot(), yaw));
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
        }
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
        return false;
    }
}
