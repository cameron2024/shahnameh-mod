package com.shahnameh.block;

import com.shahnameh.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Derafsh Kaviani. Kaveh raised his leather apron on a spear and the people rose with him.
 * While planted, it strengthens and shields players, villagers and iron golems nearby.
 */
public class KavehBannerBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    private static final int RADIUS = 12;
    private static final int PULSE_TICKS = 40;

    public KavehBannerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 20);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        AABB area = new AABB(pos).inflate(RADIUS);
        boolean rallied = false;
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area, e -> e.isAlive() && e.getType().is(ModTags.BANNER_ALLIES))) {
            entity.addEffect(new MobEffectInstance(MobEffects.STRENGTH, PULSE_TICKS + 60, 0, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, PULSE_TICKS + 60, 0, true, true));
            rallied = true;
        }
        if (rallied) {
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 1.6, pos.getZ() + 0.5, 6, 0.4, 0.4, 0.4, 0.0);
        }
        level.scheduleTick(pos, this, PULSE_TICKS);
    }
}
