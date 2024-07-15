package member;

public class Fellow extends Creature {
    private final static int[] POWERS = {3, 5, 7};
    private final static String[] NAMES = {"hobbit", "elf", "dwarf"};

    private final int type;// 0 hobbit, 1 elves, 2 dwarves
    private boolean hasSpecialWeapon; // whether the special skill is used


    public Fellow(int type) {
        super(POWERS[type], 0);
        this.type = type;
        hasSpecialWeapon = type < 2;
        isHoldCode = type == 0;
    }

    public boolean isHasSpecialWeapon() {
        return hasSpecialWeapon;
    }

    public void detachSpecialWeapon() {
        hasSpecialWeapon = false;
    }

    public String getName() {
        return (isHoldCode ? "*" : "") + NAMES[type];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getNameWithDot());
        if (hasSpecialWeapon) {
            sb.append(" (weapon)");
        }
        return sb.toString();
    }

    public String show(boolean showDamage) {
        String res = toString();
        if (showDamage) {
            if (res.length() > 10) {
                res = res.substring(0, 10) + "damage: " + damage + " " + res.substring(10);
            } else {
                res = res + " damage: " + damage;
            }
        }
        if (isDie()) {
            res += " (dead)";
        }
        return res;
    }

    public boolean isAlive() {
        return !isDie();
    }

    public int getWinChance(Evil evil) {
        int diff = Math.abs(power - evil.getPower());
        int winChance = 0;
        if (diff >= 4) {
            winChance = 90;
        } else if (diff == 3) {
            winChance = 70;
        } else if (diff == 2) {
            winChance = 50;
        } else if (diff == 1) {
            winChance = 30;
        } else if (diff == 0) {
            winChance = 10;
        }
        if (power < evil.getPower()) {
            winChance = 100 - winChance; // opposite
        }
        return winChance;
    }


    public void recover() {
        if (isDie()) {
            return;
        }
        damage = Math.max(0, damage - 1);
    }
}
