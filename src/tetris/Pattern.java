package tetris;
import snap.gfx.*;
import snap.view.*;

/**
 * A class to represent a pattern for a game block.
 */
public class Pattern {

    // The number of tiles
    public int tileCount;
    
    // The number of columns, rows and tiles
    public int colCount, rowCount;
    
    // Array of packed (col,row) pairs of tiles for pattern
    public int fill[];
    
    // The color of pattern
    public Color color;
    
    // The image
    public Image image;

    // Constants
    static int TILE_SIZE = 20;
    static Pattern SQUARE, STICK, BOAT, L1, L2, S1, S2, ALL[];
    static Effect BLOCK_EFFECT = new ShadowEffect(8,Color.BLACK,0,0);
    
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
 * Creates a new pattern for row/col count, color and tile coords array.
 */
private Pattern(int rc, int cc, Color c, int f[])
{
    rowCount = rc; colCount = cc; color = c; fill = f; image = getImage(c);
    tileCount = fill.length/2;
}

/**
 * Paints the pattern to given painter.
 */
public void paint(Painter aPntr)
{
    // Iterate over fill col/row pairs
    for(int i=0;i<fill.length;i++) {
        double x = fill[i++]*TILE_SIZE;
        double y = fill[i]*TILE_SIZE;
        aPntr.drawImage(image, x, y);
    }
}

/**
 * Returns the pattern derived by rotating this pattern clockwise.
 */
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

/**
 * Creates an image of a tile for given color.
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

}