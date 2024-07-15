package member;

import java.util.ArrayList;

/**
 * This class stores and controls game characters.
 *
 * @author Yuebo Feng
 * @version ver 1.0.0
 */
public abstract class Creature {
    public static final int DIE_DAMAGE_THRESHOLD = 10;
    protected boolean isDied;
    protected int power;
    protected int damage;
    protected boolean isHoldCode; // whether the code is held

    public Creature(int power, int damage) {
        this.isDied = false;
        this.power = power;
        this.damage = damage;
    }

    public boolean isHoldCode() {
        return isHoldCode;
    }

    public void holdCode() {
        isHoldCode = true;
        System.out.println("*** The secret code has been stolen by " + getPureName() + " ***");
    }

    public void unHoldCode() {
        isHoldCode = false;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isDie() {
        return damage >= DIE_DAMAGE_THRESHOLD || isDied;
    }

    public void die() {
        System.out.println(getName() + " is dead");
        isDied = true;
    }

    public void incrementDamage(int i, ArrayList<String> logs) {
        damage += i;
        if (isDie()) {
            die();
            logs.add(getName());
        }
    }

    public String getPureName() {
        return getName().replace("*", "");
    }

    public abstract String getName();

    public String getNameWithDot() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        for (int i = 0; i < 9 - getName().length(); i++) {
            sb.append(".");
        }
        return sb.toString();
    }

    public String getNameStats() {
        StringBuilder sb = new StringBuilder();
        sb.append(getNameWithDot());
        if (isDie()) {
            sb.append(" (dead)");
        } else {
            sb.append(" damage: ").append(damage);
        }
        return sb.toString();
    }
}
