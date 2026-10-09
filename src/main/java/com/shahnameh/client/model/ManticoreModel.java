package com.shahnameh.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class ManticoreModel extends MythModel {
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart tail2;
    private final ModelPart rightFront;
    private final ModelPart leftFront;
    private final ModelPart rightHind;
    private final ModelPart leftHind;

    public ManticoreModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.tail = root.getChild("tail");
        this.tail2 = this.tail.getChild("tail2");
        this.rightFront = root.getChild("right_front_leg");
        this.leftFront = root.getChild("left_front_leg");
        this.rightHind = root.getChild("right_hind_leg");
        this.leftHind = root.getChild("left_hind_leg");
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        look(this.head, state);
        this.rightFront.xRot += swing(state, 0.0F, 1.2F);
        this.leftHind.xRot += swing(state, 0.0F, 1.2F);
        this.leftFront.xRot += swing(state, Mth.PI, 1.2F);
        this.rightHind.xRot += swing(state, Mth.PI, 1.2F);
        // The scorpion tail sways and twitches.
        this.tail.yRot += Mth.sin(state.ageInTicks * 0.08F) * 0.25F;
        this.tail2.xRot += Mth.sin(state.ageInTicks * 0.15F) * 0.12F;
    }
}
