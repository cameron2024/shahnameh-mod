package com.shahnameh;

import com.shahnameh.entity.Div;
import com.shahnameh.entity.Huma;
import com.shahnameh.entity.Manticore;
import com.shahnameh.entity.Peri;
import com.shahnameh.entity.Simurgh;
import com.shahnameh.entity.WhiteDiv;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    private ModEntities() {}

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ShahnamehMod.MODID);

    public static final RegistryObject<EntityType<Div>> DIV = ENTITY_TYPES.register("div",
        () -> EntityType.Builder.of(Div::new, MobCategory.MONSTER)
            .sized(1.4F, 2.9F)
            .clientTrackingRange(10)
            .build(ENTITY_TYPES.key("div")));

    public static final RegistryObject<EntityType<WhiteDiv>> WHITE_DIV = ENTITY_TYPES.register("white_div",
        () -> EntityType.Builder.of(WhiteDiv::new, MobCategory.MONSTER)
            .sized(1.9F, 3.9F)
            .fireImmune()
            .clientTrackingRange(12)
            .build(ENTITY_TYPES.key("white_div")));

    public static final RegistryObject<EntityType<Manticore>> MANTICORE = ENTITY_TYPES.register("manticore",
        () -> EntityType.Builder.of(Manticore::new, MobCategory.MONSTER)
            .sized(1.2F, 1.3F)
            .clientTrackingRange(10)
            .build(ENTITY_TYPES.key("manticore")));

    public static final RegistryObject<EntityType<Simurgh>> SIMURGH = ENTITY_TYPES.register("simurgh",
        () -> EntityType.Builder.of(Simurgh::new, MobCategory.CREATURE)
            .sized(2.4F, 2.2F)
            .clientTrackingRange(16)
            .build(ENTITY_TYPES.key("simurgh")));

    public static final RegistryObject<EntityType<Huma>> HUMA = ENTITY_TYPES.register("huma",
        () -> EntityType.Builder.of(Huma::new, MobCategory.CREATURE)
            .sized(0.9F, 0.7F)
            .clientTrackingRange(12)
            .build(ENTITY_TYPES.key("huma")));

    public static final RegistryObject<EntityType<Peri>> PERI = ENTITY_TYPES.register("peri",
        () -> EntityType.Builder.of(Peri::new, MobCategory.CREATURE)
            .sized(0.5F, 0.9F)
            .clientTrackingRange(8)
            .build(ENTITY_TYPES.key("peri")));
}
