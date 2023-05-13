# Testing Report: Block Breaking

###**Date:** 2023-05-08

###**Tester:** Alex

###**Application:** Terraria Java Game with LWJGL

## Test Case: Block Breaking

**Description:**
Verify the ability to break blocks in the game world.

### Steps and Results:

1. Start the game.
    - Result: Game started successfully.

2. Move the player character close to a breakable block.
    - Result: Player character successfully positioned near a breakable block.

3. Verify that the block is interactable and can be broken.
    - Result: The block is interactable and can be broken.

4. Left-click on the block to initiate the breaking action.
    - Result: Successful initiation of breaking action on the block.

5. Verify that the block disappears from the game world.
    - Result: The block disappears from the game world after being broken.

6. Verify that the block deletion is updated correctly in the Scene GameObjects List.
    - Result: The block's deletion is correctly updated in the GameObjects list of the Scene.

### Expected Result:
The player should be able to interact with breakable blocks by left-clicking on them, initiating the breaking action. When a block is broken, it should disappear from the game world and be correctly updated in the GameObjects list of the Scene. The block's deletion should be reflected in the game world and the corresponding GameObjects list, ensuring that it is no longer present or interactable.

Note: This is a testing report, and the actual results may vary based on the specific implementation and environment.
