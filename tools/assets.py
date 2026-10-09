"""Generates item/block textures and every JSON resource the mod needs."""
import json
import math
import os
import random
from PIL import Image

ROOT = os.path.join(os.path.dirname(__file__), "..", "src/main/resources")
A = os.path.join(ROOT, "assets/shahnameh")
D = os.path.join(ROOT, "data/shahnameh")
NS = "shahnameh"


def write_json(path, obj):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w") as f:
        json.dump(obj, f, indent=2, ensure_ascii=False)
        f.write("\n")


# ------------------------------------------------------------------ pixel art

def art(rows, palette, path):
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()
    for y, row in enumerate(rows[:16]):
        for x, ch in enumerate(row[:16]):
            if ch != "." and ch in palette:
                px[x, y] = palette[ch] + (255,)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    img.save(path)


FEATHER = [
    "................",
    "...........ttt..",
    ".........ttrrtt.",
    "........trrggrt.",
    ".......trggbbgt.",
    "......trggbybgt.",
    ".....trrggbbgrt.",
    "....trrrggggrt..",
    "...trrrrrgrrt...",
    "...trrrrrrtt....",
    "..tqrrrttt......",
    "..qtttt.........",
    ".q..............",
    "q...............",
    "................",
    "................",
]
FEATHER_P = {"t": (34, 120, 112), "r": (180, 40, 52), "g": (230, 182, 60), "b": (40, 70, 170),
             "y": (250, 236, 120), "q": (240, 230, 200)}

HORN = [
    "................",
    "................",
    "..........dd....",
    ".........dbb....",
    ".........bbl....",
    "........bbw.....",
    ".......bbwl.....",
    "......bbwl......",
    ".....bbwl.......",
    "....bbbw........",
    "...bbbwl........",
    "..bbbbw.........",
    "..bbbbl.........",
    "..ccccc.........",
    "................",
    "................",
]
HORN_P = {"d": (70, 60, 50), "b": (214, 200, 166), "w": (244, 238, 220), "l": (176, 160, 128), "c": (120, 70, 40)}

HEART = [
    "................",
    "...rr.....rr....",
    "..rRRr...rRRr...",
    ".rRwwRr.rRRRRr..",
    ".rRwRRRrRRRRRr..",
    ".rRRRRRRRRRRRr..",
    ".rRRRRRRRRRRRr..",
    "..rRRRRRRRRRr...",
    "...rRRRRRRRr....",
    "....rRRRRRr.....",
    ".....rRRRr......",
    "......rRr.......",
    ".......r........",
    "................",
    "....p.....p.....",
    "................",
]
HEART_P = {"r": (110, 10, 20), "R": (200, 30, 40), "w": (255, 230, 230), "p": (240, 240, 250)}

MACE = [
    "................",
    ".........w....w.",
    ".........ww..ww.",
    "..........iIIi..",
    ".........iIIIIi.",
    ".........ieIIei.",
    ".........iIIIIi.",
    "..........iIIi..",
    "..........inni..",
    ".........hiii...",
    "........hh......",
    ".......hh.......",
    "......hh........",
    ".....kk.........",
    "....kk..........",
    "...gg...........",
]
MACE_P = {"w": (236, 228, 204), "i": (110, 110, 118), "I": (176, 178, 186), "e": (200, 40, 30),
          "n": (60, 60, 66), "h": (120, 78, 40), "k": (70, 44, 24), "g": (220, 176, 60)}

