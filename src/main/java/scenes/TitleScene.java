package scenes;
import components.SpriteRenderer;

import engine.*;
import org.joml.Vector2f;
import renderer.Renderer;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class TitleScene extends Scene {
    int frame_count = 0;
    public TitleScene() {
        System.out.println("TITLE SCENE :)");
        id = 0;
    }
    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(0, 0));
        System.out.println("INIT");

        GameObject title = new GameObject("title", new Transform(new Vector2f(650, 450), new Vector2f(1200, 350)), 10);
        SpriteRenderer title_tex = new SpriteRenderer();
        title_tex.setTexture(AssetPool.getTexture("assets/images/terraria_logo.jpg"));
        title.addComponent(title_tex);
        this.addGameObjectToScene(title);

        GameObject enter = new GameObject("enter", new Transform(new Vector2f(650, 150), new Vector2f(275, 275)), 5);
        SpriteRenderer enter_tex = new SpriteRenderer();
        enter_tex.setTexture(AssetPool.getTexture("assets/images/enter.png"));
        enter.addComponent(enter_tex);
        this.addGameObjectToScene(enter);
    }
    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        Renderer.bindShader(AssetPool.getShader("assets/shaders/default.glsl"));
        AssetPool.getTexture("assets/images/terraria_logo.jpg");
        AssetPool.getTexture("assets/images/enter.png");
        AssetPool.getTexture("assets/images/square.png");
    }
        @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(GLFW_KEY_ENTER)) {
            Window.changeScene(1);
        }
        if (frame_count++ >= 100) {
            if (this.getGameObjects().get(1).getComponent(SpriteRenderer.class).getTexture().getFilepath().equals("assets/images/enter.png")) {
                this.getGameObjects().get(1).getComponent(SpriteRenderer.class).setTexture(AssetPool.getTexture("assets/images/square.png"));
                this.getGameObjects().get(1).getComponent(SpriteRenderer.class).setDirty();
            } else {
                this.getGameObjects().get(1).getComponent(SpriteRenderer.class).setTexture(AssetPool.getTexture("assets/images/enter.png"));
                this.getGameObjects().get(1).getComponent(SpriteRenderer.class).setDirty();
            }
            frame_count = 0;
        }
        this.renderer.render();
    }


}
