package jewels;
import snap.gfx.*;
import snap.view.*;

/**
 * A View to hold the play area for a match 3 game.
 */
public class PlayView extends ParentView {

    // The Jewels
    Jewel     _jewels[][] = new Jewel[GRID_WIDTH][GRID_HEIGHT];
    
    // The grid width/height
    public static int GRID_WIDTH = 8;
    public static int GRID_HEIGHT = 8;
    public static int TILE_SIZE = 64;
    public static int BORDER_SIZE = 2;

/**
 * Creates a PlayView.
 */
public PlayView()
{
    for(int i=0;i<GRID_WIDTH;i++)
        for(int j=0;j<GRID_HEIGHT;j++) {
            Jewel jewel = _jewels[i][j] = new Jewel();
            jewel.setXY(i*TILE_SIZE + BORDER_SIZE, j*TILE_SIZE + BORDER_SIZE);
            addChild(jewel);
        }

    Image img = Image.get(getClass(), "pkg.images/Cloth.jpg");
    Painter pntr = img.getPainter(); pntr.setColor(new Color("#44998833"));
    pntr.drawRect(.5,.5,img.getWidth()-1, img.getHeight()-1);
    ImagePaint paint = new ImagePaint(img, BORDER_SIZE, BORDER_SIZE, 64, 64, false);
    setFill(paint);
    setBorder(Color.BLACK, 2);
    setPrefSize(GRID_WIDTH*TILE_SIZE + BORDER_SIZE*2, GRID_HEIGHT*TILE_SIZE + BORDER_SIZE*2);
}

/**
 * Starts the game.
 */
public void startGame()
{
    
}

}