BANNER_ITEM = [
    "...g............",
    "...p............",
    "...puuuuuuuuu...",
    "...puuuuuuuuu...",
    "...prrrrrrrrr...",
    "...prrryyyrrr...",
    "...prryysyyrr...",
    "...prrryyyrrr...",
    "...prrrrrrrrr...",
    "...pyyyyyyyyy...",
    "...py.y.y.y.y...",
    "...p............",
    "...p............",
    "...p............",
    "...p............",
    "...p............",
]
CLOTH = [
    "uuuuuuuuuuuuuuuu",
    "uUuuuuuuuuuuuUuu",
    "uuuuuuuuuuuuuuuu",
    "rrrrrrrrrrrrrrrr",
    "rrrrrrryrrrrrrrr",
    "rrrrrryyyrrrrrrr",
    "rrrrryysyyrrrrrr",
    "rryyyysssyyyyrrr",
    "rrrrryysyyrrrrrr",
    "rrrrrryyyrrrrrrr",
    "rrrrrrryrrrrrrrr",
    "rrrrrrrrrrrrrrrr",
    "yyyyyyyyyyyyyyyy",
    "yYyyyYyyyYyyyYyy",
    "ybybybybybybybyb",
    "bbbbbbbbbbbbbbbb",
]
BANNER_P = {"g": (240, 200, 60), "p": (90, 60, 34), "u": (96, 40, 130), "U": (130, 70, 170),
            "r": (176, 34, 40), "y": (232, 186, 52), "Y": (250, 220, 120), "s": (90, 200, 220),
            "b": (120, 80, 40)}


def spawn_egg(base, spots, path, seed):
    r = random.Random(seed)
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()
    for y in range(16):
        for x in range(16):
            # Egg: wider at the bottom.
            cy = 8.6
            ry = 6.6
            rx = 4.6 + (y - 3) * 0.08
            if ((x - 7.5) / rx) ** 2 + ((y - cy) / ry) ** 2 <= 1.0:
                edge = ((x - 7.5) / (rx - 1)) ** 2 + ((y - cy) / (ry - 1)) ** 2 > 1.0
                col = tuple(int(c * 0.6) for c in base) if edge else base
                if not edge and r.random() < 0.22:
                    col = spots
                if not edge and x < 7 and y < 7 and (x + y) % 5 == 0:
                    col = tuple(min(255, c + 50) for c in col)
                px[x, y] = col + (255,)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    img.save(path)


# ------------------------------------------------------------------ data

EGGS = {
    "div": ((72, 88, 62), (222, 210, 178)),
    "white_div": ((226, 228, 220), (30, 28, 30)),
    "manticore": ((196, 146, 72), (96, 30, 26)),
    "simurgh": ((36, 128, 120), (226, 176, 58)),
    "huma": ((238, 196, 72), (252, 250, 236)),
    "peri": ((196, 150, 226), (255, 196, 228)),
}

LANG = {
    "itemGroup.shahnameh": "Shahnameh: Legends of Persia",

    "entity.shahnameh.div": "Div",
    "entity.shahnameh.white_div": "Div-e Sepid, the White Div",
    "entity.shahnameh.manticore": "Manticore",
    "entity.shahnameh.simurgh": "Simurgh",
    "entity.shahnameh.huma": "Huma",
    "entity.shahnameh.peri": "Peri",

    "item.shahnameh.simurgh_feather": "Simurgh Feather",
    "item.shahnameh.simurgh_feather.lore": "Burn it, and she will come. (Right-click to call the Simurgh)",
    "item.shahnameh.div_horn": "Div Horn",
    "item.shahnameh.div_horn.lore": "Taken from a demon of Mazandaran",
    "item.shahnameh.white_div_heart": "Heart of the White Div",
    "item.shahnameh.white_div_heart.lore": "Its blood restored the sight of Kay Kavus",
    "item.shahnameh.ox_mace": "Gorz-e Gavsar",
    "item.shahnameh.ox_mace.lore": "Fereydun's ox-headed mace. Stuns foes; crushes Divs",
    "block.shahnameh.kaveh_banner": "Derafsh Kaviani",
    "item.shahnameh.kaveh_banner": "Derafsh Kaviani",

    "item.shahnameh.div_spawn_egg": "Div Spawn Egg",
    "item.shahnameh.white_div_spawn_egg": "White Div Spawn Egg",
    "item.shahnameh.manticore_spawn_egg": "Manticore Spawn Egg",
    "item.shahnameh.simurgh_spawn_egg": "Simurgh Spawn Egg",
    "item.shahnameh.huma_spawn_egg": "Huma Spawn Egg",
    "item.shahnameh.peri_spawn_egg": "Peri Spawn Egg",

    "message.shahnameh.simurgh.gift": "The Simurgh plucks three golden feathers and lays them in your hands.",
    "message.shahnameh.simurgh.already_gifted": "The Simurgh has already given her gift.",
    "message.shahnameh.simurgh.answers": "Wings darken the sky. The Simurgh has answered.",
    "message.shahnameh.huma.shadow": "The Huma's shadow passes over you. You feel fortunate.",
    "message.shahnameh.huma.curse": "You struck the Huma. Misfortune follows you.",
    "message.shahnameh.peri.prank": "Giggling echoes nearby... something in your pack has moved.",
    "message.shahnameh.peri.blessing": "A Peri touches your shoulder. Luck is with you.",
}


