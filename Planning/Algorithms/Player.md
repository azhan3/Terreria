```java
1. Start
2. Declare walkSpeed as float and initialize it as 1000
3. Declare terminal_velocity as float and initialize it as -1000.0
4. Declare onGround as boolean and initialize it as false
5. Declare rb as Rigidbody2D
6. Declare stateMachine as StateMachine
7. Declare jumpTime as int and initialize it as 0
8. Declare velocity as Vector2f and initialize it as a new Vector2f
9. Override start method
   1. Assign gameObject.getComponent(Rigidbody2D.class) to rb
   2. Print rb
   3. Assign gameObject.getComponent(StateMachine.class) to stateMachine
   4. Print stateMachine
   5. Set rb's gravity scale to 0.0
   6. Print "STARTED STARTED STARTED"
10. Override update method with parameter dt
    1. Declare idle as boolean and initialize it as true
    2. Call checkOnGround method
    3. If KeyListener.isKeyPressed(GLFW_KEY_RIGHT) or KeyListener.isKeyPressed(GLFW_KEY_D) is true, then do the following:
        1. Set velocity.x to walkSpeed
        2. Set gameObject.transform.scale.x to 43
        3. Trigger "Walk" on stateMachine
        4. Set idle to false
    4. Else if KeyListener.isKeyPressed(GLFW_KEY_LEFT) or KeyListener.isKeyPressed(GLFW_KEY_A) is true, then do the following:
        1. Set velocity.x to -walkSpeed
        2. Set gameObject.transform.scale.x to -43
        3. Set idle to false
        4. Trigger "Walk" on stateMachine
    5. If MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) is true, then do the following:
        1. Trigger "Swing" on stateMachine
        2. Set idle to false
    6. If idle is true, then do the following:
        1. Trigger "Idle" on stateMachine
        2. Set velocity.x to 0
    7. If KeyListener.isKeyPressed(GLFW_KEY_SPACE) or KeyListener.isKeyPressed(GLFW_KEY_W) is true and (jumpTime > 0 or onGround is true), then do the following:
        1. If onGround is true and jumpTime is equal to 0, then do the following:
            1. Set jumpTime to 65
            2. Set velocity.y to 1000.0
        2. Else if jumpTime is greater than 0, then do the following:
            1. Decrement jumpTime by 1
            2. Set velocity.y to 1000.0
        3. Else, set velocity.y to 0.0
        4. Trigger "Jump" on stateMachine
    8. If onGround is false, then do the following:
        1. Trigger "Jump" on stateMachine
    9. Add Window.getPhysics().getGravity().y to velocity.y
    10. Set velocity.y to the maximum value between terminal_velocity and velocity.y
    11. Set Window.getScene().camera().position.x to gameObject.transform.position.x - 632
    12. Set Window.getScene().camera().position.y to gameObject.transform.position.y - 328
    13. Set rb's velocity to velocity
    14. Set rb's angular velocity to 0
15. Define checkOnGround method
    1. Declare innerPlayer width as float and initialize it as 43
    2. Declare yVal as float and initialize it as 30
    3. Set onGround to the result of calling Physics2D.checkOnGround method with parameters gameObject, innerPlayerWidth, and yVal
16. Stop
```