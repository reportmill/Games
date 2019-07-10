package jewels;
import snap.view.*;

/**
 * The class that manages top level UI for app.
 */
public class AppPane extends ViewOwner {

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
    ViewUtils.replaceView(mainRowView.getChild(1), _playView);

    // Return MainRowView
    return mainRowView;
}

/**
 * Initialize UI.
 */
protected void initUI()
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
    AppPane app = new AppPane();
    app.setWindowVisible(true);
    app.runLater(() -> app._playView.startGame());
}

}