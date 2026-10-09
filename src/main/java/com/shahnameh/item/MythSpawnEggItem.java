package com.shahnameh.item;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

/**
 * A simple spawn egg that looks up its entity type lazily, so it does not
 * depend on the order in which items and entity types are registered.
 */
public class MythSpawnEggItem extends Item {
    private final Supplier<? extends EntityType<?>> type;

    public MythSpawnEggItem(Properties properties, Supplier<? extends EntityType<?>> type) {
        super(properties);
        this.type = type;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!(context.getLevel() instanceof ServerLevel level)) {
            return InteractionResult.SUCCESS;
        }
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Entity entity = this.type.get().create(level, EntitySpawnReason.SPAWN_EGG);
        if (entity == null) {
            return InteractionResult.FAIL;
        }
        entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        entity.setYRot(level.getRandom().nextFloat() * 360.0F);
        if (entity instanceof Mob mob) {
            mob.setPersistenceRequired();
        }
        level.addFreshEntity(entity);
        context.getItemInHand().consume(1, context.getPlayer());
        return InteractionResult.SUCCESS;
    }
}
