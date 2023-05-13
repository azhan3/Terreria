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
