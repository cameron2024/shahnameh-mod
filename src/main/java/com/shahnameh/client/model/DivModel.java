package com.shahnameh.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

/** Used for both the Div and the White Div (same skeleton, different size). */
public class DivModel extends MythModel {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public DivModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        look(this.head, state);
        this.rightLeg.xRot += swing(state, 0.0F, 1.0F);
        this.leftLeg.xRot += swing(state, Mth.PI, 1.0F);
        this.rightArm.xRot += swing(state, Mth.PI, 0.8F);
        this.leftArm.xRot += swing(state, 0.0F, 0.8F);
        // Slow menacing breathing.
        float breath = Mth.sin(state.ageInTicks * 0.06F) * 0.04F;
        this.rightArm.zRot += 0.08F + breath;
        this.leftArm.zRot -= 0.08F + breath;
        this.body.xRot += 0.04F;
    }
}
