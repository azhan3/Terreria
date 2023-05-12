package Map;

import components.Component;
import components.Ground;
import components.Sprite;
import components.Spritesheet;
import engine.GameObject;
import engine.Prefabs;
import engine.Window;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.Rigidbody2D;
import physics2d.enums.BodyType;
import util.AssetPool;
import java.util.Random;





public class NewMap extends Component {
    private static final int WORLD_WIDTH = 95;  // Width of the world in blocks
    private static final int WORLD_HEIGHT = 15; // Height of the world in blocks
    private static final int BLOCK_SIZE = 16;    // Size of each block (assumed square)

    private static final int SKY_HEIGHT = 20;    // Height of the sky area (blocks above ground)




    static Spritesheet blocks = AssetPool.getSpritesheet("assets/images/spritesheets/blocks.png");
    static Sprite dirt = blocks.getSprite(0);
    static Sprite grass = blocks.getSprite(8);
    private static Random random = new Random();

    public NewMap() {

        generateWholeWorld();
    }

    public static void generateWholeWorld() {
        int[][] terrain = new int[WORLD_WIDTH][WORLD_HEIGHT];

        // Fill terrain with blocks
        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                terrain[x][y] = WORLD_HEIGHT - SKY_HEIGHT - 1;
            }
        }



        createBlocksFromTerrain(terrain);
        addGrassToTerrain(terrain);


    }


    private static void createBlocksFromTerrain(int[][] terrain) {
        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                if (y <= terrain[x][y]) {
                    createBlock(x, y, dirt);
                } else {
                    for (int i = terrain[x][y] + 1; i <= y; i++) {
                        createBlock(x, i, dirt);
                    }
                }
            }
        }

    }
    public static void addGrassToTerrain(int[][] terrain) {
        int grassHeight = WORLD_HEIGHT + 1;
        for (int x = 0; x < WORLD_WIDTH; x++) {
            createBlock(x,SKY_HEIGHT-5,grass);

        }
    }

    private static void createBlock (int x, int y, Sprite sprite) {
        GameObject block = Prefabs.generateSpriteObject(sprite, BLOCK_SIZE, BLOCK_SIZE);
        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Static);
        block.addComponent(rb);
        Box2DCollider b2d = new Box2DCollider();
        b2d.setHalfSize(new Vector2f(BLOCK_SIZE / 2.0f, BLOCK_SIZE / 2.0f));
        block.addComponent(b2d);
        block.addComponent(new Ground());
        block.transform.position = new Vector2f(x * BLOCK_SIZE + BLOCK_SIZE / 2.0f, y * BLOCK_SIZE + BLOCK_SIZE / 2.0f);
        Window.getScene().addGameObjectToScene(block);

    }
}

