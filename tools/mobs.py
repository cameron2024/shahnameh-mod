import os
import sys

sys.path.insert(0, os.path.dirname(__file__))
from models import (C, Part, solid, gradient, feathers, peacock, with_face, eyes,
                    texture, java_method, preview)

ROOT = os.path.join(os.path.dirname(__file__), "..")
TEX = os.path.join(ROOT, "src/main/resources/assets/shahnameh/textures/entity")
JAVA = os.path.join(ROOT, "src/main/java/com/shahnameh/client/model/ModelGeometry.java")
PREVIEW = os.path.join(ROOT, "tools/previews")


# ------------------------------------------------------------------ Div

def div_parts(s, skin, spots, cloth, horn, eye_col, tooth, seed):
    """Horned demon giant. s scales the whole body about the ground (y=24)."""
    def Y(y):  # scale a pivot y about the ground
        return round(24 + (y - 24) * s)

    def n(v):  # scale a size or offset
        return round(v * s)

    sk = solid(skin, 9, seed, spots, 0.08)
    face = with_face(sk, eyes(eye_col, 0.42, gap=n(2), size=max(1, n(1)),
                              brow=(30, 30, 25), mouth=tooth))
    head = Part("head", (0, Y(-12), 0), cubes=[
        C(-n(5), -n(10), -n(5), n(10), n(10), n(10), face),
        C(-n(6), -n(14), -n(2), n(2), n(5), n(2), solid(horn, 6, seed + 1)),
        C(n(4), -n(14), -n(2), n(2), n(5), n(2), solid(horn, 6, seed + 1)),
        C(-n(7), -n(17), -n(1), n(1), n(4), n(1), solid(horn, 6, seed + 2)),
        C(n(6), -n(17), -n(1), n(1), n(4), n(1), solid(horn, 6, seed + 2)),
        C(-n(3), -n(1), -n(6), n(2), n(3), n(1), solid(tooth, 4, seed + 3)),
        C(n(1), -n(1), -n(6), n(2), n(3), n(1), solid(tooth, 4, seed + 3)),
    ])
    body = Part("body", (0, Y(6), 0), cubes=[
        C(-n(7), -n(18), -n(4), n(14), n(18), n(8), sk),
        C(-n(7) - 1, -n(3), -n(4) - 1, n(14) + 2, n(6), n(8) + 2, solid(cloth, 10, seed + 4)),
    ])
    arm_r = Part("right_arm", (-n(9), Y(-10), 0), cubes=[
        C(-n(4), -n(2), -n(2.5), n(5), n(20), n(5), sk)])
    arm_l = Part("left_arm", (n(9), Y(-10), 0), cubes=[
        C(-n(1), -n(2), -n(2.5), n(5), n(20), n(5), sk)])
    leg_r = Part("right_leg", (-n(4), Y(6), 0), cubes=[
        C(-n(3), 0, -n(3), n(6), 24 - Y(6), n(6), sk)])
    leg_l = Part("left_leg", (n(4), Y(6), 0), cubes=[
        C(-n(3), 0, -n(3), n(6), 24 - Y(6), n(6), sk)])
    return [body, head, arm_r, arm_l, leg_r, leg_l]


def div():
    return div_parts(1.0, skin=(72, 88, 62), spots=(48, 60, 40), cloth=(100, 62, 36),
                     horn=(222, 210, 178), eye_col=(255, 60, 30), tooth=(240, 236, 220), seed=10)


def white_div():
    return div_parts(1.35, skin=(226, 228, 220), spots=(196, 200, 190), cloth=(40, 36, 46),
                     horn=(30, 28, 30), eye_col=(230, 20, 20), tooth=(250, 250, 245), seed=20)


# ------------------------------------------------------------------ Manticore

