package tetris;
import snap.view.*;

/**
 * A custom class.
 */
public class TetrisPane extends ViewOwner {
    
    // The PlayView
    PlayView     _playView;

/**
 * Create UI.
 */
protected View createUI()
{
    // Do normal version
    RowView mainRowView = (RowView)super.createUI(); //new RowView(); mainRowView.setPadding(20,20,20,20);
    
    // Swap out placeholder with PlayView
    _playView = new PlayView();
    ViewUtils.replaceView(mainRowView.getChild(0), _playView);

    // Return MainRowView
    return mainRowView;
}

/**
 * Respond to UI.
 */
protected void respondUI(ViewEvent anEvent)
{
    // Handle LeftButton, RightButton, DropButton, RotateButton
    if(anEvent.equals("LeftButton")) _playView.moveLeft();
    if(anEvent.equals("RightButton")) _playView.moveRight();
    if(anEvent.equals("DropButton")) _playView.dropBlock();
    if(anEvent.equals("RotateButton")) _playView.rotateBlock();

    // Handle PauseButton, RestartButton
    if(anEvent.equals("PauseButton")) _playView.pauseGame();
    if(anEvent.equals("RestartButton")) _playView.playGame();
}

/**
 * Standard main method.
 */
public static void main(String args[])
{
    snaptea.TV.set();
    ViewUtils.runLater(() -> appThreadMain());
}

/**
 * Standard main method.
 */
static void appThreadMain()
{
    TetrisPane tp = new TetrisPane();
    tp.setWindowVisible(true);
    tp.runLater(() -> tp._playView.playGame());
}

}