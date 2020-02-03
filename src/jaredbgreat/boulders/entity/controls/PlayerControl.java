package jaredbgreat.boulders.entity.controls;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import jaredbgreat.boulders.states.AppStateSinglePlayer;

/**
 *
 * @author Jared Blackburn
 */
public class PlayerControl extends AbstractEntityControl {
    private final BetterCharacterControl physics;
    private final Vector3f movement;
    private final Vector3f heading;
    private float speed;
    private float hRotSpeed;
    private float vRotSpeed;
    private boolean w, a, s, d, r, l;
    private float relativeForward;
    private float relativeBackward;
    private boolean firstPerson;
    private float spatialAngle;
    private float camAngle;
    private final Quaternion camq;

    
    public PlayerControl(AppStateSinglePlayer appState, BetterCharacterControl bcc) {
        super(appState);
        speed = 12f;
        hRotSpeed = 100f;
        vRotSpeed = 100f;
        movement = new Vector3f();
        heading  = new Vector3f();
        w = a = s = d = r = l = false;
        relativeForward = -1f;
        relativeBackward = 1f;
        spatialAngle = 0;
        camAngle = 0;
        camq = new Quaternion();
        physics = bcc;
    }
    

    @Override
    protected void controlUpdate(float tpf) {
        SimpleApplication app = (SimpleApplication)game.getApplication();
        movement.set(0, 0, 0);
        if(w) {
            movement.addLocal(0, 0, relativeForward);
        }
        if(s) {
            movement.addLocal(0, 0, relativeBackward);
        }
        if(a) {
            movement.addLocal(relativeForward, 0, 0);
        }
        if(d) {
            movement.addLocal(relativeBackward, 0, 0);
        }
        if(movement.lengthSquared() > 0) {
            Quaternion q = spatial.getLocalRotation();
            movement.set(q.mult(movement.normalizeLocal().multLocal(speed)));
        }
        physics.setWalkDirection(movement);
        heading.set(Vector3f.UNIT_Z).negate();
        physics.setViewDirection(new Quaternion()
                .fromAngleNormalAxis(spatialAngle, Vector3f.UNIT_Y).mult(heading, heading));
        if(firstPerson) {
            Camera cam = app.getCamera();                  
            camq.fromAngleNormalAxis(camAngle, Vector3f.UNIT_X);
            cam.setLocation(spatial.getWorldTranslation().add(0, 1.0f, 0));
            cam.setRotation((spatial.getWorldRotation().mult(camq)));
        }
    }

    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
    
    public void setMoveForward(boolean going) {
        w = going;
    }
    
    
    public void setMoveBackward(boolean going) {
        s = going;
    }
    
    
    public void setMoveLeft(boolean going) {
        a = going;
    }
    
    
    public void setMoveRight(boolean going) {
        d = going;
    }
    
    
    public void setTurnRght(float value) {
        spatialAngle -= (value * hRotSpeed);
        if(spatialAngle < 0) {
            spatialAngle += FastMath.TWO_PI;
        }
    }
    
    
    public void setTurnLeft(float value) {
        spatialAngle += (value * hRotSpeed);
        if(spatialAngle > FastMath.TWO_PI) {
            spatialAngle -= FastMath.TWO_PI;
        }
    }
    
    
    public void setLookUp(float value) {
        camAngle = Math.max(camAngle - (value * vRotSpeed), -FastMath.HALF_PI);
    }
    
    
    public void setLookDown(float value) {
        camAngle = Math.min(camAngle + (value * vRotSpeed), FastMath.HALF_PI);
    }
    
    
    public void jump() {
        physics.jump();
    }
    
    
    public void setFirstPerson() {
        firstPerson = true;
        relativeForward  =  1f;
        relativeBackward = -1f;
        hRotSpeed = 100f;
        vRotSpeed = 100f;
    }
    
    
    public void setThirdPerson() {
        firstPerson = false;
        relativeForward  = -1f;
        relativeBackward =  1f;
        hRotSpeed = 400f;
        vRotSpeed = 0f;
    }
    
}
