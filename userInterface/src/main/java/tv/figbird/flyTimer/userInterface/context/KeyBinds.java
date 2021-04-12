package tv.figbird.flyTimer.userInterface.context;

import javafx.scene.input.KeyCode;

public enum KeyBinds {

    SPLIT(KeyCode.SPACE, "End Current Segment"),
    TOGGLE_TIMER(KeyCode.S, "Start or Stop Timer"),
    RESET(KeyCode.R, "Reset Timer"),
    UNDO(KeyCode.DELETE, "Undo Segment End"),
    SKIP_SEGMENT(KeyCode.END, "Skip Current Segment"),
    NO_OP(null, "No Operation");

    private KeyCode code;
    public final String displayName;

    KeyBinds(KeyCode defaultKeyCode, String displayName) {
        code = defaultKeyCode;
        this.displayName = displayName;
    }

    public KeyCode getCode() {
        return code;
    }

    public void setCode(KeyCode code) {
        this.code = code;
    }

    public static KeyBinds getBind(KeyCode code) {
        for (KeyBinds bind : KeyBinds.values()) {
            if (code == bind.getCode()) {
                return bind;
            }
        }
        return NO_OP;
    }

    //TODO: create conflict handler to ensure keybinds are unique
}
