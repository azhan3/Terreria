package Map;





import components.*;
import engine.GameObject;
import engine.Prefabs;
import engine.Window;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.Rigidbody2D;
import physics2d.enums.BodyType;
import util.AssetPool;
import java.util.Random;
import static components.MouseControls.blockInSquare;






public class NewMap extends Component {
    private static final int WORLD_WIDTH = 50;  // Width of the world in blocks
    private static final int WORLD_HEIGHT = 15; // Height of the world in blocks
    private static final int BLOCK_SIZE = 16;    // Size of each block (assumed square)

    private static final int SKY_HEIGHT = 20;    // Height of the sky area (blocks above ground)

    private static final int CAVE_WIDTH = 5;
    private static final int CAVE_HEIGHT = 5;




    static Spritesheet blocks = AssetPool.getSpritesheet("assets/images/spritesheets/blocks.png");
    static Sprite dirt = blocks.getSprite(0);
    static Sprite grass = blocks.getSprite(8);
    static Sprite upsideDownGrass = blocks.getSprite(6);
    static Sprite stone = blocks.getSprite(27);
    static Sprite cornerGrassRight = blocks.getSprite(11);
    static Sprite cornerGrassLeft = blocks.getSprite(13);
    static Sprite upsideDownCornerGrassRight = blocks.getSprite(4);
    static Sprite upsideDownCornerGrassLeft = blocks.getSprite(5);
    static Sprite sidewaysGrassLeft = blocks.getSprite(12);
    static Sprite sidewaysGrassRight = blocks.getSprite(14);
    static Sprite treeTrunk = blocks.getSprite(20);
    static Sprite treeTrunkCap = blocks.getSprite(26);
    static Sprite treeStickLeft = blocks.getSprite(24);
    static Sprite treeStickRight = blocks.getSprite(17);
    static Sprite treeRoot = blocks.getSprite(19);
    static Sprite leaves = blocks.getSprite(10);
    static Sprite longGrass = blocks.getSprite(3);




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


