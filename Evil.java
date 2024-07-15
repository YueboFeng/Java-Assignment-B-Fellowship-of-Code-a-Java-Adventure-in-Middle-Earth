package member;

public class Evil extends Creature {
    private static final String[] NAMES = {"orc", "troll", "goblin"};
    private static final int[] POWERS = {5, 9, 3};

    private int type;

    public Evil(int type) {
        super(POWERS[type], 0);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return (isHoldCode ? "*" : "") + NAMES[type];
    }

    @Override
    public String toString() {
        return getNameWithDot();
    }
}
