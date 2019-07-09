package tetris;
import snap.view.*;

/**
 * A custom class.
 */
public class TetrisPane extends ViewOwner {
    
    // The PlayView
    PlayView     _playView;
    
    // The next block box
    BoxView      _nextBlockBox;

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
 * Initialize UI.
 */
protected void initUI()
{
    _nextBlockBox = getView("NextBlockBox", BoxView.class); _nextBlockBox.setFillWidth(true); _nextBlockBox.setFillHeight(true);
    _nextBlockBox.setContent(new BoxView());
    _nextBlockBox = (BoxView)_nextBlockBox.getContent(); _nextBlockBox.setScale(.6);
    
    _nextBlockBox.setScale(.6);
    _playView.addPropChangeListener(pc -> _nextBlockBox.setContent(_playView.getNextBlock(false)),
        PlayView.NextBlock_Prop);
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
    if(anEvent.equals("RestartButton")) _playView.startGame();
}

/**
 * Called when PlayView.NextBlock changes.
 */
void nextBlockChanged()
{
    
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
    tp.runLater(() -> tp._playView.startGame());
}

}