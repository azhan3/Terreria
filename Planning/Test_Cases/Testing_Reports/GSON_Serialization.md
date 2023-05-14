# Testing Report: Serialization with GSON

### **Date:** 2023-04-28

### **Tester:** Alex

### **Application:** Terraria Java Game with LWJGL

## Test Case: Serialization with GSON

**Description:**
Verify the serialization and deserialization of game data using GSON.

### Steps and Results:

1. Start the game.
    - Result: Game started successfully.

2. Play the game and create some progress or modify game data.
    - Result: Progress made and game data modified successfully.

3. Trigger a save action or a checkpoint in the game to initiate serialization.
    - Result: Save action triggered successfully.

4. Verify that the game data is serialized into a JSON format into `level.txt` using GSON.
    - Result: Game data successfully serialized into `level.txt` in JSON format using GSON.

5. Exit the game or simulate a game restart.
    - Result: Game exited or restarted successfully.

6. Start the game again and trigger a load action or load from a checkpoint.
    - Result: Load action triggered successfully.

7. Verify that the saved game data is deserialized using GSON.
    - Result: Saved game data successfully deserialized using GSON.

8. Check that the loaded game data matches the previously saved data, including player progress, world state, etc.
    - Result: Loaded game data matches the previously saved data, preserving player progress, world state, and other relevant game data.

9. Repeat the test for different scenarios, such as saving and loading at different points in the game.
    - Result: Test successfully repeated for different scenarios, demonstrating consistent serialization and deserialization.

### Expected Result:
The game data should be successfully serialized into a JSON format using GSON when saving. The serialized data should be stored in the `level.txt` file. When loading, the game should be able to deserialize the data from `level.txt` using GSON. The loaded game data should match the previously saved data, preserving the integrity of the game state. This should include player progress, world state, and any other relevant game data. The serialization and deserialization process should allow the player to continue from where they left off without any data loss or corruption.

Note: This is a testing report, and the actual results may vary based on the specific implementation and environment.
