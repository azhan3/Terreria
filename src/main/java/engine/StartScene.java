package engine;

public class StartScene extends Scene {
    public StartScene() {
        System.out.println("Inside level scene");
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
    }

    @Override
    public void update(float dt) {

    }
}
