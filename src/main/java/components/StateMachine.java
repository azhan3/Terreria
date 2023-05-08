package components;

import renderer.Texture;
import util.AssetPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StateMachine extends Component {
    private class StateTrigger {
        public String state;
        public String trigger;

        public StateTrigger() {}

        public StateTrigger(String state, String trigger) {
            this.state = state;
            this.trigger = trigger;
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass() != StateTrigger.class) return false;
            StateTrigger t2 = (StateTrigger)o;
            return t2.trigger.equals(this.trigger) && t2.state.equals(this.state);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, trigger);
        }
    }

    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<AnimationState> states = new ArrayList<>();
    private transient AnimationState currentState = null;
    private String defaultStateTitle = "";

    public void refreshTextures() {
        for (AnimationState state : states) {
            state.refreshTextures();
        }
    }

    public void setDefaultState(String animationTitle) {
        for (AnimationState state : states) {
            if (state.title.equals(animationTitle)) {
                defaultStateTitle = animationTitle;
                if (currentState == null) {
                    currentState = state;
                }
                return;
            }
        }

        System.out.println("Unable to find default state '" + animationTitle + "'");
    }

    public void addState(String from, String to, String onTrigger) {
        this.stateTransfers.put(new StateTrigger(from, onTrigger), to);
    }

    public void addState(AnimationState state) {
        this.states.add(state);
    }

    public void trigger(String trigger) {
        for (StateTrigger state : stateTransfers.keySet()) {
//            System.out.println(state.state);
//            System.out.println(state.state.equals(currentState.title));
//            System.out.println(state.trigger);
//            System.out.println("------------------------");
            if (state.state.equals(currentState.title) && state.trigger.equals(trigger)) {
                if (stateTransfers.get(state) != null) {
                    int newStateIndex = stateIndexOf(stateTransfers.get(state));
                    if (newStateIndex > -1) {
                        currentState = states.get(newStateIndex);
                        System.out.println("Switched to " + currentState.title);
                    }
                }
                return;
            }
        }
        //System.out.println("Unable to find trigger '" + trigger + "'");
    }
    private int stateIndexOf(String stateTitle) {
        int index = 0;
        for (AnimationState state : states) {
            if (state.title.equals(stateTitle)) {
                return index;
            }
            index++;
        }

        return -1;
    }
    @Override
    public void start() {
        for (AnimationState state : states) {
            System.out.println(state.title);
            if (state.title.equals(defaultStateTitle)) {
                currentState = state;
                break;
            }
        }
        System.out.println(stateTransfers);
    }

    @Override
    public void update(float dt) {
        if (currentState != null) {
            currentState.update(dt);

            SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);

            if (Objects.equals(currentState.title, "Jump") || Objects.equals(currentState.title, "Idle")) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                Sprite a = new Sprite(AssetPool.getTexture("assets/images/JumpCharacter.png"));

                sprite.setSprite(a);
            } else {
                sprite.setSprite(currentState.getCurrentSprite());

            }
            //System.out.println(sprite.getTexture().getFilepath());
        }
    }

}
