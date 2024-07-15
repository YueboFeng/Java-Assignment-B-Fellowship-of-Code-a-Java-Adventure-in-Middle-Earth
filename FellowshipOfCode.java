import entity.*;
import member.*;
import utils.*;

import java.util.*;

public class FellowshipOfCode {
    boolean isTest = false;

    public static void main(String[] args) {
        FellowshipOfCode game = new FellowshipOfCode();
        game.displayWelcomeMessage();
        game.init();
        game.start();
    }

    public static final int NUM_FELLOWS = 3;

    private final Labyrinth labyrinth;
    private final ArrayList<Fellow> fellows;
    private int curCaveId;

    // summary variables
    private final Set<Integer> visitedCaves;
    private int codeChangedHand;
    private final ArrayList<String> diedCreatures;
    private int totalFight;
    private int totalWin;


    public FellowshipOfCode() {
        fellows = new ArrayList<Fellow>();
        labyrinth = new Labyrinth();
        curCaveId = 1;

        // summary variables
        visitedCaves = new HashSet<>();
        codeChangedHand = 0;
        diedCreatures = new ArrayList<>();
        totalFight = 0;
        totalWin = 0;
    }

    private void displayMembers(boolean showDamage, boolean showNumber) {
        displayMembers(fellows, showDamage, showNumber);
    }

    private void displayMembers(ArrayList<Fellow> fellows, boolean showDamage, boolean showNumber) {
        if (!showDamage) {
            System.out.println("Fellowship member list");
            System.out.println("======================");
        }
        int i = 0;
        for (Fellow fellow : fellows) {
            i++;
            System.out.print("  ");
            if (showNumber) {
                System.out.print(i + ") ");
            }
            System.out.println(fellow.show(showDamage));
        }
    }


    public void start() {
        while (true) {
            Cave cave = labyrinth.getCave(curCaveId);

            // 0. check if it is Mount Api
            if (cave.isMount()) {
                if (hasCodeInFellow()) {
                    // has code, success
                    System.out.println("Congratulations! The secret code has been delivered the secret code to the Java wizard.");
                    System.out.println();
                    break;
                } else {
                    // no code, redirect to cave 1 (still has fellowship members)
                    System.out.println("You need to have the secret code to deliver to the Java wizard on Mount Api");
                    System.out.println("Redirecting to cave 1...");
                    Input.readEnter();
                    curCaveId = 1;
                    continue;
                }
            }

            // 1. enter a new cave
            System.out.println("Entered cave " + curCaveId + "\n");
            visitedCaves.add(curCaveId);


            Evil evil = cave.getEvil();

            // 2. if it has evil, fight, else move to next cave
            if (evil != null) {
                // 2.1 fight
                System.out.println("*** Fighting " + evil.getName() + " ***\n");
                Fellow fellow = chooseFellowToFight();

                doFlight(fellow, evil);
                Input.readEnter();
            } else {
                // 2.2 rest
                System.out.println("This cave has no creature. Members will rest and recover.");
                for (Fellow fellow : fellows) {
                    fellow.recover();
                }
            }

            // NO alive fellow, game over
            if (isGameOver()) {
                System.out.println("All fellowship members are dead. Game over");
                System.exit(0);
            }

            // 3. print current status
            System.out.println(cave);

            // 4. ask for user to choose next cave
            // if only has one direction, auto move to next cave
            makeMove(cave);
        }

        // 5. summary
        gameSummary();
    }

    private void gameSummary() {
        System.out.println("End of game summary");
        System.out.println("===================");
        if (hasCodeInFellow()) {
            System.out.println("* The quest is a success");
            System.out.println("  The " + getCodeHolder().getPureName() + " has delivered the secret code");
        } else {
            System.out.println("* The quest is a failure");
            System.out.println("  The secret code has been stolen by the evil creatures");
        }

        System.out.println("Number of cave visits: " + visitedCaves.size());
        System.out.println("Number of dead creatures: " + diedCreatures.size());
        System.out.println("Number of times the secret code changed hands: " + codeChangedHand);
        System.out.printf("Fellowship fight success rate: %.2f%%\n", (totalWin * 100.0 / totalFight));
        System.out.println("\nGoodbye");
    }

