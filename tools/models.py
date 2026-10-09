"""
Generates mob geometry (Java LayerDefinitions) and matching box-UV textures.

Each model is a tree of parts. Each part has a pivot, a rotation (degrees) and
a list of cubes. Each cube has its own colour/painter, and gets its own UV
region packed automatically, so geometry and texture always line up.

Coordinates follow Minecraft entity-model space: y points DOWN, ground is
y = 24, and the model faces -Z (the "front" UV region is the -Z face).
"""
import math
import random
from PIL import Image

# ---------------------------------------------------------------- data types

class Cube:
    def __init__(self, x, y, z, w, h, d, painter):
        self.x, self.y, self.z = x, y, z
        self.w, self.h, self.d = int(w), int(h), int(d)
        self.painter = painter
        self.u = self.v = 0


class Part:
    def __init__(self, name, pivot=(0, 0, 0), rot=(0, 0, 0), cubes=(), children=()):
        self.name = name
        self.pivot = pivot
        self.rot = rot
        self.cubes = list(cubes)
        self.children = list(children)


def C(x, y, z, w, h, d, painter):
    return Cube(x, y, z, w, h, d, painter)


# ---------------------------------------------------------------- painters
# A painter is called with (img, cube) and paints the cube's six UV faces.

def _rng(seed):
    return random.Random(seed)


def _jitter(col, amt, r):
    return tuple(max(0, min(255, c + r.randint(-amt, amt))) for c in col)


def _shade(col, f):
    return tuple(max(0, min(255, int(c * f))) for c in col)


def faces(cube):
    """Returns dict face -> (x0, y0, w, h) in texture pixels (box UV)."""
    u, v, w, h, d = cube.u, cube.v, cube.w, cube.h, cube.d
    return {
        "top": (u + d, v, w, d),
        "bottom": (u + d + w, v, w, d),
        "right": (u, v + d, d, h),
        "front": (u + d, v + d, w, h),
        "left": (u + d + w, v + d, d, h),
        "back": (u + d + w + d, v + d, w, h),
    }


FACE_SHADE = {"top": 1.12, "bottom": 0.72, "front": 1.0, "back": 0.9, "left": 0.85, "right": 0.85}


def solid(col, noise=8, seed=1, spots=None, spot_rate=0.0):
    def paint(img, cube):
        r = _rng(seed + cube.u * 7 + cube.v * 13)
        px = img.load()
        for name, (x0, y0, w, h) in faces(cube).items():
            base = _shade(col, FACE_SHADE[name])
            for yy in range(y0, y0 + h):
                for xx in range(x0, x0 + w):
                    c = _jitter(base, noise, r)
                    if spots and r.random() < spot_rate:
                        c = _jitter(_shade(spots, FACE_SHADE[name]), noise, r)
                    px[xx, yy] = c + (255,)
    return paint


def gradient(top_col, bottom_col, noise=6, seed=2):
    """Vertical gradient on the side faces (good for feathers and dresses)."""
    def paint(img, cube):
        r = _rng(seed + cube.u * 3 + cube.v)
        px = img.load()
        for name, (x0, y0, w, h) in faces(cube).items():
            for yy in range(y0, y0 + h):
                t = 0 if h <= 1 else (yy - y0) / (h - 1)
                if name in ("top",):
                    t = 0
                if name in ("bottom",):
                    t = 1
                base = tuple(int(a + (b - a) * t) for a, b in zip(top_col, bottom_col))
                base = _shade(base, FACE_SHADE[name])
                for xx in range(x0, x0 + w):
                    px[xx, yy] = _jitter(base, noise, r) + (255,)
    return paint