def item_model(name, texture=None, parent="minecraft:item/generated"):
    texture = texture or f"{NS}:item/{name}"
    write_json(f"{A}/models/item/{name}.json", {"parent": parent, "textures": {"layer0": texture}})
    write_json(f"{A}/items/{name}.json", {"model": {"type": "minecraft:model", "model": f"{NS}:item/{name}"}})


def count(lo, hi):
    return {"type": "minecraft:uniform", "min": float(lo), "max": float(hi)}


def drop(item, lo, hi, looting=True):
    functions = [{"function": "minecraft:set_count", "count": count(lo, hi)}]
    if looting:
        functions.append({"function": "minecraft:enchanted_count_increase", "enchantment": "minecraft:looting",
                          "count": count(0, 1)})
    return {"rolls": 1.0, "entries": [{"type": "minecraft:item", "name": item, "functions": functions}]}


def spawns(name, biomes, entity, weight, lo, hi):
    write_json(f"{D}/forge/biome_modifier/{name}.json", {
        "type": "forge:add_spawns",
        "biomes": biomes,
        "spawners": [{"type": f"{NS}:{entity}", "weight": weight, "minCount": lo, "maxCount": hi}],
    })


def main():
    tex_item = f"{A}/textures/item"
    art(FEATHER, FEATHER_P, f"{tex_item}/simurgh_feather.png")
    art(HORN, HORN_P, f"{tex_item}/div_horn.png")
    art(HEART, HEART_P, f"{tex_item}/white_div_heart.png")
    art(MACE, MACE_P, f"{tex_item}/ox_mace.png")
    art(BANNER_ITEM, BANNER_P, f"{tex_item}/kaveh_banner.png")
    art(CLOTH, BANNER_P, f"{A}/textures/block/kaveh_banner_cloth.png")
    for i, (mob, (base, spots)) in enumerate(EGGS.items()):
        spawn_egg(base, spots, f"{tex_item}/{mob}_spawn_egg.png", 100 + i)
        item_model(f"{mob}_spawn_egg")

    item_model("simurgh_feather")
    item_model("div_horn")
    item_model("white_div_heart")
    item_model("ox_mace", parent="minecraft:item/handheld")
    item_model("kaveh_banner")

    # Block: a pole with a crossbar and the banner cloth hanging from it.
    log = "minecraft:block/stripped_dark_oak_log"
    gold = "minecraft:block/gold_block"
    cloth = f"{NS}:block/kaveh_banner_cloth"

    def cube(frm, to, tex, uv_full=False):
        faces = {}
        for f in ("north", "south", "east", "west", "up", "down"):
            face = {"texture": tex}
            if uv_full:
                face["uv"] = [0, 0, 16, 16]
            faces[f] = face
        return {"from": frm, "to": to, "faces": faces}

    write_json(f"{A}/models/block/kaveh_banner.json", {
        "parent": "minecraft:block/block",
        "textures": {"particle": cloth, "log": log, "gold": gold, "cloth": cloth},
        "elements": [
            cube([7.25, 0, 7.25], [8.75, 30, 8.75], "#log"),
            cube([6.75, 30, 6.75], [9.25, 32, 9.25], "#gold"),
            cube([1.5, 27.5, 7.5], [14.5, 28.75, 8.5], "#log"),
            cube([2, 11, 7.75], [14, 27.5, 8.25], "#cloth", uv_full=True),
        ],
    })
    write_json(f"{A}/blockstates/kaveh_banner.json", {"variants": {"": {"model": f"{NS}:block/kaveh_banner"}}})

    write_json(f"{A}/lang/en_us.json", LANG)

    # Loot
    write_json(f"{D}/loot_table/entities/div.json", {"type": "minecraft:entity", "pools": [
        drop(f"{NS}:div_horn", 0, 2), drop("minecraft:bone", 0, 3)]})
    write_json(f"{D}/loot_table/entities/white_div.json", {"type": "minecraft:entity", "pools": [
        drop(f"{NS}:white_div_heart", 1, 1, looting=False), drop(f"{NS}:div_horn", 4, 6),
        drop("minecraft:diamond", 1, 3)]})
    write_json(f"{D}/loot_table/entities/manticore.json", {"type": "minecraft:entity", "pools": [
        drop("minecraft:leather", 1, 3), drop("minecraft:spider_eye", 0, 1)]})
    write_json(f"{D}/loot_table/blocks/kaveh_banner.json", {"type": "minecraft:block", "pools": [{
        "rolls": 1.0, "conditions": [{"condition": "minecraft:survives_explosion"}],
        "entries": [{"type": "minecraft:item", "name": f"{NS}:kaveh_banner"}]}]})

    # Recipes
    write_json(f"{D}/recipe/ox_mace.json", {
        "type": "minecraft:crafting_shaped", "category": "equipment",
        "key": {"H": f"{NS}:div_horn", "B": "minecraft:iron_block", "S": "minecraft:stick"},
        "pattern": ["HBH", " S ", " S "], "result": {"id": f"{NS}:ox_mace"}})
    write_json(f"{D}/recipe/kaveh_banner.json", {
        "type": "minecraft:crafting_shaped", "category": "misc",
        "key": {"L": "minecraft:leather", "D": "minecraft:diamond", "G": "minecraft:gold_ingot", "S": "minecraft:stick"},
        "pattern": ["LDL", "LGL", " S "], "result": {"id": f"{NS}:kaveh_banner"}})

    # Tags
    write_json(f"{D}/tags/entity_type/divs.json", {"values": [f"{NS}:div", f"{NS}:white_div"]})
    write_json(f"{D}/tags/entity_type/banner_allies.json", {"values": [
        "minecraft:player", "minecraft:villager", "minecraft:iron_golem", "minecraft:wandering_trader"]})

    # Natural spawning
    spawns("div_forest", "#minecraft:is_forest", "div", 12, 1, 2)
    spawns("div_taiga", "#minecraft:is_taiga", "div", 10, 1, 2)
    spawns("div_mountain", "#minecraft:is_mountain", "div", 10, 1, 2)
    spawns("manticore_savanna", "#minecraft:is_savanna", "manticore", 25, 1, 1)
    spawns("manticore_badlands", "#minecraft:is_badlands", "manticore", 20, 1, 1)
    spawns("simurgh_peaks", ["minecraft:jagged_peaks", "minecraft:frozen_peaks", "minecraft:stony_peaks"], "simurgh", 3, 1, 1)
    spawns("huma_meadows", ["minecraft:meadow", "minecraft:sunflower_plains", "minecraft:savanna_plateau"], "huma", 4, 1, 1)
    spawns("peri_gardens", ["minecraft:flower_forest", "minecraft:meadow", "minecraft:cherry_grove"], "peri", 10, 1, 2)
    print("assets written")


if __name__ == "__main__":
    main()