    private void printStats() {
        System.out.println("End of cave visit stats");
        System.out.println("=======================");
        Creature holder = getCodeHolder();
        if (hasCodeInFellow()) {
            holder = getCodeHolder();
            System.out.println("Fellowship team member (" + holder.getPureName() + ") has the secret code");
        } else {
            System.out.println("A creature in a cave (" + holder.getPureName() + ") has the secret code");
        }

        // 1. fellowship members
        System.out.println("\nFellowship team (current status):");
        displayMembers(true, false);

        // 2. visited caves
        List<Integer> sortedCaves = new ArrayList<>(visitedCaves);
        Collections.sort(sortedCaves);
        System.out.println("\nVisited caves (historical status):");
        for (int i = 0; i < sortedCaves.size(); i++) {
            int caveId = sortedCaves.get(i);
            Cave cave = labyrinth.getCave(caveId);
            System.out.print("  " + (i + 1) + ") cave " + caveId + "... ");
            if (cave.getEvil() != null) {
                System.out.println(cave.getEvil().getNameStats());
            } else {
                System.out.println("(no creature)");
            }
        }

        // 3. creatures in caves
        System.out.println("Creatures in caves (current status):");
        labyrinth.displayAllEvilsInCaves();

        System.out.println("(The * indicates who has the secret code)");
    }

    private void makeMove(Cave cave) {
        int nextId = -1;
        int[] exits = cave.getExits();
        if (cave.getValidExitCount() == 1) {
            int i = 0;
            for (int j = 0; j < exits.length; j++) {
                if (exits[j] != 0) {
                    i = j;
                    break;
                }
            }
            System.out.println("There is only one exit. Taking exit " + cave.getExitName(i));
            nextId = exits[i];
            Input.readEnter();
        } else {
            while (true) {
                ArrayList<String> validDirs = new ArrayList<>();
                for (int i = 0; i < exits.length; i++) {
                    if (exits[i] != 0) {
                        validDirs.add(cave.getExitName(i));
                    }
                }

                System.out.print("Which exit? (");
                for (int i = 0; i < validDirs.size(); i++) {
                    String dir = validDirs.get(i).toLowerCase();
                    if (i != validDirs.size() - 1) {
                        System.out.print(dir + ",");
                    } else {
                        System.out.print(dir + ") ");
                    }
                }

                String choice = Input.readString().toUpperCase();
                if (validDirs.contains(choice)) {
                    if (choice.equals("M")) {
                        System.out.println("Taking exit " + choice);
                        nextId = 100;
                        break;
                    } else {
                        System.out.println("Taking exit " + choice);
                        int index = cave.getExitIndex(choice);
                        Input.readEnter();
                        nextId = exits[index];
                        break;
                    }
                } else {
                    System.out.println("Invalid choice\n");
                }
            }
        }

        printStats();

        System.out.println("The fellowship team will go from cave " + curCaveId + " to " +
                (nextId != 100 ? "cave " + nextId : "Mount Api"));
        curCaveId = nextId;
        Input.readEnter();
    }


    private void doFlight(Fellow fellow, Evil evil) {
        totalFight++;

        // 1. has a special weapon
        if (fellow.isHasSpecialWeapon()) {
            System.out.println(fellow.getPureName() + " has a special weapon.");
            boolean useIt = Input.readYesNo("Use it? (y/n) ");
            // evil died
            if (useIt) {
                System.out.println("The fellow ship team has WON the fight");
                totalWin++;
                fellow.detachSpecialWeapon();
                evil.die();
                diedCreatures.add(evil.getName());
                return;
            }
        }

        // 2. no special weapon or not use it
        // fight
        int winChance = fellow.getWinChance(evil);
        boolean isFellowWin = RandomGenerator.randomBoolean(winChance);
        isFellowWin = false;
        totalWin += isFellowWin ? 1 : 0;
        if (isFellowWin) {
            // fellow win
            System.out.println("The fellow ship team has WON the fight");
            fellow.incrementDamage(1, diedCreatures);
            evil.incrementDamage(4, diedCreatures);
            if (evil.isHoldCode()) {
                codeChangedHand++;
                fellow.holdCode();
                evil.unHoldCode();
            }
        } else {
            // evil win
            System.out.println("The evil has WON the fight");
            fellow.incrementDamage(4, diedCreatures);
            evil.incrementDamage(1, diedCreatures);
            if (fellow.isHoldCode()) {
                codeChangedHand++;
                fellow.unHoldCode();
                evil.holdCode();
            }
        }
    }

