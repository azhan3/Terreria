package engine;

import components.*;
import physics2d.components.PillboxCollider;
import physics2d.components.Rigidbody2D;
import physics2d.enums.BodyType;
import renderer.Texture;
import util.AssetPool;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject block = Window.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }



    public static GameObject generatePlayer() {
        Spritesheet walkSprites = AssetPool.getSpritesheet("assets/images/spritesheets/player_walk.png");
        Spritesheet swingSprites = AssetPool.getSpritesheet("assets/images/spritesheets/player_swing.png");
        Texture idlePlayer = AssetPool.getTexture("assets/images/IdleCharacter.png");
        Texture jumpPlayer = AssetPool.getTexture("assets/images/JumpCharacter.png");
        GameObject player = generateSpriteObject(walkSprites.getSprite(0), 43, 60);

        AnimationState walk = new AnimationState();
        AnimationState swing = new AnimationState();
        AnimationState idle = new AnimationState();
        AnimationState jump = new AnimationState();
        swing.title = "Swing";
        walk.title = "Walk";
        idle.title = "Idle";
        jump.title = "Jump";
        float defaultFrameTime = 0.08f;
        jump.addFrame(new Sprite(jumpPlayer), defaultFrameTime);
        jump.setLoop(true);
        idle.addFrame(new Sprite(idlePlayer), defaultFrameTime);
        idle.setLoop(true);
        walk.addFrame(walkSprites.getSprite(0), defaultFrameTime);
        walk.addFrame(walkSprites.getSprite(1), defaultFrameTime);
        walk.addFrame(walkSprites.getSprite(2), defaultFrameTime);
        walk.addFrame(walkSprites.getSprite(3), defaultFrameTime);
        walk.addFrame(walkSprites.getSprite(4), defaultFrameTime);
        walk.addFrame(walkSprites.getSprite(5), defaultFrameTime);
        walk.addFrame(walkSprites.getSprite(6), defaultFrameTime);
        walk.addFrame(walkSprites.getSprite(7), defaultFrameTime);
        walk.addFrame(walkSprites.getSprite(8), defaultFrameTime);
        walk.addFrame(walkSprites.getSprite(9), defaultFrameTime);
        walk.setLoop(true);

        swing.addFrame(swingSprites.getSprite(0), defaultFrameTime);
        swing.addFrame(swingSprites.getSprite(1), defaultFrameTime);
        swing.addFrame(swingSprites.getSprite(2), defaultFrameTime);
        swing.addFrame(swingSprites.getSprite(3), defaultFrameTime);
        swing.setLoop(true);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(walk);
        stateMachine.addState(swing);
        stateMachine.addState(idle);
        stateMachine.addState(jump);
        stateMachine.addState(walk.title, swing.title, "Swing");
        stateMachine.addState(idle.title, swing.title, "Swing");
        stateMachine.addState(jump.title, swing.title, "Swing");
        stateMachine.addState(swing.title, walk.title, "Walk");
        stateMachine.addState(idle.title, walk.title, "Walk");
        stateMachine.addState(jump.title, walk.title, "Walk");
        stateMachine.addState(walk.title, idle.title, "Idle");
        stateMachine.addState(swing.title, idle.title, "Idle");
        stateMachine.addState(jump.title, idle.title, "Idle");
        stateMachine.addState(walk.title, jump.title, "Jump");
        stateMachine.addState(swing.title, jump.title, "Jump");
        stateMachine.addState(idle.title, jump.title, "Jump");

        stateMachine.setDefaultState(idle.title);

        PillboxCollider pb = new PillboxCollider();
        pb.width = 0.39f;
        pb.height = 0.31f;
        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);

        player.addComponent(stateMachine);
        player.addComponent(rb);
        player.addComponent(pb);
        player.addComponent(new PlayerController());

        return player;
    }
}