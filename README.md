# Shahnameh: Legends of Persia

A Minecraft **26.2 Forge** mod with creatures and treasures from Ferdowsi's *Shahnameh* and older Persian myth.

## What's in it

| | What it does | Where to find it |
|---|---|---|
| **Simurgh** | Giant benevolent bird. Swoops down to greet you; right-click her with an empty hand and she gives you 3 **Simurgh Feathers** (once per Simurgh). | Rare, over jagged, frozen and stony peaks |
| **Simurgh Feather** | Right-click to "burn" it: the Simurgh appears, fully heals you, clears poison/wither/blindness, and gives Regeneration and Absorption. | Gift from a Simurgh |
| **Div** | Horned demon giant. Hunts players and villagers at night. Drops **Div Horns**. | Forests, taigas, mountains at night |
| **Div-e Sepid (White Div)** | Boss with a health bar. Ground slam throws you back; his roar blinds. Drops his **Heart**, Div Horns and diamonds. | Spawn egg for now (a quest will lead to him later) |
| **Heart of the White Div** | Right-click: cures Blindness and Darkness, grants 8 minutes of Night Vision. | White Div |
| **Gorz-e Gavsar (ox-headed mace)** | 11 attack damage, slows and weakens what it hits, +50% damage to Divs. | Crafted |
| **Derafsh Kaviani (Kaveh's Banner)** | Place it: players, villagers and iron golems within 12 blocks get Strength and Resistance. | Crafted |
| **Manticore** | Lion body, human face, scorpion tail. Fast, heavily armored, poisonous sting. Roars before it strikes. | Savannas and badlands at night |
| **Huma** | Sacred golden bird that never lands. Pass under its shadow for Luck. Hurt it and you're cursed with bad luck. | Meadows, sunflower plains, savanna plateaus |
| **Peri** | Tiny winged trickster. Every few minutes she either swaps two items in your inventory or blesses you with Luck. | Flower forests, meadows, cherry groves |

### Recipes

```
Gorz-e Gavsar          Derafsh Kaviani
H B H                  L D L
  S                    L G L
  S                      S
H = Div Horn           L = Leather, D = Diamond
B = Iron Block         G = Gold Ingot
S = Stick              S = Stick
```

Everything is also in the **Shahnameh** creative tab, including spawn eggs.

## Building the mod with GitHub (no Java install needed)

1. Sign in at github.com (or create a free account).
2. Click **+** (top right) → **New repository**. Name it `shahnameh-mod`, keep it **Private** or Public, and click **Create repository**.
3. On the new repo page, click **uploading an existing file**. Drag in **everything inside** the `shahnameh` folder (not the folder itself), then click **Commit changes**.
4. Check that a `.github` folder appears in the file list. Some computers hide folders whose names start with a dot and skip them during upload. If it's missing: click **Add file → Create new file**, type `.github/workflows/build.yml` as the name, paste in the contents of `build.yml` from this project, and commit.
5. Open the **Actions** tab. A "Build mod" run starts automatically (about 5–10 minutes the first time). If GitHub asks you to enable Actions, click the green button.
6. When it shows a green check, click the run, scroll to **Artifacts**, and download **shahnameh-mod**. Unzip it to get `shahnameh-26.2-0.1.0.jar`.

If the run shows a red X, click it, open the failed step, copy the error text, and paste it back to Claude.

## Installing and playing

1. Install **Forge 26.2** (recommended build, 65.1.x) from files.minecraftforge.net using the installer, and launch the Forge profile once.
2. Put `shahnameh-26.2-0.1.0.jar` into your `.minecraft/mods` folder (with Minecraft closed).
3. Launch the Forge 26.2 profile and create a **new world** (natural spawns only happen in newly generated areas for the birds).

## For developers

- Mob geometry and textures come from `tools/mobs.py`; item art and JSON from `tools/assets.py`. Edit those and re-run `python3 tools/mobs.py && python3 tools/assets.py` instead of editing generated files.
- Local build: install JDK 25, then `gradle build` (Gradle 9.5) or `./gradlew build`.
