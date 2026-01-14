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

import static components.MouseControls.blockInSquare;
import static components.MouseControls.holdingObject;

public class NewMap extends Component {
    public static final int BLOCK_SIZE = 16;

    static Spritesheet blocks = AssetPool.getSpritesheet("assets/images/spritesheets/blocks.png");
    public static Sprite dirt = blocks.getSprite(0);
    public static Sprite grass = blocks.getSprite(8);
    public static Sprite upsideDownGrass = blocks.getSprite(6);
    public static Sprite stone = blocks.getSprite(27);
    public static Sprite cornerGrassRight = blocks.getSprite(11);
    public static Sprite cornerGrassLeft = blocks.getSprite(13);
    public static Sprite upsideDownCornerGrassRight = blocks.getSprite(4);
    public static Sprite upsideDownCornerGrassLeft = blocks.getSprite(5);
    public static Sprite sidewaysGrassLeft = blocks.getSprite(12);
    public static Sprite sidewaysGrassRight = blocks.getSprite(14);
    public static Sprite treeTrunk = blocks.getSprite(20);
    public static Sprite treeTrunkCap = blocks.getSprite(26);
    public static Sprite treeStickLeft = blocks.getSprite(24);
    public static Sprite treeStickRight = blocks.getSprite(17);
    public static Sprite treeRoot = blocks.getSprite(19);
    public static Sprite leaves = blocks.getSprite(10);
    public static Sprite longGrass = blocks.getSprite(3);
    public static Sprite diamonds = blocks.getSprite(30);

    private ChunkManager chunkManager;

    public NewMap() {
        chunkManager = new ChunkManager(1337L);

        // Pre-generate chunks around the origin before spawning the player
        chunkManager.update(new Vector2f(0, 0));
        
        createPlayer(0, 35);

        // Add this NewMap component to the scene so update() is called each frame
        GameObject mapManager = new GameObject("MapManager", new engine.Transform(), 0);
        mapManager.addComponent(this);
        Window.getScene().addGameObjectToScene(mapManager);
    }

    @Override
    public void update(float dt) {
        if (Window.getScene().camera() != null) {
            chunkManager.update(Window.getScene().camera().position);
        }
    }

    private static void createPlayer(float x, float y) {
        MouseControls.holdingObject = Prefabs.generatePlayer();
        holdingObject.transform.position.x = 16 * x;
        holdingObject.transform.position.y = 16 * y;
        MouseControls.place();
    }

    public static GameObject createBlock(int x, int y, Sprite sprite) {
        int gridX = (int) (x * BLOCK_SIZE + BLOCK_SIZE / 2.0f);
        int gridY = (int) (y * BLOCK_SIZE + BLOCK_SIZE / 2.0f);
        if (blockInSquare(gridX, gridY)) {
            return null;
        }
        GameObject block = Prefabs.generateSpriteObject(sprite, BLOCK_SIZE, BLOCK_SIZE);
        block.setNoSerialize(); // Disable serialization for terrain to save storage!
        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Static);
        block.addComponent(rb);
        Box2DCollider b2d = new Box2DCollider();
        b2d.setHalfSize(new Vector2f(17, 16));
        block.addComponent(b2d);
        block.addComponent(new Ground());
        block.transform.position = new Vector2f(gridX, gridY);
        Window.getScene().addGameObjectToScene(block);
        
        return block;
    }
}
