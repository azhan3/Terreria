# Game Test Cases

This document contains a list of test cases for Terreria.

### Test Case: Batch Rendering

**Description:** Verify the functionality of batch rendering for improved performance.

**Preconditions:** The game has multiple game objects with sprite renderers that need to be rendered efficiently.

**Steps:**
1. Start the game.
2. Create and add multiple game objects with sprite renderers to the scene.
3. Verify that the sprite renderers are added to the appropriate render batch based on their z-index.
4. Verify that the render batches are sorted correctly based on their z-index.
5. Check that the render batches are created and initialized correctly during the game's initialization process.
6. Trigger a render update.
7. Verify that the render batches are rendered in the correct order.
8. Check that the render batches utilize batch rendering techniques to minimize draw calls.
9. Verify that the appropriate shader is used for rendering the render batches.
10. Check that the render batches correctly upload the projection and view matrices to the shader.
11. Verify that the render batches bind the correct textures and upload the texture IDs to the shader.
12. Check that the render batches bind the correct vertex array object (VAO) and enable the vertex attribute pointers.
13. Verify that the render batches render the sprites using the appropriate OpenGL draw calls.
14. Check that the render batches disable the vertex attribute pointers and unbind the VAO after rendering.
15. Verify that the render batches correctly unbind the textures and detach the shader after rendering.
16. Repeat the test with different combinations of game objects and sprite renderers to ensure proper rendering in various scenarios.

**Expected Result:**
The batch rendering system should effectively group and render the sprite renderers based on their z-index, resulting in improved performance by minimizing draw calls and efficiently utilizing OpenGL's rendering capabilities. The render batches should correctly handle shader and texture bindings, enable and disable vertex attribute pointers, and render the sprites in the correct order based on their z-index.



### Test Case: Player Movement

**Description:** Verify that the player can move in different directions.

**Steps:**
1. Start the game.
2. Verify that the player character is controllable.
3. Press the arrow keys or WASD keys to move the player character.
4. Verify that the player character moves accordingly.
5. Repeat for different directions.

**Expected Result:**
The player character should be able to move in different directions based on the input from the arrow keys or WASD keys. The character's movement should be responsive and aligned with the direction of the keys pressed.


### Test Case: Block Placement

**Description:** Validate the ability to place blocks in the game world.

**Steps:**
1. Start the game.
2. Open the player's inventory.
3. Select a block from the inventory.
4. Move the player character to a desired location.
5. Right-click to place the selected block.
6. Verify that the block is placed correctly in the world.
7. Repeat for different block types.

**Expected Result:**
The player should be able to select a block from the inventory and place it in the game world by right-clicking at the desired location. The placed block should appear in the world at the correct position and orientation, matching the player's action. This process should be repeatable for different types of blocks, ensuring consistent and accurate block placement.



### Test Case: Block Breaking

**Description:** Verify the ability to break blocks in the game world.

**Steps:**
1. Start the game.
2. Move the player character close to a breakable block.
3. Verify that the block is interactable and can be broken.
4. Left-click on the block to initiate the breaking action.
5. Verify that the block disappears from the game world.
6. Verify that the block deletion is updated correctly in the Scene GameObjects List.

**Expected Result:**
The player should be able to interact with breakable blocks by left-clicking on them, initiating the breaking action. When a block is broken, it should disappear from the game world and be correctly updated in the GameObjects list of the Scene. The block's deletion should be reflected in the game world and the corresponding GameObjects list, ensuring that it is no longer present or interactable.


### Test Case: Serialization with GSON

**Description:** Verify the serialization and deserialization of game data using GSON.

**Steps:**
1. Start the game.
2. Play the game and create some progress or modify game data.
3. Trigger a save action or a checkpoint in the game to initiate serialization.
4. Verify that the game data is serialized into a JSON format into `level.txt` using GSON.
5. Exit the game or simulate a game restart.
6. Start the game again and trigger a load action or load from a checkpoint.
7. Verify that the saved game data is deserialized using GSON.
8. Check that the loaded game data matches the previously saved data, including player progress, world state, etc.
9. Repeat the test for different scenarios, such as saving and loading at different points in the game.

**Expected Result:**
The game data should be successfully serialized into a JSON format using GSON when saving. The serialized data should be stored in the `level.txt` file. When loading, the game should be able to deserialize the data from `level.txt` using GSON. The loaded game data should match the previously saved data, preserving the integrity of the game state. This should include player progress, world state, and any other relevant game data. The serialization and deserialization process should allow the player to continue from where they left off without any data loss or corruption.

