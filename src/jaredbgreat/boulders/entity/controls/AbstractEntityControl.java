package jaredbgreat.boulders.entity.controls;

import com.jme3.scene.control.AbstractControl;
import jaredbgreat.boulders.states.AppStateSinglePlayer;

/**
 *
 * @author jared
 */
public abstract class AbstractEntityControl extends AbstractControl {
    AppStateSinglePlayer game;
    
    AbstractEntityControl(AppStateSinglePlayer appState) {
        game = appState;
    }
    
    
}
