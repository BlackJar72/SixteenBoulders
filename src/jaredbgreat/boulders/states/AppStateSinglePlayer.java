package jaredbgreat.boulders.states;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import jaredbgreat.boulders.Arena;
import jaredbgreat.boulders.Main;
import jaredbgreat.boulders.entity.Player;
import jaredbgreat.boulders.entity.Boulder;
import jaredbgreat.boulders.entity.controls.CollisionHandler;
import java.util.Random;

/**
 *
 * @author Jared Blackburn
 */
public class AppStateSinglePlayer extends BaseAppState {
    static final int NUM_SHEEP = 16;
    AssetManager assetman;
    BaseAppState controls;
    BulletAppState physics;
    Node phynode;
    Random random;
    Main app;
    Boulder[] boulder;
    Player player;
    Arena playfield;
    AmbientLight ambLight;
    DirectionalLight dirLight;
    CollisionHandler colhand;
    Spatial sky;
    
    BitmapFont font;
    BitmapFont bigfont;
    BitmapText scoretxt;
    BitmapText endtxt;
    StringBuilder score;
    
    boolean gameover;
    long endtime;
    
    
    public AppStateSinglePlayer() {
        super();
        boulder = new Boulder[NUM_SHEEP];
        random = new Random();
    }
    
    
    @Override
    protected void initialize(Application ap) {
        app = (Main)ap;
        physics = new BulletAppState();
        app.getStateManager().attach(physics);
        assetman = app.getAssetManager();        
    }

    
    @Override
    protected void onEnable() {
        gameover = false;
        playfield = new Arena(assetman, app.getRootNode());
        phynode = playfield.initPhysics(physics);
        //playfield.addRamps(assetman, physics);
        for(int i = 0; i < boulder.length; i++) {
            boulder[i] = new Boulder(this, assetman, makeRandomLocation(20f), 
                    phynode, physics);
        } 
        Vector3f lightvec = new Vector3f();
        lightvec.set(2, -10, 3);
        lightvec.normalizeLocal();
        Node rootNode = app.getRootNode();
        ambLight = new AmbientLight(ColorRGBA.DarkGray);
        dirLight = new DirectionalLight(lightvec);
        dirLight.setColor(ColorRGBA.LightGray);
        rootNode.addLight(ambLight);
        rootNode.addLight(dirLight);
        player = new Player(this, phynode, physics);
        app.getStateManager().attach(new AppStateFirstPerson(player));
        setupTexts();
        addSkybox(app.getAssetManager(), app.getRootNode());
        colhand = new CollisionHandler();
        physics.getPhysicsSpace().addCollisionListener(colhand);
    }

    
    @Override
    protected void onDisable() {
        app.getGuiNode().detachAllChildren();
        scoretxt = null;
        endtxt = null;
        playfield = null;
        phynode.removeFromParent();
        phynode = null;
        Node rootNode = app.getRootNode();        
        rootNode.removeLight(ambLight);
        rootNode.removeLight(dirLight);
        app.getStateManager().detach(new AppStateFirstPerson(player));
    }
    
    
    @Override
    protected void cleanup(Application ap) {}
    
    
    @Override
    public void update(float tpf) {
        if(gameover && (System.currentTimeMillis() > endtime)) {
            app.endGame();
        }
        if((player.getLocation().getY() < -120) && !gameover) {
            player.die();
            gameover = true;
            endtime = System.currentTimeMillis() + 5000;
        }
        score.delete(7, Integer.MAX_VALUE);
        score.append(Player.getScore());
        if(Boulder.getNum() < 1) { 
            endtxt.setText("You Win!!!");
            if(!gameover) {
                gameover = true;
                endtime = System.currentTimeMillis() + 10000;
            }
        } else if(!player.isAlive()) {            
            endtxt.setText("You Died!!!\nYou Loose!");
        }
        scoretxt.setText(score);
    }
    
    
    /**
     * Returns the player object.
     * 
     * @return player
     */
    public Player getPlayer() {
        return player;
    }
    
    
    private Vector3f makeRandomLocation(float scale) {
        return new Vector3f(random.nextFloat() * (scale * 2f) - scale, 0f, 
                            random.nextFloat() * (scale * 2f) - scale);
    }
    
    
    private void addSkybox(AssetManager assetman, Node rootNode) {
        Texture stars = assetman.loadTexture("Textures/Starfield-square.png");
        sky = SkyFactory.createSky(assetman, stars, stars, stars, stars, stars, stars);
        rootNode.attachChild(sky);
    }
    
    
    private void setupTexts() {        
        font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");        
        bigfont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        scoretxt = new BitmapText(font);
        endtxt = new BitmapText(bigfont);
        
        scoretxt.setText(score = new StringBuilder("Score: 0"));
        scoretxt.setSize(font.getCharSet().getRenderedSize());
        scoretxt.move((app.getContext().getSettings().getWidth() 
                              - scoretxt.getLineWidth()) / 2, 
                      app.getContext().getSettings().getHeight() 
                              - scoretxt.getLineHeight() * 2,
                      0);
        app.getGuiNode().attachChild(scoretxt);
        
        endtxt.setText("You Win!!!");
        endtxt.setSize(font.getCharSet().getRenderedSize() * 3);
        endtxt.move((app.getContext().getSettings().getWidth() 
                              - endtxt.getLineWidth()) / 2, 
                      app.getContext().getSettings().getHeight() 
                              - endtxt.getLineHeight() * 2,
                      0);
        endtxt.setText("");
        app.getGuiNode().attachChild(endtxt);
    }
    
    
    
}