def manticore():
    fur = (196, 146, 72)
    fur_dark = (150, 100, 46)
    mane = (110, 56, 24)
    face_skin = (214, 168, 128)
    chitin = (96, 30, 26)
    sting = (30, 22, 22)
    head_face = with_face(solid(face_skin, 6, 31),
                          eyes((40, 20, 10), 0.35, gap=2, size=1, brow=(70, 40, 20),
                               mouth=(120, 40, 40), beard=mane))
    body = Part("body", (0, 10, 2), cubes=[
        C(-5, -4, -9, 10, 9, 18, solid(fur, 8, 30, fur_dark, 0.05))])
    head = Part("head", (0, 8, -7), cubes=[
        C(-4, -4, -7, 8, 8, 7, head_face),
        C(-6, -6, -2, 12, 12, 4, solid(mane, 12, 32)),
    ])
    legs = []
    for name, x, z in (("right_front_leg", -3, -5), ("left_front_leg", 3, -5),
                       ("right_hind_leg", -3, 8), ("left_hind_leg", 3, 8)):
        legs.append(Part(name, (x, 14, z), cubes=[C(-2, 0, -2, 4, 10, 4, solid(fur_dark, 8, 33))]))
    stinger = Part("stinger", (0, 0, 5), (35, 0, 0), cubes=[C(-1, -1, 0, 2, 2, 3, solid(sting, 5, 34))])
    seg3 = Part("tail3", (0, 0, 7), (55, 0, 0), cubes=[C(-1, -1, 0, 2, 2, 6, solid(chitin, 8, 35))],
                children=[stinger])
    seg2 = Part("tail2", (0, 0, 7), (55, 0, 0), cubes=[C(-1.5, -1.5, 0, 3, 3, 8, solid(chitin, 8, 36))],
                children=[seg3])
    tail = Part("tail", (0, 7, 10), (60, 0, 0), cubes=[C(-1.5, -1.5, 0, 3, 3, 8, solid(chitin, 8, 37))],
                children=[seg2])
    return [body, head, tail] + legs


# ------------------------------------------------------------------ Simurgh

def simurgh():
    teal = (36, 128, 120)
    green = (62, 150, 88)
    gold = (226, 176, 58)
    crimson = (176, 38, 52)
    blue = (40, 70, 160)
    eye = (250, 220, 60)
    body = Part("body", (0, 6, 0), cubes=[
        C(-7, -6, -11, 14, 12, 22, feathers(teal, green, gold, 40))])
    head = Part("head", (0, -10, 0), (-18, 0, 0), cubes=[
        C(-4, -7, -5, 8, 7, 9, with_face(feathers(teal, green, gold, 41),
                                         eyes(eye, 0.35, gap=2, size=1, brow=(20, 20, 20)))),
        C(-2.5, -4, -10, 5, 4, 5, with_face(solid((214, 160, 60), 6, 42),
                                            eyes((30, 20, 10), 0.2, gap=1, size=1))),
        C(-1, -11, -2, 2, 4, 7, solid(crimson, 8, 43)),
    ])
    neck = Part("neck", (0, 1, -9), (18, 0, 0), cubes=[
        C(-3, -10, -3, 6, 11, 6, feathers(green, teal, gold, 44))], children=[head])
    wing_l = Part("left_wing", (7, -3, -4), (0, 0, -10), cubes=[
        C(0, -1, -6, 26, 2, 16, gradient(crimson, gold, 8, 45))])
    wing_r = Part("right_wing", (-7, -3, -4), (0, 0, 10), cubes=[
        C(-26, -1, -6, 26, 2, 16, gradient(crimson, gold, 8, 46))])
    tail = Part("tail", (0, 2, 10), (20, 0, 0), cubes=[
        C(-9, -1, 0, 18, 2, 20, peacock((30, 110, 100), blue, gold, 47))])
    legs = []
    for name, x in (("right_leg", -4), ("left_leg", 4)):
        legs.append(Part(name, (x, 12, 2), cubes=[
            C(-1.5, 0, -1.5, 3, 9, 3, solid(gold, 6, 48)),
            C(-2.5, 9, -3.5, 5, 3, 6, solid((150, 110, 40), 6, 49))]))
    return [body, neck, wing_l, wing_r, tail] + legs


# ------------------------------------------------------------------ Huma

