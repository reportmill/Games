package tetris;
import snap.gfx.*;
import snap.view.*;

/**
 * A custom class.
 */
public class Block extends View {
    
    // The pattern
    Pattern  _pattern;
    
    // Constants
    static int TILE_SIZE = 20;
    static int PIECE_COUNT = 7;
    static Pattern SQUARE, STICK, BOAT, L1, L2, S1, S2, ALL[];

/**
 * Creates Patterns.
 */    
static
{
    SQUARE = new Pattern(2, 2, Color.BLUE.brighter().brighter(), new int[] { 0, 0, 0, 1, 1, 0, 1, 1 });
    STICK = new Pattern(4, 1, Color.MAGENTA, new int[] { 0, 0, 0, 1, 0, 2, 0, 3 });
    BOAT = new Pattern(2, 3, Color.GREEN, new int[] { 0, 0, 1, 0, 2, 0, 1, 1 });
    L1 = new Pattern(3, 2, Color.YELLOW, new int[] { 0, 0, 0, 1, 0, 2, 1, 2 });
    L2 = new Pattern(3, 2, Color.ORANGE, new int[] { 1, 0, 1, 1, 0, 2, 1, 2 });
    S1 = new Pattern(2, 3, Color.PINK, new int[] { 0, 0, 1, 0, 1, 1, 2, 1 });
    S2 = new Pattern(2, 3, Color.CYAN, new int[] { 1, 0, 2, 0, 0, 1, 1, 1 });
    ALL = new Pattern[] { SQUARE, STICK, BOAT, L1, L2, S1, S2 };
}

/**
 * Creates a Block.
 */
public Block()
{
    int patInd = (int)Math.floor(Math.random()*PIECE_COUNT);
    _pattern = ALL[patInd];
    setSize(_pattern.colCount*TILE_SIZE, _pattern.rowCount*TILE_SIZE);
    setPrefSize(_pattern.colCount*TILE_SIZE, _pattern.rowCount*TILE_SIZE);
}

/**
 * Paint block pattern.
 */
protected void paintFront(Painter aPntr)
{
    // Iterate over pattern fill x/y pairs
    for(int i=0;i<_pattern.fill.length;i++) {
        double x = _pattern.fill[i++]*TILE_SIZE;
        double y = _pattern.fill[i]*TILE_SIZE;
        aPntr.drawImage(_pattern.image, x, y);
    }
}

/**
 * Returns the number of tiles.
 */
public int getTileCount()  { return _pattern.fill.length/2; }

/**
 * Returns the tile rect at given index.
 */
public Rect getTileRectInParent(int anIndex)
{
    double x = _pattern.fill[anIndex*2]*TILE_SIZE;
    double y = _pattern.fill[anIndex*2+1]*TILE_SIZE;
    return new Rect(getX() + x, getY() + y, TILE_SIZE, TILE_SIZE);
}

/**
 * Rotate right.
 */
public void rotateRight()
{
    _pattern = _pattern.getRotateRight();
    setSize(_pattern.colCount*TILE_SIZE, _pattern.rowCount*TILE_SIZE);
    setPrefSize(_pattern.colCount*TILE_SIZE, _pattern.rowCount*TILE_SIZE);
}

/**
 * Creates the image.
 */
static Image getImage(Color aColor)
{
    View view = new View() { };
    view.setSize(TILE_SIZE, TILE_SIZE);
    view.setPrefSize(TILE_SIZE, TILE_SIZE);
    view.setBorder(aColor.blend(Color.BLACK,.1), 1);
    view.setFill(aColor);
    view.setEffect(new EmbossEffect(60, 120, 4));
    return ViewUtils.getImage(view);
}

/**
 * A class to represent pattern.
 */
static class Pattern {
    
    public int rowCount, colCount;
    public int fill[];
    public Color color;
    public Image image;
    public Pattern(int rc, int cc, Color c, int f[])
    {
        rowCount = rc; colCount = cc; color = c; fill = f; image = getImage(c);
    }
    
    public Pattern getRotateRight()
    {
        int f2[] = new int[fill.length];
        double mx = colCount, my = rowCount;
        Transform xfm = new Transform(mx/2, my/2); xfm.rotate(-90); xfm.translate(-mx/2, -my/2);
        Point or = xfm.transform(colCount, 0); xfm.preTranslate(-or.x, -or.y);
        for(int i=0;i<fill.length;i+=2) {
            Point p = xfm.transform(fill[i] + .5, fill[i+1] + .5);
            f2[i] = (int)Math.round(p.x - .5);
            f2[i+1] = (int)Math.round(p.y - .5);
        }
        return new Pattern(colCount, rowCount, color, f2);
    }
}

static String fmt(double v) { return snap.util.StringUtils.formatNum("#.#", v); }

}