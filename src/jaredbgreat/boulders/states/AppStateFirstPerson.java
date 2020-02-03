/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaredbgreat.boulders.states;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import jaredbgreat.boulders.ControlConstants;
import static jaredbgreat.boulders.ControlConstants.GO_BACKWARD;
import static jaredbgreat.boulders.ControlConstants.GO_FORWARD;
import static jaredbgreat.boulders.ControlConstants.GO_LEFT;
import static jaredbgreat.boulders.ControlConstants.GO_RIGHT;
import static jaredbgreat.boulders.ControlConstants.JUMP;
import static jaredbgreat.boulders.ControlConstants.LOOK_DOWN;
import static jaredbgreat.boulders.ControlConstants.LOOK_UP;
import static jaredbgreat.boulders.ControlConstants.TO_MENUS;
import static jaredbgreat.boulders.ControlConstants.TURN_LEFT;
import static jaredbgreat.boulders.ControlConstants.TURN_RIGHT;
import jaredbgreat.boulders.Main;
import jaredbgreat.boulders.entity.Player;
import jaredbgreat.boulders.entity.controls.PlayerControl;

/**
 *
 * @author Jared Blackburn
 */
public class AppStateFirstPerson extends BaseAppState implements ActionListener, AnalogListener {  
    Main app;
    Player player;
    PlayerControl control;
    
    
    
    public AppStateFirstPerson(Player player) {
        super();
        this.player = player;
        this.control = player.getControl();
    }

    
    @Override
    protected void initialize(Application ap) {
        app = (Main)ap;
        app.setDisplayStatView(false);
        app.setDisplayFps(false);
        setControls(app.getInputManager());
        player.setFirstPerson();
    }
    

    @Override
    protected void onEnable() {}
    
    
    @Override
    public void update(float tpf){}
    
    @Override
    protected void onDisable() {}

    
    @Override
    protected void cleanup(Application ap) {
        unsetControls(app.getInputManager());
    }
    
    
    /*---------------------------------------------------------------------------------*/
    /*                                    CONTROLS                                     */
    /*---------------------------------------------------------------------------------*/
    
    
    protected void setControls(InputManager inputMan) {
        inputMan.addMapping(GO_FORWARD, new KeyTrigger(KeyInput.KEY_W));
        inputMan.addMapping(GO_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
        inputMan.addMapping(GO_LEFT, new KeyTrigger(KeyInput.KEY_A));
        inputMan.addMapping(GO_RIGHT, new KeyTrigger(KeyInput.KEY_D));
        
        inputMan.addMapping(TURN_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true),
                new KeyTrigger(KeyInput.KEY_LEFT));
        inputMan.addMapping(TURN_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false),
                new KeyTrigger(KeyInput.KEY_RIGHT));
        inputMan.addMapping(LOOK_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false),
                new KeyTrigger(KeyInput.KEY_UP));
        inputMan.addMapping(LOOK_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true),
                new KeyTrigger(KeyInput.KEY_DOWN));
        
        inputMan.addMapping(JUMP, new KeyTrigger(KeyInput.KEY_SPACE));
        
        inputMan.addMapping(TO_MENUS, new KeyTrigger(KeyInput.KEY_ESCAPE));
        
        inputMan.addListener(this, ControlConstants.MAPPINGS);
    }
    
    
    
    
    protected void unsetControls(InputManager inputMan) {
        inputMan.deleteMapping(GO_FORWARD);
        inputMan.deleteMapping(GO_BACKWARD);
        inputMan.deleteMapping(GO_LEFT);
        inputMan.deleteMapping(GO_RIGHT);
        
        inputMan.deleteMapping(TURN_LEFT);
        inputMan.deleteMapping(TURN_RIGHT);
        inputMan.deleteMapping(LOOK_UP);
        inputMan.deleteMapping(LOOK_DOWN);
        
        inputMan.deleteMapping(JUMP);
        inputMan.removeListener(this);
    }

    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name == ControlConstants.GO_FORWARD) {
            control.setMoveForward(isPressed);
        } else if(name == ControlConstants.GO_BACKWARD) {
            control.setMoveBackward(isPressed);
        } else if(name == ControlConstants.GO_RIGHT) {
            control.setMoveRight(isPressed);
        } else if(name == ControlConstants.GO_LEFT) {
            control.setMoveLeft(isPressed);
        } else if(name == ControlConstants.JUMP) {
            control.jump();
        } else if(name == ControlConstants.JUMP) {
            control.jump();
        }  else if(name == ControlConstants.TO_MENUS) {
            app.endGame();
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {       
        if(name == ControlConstants.TURN_RIGHT) {
            control.setTurnRght(tpf * value);
        } else if(name == ControlConstants.TURN_LEFT) {
            control.setTurnLeft(tpf * value);
        } else if(name == ControlConstants.LOOK_UP) {
            control.setLookUp(tpf * value);
        } else if(name == ControlConstants.LOOK_DOWN) {
            control.setLookDown(tpf * value);            
        }
    }
}