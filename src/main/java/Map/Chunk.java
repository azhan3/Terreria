package Map;

import components.Sprite;
import engine.GameObject;

public class Chunk {
    public static final int CHUNK_SIZE = 32;
    public int chunkX, chunkY;
    public GameObject[][] blocks;
    private boolean generated = false;

    public Chunk(int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.blocks = new GameObject[CHUNK_SIZE][CHUNK_SIZE];
    }

    public void generate(PerlinNoise noise) {
        if (generated) return;
        
        for (int localX = 0; localX < CHUNK_SIZE; localX++) {
            int worldX = chunkX * CHUNK_SIZE + localX;
            
            // Sample noise for surface height.
            // Adjust the multiplier (0.05) to change the frequency (smoothness) of hills.
            double noiseVal = noise.noise(worldX * 0.05, 0.0);
            
            // Map noise from ~[-0.7, 0.7] to a height value around 20
            int surfaceHeight = (int) (20 + noiseVal * 10);

            for (int localY = 0; localY < CHUNK_SIZE; localY++) {
                int worldY = chunkY * CHUNK_SIZE + localY;
                
                // Only instantiate blocks if they are underground or on the surface
                if (worldY <= surfaceHeight) {
                    Sprite spriteToUse;
                    if (worldY == surfaceHeight) {
                        spriteToUse = NewMap.grass;
                        
                        // Deterministically decide to spawn a tree on this surface block (approx 8% chance)
                        // Use the worldX coordinate as a seed to ensure it's the same every time we visit
                        java.util.Random treeRand = new java.util.Random(worldX * 8191L ^ 0x1337);
                        if (treeRand.nextFloat() < 0.08f) {
                            createTree(worldX, worldY + 1);
                        }
                        
                    } else if (worldY > surfaceHeight - 5) {
                        spriteToUse = NewMap.dirt;
                    } else {
                        spriteToUse = NewMap.stone;
                    }
                    
                    GameObject block = NewMap.createBlock(worldX, worldY, spriteToUse);
                    blocks[localX][localY] = block;
                }
            }
        }
        generated = true;
    }

    private void createTree(int startX, int startY) {
        for (int y = 0; y < 10; y++) {
            NewMap.createBlock(startX, y + startY, NewMap.treeTrunk);
        }
        NewMap.createBlock(startX + 1, startY + 5, NewMap.treeStickRight);
        NewMap.createBlock(startX - 1, startY + 8, NewMap.treeStickLeft);
        NewMap.createBlock(startX, startY + 10, NewMap.treeTrunkCap);
        NewMap.createBlock(startX + 1, startY, NewMap.treeRoot);
        
        NewMap.createBlock(startX - 1, startY + 10, NewMap.leaves);
        NewMap.createBlock(startX - 2, startY + 10, NewMap.leaves);
        NewMap.createBlock(startX + 1, startY + 10, NewMap.leaves);
        NewMap.createBlock(startX + 2, startY + 10, NewMap.leaves);
        
        for (int x = 0; x < 7; x++) {
            NewMap.createBlock(x + startX - 3, startY + 11, NewMap.leaves);
        }
        for (int x = 0; x < 5; x++) {
            NewMap.createBlock(x + startX - 2, startY + 12, NewMap.leaves);
        }
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 3; x++) {
                NewMap.createBlock(x + startX - 1, y + startY + 13, NewMap.leaves);
            }
        }
    }
}
