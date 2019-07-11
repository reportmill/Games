package jewels;
import snap.gfx.*;
import snap.view.*;

/**
 * A View to hold the play area for a match 3 game.
 */
public class PlayView extends ParentView {

    // The gems
    Gem         _gems[][] = new Gem[GRID_WIDTH][GRID_HEIGHT];
    
    // The timer
    ViewTimer   _timer = new ViewTimer(25, t -> timerFired());
    
    // The grid width/height
    public static int GRID_WIDTH = 8;
    public static int GRID_HEIGHT = 8;
    public static int TILE_SIZE = 64;
    public static int BORDER_SIZE = 2;
    public static int ROW_SPEEDS[] = new int[GRID_WIDTH];

/**
 * Creates a PlayView.
 */
public PlayView()
{
    /*for(int i=0;i<GRID_WIDTH;i++)
        for(int j=0;j<GRID_HEIGHT;j++) {
            Jewel jewel = _jewels[i][j] = new Jewel();
            jewel.setXY(i*TILE_SIZE + BORDER_SIZE, j*TILE_SIZE + BORDER_SIZE);
            addChild(jewel);
        }*/

    // Create background texture
    Image img = Image.get(getClass(), "pkg.images/Cloth.jpg");
    Painter pntr = img.getPainter(); pntr.setColor(new Color("#44998833"));
    pntr.drawRect(.5,.5,img.getWidth()-1, img.getHeight()-1);
    ImagePaint paint = new ImagePaint(img, BORDER_SIZE, BORDER_SIZE, 64, 64, false);
    
    // Set PlayView fill, border, PrefSize
    setFill(paint); setBorder(Color.BLACK, 2);
    setPrefSize(GRID_WIDTH*TILE_SIZE + BORDER_SIZE*2, GRID_HEIGHT*TILE_SIZE + BORDER_SIZE*2);
    setClipToBounds(true);
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
 * Pauses game.
 */
public void pauseGame()
{
    clearGems();
    //if(_timer.isRunning()) _timer.pause();
    //else _timer.start();
}

/**
 * Called when timer fires.
 */
void timerFired()  { }

/**
 * Reloads the field of gems.
 */
void reloadGems()
{
    for(int j=GRID_HEIGHT-1;j>=0;j--) {
        for(int i=0;i<GRID_WIDTH;i++) {
            if(_gems[i][j]==null) {
                Gem gem = getGemAbove(i, j);
                double oldY = gem.getY() - gem.getTransY();
                Point pnt = gridToLocal(i, j); gem.setY(pnt.y);
                double dy = gem.getY() - oldY;
                gem.setTransY(-dy);
                int time = (int)Math.round(dy*ROW_SPEEDS[i]/TILE_SIZE);
                gem.getAnimCleared(time).setTransY(0).setLinear().play();
                _gems[i][j] = gem;
            }
        }
    }
}

/**
 * Returns the jewel for slot row/col, creating a new one if needed.
 */
Gem getGemAbove(int aX, int aY)
{
    for(int i=aY-1;i>=0;i--) { Gem gem = _gems[aX][i];
        if(gem!=null) {
            _gems[aX][i] = null; return gem; } }
    
    Gem gem = new Gem();
    Point pnt = gridToLocal(aX, -1);
    for(int i=0;i<GRID_HEIGHT;i++) { Gem g = _gems[aX][i]; if(g==null) continue;
        pnt.y = Math.min(pnt.y, g.getY() + g.getTransY() - TILE_SIZE); }
    gem.setXY(pnt.x, pnt.y);
    addChild(gem);
    return gem;
}

/**
 * Clears the gems.
 */
void clearGems()
{
    for(int i=0;i<GRID_WIDTH;i++) for(int j=0;j<GRID_HEIGHT;j++) { Gem gem = _gems[i][j];
        if(gem!=null) removeChild(gem);
        _gems[i][j] = null;
    }
}

/**
 * Return the point in view coords for point in grid coords.
 */
public Point gridToLocal(int aX, int aY)  { return new Point(aX*TILE_SIZE+BORDER_SIZE, aY*TILE_SIZE+BORDER_SIZE); }

}