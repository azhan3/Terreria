package components;

import engine.GameObject;
import engine.KeyListener;
import engine.MouseListener;
import engine.Window;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics2d.components.PillboxCollider;
import physics2d.components.Rigidbody2D;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController extends Component {
    public float walkSpeed = 1.9f;
    public float jumpBoost = 1.0f;
    public float jumpImpulse = 3.0f;
    public float slowDownForce = 0.05f;
    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);

    public transient boolean onGround = false;
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

        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            idle = false;
            this.stateMachine.trigger("Walk");
            this.velocity.x = 2.0f;
        }  if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            this.velocity.x = -2.0f;
            idle = false;

            this.stateMachine.trigger("Walk");
            Body body = this.gameObject.getComponent(Rigidbody2D.class).getRawBody();
            float angle = body.getTransform().q.getAngle();
            Vec2 position = body.getPosition();
            Vec2 flippedPosition = new Vec2(position.x + this.velocity.x, position.y);

            body.setTransform(flippedPosition, angle);

        }  if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            this.stateMachine.trigger("Jump");
            this.velocity.y = 2f;
            idle = false;

        }  if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            this.stateMachine.trigger("Swing");
            idle = false;

        }
        if (idle) {
            this.stateMachine.trigger("Idle");
            this.velocity.x = 0;
        }

        Body body = this.gameObject.getComponent(Rigidbody2D.class).getRawBody();
        Vector4f col = collision();
        if (col.z == 1.0f) {
            velocity.y = Math.max(velocity.y, 0);
        } if (col.x == 1.0f) {
            velocity.x = Math.min(velocity.x, 0);
        }
        System.out.println(detectCollision((int) body.getPosition().x, (int) body.getPosition().y, 43, 60));
        Vec2 position = body.getPosition();

        Vec2 newPos =  new Vec2(position.x + velocity.x, position.y + velocity.y);
        this.gameObject.getComponent(Rigidbody2D.class).getRawBody().setTransform(newPos, 0);

        this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;

        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0);
    }

    private Vector4f collision() {
        Body player = this.gameObject.getComponent(Rigidbody2D.class).getRawBody();
        Vector4f dir = new Vector4f();
        boolean canMoveLeft = true;
        boolean canMoveRight = true;
        boolean canMoveUp = true;
        boolean canMoveDown = true;

        for (GameObject i : Window.getScene().getGameObjects()) {
            if (i.getComponent(PillboxCollider.class) != null) continue;
            Vector2f object = i.transform.position;

            if (object.x - 8 > player.getPosition().x + 21) {
                continue;
            }

            if (player.getPosition().x - 21 > object.x + 8) {
                continue;
            }

            if (player.getPosition().y + 30 < object.y - 8) {
                continue;
            }

            if (object.y + 8 < player.getPosition().y - 31) {
                continue;
            }

            // collision detected
            
            
            if (object.x - 8 < player.getPosition().x + 21) {
                dir.x = 1;
            }
            if (player.getPosition().x - 21 < object.x + 8) {
                dir.y = 1;
            }

            if (player.getPosition().y + 30 > object.y - 8) {
                dir.z = 1;
            }

            if (object.y + 8 > player.getPosition().y - 31) {
                dir.w = 1;
            }
            return dir;
        }

        return dir;
    }
    public static Set<String> detectCollision(int playerX, int playerY, int playerWidth, int playerHeight) {
        Set<String> validDirections = new HashSet<>();

        int playerHalfWidth = playerWidth / 2;
        int playerHalfHeight = playerHeight / 2;

        for (GameObject block : Window.getScene().getGameObjects()) {
            if (block.getComponent(PillboxCollider.class) != null) continue;

            int obstacleX = (int) block.transform.position.x;  // x-position of obstacle's middle point
            int obstacleY = (int) block.transform.position.y;  // y-position of obstacle's middle point
            int obstacleWidth = 16;  // obstacle's width
            int obstacleHeight = 16;  // obstacle's height

            int obstacleHalfWidth = obstacleWidth / 2;
            int obstacleHalfHeight = obstacleHeight / 2;

            int dx = Math.abs(playerX - obstacleX);
            int dy = Math.abs(playerY - obstacleY);

            int combinedHalfWidth = playerHalfWidth + obstacleHalfWidth;
            int combinedHalfHeight = playerHalfHeight + obstacleHalfHeight;


            if (dx <= combinedHalfWidth && dy <= combinedHalfHeight) {
                int overlapX = combinedHalfWidth - dx;
                int overlapY = combinedHalfHeight - dy;
                System.out.println(overlapX + " " + overlapY);

                if (overlapX >= overlapY) {
                    if (playerY > obstacleY) {
                        validDirections.add("up");
                    } else {
                        validDirections.add("down");
                    }
                }

                if (overlapY >= overlapX) {
                    if (playerX > obstacleX) {
                        validDirections.add("left");
                    } else {
                        validDirections.add("right");
                    }
                }
            }
        }

        return validDirections;
    }



}
