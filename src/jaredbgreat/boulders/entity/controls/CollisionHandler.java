package jaredbgreat.boulders.entity.controls;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;

/**
 *
 * @author Jared Blackburn
 */
public class CollisionHandler implements PhysicsCollisionListener {

    @Override
    public void collision(PhysicsCollisionEvent event) {
        Spatial a = event.getNodeA();
        Spatial b = event.getNodeB();
        if(a == null || b == null) {
            return;
        }
        String nameA = a.getName();
        String nameB = b.getName();
        // FIXME: Use String constants for quick identity comparison
        if(nameA == null || nameB == null || event.getAppliedImpulse()  < 25) {
            return;
        }
        if(nameA.equals("boulder")) {
            switch (nameB) {
                case "wall":
                    ((BoulderControl)a.getControl(BoulderControl.class)).playSound(1);
                    break;
                case "field":
                    ((BoulderControl)a.getControl(BoulderControl.class)).playSound(2);                
                    break;
                default:
                    ((BoulderControl)a.getControl(BoulderControl.class)).playSound(0);
                    break;
            }
        } else if(nameB.equals("boulder")) {
            switch (nameA) {
                case "wall":
                    ((BoulderControl)b.getControl(BoulderControl.class)).playSound(1);
                    break;
                case "field":
                    ((BoulderControl)b.getControl(BoulderControl.class)).playSound(2);                
                    break;
                default:
                    ((BoulderControl)b.getControl(BoulderControl.class)).playSound(0);
                    break;
            }
        }
        
    }
    
}
