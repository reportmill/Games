package jewels;
import snap.gfx.*;
import snap.view.*;

/**
 * A view class to display a jewel.
 */
public class Jewel extends ImageView {

    // The Gem id
    int     _gid;
    
    // Constants
    static int TILE_SIZE = PlayView.TILE_SIZE;
    
    // The gems
    static Image BLUE_GEM = Image.get(Jewel.class, "pkg.images/BlueGem.png");
    static Image GREEN_GEM = Image.get(Jewel.class, "pkg.images/GreenGem.png");
    static Image ORANGE_GEM = Image.get(Jewel.class, "pkg.images/OrangeGem.png");
    static Image PURPLE_GEM = Image.get(Jewel.class, "pkg.images/PurpleGem.png");
    static Image RED_GEM = Image.get(Jewel.class, "pkg.images/RedGem.png");
    static Image WHITE_GEM = Image.get(Jewel.class, "pkg.images/WhiteGem.png");
    static Image YELLOW_GEM = Image.get(Jewel.class, "pkg.images/YellowGem.png");
    static Image ALL[] = new Image[] { BLUE_GEM, GREEN_GEM, ORANGE_GEM, PURPLE_GEM, RED_GEM, WHITE_GEM, YELLOW_GEM };

/**
 * Create new Gem.
 */
public Jewel()
{
    _gid = (int)Math.floor(Math.random()*7);
    setImage(ALL[_gid]);
    setSize(TILE_SIZE,TILE_SIZE);
    setPrefSize(TILE_SIZE,TILE_SIZE);
}

}