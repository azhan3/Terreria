package jade;

import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {
    GameObject obj1, obj2;
    Spritesheet sprites;
    GameObject[] objects = new GameObject[32];
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(0, 500), new Vector2f(100, 100)), 2);

        obj1.addComponent(new SpriteRenderer(sprites.getSprite(1)));
        this.addGameObjectToScene(obj1);
        obj2 = new GameObject("Object 1", new Transform(new Vector2f(95, 492), new Vector2f(100, 100)), 3);

        obj2.addComponent(new SpriteRenderer(sprites.getSprite(1)));
        this.addGameObjectToScene(obj2);

        /*for (int i = 0 ; i < 512 ; i+=16) {
            objects[i/16] = new GameObject(String.format("Object %d", i), new Transform(new Vector2f(i, 100), new Vector2f(17, 17)), 1);
            objects[i/16].addComponent(new SpriteRenderer(sprites.getSprite(1)));
            this.addGameObjectToScene(objects[i/16]);
        }*/


    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        48, 48, 2, 0));
    }
    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;
    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            camera.position.x += 100f * dt;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
            camera.position.x -= 100f * dt;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            camera.position.y += 100f * dt;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.position.y -= 100f * dt;
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        /*spriteFlipTimeLeft -= dt;
        if (spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 1) {
                spriteIndex = 0;
            }
            obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }*/

        this.renderer.render();
    }
}