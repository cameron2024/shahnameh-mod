package com.shahnameh.client.renderer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

/** One renderer for every mob in the mod: a model plus a texture. */
public class MythRenderer<T extends LivingEntity, M extends EntityModel<LivingEntityRenderState>>
        extends LivingEntityRenderer<T, LivingEntityRenderState, M> {
    private final Identifier texture;

    public MythRenderer(EntityRendererProvider.Context context, M model, float shadowRadius, Identifier texture) {
        super(context, model, shadowRadius);
        this.texture = texture;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return this.texture;
    }
}
