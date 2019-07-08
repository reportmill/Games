package tetris;
import java.util.*;
import snap.gfx.*;
import snap.util.MathUtils;
import snap.view.*;
import tetris.Block.Pattern;

/**
 * A class to represent row of block tiles at bottom of PlayView.
 */
public class StackRow extends View {

    // The array of filled tiles
    Pattern           _cols[] = new Block.Pattern[GRID_WIDTH];
    
    // The array of tile rects
    Rect              _tileRects[];
    
    // The row number
    int               _rowNum;
    
    // Constants
    static int TILE_SIZE = Block.TILE_SIZE;
    static int GRID_WIDTH = PlayView.GRID_WIDTH;

/**
 * Creates a StackRow.
 */
public StackRow()
{
    setSize(PlayView.GRID_WIDTH*TILE_SIZE, TILE_SIZE);
    setEffect(new ShadowEffect());
}

/**
 * Returns the number of tiles.
 */
public Rect[] getTileRectsInParent()
{
    if(_tileRects!=null) return _tileRects;
    
    List <Rect> rects = new ArrayList();
    int tc = 0; for(int i=0;i<_cols.length;i++) { Pattern col = _cols[i]; if(col==null) continue;
        double x = i*TILE_SIZE;
        Rect rect = new Rect(getX() + x, getY(), TILE_SIZE, TILE_SIZE);
        rects.add(rect);
    }
    return _tileRects = rects.toArray(new Rect[rects.size()]);
}

/**
 * Returns whether block intersects row.
 */
public boolean intersectsBlock(Block aBlock)
{
    // If block above row, return false
    if(MathUtils.lt(aBlock.getMaxY(), getY())) return false;
    
    // Iterate over block tiles and see if any intersect row tiles
    for(int i=0;i<aBlock.getTileCount();i++) { Rect brect = aBlock.getTileRectInParent(i);
        Rect brect2 = brect.getInsetRect(1, .1);
        for(Rect rrect : getTileRectsInParent())
            if(rrect.intersects(brect2))
                return true;
    }
    
    // Return false sice no block tiles hit row tiles
    return false;
}

/**
 * Adds block tiles.
 */
public void addBlockTiles(Block aBlock)
{
    for(int i=0;i<aBlock.getTileCount();i++) { Rect rect = aBlock.getTileRectInParent(i);
        double x = rect.getMidX() - getX();
        if(!contains(x, rect.getMidY() - getY())) continue;
        int ind = (int)Math.floor(x/TILE_SIZE);
        _cols[ind] = aBlock._pattern;
    }
    
    // Repaint & reset TileRects
    repaint(); _tileRects = null;
}

/**
 * Returns whether row is full.
 */
public boolean isFull()
{
    for(Pattern c : _cols) if(c==null) return false;
    return true;
}

/**
 * Paint block pattern.
 */
protected void paintFront(Painter aPntr)
{
    for(int i=0;i<_cols.length;i++) {
        Block.Pattern pat = _cols[i]; if(pat==null) continue;
        double x = i*TILE_SIZE;
        aPntr.drawImage(pat.image, x, 0);
    }
}

}