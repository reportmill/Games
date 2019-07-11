package tetris;
import snap.gfx.*;
import snap.view.*;

/**
 * A class to represent a tetris block.
 */
public class Block extends View {
    
    // The pattern
    Pattern  _pattern;
    
    // Constants
    static int TILE_SIZE = Pattern.TILE_SIZE;
    static int PIECE_COUNT = 7;
    static Effect BLOCK_EFFECT = new ShadowEffect(8,Color.BLACK,0,0);

/**
 * Creates a Block.
 */
public Block()
{
    // Get index for random pattern and set pattern
    int patInd = (int)Math.floor(Math.random()*PIECE_COUNT);
    _pattern = Pattern.ALL[patInd];
    
    // Set size from pattern
    setSize(_pattern.colCount*TILE_SIZE, _pattern.rowCount*TILE_SIZE);
    setPrefSize(_pattern.colCount*TILE_SIZE, _pattern.rowCount*TILE_SIZE);
    setEffect(BLOCK_EFFECT);
}

/**
 * Returns the number of tiles.
 */
public int getTileCount()  { return _pattern.tileCount; }

/**
 * Returns the tile rect at given index in parent view coords.
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
 * Paint block pattern.
 */
protected void paintFront(Painter aPntr)  { _pattern.paint(aPntr); }

}