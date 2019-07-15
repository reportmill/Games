package jewels;
import java.util.*;
import snap.gfx.*;
import snap.view.*;

/**
 * A View to hold the play area for a match 3 game.
 */
public class PlayView extends ParentView {

    // The gems
    Gem         _gems[][] = new Gem[GRID_WIDTH][GRID_HEIGHT];
    
    // The last gem hit by mouse press
    Gem         _pressGem;
    
    // The timer
    ViewTimer   _timer = new ViewTimer(25, t -> timerFired());
    
    // The grid width/height
    public static int GRID_WIDTH = 8;
    public static int GRID_HEIGHT = 8;
    public static int TILE_SIZE = 64;
    public static int BORDER_SIZE = 2;
    public static int GEM_SPEED = 140;
    public static int ROW_SPEEDS[] = new int[GRID_WIDTH];

/**
 * Creates a PlayView.
 */
public PlayView()
{
    // Create background texture
    Image img = Image.get(PlayView.class, "pkg.images/Cloth.jpg");
    img.addLoadListener(pc -> backImageLoaded(img));
    
    // Set PlayView fill, border, PrefSize
    setBorder(Color.BLACK, 2);
    setPrefSize(GRID_WIDTH*TILE_SIZE + BORDER_SIZE*2, GRID_HEIGHT*TILE_SIZE + BORDER_SIZE*2);
    setClipToBounds(true);
    enableEvents(MousePress, MouseDrag);
}

/**
 * Starts the game.
 */
public void startGame()
{
    // Set (or reset) row speeds
    for(int i=0;i<GRID_WIDTH;i++) ROW_SPEEDS[i] = 120 + (int)Math.round(Math.random()*40);
    
    // Clear/reload gems
    clearGems();
    reloadGems();
}

/**
 * Called when background texture image is loaded.
 */
void backImageLoaded(Image anImage)
{
    anImage.getPainter();
    Painter pntr = anImage.getPainter(); pntr.setColor(new Color("#44998833"));
    pntr.drawRect(.5,.5, anImage.getWidth()-1, anImage.getHeight()-1);
    ImagePaint paint = new ImagePaint(anImage, BORDER_SIZE, BORDER_SIZE, 64, 64, false);
    setFill(paint);
}

/**
 * Pauses game.
 */
public void pauseGame()
{
    if(_timer.isRunning()) _timer.pause();
    else _timer.start();
}

/**
 * Called when timer fires.
 */
void timerFired()  { }

/**
 * Returns the gem at given x/y.
 */
Gem getGem(int aCol, int aRow)
{
    if(aCol<0 || aCol>=GRID_WIDTH || aRow<0 || aRow>=GRID_HEIGHT) return null;
    return _gems[aCol][aRow];
}

/**
 * Sets the gem at given x/y.
 */
Gem setGem(Gem aGem, int aCol, int aRow)
{
    Gem oldGem = _gems[aCol][aRow]; _gems[aCol][aRow] = aGem;
    
    // Handle null
    if(aGem==null) {
        return oldGem;
    }
    
    // Handle setting new gem
    aGem.setColRow(aCol, aRow);
    Point pnt = gridToLocal(aCol, aRow);
    aGem.setXY(pnt.x, pnt.y);
    aGem.setTransX(0); aGem.setTransY(0);
    return oldGem;
}

/**
 * Sets the gem at given x/y.
 */
void setGemAnimated(Gem aGem, int aCol, int aRow)
{
    // Sets gem
    double x0 = aGem!=null? aGem.getX() + aGem.getTransX() : 0;
    double y0 = aGem!=null? aGem.getY() + aGem.getTransY() : 0;
    Gem oldGem = setGem(aGem, aCol, aRow);
    
    // If replacing with null, animate out and remove
    if(aGem==null) {
        oldGem.getAnimCleared(500).setScale(.01).setOpacity(.1).setOnFinish(a -> removeChild(oldGem)).play();
        return;
    }
    
    // Animate new gem into place
    double x1 = aGem.getX() + aGem.getTransX();
    double y1 = aGem.getY() + aGem.getTransY();
    double dx = x1 - x0, dy = y1 - y0, dist = Math.max(Math.abs(dx), Math.abs(dy));
    aGem.setTransX(-dx); aGem.setTransY(-dy);
    int time = (int)Math.round(dist*GEM_SPEED/TILE_SIZE);
    aGem.getAnimCleared(time).setTransX(0).setTransY(0).setLinear().play();
}

/**
 * Returns the gem at given x/y.
 */
Gem getGemAtXY(double aX, double aY)
{
    GridXY gpnt = localToGrid(aX, aY);
    return getGem(gpnt.x, gpnt.y);
}

/**
 * Returns the jewel for slot row/col, creating a new one if needed.
 */
Gem getGemAboveColRow(int aX, int aY)
{
    for(int i=aY-1;i>=0;i--) { Gem gem = getGem(aX, i);
        if(gem!=null) {
            setGem(null, aX, i); return gem; } }
    
    Gem gem = new Gem();
    Point pnt = gridToLocal(aX, -1);
    for(int i=0;i<GRID_HEIGHT;i++) { Gem g = _gems[aX][i]; if(g==null) continue;
        pnt.y = Math.min(pnt.y, g.getY() + g.getTransY() - TILE_SIZE); }
    gem.setXY(pnt.x, pnt.y);
    addChild(gem);
    return gem;
}

/**
 * Reloads the field of gems.
 */
void reloadGems()
{
    for(int j=GRID_HEIGHT-1;j>=0;j--) {
        for(int i=0;i<GRID_WIDTH;i++) {
            if(_gems[i][j]==null) {
                Gem gem = getGemAboveColRow(i, j);
                double oldY = gem.getY() - gem.getTransY();
                Point pnt = gridToLocal(i, j); gem.setY(pnt.y);
                double dy = gem.getY() - oldY;
                gem.setTransY(-dy);
                int time = (int)Math.round(dy*ROW_SPEEDS[i]/TILE_SIZE);
                gem.getAnimCleared(time).setTransY(0).setLinear().play();
                _gems[i][j] = gem; gem.setColRow(i,j); //setGem(gem, i, j);
            }
        }
    }
}

/**
 * Clears the gems.
 */
void clearGems()
{
    for(int i=0;i<GRID_WIDTH;i++) for(int j=0;j<GRID_HEIGHT;j++) { Gem gem = _gems[i][j];
        if(gem!=null) removeChild(gem);
        setGem(null, i, j);
    }
}

/**
 * Return the point in view coords for point in grid coords.
 */
public Point gridToLocal(int aX, int aY)  { return new Point(aX*TILE_SIZE+BORDER_SIZE, aY*TILE_SIZE+BORDER_SIZE); }

/**
 * Return the point in view coords for point in grid coords.
 */
public GridXY localToGrid(double aX, double aY)
{
   int x = (int)Math.floor((aX - BORDER_SIZE)/TILE_SIZE);
   int y = (int)Math.floor((aY - BORDER_SIZE)/TILE_SIZE);
   return new GridXY(x, y);
}

/**
 * Swaps two gems.
 */
public void swapGems(Gem aGem1, Gem aGem2)
{
    // Swap the two gems
    int col1 = aGem1.getCol(), row1 = aGem1.getRow();
    int col2 = aGem2.getCol(), row2 = aGem2.getRow();
    setGemAnimated(aGem1, col2, row2);
    setGemAnimated(aGem2, col1, row1);
    
    // Check and clear matched gems
    checkAndClearAllMatches();
}

/**
 * Checks for matches on all gems and clears them.
 */
void checkAndClearAllMatches()
{
    for(int i=0;i<GRID_WIDTH;i++)
        for(int j=0;j<GRID_HEIGHT;j++)
            checkAndClearGemMatches(i, j);
}

/**
 * Checks for matches on given gem and clears them.
 */
void checkAndClearGemMatches(int aCol, int aRow)
{
    Match m = getMatch(aCol, aRow);
    if(m!=null) {
        GridXY pnts[] = m.getPoints();
        for(GridXY pnt : pnts)
            setGemAnimated(null, pnt.x, pnt.y);
    }
}

/**
 * Returns any match found at given grid xy.
 */
public Match getMatch(int aCol, int aRow)
{
    Gem gem = getGem(aCol, aRow); if(gem==null) return null;
    int gid = gem.getId();
    int col0 = aCol, row0 = aRow, col1 = aCol, row1 = aRow;
    
    for(int i=aCol-1;i>=0;i--) if(getGemId(i, aRow)==gid) col0--; else break;
    for(int i=aCol+1;i<GRID_WIDTH;i++) if(getGemId(i, aRow)==gid) col1++; else break;
    for(int i=aRow-1;i>=0;i--) if(getGemId(aCol, i)==gid) row0--; else break;
    for(int i=aRow+1;i<GRID_HEIGHT;i++) if(getGemId(aCol, i)==gid) row1++; else break;
    int dx = col1 - col0; if(dx<2) col0 = col1 = aCol;
    int dy = row1 - row0; if(dy<2) row0 = row1 = aRow;
    if(dx<2 && dy<2) return null;
    return new Match(aCol, aRow, col0, row0, col1, row1);
}

/**
 * Returns whether gem at col/row matches id.
 */
public int getGemId(int aCol, int aRow)  { Gem g = getGem(aCol, aRow); return g!=null? g.getId() : -1; }

/**
 * Handle events.
 */
protected void processEvent(ViewEvent anEvent)
{
    // Handle MouseDown
    if(anEvent.isMousePress()) {
        _pressGem = getGemAtXY(anEvent.getX(), anEvent.getY());
    }

    // Handle MouseDrag
    else if(_pressGem!=null && anEvent.isMouseDrag()) {
        Size move = getDragChange(anEvent);
        if(Math.abs(move.width)>5 || Math.abs(move.height)>5) {
            int col = _pressGem.getCol(), row = _pressGem.getRow();
            if(Math.abs(move.width)>Math.abs(move.height)) col += move.width>0? 1 : -1;
            else row += move.height>0? 1 : -1;
            Gem gem2 = getGem(col, row); if(gem2==null) return;
            swapGems(_pressGem, gem2);
            _pressGem = null;
        }
    }
}

/**
 * Returns the drag change.
 */
public Size getDragChange(ViewEvent anEvent)
{
    Point pnt1 = anEvent.getPoint();
    Point pnt0 = ViewUtils.getMouseDown().getPoint(anEvent.getView());
    Size move = new Size(pnt1.x - pnt0.x, pnt1.y - pnt0.y);
    return move;
}

/**
 * A class to represent Grid x/y.
 */
static class GridXY {
    public int x, y;
    public GridXY(int aX, int aY)  { x = aX; y = aY; }
}

/**
 * A class to represent a match.
 */
static class Match {
    int col, row, col0, row0, col1, row1, dx, dy;
    
    /** Creates a Match for given col/row and extents. */
    public Match(int aCol, int aRow, int aCol0, int aRow0, int aCol1, int aRow1)
    {
        col = aCol; row = aRow; col0 = aCol0; row0 = aRow0; col1 = aCol1; row1 = aRow1;
        dx = col1 - col0; dy = row1 - row0;
    }
    
    /** Returns points that make up the match. */
    public GridXY[] getPoints()
    {
        List <GridXY> pnts = new ArrayList();
        if(dx>=2) for(int i=col0;i<=col1;i++) pnts.add(new GridXY(i, row));
        if(dy>=2) for(int i=row0;i<=row1;i++) pnts.add(new GridXY(col, i));
        return pnts.toArray(new GridXY[pnts.size()]);
    }
}

}