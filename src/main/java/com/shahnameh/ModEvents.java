package com.shahnameh;

import com.shahnameh.entity.Div;
import com.shahnameh.entity.Huma;
import com.shahnameh.entity.Manticore;
import com.shahnameh.entity.Peri;
import com.shahnameh.entity.Simurgh;
import com.shahnameh.entity.WhiteDiv;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/** Gameplay hooks that are not tied to a single class. */
public final class ModEvents {
    private ModEvents() {}

    public static void register() {
        EntityAttributeCreationEvent.BUS.addListener(ModEvents::onAttributes);
        SpawnPlacementRegisterEvent.BUS.addListener(ModEvents::onSpawnPlacements);
        LivingHurtEvent.BUS.addListener(ModEvents::onLivingHurt);
    }

    private static void onAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DIV.get(), Div.createAttributes().build());
        event.put(ModEntities.WHITE_DIV.get(), WhiteDiv.createAttributes().build());
        event.put(ModEntities.MANTICORE.get(), Manticore.createAttributes().build());
        event.put(ModEntities.SIMURGH.get(), Simurgh.createAttributes().build());
        event.put(ModEntities.HUMA.get(), Huma.createAttributes().build());
        event.put(ModEntities.PERI.get(), Peri.createAttributes().build());
    }

    private static void onSpawnPlacements(SpawnPlacementRegisterEvent event) {
        var replace = SpawnPlacementRegisterEvent.Operation.REPLACE;
        var ground = SpawnPlacementTypes.ON_GROUND;
        var heightmap = Heightmap.Types.MOTION_BLOCKING_NO_LEAVES;
        event.register(ModEntities.DIV.get(), ground, heightmap, Monster::checkMonsterSpawnRules, replace);
        event.register(ModEntities.MANTICORE.get(), ground, heightmap, Monster::checkMonsterSpawnRules, replace);
        event.register(ModEntities.WHITE_DIV.get(), ground, heightmap, Monster::checkMonsterSpawnRules, replace);
        event.register(ModEntities.SIMURGH.get(), ground, heightmap, Mob::checkMobSpawnRules, replace);
        event.register(ModEntities.HUMA.get(), ground, heightmap, Mob::checkMobSpawnRules, replace);
        event.register(ModEntities.PERI.get(), ground, heightmap, Mob::checkMobSpawnRules, replace);
    }

    private static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();
        Entity source = event.getSource().getEntity();
        boolean directHit = source != null && event.getSource().getDirectEntity() == source;

        // The ox-headed mace: stuns, and hits Divs half again as hard.
        if (directHit && source instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            if (weapon.is(ModItems.OX_MACE.get())) {
                victim.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 50, 3));
                victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 50, 1));
                if (victim.getType().is(ModTags.DIVS)) {
                    event.setAmount(event.getAmount() * 1.5F);
                }
                wearDown(attacker, weapon);
            }
        }

        // The Manticore's scorpion sting.
        if (directHit && source instanceof Manticore) {
            victim.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 5, 1));
        }

        // Harming the Huma brings misfortune.
        if (victim instanceof Huma && source instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 20 * 60 * 10, 1));
            player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 20 * 6, 0));
            player.sendOverlayMessage(Component.translatable("message.shahnameh.huma.curse"));
        }

        // Raising a hand against the Simurgh is unwise.
        if (victim instanceof Simurgh && source instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 20 * 60 * 5, 0));
        }
    }

    /** Uses one point of durability, breaking the weapon if it runs out. */
    private static void wearDown(LivingEntity holder, ItemStack weapon) {
        if (!weapon.isDamageableItem() || (holder instanceof Player p && p.getAbilities().instabuild)) {
            return;
        }
        int damage = weapon.getDamageValue() + 1;
        if (damage >= weapon.getMaxDamage()) {
            holder.level().playSound(null, holder.getX(), holder.getY(), holder.getZ(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 0.8F);
            weapon.shrink(1);
        } else {
            weapon.setDamageValue(damage);
        }
    }
}
