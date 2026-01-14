package Map;

import org.joml.Vector2f;
import java.util.HashMap;
import java.util.Objects;

public class ChunkManager {

    private static class ChunkPos {
        int x, y;

        ChunkPos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChunkPos chunkPos = (ChunkPos) o;
            return x == chunkPos.x && y == chunkPos.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private HashMap<ChunkPos, Chunk> chunks = new HashMap<>();
    private PerlinNoise noise;

    public ChunkManager(long seed) {
        this.noise = new PerlinNoise(seed);
    }

    public void update(Vector2f cameraPosition) {
        // Convert pixel coordinates to block coordinates
        float camBlockX = cameraPosition.x / NewMap.BLOCK_SIZE;
        float camBlockY = cameraPosition.y / NewMap.BLOCK_SIZE;

        int camChunkX = (int) Math.floor(camBlockX / Chunk.CHUNK_SIZE);
        int camChunkY = (int) Math.floor(camBlockY / Chunk.CHUNK_SIZE);

        // Load chunks in a 3x3 grid around the camera to save memory
        for (int x = camChunkX - 1; x <= camChunkX + 1; x++) {
            for (int y = camChunkY - 1; y <= camChunkY + 1; y++) {
                ChunkPos pos = new ChunkPos(x, y);
                if (!chunks.containsKey(pos)) {
                    Chunk chunk = new Chunk(x, y);
                    chunk.generate(noise);
                    chunks.put(pos, chunk);
                }
            }
        }
    }
}