        createStaircase();
        createInvertedStaircase();
        createFlippedHorizontalStaircase();
        createRandomOres();
        createFloatingIsland();
        createBlocksFromTerrain(terrain);
        addGrassToTerrain();
        createHill(terrain);
        createTrees(-19,27);
        createTrees(35,16);
        createGrass();
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
        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                if (y <= terrain[x][y]) {
                    createBlock(x, y-10, stone);
                } else {
                    for (int i = terrain[x][y] + 1; i <= y; i++) {
                        createBlock(x, i-10, stone);
                    }
                }
            }
        }


    }
    public static void addGrassToTerrain() {

        for (int x = 0; x < WORLD_WIDTH; x++) {
            createBlock(x,SKY_HEIGHT-5,grass);

        }
    }
    private static void createRandomOres() {
        int startX = -46; // the x-coordinate where the structure will begin
        int startY = -15; // the y-coordinate where the structure will begin
        int width = 50; // the width of the structure
        int height = 20; // the height of the structure
        int passageWidth = 4; // the width of the passages between the stones

        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
                if (x < startX + passageWidth || x >= startX + width - passageWidth ||
                        Math.random() < 0.2) {
                    createBlock(x, y, dirt);
                } else {
                    createBlock(x, y, stone);
                }
            }
        }
    }

    private static void createStaircase() {
        int startX = -1; // starting x-coordinate of the staircase
        int startY = SKY_HEIGHT - 6; // starting y-coordinate of the staircase
        int steps = 11; // number of steps in the staircase

// create grass block on bottom of staircase


// create dirt-filled staircase
        for (int i = steps - 1; i >= 0; i--) {
            // create stone blocks to the left of staircase
            for (int y = startY - i; y >= startY - steps; y--) {
                createBlock(startX - i, y, dirt);
            }

            // create dirt blocks for the staircase
            for (int j = 0; j <= i; j++) {
                createBlock(startX - i + j, startY - i, dirt);
            }
            for (int j = 0; j <= i; j++) {
                createBlock(startX - i + j, startY - i+1, cornerGrassRight);
            }
        }
    }
    private static void createFlippedHorizontalStaircase() {
        int startX = -58; // starting x-coordinate of the staircase
        int startY = SKY_HEIGHT ; // starting y-coordinate of the staircase
        int steps = 17; // number of steps in the staircase

// create grass block on bottom of staircase

// create dirt-filled staircase
        for (int i = steps - 1; i >= 0; i--) {
            // create stone blocks to the right of staircase
            for (int y = startY - i; y >= startY - steps; y--) {
                createBlock(startX + i, y, dirt);
            }

            // create dirt blocks for the staircase
            for (int j = 0; j <= i; j++) {
                createBlock(startX + i - j, startY - i, dirt);
            }
            for (int j = 0; j <= i; j++) {
                createBlock(startX + i - j, startY - i + 1, cornerGrassLeft);
            }
        }


    }
    private static void createInvertedStaircase() {
        int startX = -20; // starting x-coordinate of the staircase
        int startY = SKY_HEIGHT - 6; // starting y-coordinate of the staircase
        int steps = 11; // number of steps in the staircase

        // create grass block on top of staircase
        for (int i = 0; i <= steps+1; i++) {
            for (int j = 0; j <= i ; j++) {
                createBlock(startX + j, startY + i-1, upsideDownCornerGrassLeft);
            }
        }

        // create dirt-filled staircase
        for (int i = 0; i <= steps; i++) {
            for (int j = 0; j < i + 1; j++) {
                createBlock(startX + j, startY + i, dirt);
            }
        }

        // create stone blocks to the left of staircase


    }

    private static void createFloatingIsland(){

        for (int x = 0; x < 16; x++) {
            createBlock(x-36,SKY_HEIGHT-7,upsideDownGrass);

        }

        int startX = 20; // starting x-coordinate of the staircase
        int startY = SKY_HEIGHT - 6; // starting y-coordinate of the staircase
        int steps = 11; // number of steps in the staircase

// create grass block on top of staircase
        for (int i = 0; i <= steps+1; i++) {
            for (int j = 0; j <= i ; j++) {
                createBlock(startX - j -57, startY + i-1, upsideDownCornerGrassRight);
            }
        }

// create dirt-filled staircase
        for (int i = 0; i <= steps; i++) {
            for (int j = 0; j < i + 1; j++) {
                createBlock(startX - j -57, startY + i, dirt);
            }
        }
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 12; y++) {
                createBlock(x-36, y+14, dirt);
            }
        }

        for (int x = 0; x < 42; x++) {
            createBlock(x-49,SKY_HEIGHT+6,grass);

        }
    }

    private static void createHill(int[][] terrain){


        for (int x = 0; x < 23; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                if (y <= terrain[x][y]) {
                    createBlock(x-74, y+6, dirt);
                } else {
                    for (int i = terrain[x][y] + 1; i <= y; i++) {
                        createBlock(x-74, i, dirt);
                    }
                }
            }
        }
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 12; y++) {
                createBlock(x-74, y+9, dirt);
            }
        }
        for (int x = 0; x < 36; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                if (y <= terrain[x][y]) {
                    createBlock(x-74, y-10, stone);
                } else {
                    for (int i = terrain[x][y] + 1; i <= y; i++) {
                        createBlock(x-74, i-10, stone);
                    }
                }
            }
        }
        //ground
        for (int x = 0; x < 16; x++) {
            createBlock(x-74,21,dirt);

        }
        //bottom layer hill
        for (int y = 0; y <3;y++) {
            for (int x = 0; x < 14; x++) {
                createBlock(x - 73, y+22, dirt);

            }
        }
        //2nd last layer hill
        for (int x = 0; x < 13; x++) {
            createBlock(x-73,25,dirt);

        }
        //3rd last layer hill
        for (int y = 0; y <2;y++) {
            for (int x = 0; x < 12; x++) {
                createBlock(x - 73, y+26, dirt);

            }
        }
        //4th last layer hill
        for (int y = 0; y <2;y++) {
            for (int x = 0; x < 10; x++) {
                createBlock(x - 72, y+28, dirt);

            }
        }
        //5th layer hill
        for (int x = 0; x < 8; x++) {
            createBlock(x-71,30,dirt);

        }
        //top layer hill
        for (int x = 0; x < 6; x++) {
            createBlock(x-70,31,dirt);

        }
        createBlock(- 59,22,sidewaysGrassRight);
        createBlock(- 59,23,sidewaysGrassRight);
        createBlock(- 59,24,cornerGrassLeft);

        for (int y = 0; y < 4; y++) {
            createBlock(-74,y+22,sidewaysGrassLeft);

        }

        createBlock(- 74,26,cornerGrassRight);
        createBlock(- 73,28,cornerGrassRight);
        createBlock(- 72,30,cornerGrassRight);
        createBlock(- 71,31,cornerGrassRight);
        createBlock(- 70,32,cornerGrassRight);
        createBlock(- 65,32,cornerGrassLeft);
        createBlock(- 64,31,cornerGrassLeft);
        createBlock(- 63,30,cornerGrassLeft);
        createBlock(- 62,28,cornerGrassLeft);
        createBlock(- 61,26,cornerGrassLeft);
        createBlock(- 60,25,cornerGrassLeft);
        for (int x = 0; x < 4; x++) {
            createBlock(x-69,32,grass);

        }
    }

    private static void createTrees(int startX, int startY){
        for (int y = 0; y < 10; y++) {
            createBlock(startX,y+startY,treeTrunk);

        }
        createBlock(startX+1,startY+5,treeStickRight);
        createBlock(startX-1,startY+8,treeStickLeft);
        createBlock(startX,startY+10,treeTrunkCap);
        createBlock(startX+1,startY,treeRoot);
        createBlock(startX-1,startY+10,leaves);
        createBlock(startX-2,startY+10,leaves);
        createBlock(startX+1,startY+10,leaves);
        createBlock(startX+2,startY+10,leaves);
        for (int x = 0; x < 7; x++) {
            createBlock(x+startX-3,startY+11,leaves);
        }
        for (int x = 0; x < 5; x++) {
            createBlock(x+startX-2,startY+12,leaves);
        }
        for (int y = 0; y<2; y++) {
            for (int x = 0; x < 3; x++) {
                createBlock(x+startX - 1, y+startY + 13, leaves);
            }
        }
    }
    private static void createGrass(){

        createBlock(5,16,longGrass);
        createBlock(10,16,longGrass);
        createBlock(20,16,longGrass);
        createBlock(21,16,longGrass);
        createBlock(22,16,longGrass);
        createBlock(30,16,longGrass);
        createBlock(31,16,longGrass);
        createBlock(32,16,longGrass);
        createBlock(41,16,longGrass);
        createBlock(42,16,longGrass);
        createBlock(43,16,longGrass);
        createBlock(44,16,longGrass);


        createBlock(-15,27,longGrass);
        createBlock(-14,27,longGrass);
        createBlock(-13,27,longGrass);
        createBlock(-12,27,longGrass);
        createBlock(-11,27,longGrass);
        createBlock(-21,27,longGrass);
        createBlock(-22,27,longGrass);
        createBlock(-23,27,longGrass);
        createBlock(-28,27,longGrass);
        createBlock(-32,27,longGrass);
        createBlock(-33,27,longGrass);
        createBlock(-37,27,longGrass);
        createBlock(-43,27,longGrass);
        createBlock(-44,27,longGrass);
        createBlock(-46,27,longGrass);
    }







    private static void createBlock (int x, int y, Sprite sprite) {
        int gridX = (int) (x * BLOCK_SIZE + BLOCK_SIZE / 2.0f);
        int gridY = (int) (y * BLOCK_SIZE + BLOCK_SIZE / 2.0f);
        if (blockInSquare(gridX, gridY)) {
            return;
        }
        GameObject block = Prefabs.generateSpriteObject(sprite, BLOCK_SIZE, BLOCK_SIZE);
        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Static);
        block.addComponent(rb);
        Box2DCollider b2d = new Box2DCollider();
        b2d.setHalfSize(new Vector2f(17, 16));
        block.addComponent(b2d);
        block.addComponent(new Ground());
        block.transform.position = new Vector2f(gridX, gridY);
        Window.getScene().addGameObjectToScene(block);

    }
}