def huma():
    gold = (238, 196, 72)
    pale = (250, 238, 200)
    beak = (220, 120, 40)
    body = Part("body", (0, 16, 0), cubes=[C(-3, -3, -5, 6, 6, 10, feathers(gold, pale, (200, 150, 50), 50))])
    head = Part("head", (0, 13, -4), cubes=[
        C(-2.5, -5, -3, 5, 5, 5, with_face(solid(pale, 6, 51), eyes((20, 20, 20), 0.4, gap=1))),
        C(-1, -3, -5, 2, 2, 2, solid(beak, 5, 52))])
    wing_l = Part("left_wing", (3, 14, -2), cubes=[C(0, -0.5, -3, 14, 1, 8, gradient(pale, gold, 6, 53))])
    wing_r = Part("right_wing", (-3, 14, -2), cubes=[C(-14, -0.5, -3, 14, 1, 8, gradient(pale, gold, 6, 54))])
    tail = Part("tail", (0, 15, 5), (15, 0, 0), cubes=[C(-3, -0.5, 0, 6, 1, 9, gradient(gold, (255, 250, 230), 6, 55))])
    return [body, head, wing_l, wing_r, tail]


# ------------------------------------------------------------------ Peri

def peri():
    skin = (246, 214, 190)
    dress = (196, 150, 226)
    hair = (60, 34, 80)
    wing_a = (255, 196, 228)
    wing_b = (170, 220, 255)
    body = Part("body", (0, 14, 0), cubes=[
        C(-2, -5, -1, 4, 5, 2, solid(dress, 6, 60)),
        C(-3, 0, -2, 6, 4, 4, gradient(dress, (150, 110, 200), 6, 61))])
    head = Part("head", (0, 9, 0), cubes=[
        C(-2.5, -5, -2.5, 5, 5, 5, with_face(solid(skin, 4, 62),
                                             eyes((70, 40, 120), 0.5, gap=1, size=1, mouth=(220, 120, 140)))),
        C(-3, -6, -1, 6, 2, 4, solid(hair, 6, 63)),
        C(-3, -4, 1, 6, 6, 2, solid(hair, 6, 64))])
    arms = [Part("right_arm", (-2.5, 10, 0), cubes=[C(-1, 0, -0.5, 1, 5, 1, solid(skin, 4, 65))]),
            Part("left_arm", (2.5, 10, 0), cubes=[C(0, 0, -0.5, 1, 5, 1, solid(skin, 4, 66))])]
    legs = [Part("right_leg", (-1, 18, 0), cubes=[C(-0.5, 0, -0.5, 1, 6, 1, solid(skin, 4, 67))]),
            Part("left_leg", (1, 18, 0), cubes=[C(-0.5, 0, -0.5, 1, 6, 1, solid(skin, 4, 68))])]
    wings = [Part("left_wing", (1, 11, 1), (0, -30, 0), cubes=[C(0, -6, 0, 7, 10, 1, gradient(wing_a, wing_b, 5, 69))]),
             Part("right_wing", (-1, 11, 1), (0, 30, 0), cubes=[C(-7, -6, 0, 7, 10, 1, gradient(wing_a, wing_b, 5, 70))])]
    return [body, head] + arms + legs + wings


MOBS = [
    ("div", "createDiv", div, 128),
    ("white_div", "createWhiteDiv", white_div, 256),
    ("manticore", "createManticore", manticore, 128),
    ("simurgh", "createSimurgh", simurgh, 256),
    ("huma", "createHuma", huma, 64),
    ("peri", "createPeri", peri, 64),
]

HEADER = """package com.shahnameh.client.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * Mob geometry. GENERATED by tools/mobs.py together with the matching textures,
 * so edit the Python file and re-run it instead of editing this by hand.
 */
public final class ModelGeometry {
    private ModelGeometry() {}

"""


def main():
    os.makedirs(TEX, exist_ok=True)
    os.makedirs(PREVIEW, exist_ok=True)
    methods = []
    for name, method, fn, width in MOBS:
        roots = fn()
        img, tw, th = texture(roots, width)
        img.save(os.path.join(TEX, f"{name}.png"))
        methods.append(java_method(method, roots, tw, th))
        preview(roots, os.path.join(PREVIEW, f"{name}.png"))
        print(f"{name}: texture {tw}x{th}")
    with open(JAVA, "w") as f:
        f.write(HEADER + "\n\n".join(methods) + "\n}\n")


if __name__ == "__main__":
    main()
