package components;

import engine.GameObject;
import engine.MouseListener;
import engine.Window;
import org.joml.Vector2f;
import renderer.DebugDraw;
import util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObject = null;
    Float curX, curY;
    int curGridX, curGridY;

    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        System.out.println(this.holdingObject);
    }
    public void place() {
        Window.getScene().addGameObjectToScene(this.holdingObject);
        this.holdingObject = null;
        return;
//        if (this.holdingObject.getComponent(PillboxCollider.class) != null) {
//
//        }
//        GameObject newObj = holdingObject.copy();
//        if (newObj.getComponent(StateMachine.class) != null) {
//            newObj.getComponent(StateMachine.class).refreshTextures();
//        }
//        newObj.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
//        newObj.removeComponent(NonPickable.class);
//        Window.getScene().addGameObjectToScene(newObj);
    }
//    public void place() {
//        this.holdingObject.addComponent(new Box2DCollider());
//        Window.getScene().addGameObjectToScene(this.holdingObject);
//        System.out.println(this.holdingObject.getComponent(Box2DCollider.class));
//
//        this.holdingObject = null;
//    }

    @Override
    public void update(float dt) {
        curX = MouseListener.getOrthoX();
        curY = MouseListener.getOrthoY();
        curGridX = (int)(curX / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
        curGridY = (int)(curY / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;
        DebugDraw.addLine2D(new Vector2f(curGridX, curGridY+8), new Vector2f(curGridX+16, curGridY+8));
        DebugDraw.addLine2D(new Vector2f(curGridX+8, curGridY), new Vector2f(curGridX+8, curGridY+16));

        if (holdingObject != null) {

            holdingObject.transform.position.x = curGridX + 8;
            holdingObject.transform.position.y = curGridY + 8;
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }

}