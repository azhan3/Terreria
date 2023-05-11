package components;

import engine.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics2d.Physics2D;
import physics2d.components.PillboxCollider;
import physics2d.components.Rigidbody2D;
import util.AssetPool;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController extends Component {
    public float walkSpeed = 1000;
    public float jumpBoost = 100.0f;
    public float jumpImpulse = 3.0f;
    public float slowDownForce = 0.05f;
    public float terminal_velocity = -1000.0f;
    public transient boolean onGround = false, ceiling = false, right=false, left=false;
    private transient float groundDebounce = 0.0f;
    private transient float groundDebounceTime = 0.1f;
    private transient Rigidbody2D rb;
    private transient StateMachine stateMachine;
    private transient float bigJumpBoostFactor = 1.05f;
    private transient float playerWidth = 0.25f;
    private transient int jumpTime = 0;
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient boolean isDead = false;
    private transient int enemyBounce = 0;

    @Override
    public void start() {
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        System.out.println(rb);

        this.stateMachine = gameObject.getComponent(StateMachine.class);
        System.out.println(stateMachine);

        this.rb.setGravityScale(0.0f);

        System.out.println("STARTED STARTED STARTED");
    }

    @Override
    public void update(float dt) {
        boolean idle = true;
        checkOnGround();
        //System.out.println(right);
        System.out.println(onGround);



        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyListener.isKeyPressed(GLFW_KEY_D)) {
            this.velocity.x = walkSpeed;
            this.gameObject.transform.scale.x = 43;
            this.stateMachine.trigger("Walk");
            idle = false;

        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A)) {
            this.velocity.x = -walkSpeed;
            this.gameObject.transform.scale.x = -43;
            idle = false;
            this.stateMachine.trigger("Walk");
        }

        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            this.stateMachine.trigger("Swing");
            idle = false;

        }
        if (idle) {
            this.stateMachine.trigger("Idle");
            this.velocity.x = 0;
        }

        if ((KeyListener.isKeyPressed(GLFW_KEY_SPACE) || KeyListener.isKeyPressed(GLFW_KEY_W)) && (jumpTime > 0 || onGround)) {
            if (onGround && jumpTime==0) {
                jumpTime = 65;
                this.velocity.y = 1000.0f;
            } else if (jumpTime > 0) {
                jumpTime--;
                this.velocity.y = 1000.0f;

            } else {
                this.velocity.y = 0.0f;
            }
            stateMachine.trigger("Jump");

        }
        if (!onGround) {
            this.stateMachine.trigger("Jump");
        }



        this.velocity.y += Window.getPhysics().getGravity().y;
        this.velocity.y = Math.max(terminal_velocity, this.velocity.y);
        Window.getScene().camera().position.x = this.gameObject.transform.position.x - 632;
        Window.getScene().camera().position.y = this.gameObject.transform.position.y - 328;
        //System.out.println(this.velocity);
        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0);
    }
    public void checkOnGround() {
        float innerPlayerWidth = 43;
        float yVal = 30;
        onGround = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }
}
