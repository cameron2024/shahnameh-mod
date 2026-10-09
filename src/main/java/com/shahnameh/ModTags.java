package com.shahnameh;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class ModTags {
    private ModTags() {}

    /** All Divs. The ox-headed mace hits these harder. */
    public static final TagKey<EntityType<?>> DIVS = TagKey.create(Registries.ENTITY_TYPE, ShahnamehMod.id("divs"));

    /** Who Kaveh's Banner rallies: players, villagers, iron golems. */
    public static final TagKey<EntityType<?>> BANNER_ALLIES = TagKey.create(Registries.ENTITY_TYPE, ShahnamehMod.id("banner_allies"));
}
