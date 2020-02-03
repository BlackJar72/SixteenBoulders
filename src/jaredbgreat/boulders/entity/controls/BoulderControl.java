package jaredbgreat.boulders.entity.controls;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioKey;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import jaredbgreat.boulders.states.AppStatePlayGame;
import jaredbgreat.boulders.entity.Boulder;

/**
 *
 * @author Jared Blackburn
 */
public class BoulderControl extends AbstractEntityControl {
    private static final float TOO_CLOSE  = 6f;
    private static final float FAR_ENOUGH = 12f;
    private static final float MAX_SPEED  = 15f;
    private static final float MIN_SPEED  = 0f;
    private static final float ACCELERATE = 24f;
    private static final float DECELERATE = 6f;
    private static final long  SCARE_TIME = 10000;
    
    // Per-Sheep Variables
    Boulder owner;
    RigidBodyControl physics;
    Vector3f velocity;
    Vector3f acceleration;
    Vector3f playerLoc;
    Vector3f sheepLoc;
    Vector3f scratch;
    boolean scared;
    boolean safe;
    float flipdir;
    long scareTime;
    long currentTime;
    boolean running;
    AudioNode[] sounds;

    
    public BoulderControl(AppStatePlayGame appState, RigidBodyControl phy, Boulder caller) {
        super(appState);
        owner = caller;
        physics = phy;
        physics.setRestitution(1.0f);
        acceleration = new Vector3f();
        safe = true;
        flipdir = 1.0f;
        scareTime = 0;
        currentTime = System.currentTimeMillis();
        running = false;
    }
        

    @Override
    protected void controlUpdate(float tpf) {
        velocity = physics.getLinearVelocity();
        playerLoc = game.getPlayer().getLocation();
        sheepLoc  = spatial.getWorldTranslation();
        float dist = sheepLoc.distance(playerLoc);
        currentTime = System.currentTimeMillis();
        if(dist < TOO_CLOSE) {
            scared = true;
            safe = false;
        } else {
            scared = false;
            if ((dist > FAR_ENOUGH) && (currentTime > scareTime)){
                safe = true;
                running = false;
            }
        }
        if(scared) {
            sheepLoc.subtract(playerLoc, acceleration);
            boolean threatened = isPlayerApproaching(playerLoc);
            if(threatened) {
                acceleration.normalizeLocal().multLocal(ACCELERATE * tpf * 2.0f);
                scareTime = System.currentTimeMillis() + SCARE_TIME;
            } else {                
                acceleration.normalizeLocal().multLocal(ACCELERATE * tpf);
            }
            velocity.addLocal(acceleration);
            scratch = sheepLoc.add(velocity);
            if(scratch.distance(playerLoc) < sheepLoc.distance(playerLoc)) {
                float mdist1 = sheepLoc.x - playerLoc.x;
                mdist1 *= mdist1;
                float mdist2 = scratch.x - playerLoc.x;
                mdist2 *= mdist2;
                if(mdist2 < mdist1) {
                    velocity.setX(0.0f);
                }
                mdist1 = sheepLoc.z - playerLoc.z;
                mdist1 *= mdist1;
                mdist2 = scratch.z - playerLoc.z;
                mdist2 *= mdist2;
                if(mdist2 < mdist1) {
                    velocity.setX(0.0f);
                }
                if(velocity.length() == 0) {
                    acceleration.cross(Vector3f.UNIT_Y, velocity);
                    velocity.multLocal(flipdir);
                    flipdir *= -1.0f;
                }
            }
            float speed = velocity.length();
            if(speed > MAX_SPEED) {
                velocity.normalizeLocal().multLocal(MAX_SPEED);
                running = true;
            } else if(running) {
                velocity.normalizeLocal().multLocal(MAX_SPEED);
            }
        } else if(safe) {
            float speed = Math.max(velocity.length() - (DECELERATE * tpf), 0f);
            velocity.normalizeLocal().multLocal(speed);
        }
        physics.setLinearVelocity(velocity);
        if(spatial.getWorldTranslation().y < 0) {
            die();
        }
    }

    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        // So far I don't think there will be any special rendering need. (Do nothing.)
    }
    
    
    private boolean isPlayerApproaching(Vector3f playerLoc) {
        SimpleApplication app = (SimpleApplication)game.getApplication();
        Vector3f pheading = app.getCamera().getDirection();
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(playerLoc, pheading);
        app.getRootNode().collideWith(ray, results);
        if(results.size() > 0) {
            return (results.getClosestCollision().getGeometry() == spatial);
        }
        return false;
    }
    
    
    // FIXME: There must be a better way, one that does not
    // create a circular referrence in the first place.
    public void die() {
        physics = null;
        owner.die();
        owner = null;
    }
    
    
    public void setSounds(AudioNode stony, AudioNode woody, AudioNode thud) {
        sounds = new AudioNode[3];
        sounds[0] = stony;
        sounds[1] = woody;
        sounds[2] = thud;
    }
    
    
    public void playSound(int i) {
        sounds[i].setLocalTranslation(spatial.getLocalTranslation());
        sounds[i].play(); // playInstance()?
    }
    
    
    public void playSound(int i, float loud) {
        sounds[i].setLocalTranslation(spatial.getLocalTranslation());
        sounds[i].setVolume(loud);
        sounds[i].play(); // playInstance()?
    }
}

