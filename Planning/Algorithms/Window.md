```java
1. Start
2. Create a class called Window
3. Declare private variables: width, height, title, glfwWindow, imguiLayer, r, g, b, a, fadeToBlack
4. Declare a static variable: window
5. Declare a static variable: currentScene
6. Create a private constructor for Window
    1. Set width to 1920
    2. Set height to 1080
    3. Set title to "Terraria"
    4. Set r, g, b, a to 1
7. Create a static method called changeScene, accepting a parameter newScene
    1. Switch newScene
        1. Case 0:
            1. Set currentScene to a new instance of LevelEditorScene
            2. Break
        2. Case 1:
            1. Set currentScene to a new instance of LevelScene
            2. Break
        3. Default:
            1. Assert false with message "Unknown scene 'newScene'"
            2. Break
    2. Call currentScene.load()
    3. Call currentScene.init()
    4. Call currentScene.start()
8. Create a static method called get
    1. If window is null
        1. Set window to a new instance of Window
    2. Return window
9. Create a static method called getScene
    1. Return the currentScene of get()
10. Create a method called run
11. Print "Started!"
12. Call init()
13. Call loop()
14. Call glfwFreeCallbacks with glfwWindow
15. Call glfwDestroyWindow with glfwWindow
16. Call glfwTerminate()
17. Set the GLFW error callback to null and free it
18. Create a method called init
19. Set the GLFW error callback to create a print on System.err
20. Initialize GLFW
     1. If glfwInit returns false, throw an IllegalStateException
21. Set default window hints
22. Set window hints: GLFW_VISIBLE to GLFW_FALSE, GLFW_RESIZABLE to GLFW_TRUE, GLFW_MAXIMIZED to GLFW_TRUE
23. Create the window using glfwCreateWindow with width, height, title, NULL, NULL
     1. If glfwCreateWindow returns NULL, throw an IllegalStateException
24. Set the cursor position callback to MouseListener::mousePosCallback
25. Set the mouse button callback to MouseListener::mouseButtonCallback
26. Set the scroll callback to MouseListener::mouseScrollCallback
27. Set the key callback to KeyListener::keyCallback
28. Set the window size callback to a lambda function that sets newWidth and newHeight through Window.setWidth and Window.setHeight
29. Make the OpenGL context current using glfwMakeContextCurrent with glfwWindow
30. Enable v-sync using glfwSwapInterval(1)
31. Make the window visible using glfwShowWindow with glfwWindow
32. Create the OpenGL capabilities using GL.createCapabilities()
33. Enable blending using glEnable with GL_BLEND
34. Set the blend function using glBlendFunc with GL_ONE, GL_ONE_MINUS_SRC_ALPHA
35. Create an instance of ImGuiLayer with glfwWindow and assign it to imguiLayer
36. Call initImGui on imguiLayer
37. Call changeScene with 0
38. Create a method
```