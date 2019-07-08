package tetris;
import java.util.*;
import snap.gfx.*;
import snap.util.MathUtils;
import snap.view.*;

/**
 * A custom class.
 */
public class PlayView extends ParentView {
    
    // The current block
    Block       _block;
    
    // The timer
    ViewTimer   _timer = new ViewTimer(25, t -> timerFired());
    
    // The list of rows
    List <StackRow>  _rows = new ArrayList();
    
    // Whether user has requested block to drop faster
    boolean     _dropFast;
    
    // Whether game is over
    boolean     _gameOver;
    
    // The size of the field
    static int TILE_SIZE = Block.TILE_SIZE;
    static int GRID_WIDTH = 10, GRID_HEIGHT = 20;
    static int BORDER_WIDTH = 2;
    

/**
 * Creates a PlayView.
 */
public PlayView()
{
    setFocusable(true);
    setFill(Color.WHITE);
    setBorder(Color.BLACK, 2);
    setPrefSize(GRID_WIDTH*Block.TILE_SIZE + BORDER_WIDTH*2, GRID_HEIGHT*Block.TILE_SIZE + BORDER_WIDTH*2);
    enableEvents(KeyPress);
}

/**
 * Starts play.
 */
public void playGame()
{
    // Reset state
    _rows.clear(); removeChildren(); _gameOver = false;
    
    // Start timer, add piece
    _timer.start();
    addPiece();
    requestFocus();
    getRootView().repaint();
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
 * Adds a piece.
 */
public void addPiece()
{
    _block = new Block();
    double x = (getWidth() - _block.getWidth())/2; x = MathUtils.round(x, Block.TILE_SIZE) + BORDER_WIDTH;
    double y = BORDER_WIDTH;
    _block.setXY(x, y);
    addChild(_block);
    _dropFast = false;
    _block.setEffect(new ShadowEffect());
}

/**
 * Called when timer fires.
 */
void timerFired()
{
    // If no block, return
    if(_block==null) return;
    
    // Update block position
    int dy = 2; if(_dropFast) dy += 8;
    _block.setY(_block.getY() + dy);
    
    // If block stopped, 
    if(isBlockStopped())
        blockDidHit();
}

/**
 * Returns whether block has hit something.
 */
boolean isBlockStopped()
{
    double blockBtm = _block.getMaxY();
    for(int i=_rows.size()-1;i>=0;i--) { StackRow row = _rows.get(i);
        if(MathUtils.lt(blockBtm, row.getY())) return false;
        if(row.intersects(_block))
            return true;
    }
    
    if(MathUtils.lt(blockBtm, getHeight()))
        return false;
    return true;
}

/**
 * Called when block hits something.
 */
void blockDidHit()
{
    // Back block up
    while(isBlockStopped() && _block.getY()>BORDER_WIDTH)
        _block.setY(_block.getY()-1);
    
    if(getTopRow()!=null && MathUtils.gt(_block.getMaxY(), getTopRow().getY())) {
        System.out.println("BlockDidHit: " + fmt(_block.getMaxY()) + "  " + getTopRow().getY());
        boolean v = isBlockStopped();
        isBlockStopped();
    }
        
    // Add rows to accommodate piece
    addRows(); if(_gameOver) return;
    addBlockToRows();
    
    // 
    addPiece();
}

/**
 * Adds a row.
 */
void addRows()
{
    while(_rows.size()==0 || _block.getY() + TILE_SIZE/2 < getTopRow().getY()) {
        addRow(); if(_gameOver) return; }
}

/**
 * Adds a row.
 */
void addRow()
{
    if(_rows.size()>=GRID_HEIGHT-1) {
        gameOver(); return; }
    
    StackRow topRow = getTopRow();
    StackRow newRow = new StackRow();
    double y = topRow!=null? topRow.getY() : (getHeight() - BORDER_WIDTH); y -= TILE_SIZE;
    newRow.setXY(BORDER_WIDTH, y);
    newRow._rowNum = _rows.size();
    _rows.add(newRow); addChild(newRow);
}

/**
 * Adds the current block to rows.
 */
void addBlockToRows()
{
    // Get block row/col counts
    int rc = _block._pattern.rowCount, cc = _block._pattern.colCount;
    
    // Iterate over block rows
    for(int i=0;i<rc;i++) {
        double y = _block.getY() + i*TILE_SIZE + TILE_SIZE/2;
        StackRow row = getRowForY(y); if(row==null) continue;
        row.addBlockTiles(_block);
    }
    
    // Remove block
    removeChild(_block);
    
    // Remove full rows
    for(int i=_rows.size()-1;i>=0;i--) { StackRow row = _rows.get(i);
        if(row.isFull())
            explodeRow(row);
    }
}

/**
 * Explodes row.
 */
void explodeRow(StackRow aRow)
{
    int ind = _rows.indexOf(aRow);
    Explode.explode(aRow, 0);
    _rows.remove(aRow);
    for(int i=ind;i<_rows.size();i++) { StackRow row = _rows.get(i);
        row.getAnim(500).setY(getHeight() - (i+1)*TILE_SIZE).play();
    }
}

/**
 * Returns the top row.
 */
StackRow getTopRow()  { return _rows.size()>0? _rows.get(_rows.size()-1) : null; }

/**
 * Returns the row for y value.
 */
StackRow getRowForY(double aY)
{
    for(StackRow row : _rows)
        if(row.contains(row.getWidth()/2, aY - row.getY()))
            return row;
    return null;
}

/**
 * Called when game is over.
 */
void gameOver()
{
    _gameOver = true;
    _timer.stop();
    for(int i=0;i<_rows.size();i++) { StackRow row = _rows.get(_rows.size()-i-1);
        Explode.explode(row, i*150); }

    addBlockToRows();
}

/**
 * Handles event.
 */
protected void processEvent(ViewEvent anEvent)
{
    // Handle LeftArrow, RightArrow, DownArrow, Space    
    if(anEvent.isLeftArrow()) moveLeft();
    else if(anEvent.isRightArrow()) moveRight();
    else if(anEvent.isDownArrow()) dropBlock();
    else if(anEvent.getKeyString().equals(" ")) rotateBlock();
}

/**
 * Move Left.
 */
public void moveLeft()
{
    if(_block.getAnim(0).isPlaying()) _block.getAnim(0).finish();
    if(_block.getX()<=BORDER_WIDTH) return;
    _block.getAnimCleared(300).setX(_block.getX() - Block.TILE_SIZE).play();
}

/**
 * Move Right.
 */
public void moveRight()
{
    if(_block.getAnim(0).isPlaying()) _block.getAnim(0).finish();
    if(_block.getMaxX()>=getWidth()-BORDER_WIDTH) return;
    _block.getAnimCleared(300).setX(_block.getX() + Block.TILE_SIZE).play();
}

/**
 * Drop block.
 */
public void dropBlock()  { _dropFast = true; }

/**
 * Rotate block.
 */
public void rotateBlock()  { _block.rotateRight(); }

String fmt(double aVal)  { return snap.util.StringUtils.formatNum("#.#", aVal); }

}