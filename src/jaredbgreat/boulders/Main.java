package jaredbgreat.boulders;

import jaredbgreat.boulders.states.AppStateSinglePlayer;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import jaredbgreat.boulders.states.AppStateStartScreen;

/**
 * @author Jared Blackburn
 */
public class Main extends SimpleApplication {
    private static AppStateSinglePlayer play;
    private static AppStateStartScreen start;

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Sixteen Boulders");
        settings.setSettingsDialogImage("Interface/16Boulders.png");
        settings.setVSync(true);
        Main app = new Main();
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        play  = new AppStateSinglePlayer();
        start = new AppStateStartScreen();
        stateManager.attach(start);
    }

    @Override
    public void simpleUpdate(float tpf) {/*Do Nothing*/}

    @Override
    public void simpleRender(RenderManager rm) {/*Do Nothing*/}
    
    
    public void startGame() {
        if(stateManager.hasState(start)) {
            stateManager.detach(start);
        }
        if(stateManager.hasState(play)) {
            stateManager.detach(play);            
        }
        stateManager.attach(play = new AppStateSinglePlayer());
    }
    
    
    public void endGame() {
        if(stateManager.hasState(play)) {
            stateManager.detach(play);            
        }
        stateManager.attach(start);
    }
    
}


