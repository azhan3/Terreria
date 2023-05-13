```java
1. Start
2. Import the required libraries: org.joml.Matrix4f, org.joml.Vector2f, org.joml.Vector3f
3. Declare a class called Camera
4. Declare private variables: projectionMatrix, viewMatrix, inverseProjection, inverseView as Matrix4f
5. Declare public variable: position as Vector2f
6. Declare private variable: projectionSize as Vector2f and initialize it as a new Vector2f with values (32.0f * 40.0f, 32.0f * 21.0f)
7. Create a constructor for Camera that accepts a parameter position of type Vector2f
    1. Set the position of the camera to the provided position
    2. Initialize the projectionMatrix, viewMatrix, inverseProjection, and inverseView as new Matrix4f instances
    3. Call the adjustProjection() method
8. Create a method called adjustProjection
    1. Set the projectionMatrix to an identity matrix
    2. Set the projectionMatrix using the ortho() method with parameters 0.0f, projectionSize.x, 0.0f, projectionSize.y, 0.0f, 100.0f
    3. Calculate the inverse of the projectionMatrix and store it in inverseProjection
9. Create a method called getViewMatrix
    1. Create a Vector3f called cameraFront with values (0.0f, 0.0f, -1.0f)
    2. Create a Vector3f called cameraUp with values (0.0f, 1.0f, 0.0f)
    3. Set the viewMatrix to an identity matrix
    4. Set the viewMatrix using the lookAt() method with parameters (new Vector3f(position.x, position.y, 20.0f), cameraFront.add(position.x, position.y, 0.0f), cameraUp)
    5. Calculate the inverse of the viewMatrix and store it in inverseView
    6. Return the viewMatrix
10. Create a method called getProjectionMatrix
    1. Return the projectionMatrix
11. Create a method called getInverseProjection
    1. Return the inverseProjection
12. Create a method called getInverseView
    1. Return the inverseView
13. Create a method called getProjectionSize
    1. Return the projectionSize
14. Stop
```

## Flowchart
![code2flow_PeSorw (3)](https://github.com/azhan3/Java-Game/assets/96319134/52890d96-563a-4bab-b9a7-8473a7203db5)
