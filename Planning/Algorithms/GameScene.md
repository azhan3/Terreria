```java
1. Start GameScene
2. Initialize selected as null
3. Initialize obj1 as null
4. Initialize sprites and blocks as null
5. Initialize obj1Sprite as null
6. Set levelGenerated as false
7. Create GameStuff as a new GameObject with name "Game", Transform with position Vector2f(0, 0), and layer 0
8. Add MouseControls component to GameStuff
9. Add GridLines component to GameStuff
10. Call loadResources method
11. Create camera with position Vector2f(0, 0)
12. Get spritesheet "assets/images/spritesheets/spritesheet.png" from AssetPool and assign it to sprites
13. Get spritesheet "assets/images/spritesheets/blocks.png" from AssetPool and assign it to blocks
14. If levelLoaded is true, then set activeGameObject as the first gameObjects and return
15. Create file with path "level.txt"
16. Try
    17. Create scanner for the file
    18. If scanner has next line, then set levelGenerated as true; otherwise, set it as false
    19. Close scanner
20. Catch FileNotFoundException and print stack trace
21. If levelGenerated is false, then create a new NewMap
22. Print the size of gameObjects in Window's scene
23. For each gameObject in gameObjects do the following:
    24. Call update method on gameObject with delta time dt
25. Call adjustProjection method on camera
26. Call update method on physics2D with delta time dt
27. For i from 0 to gameObjects.size():
    28. Get gameObject at index i
    29. Call update method on gameObject with delta time dt
    30. If gameObject is dead, then do the following:
        31. Remove gameObject from gameObjects
        32. Destroy gameObject in renderer
        33. Destroy gameObject in physics2D
        34. Decrement i by 1
35. Call render method in renderer
36. Call imgui method

method loadResources()
    1. Get shader "assets/shaders/default.glsl" from AssetPool
    2. Bind the shader in Renderer
    3. Add spritesheet "assets/images/spritesheets/spritesheet.png" to AssetPool with Spritesheet(AssetPool.getTexture("assets/images/spritesheets/spritesheet.png"), 48, 48, 2, 0)
    4. Add spritesheet "assets/images/spritesheets/blocks.png" to AssetPool with Spritesheet(AssetPool.getTexture("assets/images/spritesheets/blocks.png"), 16, 16, 30, 0)
    5. Add spritesheet "assets/images/spritesheets/player_walk.png" to AssetPool with Spritesheet(AssetPool.getTexture("assets/images/spritesheets/player_walk.png"), 43, 60, 10, 0)
    6. Add spritesheet "assets/images/spritesheets/player_swing.png" to AssetPool with Spritesheet(AssetPool.getTexture("assets/images/spritesheets/player_swing.png"), 43, 60, 10, 0)
    7. Get texture "assets/images/Dirt_Block.png" from AssetPool
    8. Get texture "assets/images/IdleCharacter.png" from AssetPool
    9. Get texture "assets/images/JumpCharacter.png" from AssetPool
    10. For each gameObject in gameObjects do the following:
        11. If gameObject(component(SpriteRenderer) exists) then do the following:
        12. Get SpriteRenderer component of gameObject
        13. If the component's texture is not null, then do the following:
        14. Get the texture's filepath
        15. Set the component's texture as AssetPool.getTexture(filepath)
        16. If gameObject has component(StateMachine), then do the following:
        17. Get StateMachine component of gameObject
        18. Call refreshTextures method on the component
        19. End loop

method update(dt)
1. Call update method on GameStuff with delta time dt
2. For each gameObject in gameObjects, do the following:
3. Call update method on gameObject with delta time dt
4. Call adjustProjection method on camera
5. Call update method on physics2D with delta time dt
6. For i from 0 to gameObjects.size(), do the following:
7. Get gameObject at index i
8. Call update method on gameObject with delta time dt
9. If gameObject is dead, then do the following:
10. Remove gameObject from gameObjects
11. Destroy gameObject in renderer
12. Destroy gameObject in physics2D
13. Decrement i by 1
14. Call render method in renderer

method imgui()
1. Begin ImGui window with label "Inventory"
2. Get window position and assign it to windowPos
3. Get window size and assign it to windowSize
4. Get item spacing and assign it to itemSpacing
5. Calculate windowX2 as windowPos.x + windowSize.x
6. For i from 0 to blocks.size(), do the following:
7. Get sprite at index i from blocks
8. Get sprite width and assign it to spriteWidth
9. Get sprite height and assign it to spriteHeight
10. Get texture ID of the sprite and assign it to id
11. Get texture coordinates of the sprite and assign them to texCoords
12. Push ID with value i
13. If image button with ID id, width spriteWidth, height spriteHeight, and texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y is pressed, then do the following:
14. Create object with Prefabs.generateSpriteObject(sprite, 16, 16)
15. Create rb as new Rigidbody2D
16. Set rb's body type as BodyType.Static
17. Add rb to the object
18. Create b2d as new Box2DCollider
19. Set b2d's half size as Vector2f(17, 16)
20. Set b2d's offset as Vector2f(0, 0)
21. Add b2d to the object
22. Add Ground component to the object
23. Call MouseControls's pickupObject method with the object as an argument
24. Pop ID
25. Get last button position and assign it to lastButtonPos
26. Calculate lastButtonX2 as lastButtonPos.x
27. Calculate nextButtonX2 as lastButtonX2 + itemSpacing.x + spriteWidth
28. If i + 1 < blocks.size() and nextButtonX2 < windowX2, then call ImGui's sameLine method 
29. End loop
30. Get playerSprites as AssetPool.getSpritesheet("assets/images/spritesheets/player_walk.png")
31. Get sprite at index 0 from playerSprites and assign it to sprite
32. Calculate spriteWidth as sprite.getWidth() * 4
33. Calculate spriteHeight as sprite.getHeight() * 4
34. Get texture ID of the sprite and assign it to id
35. Get texture coordinates of the sprite and assign them to texCoords
36. If image button with ID id, width spriteWidth, height spriteHeight, and texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y is pressed, then do the following:
37. Create plr as Prefabs.generatePlayer()
38. Call MouseControls's pickupObject method with plr as an argument
39. End ImGui window "Inventory"
40. Begin ImGui window "Instructions"
41. Display text for Instructions
42. End ImGui window "Instructions"
43. End
```