def feathers(col_a, col_b, edge, seed=3):
    """Scaled feather pattern: alternating rows with darker feather edges."""
    def paint(img, cube):
        r = _rng(seed + cube.u + cube.v * 5)
        px = img.load()
        for name, (x0, y0, w, h) in faces(cube).items():
            for yy in range(y0, y0 + h):
                for xx in range(x0, x0 + w):
                    lx, ly = xx - x0, yy - y0
                    row = ly // 2
                    col = col_a if (lx // 3 + row) % 2 == 0 else col_b
                    if ly % 2 == 1 and (lx + row) % 3 == 0:
                        col = edge
                    px[xx, yy] = _jitter(_shade(col, FACE_SHADE[name]), 7, r) + (255,)
    return paint


def peacock(base, ring, eye, seed=4):
    """Peacock-tail pattern of 'eyes' on the top face, plain elsewhere."""
    def paint(img, cube):
        solid(base, 6, seed)(img, cube)
        px = img.load()
        x0, y0, w, h = faces(cube)["top"]
        for cy in range(y0 + 3, y0 + h - 1, 5):
            for cx in range(x0 + 2 + ((cy // 5) % 2) * 2, x0 + w - 1, 4):
                for dy in (-1, 0, 1):
                    for dx in (-1, 0, 1):
                        if x0 <= cx + dx < x0 + w and y0 <= cy + dy < y0 + h:
                            px[cx + dx, cy + dy] = ring + (255,)
                if x0 <= cx < x0 + w:
                    px[cx, cy] = eye + (255,)
    return paint


def with_face(painter, face_fn):
    """Paint normally, then draw details on the front face with face_fn."""
    def paint(img, cube):
        painter(img, cube)
        x0, y0, w, h = faces(cube)["front"]
        face_fn(img.load(), x0, y0, w, h)
    return paint


def eyes(col, y_frac=0.45, gap=None, size=1, brow=None, mouth=None, beard=None):
    def fn(px, x0, y0, w, h):
        g = gap if gap is not None else max(1, w // 4)
        ey = y0 + int(h * y_frac)
        cx = x0 + w // 2
        for s in (-1, 1):
            ex = cx + s * g - (1 if s < 0 else 0)
            for dy in range(size):
                for dx in range(size):
                    px[ex + (dx if s > 0 else -dx), ey + dy] = col + (255,)
            if brow:
                for dx in range(size + 1):
                    px[ex + (dx if s > 0 else -dx), ey - 1] = brow + (255,)
        if mouth:
            my = ey + size + max(1, h // 5)
            for dx in range(-max(1, w // 6), max(1, w // 6) + 1):
                if y0 <= my < y0 + h:
                    px[cx + dx - (1 if w % 2 == 0 and dx < 0 else 0), my] = mouth + (255,)
        if beard:
            for yy in range(ey + size + 1, y0 + h):
                for xx in range(x0 + 1, x0 + w - 1):
                    if (xx + yy) % 3 != 0:
                        px[xx, yy] = beard + (255,)
    return fn


# ---------------------------------------------------------------- packing

def all_cubes(part):
    for c in part.cubes:
        yield c
    for ch in part.children:
        yield from all_cubes(ch)


def pack(model_root, width):
    cubes = sorted(all_cubes(model_root), key=lambda c: -(c.d + c.h))
    x = y = shelf = 0
    for c in cubes:
        cw, ch = 2 * (c.w + c.d), c.d + c.h
        if cw > width:
            raise ValueError("cube too wide for texture")
        if x + cw > width:
            x, y, shelf = 0, y + shelf, 0
        c.u, c.v = x, y
        x += cw
        shelf = max(shelf, ch)
    height = y + shelf
    size = width
    while size < height:
        size *= 2
    return width, size


# ---------------------------------------------------------------- outputs

def fmt(f):
    f = float(f)
    if f == int(f):
        return f"{int(f)}.0F"
    return f"{round(f, 4)}F"


def java_part(part, parent_var, lines, counter):
    var = f"p{counter[0]}"
    counter[0] += 1
    cubes = "CubeListBuilder.create()"
    for c in part.cubes:
        cubes += (f".texOffs({c.u}, {c.v}).addBox({fmt(c.x)}, {fmt(c.y)}, {fmt(c.z)}, "
                  f"{fmt(c.w)}, {fmt(c.h)}, {fmt(c.d)})")
    px, py, pz = part.pivot
    rx, ry, rz = (math.radians(a) for a in part.rot)
    pose = (f"PartPose.offsetAndRotation({fmt(px)}, {fmt(py)}, {fmt(pz)}, "
            f"{fmt(rx)}, {fmt(ry)}, {fmt(rz)})")
    lines.append(f"        PartDefinition {var} = {parent_var}.addOrReplaceChild(\"{part.name}\", {cubes}, {pose});")
    for ch in part.children:
        java_part(ch, var, lines, counter)


def java_method(method_name, roots, tw, th):
    lines = [f"    public static LayerDefinition {method_name}() {{",
             "        MeshDefinition mesh = new MeshDefinition();",
             "        PartDefinition root = mesh.getRoot();"]
    counter = [0]
    for p in roots:
        java_part(p, "root", lines, counter)
    lines.append(f"        return LayerDefinition.create(mesh, {tw}, {th});")
    lines.append("    }")
    return "\n".join(lines)


def texture(roots, width):
    holder = Part("__root", children=roots)
    tw, th = pack(holder, width)
    img = Image.new("RGBA", (tw, th), (0, 0, 0, 0))
    for c in all_cubes(holder):
        c.painter(img, c)
    return img, tw, th


# ---------------------------------------------------------------- preview

def _rotmat(rx, ry, rz):
    cx, sx = math.cos(rx), math.sin(rx)
    cy, sy = math.cos(ry), math.sin(ry)
    cz, sz = math.cos(rz), math.sin(rz)
    Rx = [[1, 0, 0], [0, cx, -sx], [0, sx, cx]]
    Ry = [[cy, 0, sy], [0, 1, 0], [-sy, 0, cy]]
    Rz = [[cz, -sz, 0], [sz, cz, 0], [0, 0, 1]]

    def mul(a, b):
        return [[sum(a[i][k] * b[k][j] for k in range(3)) for j in range(3)] for i in range(3)]
    return mul(Rz, mul(Ry, Rx))


def _apply(m, t, v):
    return tuple(t[i] + sum(m[i][k] * v[k] for k in range(3)) for i in range(3))


def world_boxes(part, m=None, t=(0, 0, 0)):
    """Yields 8-corner lists in model space (for previews/sanity checks)."""
    if m is None:
        m = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]
    pm = _rotmat(*(math.radians(a) for a in part.rot))
    nm = [[sum(m[i][k] * pm[k][j] for k in range(3)) for j in range(3)] for i in range(3)]
    nt = _apply(m, t, part.pivot)
    for c in part.cubes:
        corners = []
        for dx in (0, c.w):
            for dy in (0, c.h):
                for dz in (0, c.d):
                    corners.append(_apply(nm, nt, (c.x + dx, c.y + dy, c.z + dz)))
        yield corners, c
    for ch in part.children:
        yield from world_boxes(ch, nm, nt)


def preview(roots, path, scale=4):
    """Side (+X looking) and front views, flat-shaded silhouettes."""
    boxes = [b for r in roots for b in world_boxes(r)]
    pts = [p for b, _ in boxes for p in b]
    minx, maxx = min(p[0] for p in pts), max(p[0] for p in pts)
    miny, maxy = min(p[1] for p in pts), max(p[1] for p in pts)
    minz, maxz = min(p[2] for p in pts), max(p[2] for p in pts)
    miny = min(miny, 0)
    maxy = max(maxy, 24)
    W = int((maxz - minz) + (maxx - minx) + 12) * scale
    H = int(maxy - miny + 6) * scale
    img = Image.new("RGB", (W, H), (235, 235, 240))
    from PIL import ImageDraw
    dr = ImageDraw.Draw(img)
    gy = (24 - miny + 3) * scale
    dr.line([(0, gy), (W, gy)], fill=(120, 160, 90), width=2)
    for corners, c in boxes:
        col = (100, 100, 110)
        # side view: horizontal = z, vertical = y
        xs = [(p[2] - minz + 3) * scale for p in corners]
        ys = [(p[1] - miny + 3) * scale for p in corners]
        dr.polygon(_hull(list(zip(xs, ys))), fill=col, outline=(30, 30, 30))
        # front view: horizontal = x
        off = (maxz - minz + 9) * scale
        xs = [off + (p[0] - minx) * scale for p in corners]
        dr.polygon(_hull(list(zip(xs, ys))), fill=col, outline=(30, 30, 30))
    img.save(path)


def _hull(points):
    pts = sorted(set(points))
    if len(pts) <= 2:
        return pts + pts[:1]

    def cross(o, a, b):
        return (a[0] - o[0]) * (b[1] - o[1]) - (a[1] - o[1]) * (b[0] - o[0])
    lower, upper = [], []
    for p in pts:
        while len(lower) >= 2 and cross(lower[-2], lower[-1], p) <= 0:
            lower.pop()
        lower.append(p)
    for p in reversed(pts):
        while len(upper) >= 2 and cross(upper[-2], upper[-1], p) <= 0:
            upper.pop()
        upper.append(p)
    return lower[:-1] + upper[:-1]
