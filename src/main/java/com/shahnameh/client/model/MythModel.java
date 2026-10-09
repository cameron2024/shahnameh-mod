package com.shahnameh.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

/** Shared helpers for this mod's entity models. */
public abstract class MythModel extends EntityModel<LivingEntityRenderState> {
    protected static final float DEG = Mth.DEG_TO_RAD;

    protected MythModel(ModelPart root) {
        super(root);
    }

    /** Standard walk cycle swing for a limb. */
    protected static float swing(LivingEntityRenderState state, float phase, float amount) {
        return Mth.cos(state.walkAnimationPos * 0.6662F + phase) * amount * state.walkAnimationSpeed;
    }

    protected static void look(ModelPart head, LivingEntityRenderState state) {
        head.yRot += state.yRot * DEG;
        head.xRot += state.xRot * DEG;
    }
}
