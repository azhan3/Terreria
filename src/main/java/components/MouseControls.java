package components;

import engine.GameObject;
import engine.MouseListener;
import engine.Window;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import physics2d.components.PillboxCollider;
import renderer.DebugDraw;
import util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class MouseControls extends Component {
    GameObject holdingObject = null;
    Float curX, curY;
    int curGridX, curGridY;

    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        System.out.println(this.holdingObject);
    }
    public void place() {
        if (holdingObject.getComponent(PillboxCollider.class) != null) {
            Window.getScene().addGameObjectToScene(holdingObject);

            holdingObject = null;
            return;
        }
        GameObject newObj = holdingObject.copy();
        if (newObj.getComponent(StateMachine.class) != null) {
            newObj.getComponent(StateMachine.class).refreshTextures();
        }
        newObj.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
        newObj.removeComponent(NonPickable.class);
        Window.getScene().addGameObjectToScene(newObj);
    }

    @Override
    public void update(float dt) {

        curX = MouseListener.getOrthoX();
        curY = MouseListener.getOrthoY();
        curGridX = (int)(curX / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
        curGridY = (int)(curY / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;
        //System.out.println(curGridX + " " + curGridY);
        DebugDraw.addLine2D(new Vector2f(curGridX, curGridY+8), new Vector2f(curGridX+16, curGridY+8));
        DebugDraw.addLine2D(new Vector2f(curGridX+8, curGridY), new Vector2f(curGridX+8, curGridY+16));
        if (holdingObject != null) {

            holdingObject.transform.position.x = curGridX + 8;
            holdingObject.transform.position.y = curGridY + 8;
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT) && !blockInSquare(curGridX, curGridY)) {
                place();
            }
        }
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            deleteBlock(curGridX, curGridY);
        }
    }
    private void deleteBlock(float x, float y) {
        for (GameObject block : Window.getScene().getGameObjects()) {
            if (block.transform.position.x == x + 8 && block.transform.position.y == y + 8) {
                block.destroy();
            }
        }
    }

    public static boolean blockInSquare(float x, float y) {
        for (GameObject block : Window.getScene().getGameObjects()) {
            if (block.transform.position.x == x + 8 && block.transform.position.y == y + 8) {
                return true;
            }
        }

        return false;
    }

}