    public Fellow chooseFellowToFight() {
        // filter alive fellows
        ArrayList<Fellow> aliveFellows = getAliveFellows();
        if (aliveFellows.isEmpty()) {
            System.out.println("You have no more fellows to fight");
            System.out.println("Game over");
            System.exit(0);
        }

        // display alive fellows
        while (true) {
            System.out.println("Choose who is going to fight");
            displayMembers(aliveFellows, true, true);
            System.out.println("(The * indicates who has the secret code)");

            System.out.print("Choice: ");
            int choice = Input.readInt();
            if (choice >= 1 && choice <= aliveFellows.size()) {
                Fellow fellow = aliveFellows.get(choice - 1);
                System.out.println("You have chosen: " + fellow.getName());
                return fellow;
            } else {
                System.out.println("Invalid choice\n");
            }
        }
    }

    private ArrayList<Fellow> getAliveFellows() {
        ArrayList<Fellow> res = new ArrayList<Fellow>();
        for (Fellow fellow : fellows) {
            if (fellow.isAlive()) {
                res.add(fellow);
            }
        }
        return res;
    }

    public void displayWelcomeMessage() {
        System.out.println("Welcome to \"Fellowship of Code: a Java Adventure in Middle Earth!\" game\n");
        System.out.println("Instructions");
        System.out.println("============");
        System.out.println("The fellowship team has 4 members. The leader is a hobbit.");
        System.out.println("You will need to choose 3 other members.");
        System.out.println("Your job is to deliver the secret code to the Java wizard on Mount Api\n");
    }


    public void init() {
        System.out.println("* setting up Middle Earth...");
        System.out.println("  reading cave information from labyrinth.txt");
        labyrinth.loadFromFile("labyrinth.txt");

        // set fellow team
        System.out.println("*  setting up the Fellowship team...\n");
        System.out.println("Choose the fellowship members");
        System.out.println("The leader is a hobbit");
        System.out.println("You need to choose 3 more members");
        System.out.println();


        // let user choose the rest of the team
        fellows.add(new Fellow(0)); // hobbit


        if (isTest) {
            // todo: for testing, remove later
            fellows.add(new Fellow(1)); // elf
            fellows.add(new Fellow(2)); // dwarf
            fellows.add(new Fellow(1)); // elf
            labyrinth.testStub();
        } else {
            for (int i = 0; i < NUM_FELLOWS; i++) {
                while (true) {
                    System.out.println("Choose #" + (i + 1) + " member:");
                    System.out.println("1) Elf");
                    System.out.println("2) Dwarf");
                    System.out.print("Choice: ");
                    int choice = Input.readInt();
                    if (choice == 1 || choice == 2) {
                        Fellow fellow = new Fellow(choice);
                        fellows.add(fellow);
                        System.out.println("Added an " + fellow.getName() + " to the fellowship\n");
                        break;
                    } else {
                        System.out.println("Invalid choice\n");
                    }
                }
            }
        }

        displayMembers(false, false);
        System.out.println("(The * indicates who has the secret code)");
        Input.readEnter("\nPress ENTER to start the journey...");
    }

    private boolean hasCodeInFellow() {
        return getCodeHolder() != null && getCodeHolder() instanceof Fellow;
    }

    public Creature getCodeHolder() {
        for (Fellow fellow : fellows) {
            if (fellow.isHoldCode()) {
                return fellow;
            }
        }
        for (Evil evil : labyrinth.getAllEvils()) {
            if (evil.isHoldCode()) {
                return evil;
            }
        }
        return null;
    }

    private boolean isGameOver() {
        return getAliveFellows().isEmpty();
    }
}
