package com.shahnameh.item;

import com.shahnameh.entity.Simurgh;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Zal's gift: burn a feather and the Simurgh comes to heal you.
 */
public class SimurghFeatherItem extends LoreItem {
    public SimurghFeatherItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            Simurgh.answerCall(serverLevel, player);
            stack.consume(1, player);
        }
        return InteractionResult.SUCCESS;
    }
}
