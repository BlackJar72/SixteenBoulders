/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
