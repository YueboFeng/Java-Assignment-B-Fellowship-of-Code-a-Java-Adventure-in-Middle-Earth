package entity;

import member.Evil;
import utils.RandomGenerator;

public class Cave {
    private static final int MOUNT_API_ID = 100;
    private final int id;
    private final int north;
    private final int east;
    private final int south;
    private final int west;
    private Evil evil;

    public Cave(int caveIdentity, int north, int east, int south, int west) {
        this.id = caveIdentity;
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
        if (id != MOUNT_API_ID) {
            this.evil = RandomGenerator.generateEvilInCave();
        }
    }

    public void setEvil(Evil evil) {
        this.evil = evil;
    }

    public boolean isMount() {
        return id == MOUNT_API_ID;
    }


    private String getSingleDirRepresentation(int id, String dir, String charIfEmpty) {
        if (id == 0) {
            return charIfEmpty;
        } else if (id == MOUNT_API_ID) {
            return "M";
        } else {
            return dir;
        }
    }

    public String toString() {
        return "Cave diagram" + "\n" +
                "    |-" + getSingleDirRepresentation(north, "N", "-") + "-|\n" +
                "    |   |\n" +
                "|---     ---|\n" +
                getSingleDirRepresentation(west, "W", "|") + "           " +
                getSingleDirRepresentation(east, "E", "|") + "\n" +
                "|---     ---|\n" +
                "    |   |\n" +
                "    |-" + getSingleDirRepresentation(south, "S", "-") + "-|\n";
    }

    public int getId() {
        return id;
    }

    public Evil getEvil() {
        return evil;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cave) {
            Cave cave = (Cave) obj;
            return id == cave.id;
        }
        return false;
    }

    public int getDirectionCount() {
        int count = 0;
        if (north != 0) {
            count++;
        }
        if (east != 0) {
            count++;
        }
        if (south != 0) {
            count++;
        }
        if (west != 0) {
            count++;
        }
        return count;
    }

    public int[] getExits() {
        return new int[]{north, east, south, west};
    }

    public int getValidExitCount() {
        int count = 0;
        if (north != 0) {
            count++;
        }
        if (east != 0) {
            count++;
        }
        if (south != 0) {
            count++;
        }
        if (west != 0) {
            count++;
        }
        return count;
    }

    public String getExitName(int index) {
        switch (index) {
            case 0:
                if (north == 100) {
                    return "M";
                }
                return "N";
            case 1:
                if (east == 100) {
                    return "M";
                }
                return "E";
            case 2:
                if (south == 100) {
                    return "M";
                }
                return "S";
            case 3:
                if (west == 100) {
                    return "M";
                }
                return "W";
            default:
                return "";
        }
    }

    public int getExitIndex(String exit) {
        switch (exit) {
            case "N":
                return 0;
            case "E":
                return 1;
            case "S":
                return 2;
            case "W":
                return 3;
            default:
                return -1;
        }
    }
}
