import Map.NewMap;
import java.util.Random;

public class WorldGen {

    private static final int WORLD_WIDTH = 100;
    private static final int WORLD_HEIGHT = 100;
    private static final int BLOCK_SIZE = 16;

    private static final int GRASS_HEIGHT = 5;
    private static final int DIRT_HEIGHT = 10;
    private static final int STONE_HEIGHT = 20;

    private static final int TREE_CHANCE = 5;

    private static final int TREE_HEIGHT = 8;
    private static final int TREE_WIDTH = 5;

    private static final int LOOP_SIZE = 10;

    private static final Random RANDOM = new Random();

    public static int[][] generateWorld() {
        int[][] terrain = new int[WORLD_WIDTH][WORLD_HEIGHT];

        for (int x = 0; x < WORLD_WIDTH; x++) {
            int dirtHeight = RANDOM.nextInt(DIRT_HEIGHT);
            for (int y = 0; y < dirtHeight; y++) {
                terrain[x][y] = 1;
            }

            int grassHeight = GRASS_HEIGHT + RANDOM.nextInt(3);
            for (int y = dirtHeight; y < dirtHeight + grassHeight; y++) {
                terrain[x][y] = 2;
            }

            int stoneHeight = STONE_HEIGHT + RANDOM.nextInt(5);
            for (int y = dirtHeight + grassHeight; y < dirtHeight + grassHeight + stoneHeight; y++) {
                terrain[x][y] = 3;
            }

            if (RANDOM.nextInt(TREE_CHANCE) == 0 && x + TREE_WIDTH < WORLD_WIDTH) {
                int treeHeight = TREE_HEIGHT + RANDOM.nextInt(3);
                for (int y = dirtHeight + grassHeight + stoneHeight; y < dirtHeight + grassHeight + stoneHeight + treeHeight; y++) {
                    terrain[x + TREE_WIDTH / 2][y] = 4;
                }
                for (int i = 0; i < TREE_WIDTH; i++) {
                    terrain[x + i][dirtHeight + grassHeight + stoneHeight + treeHeight] = 4;
                }
            }
        }

        for (int x = 0; x < LOOP_SIZE; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                terrain[WORLD_WIDTH - LOOP_SIZE + x][y] = terrain[x][y];
            }
        }

        return terrain;
    }
}