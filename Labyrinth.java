package entity;

import member.Evil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Labyrinth {
    private ArrayList<Cave> caves;
    private HashMap<Integer, Cave> id2Cave;

    public Labyrinth() {
        caves = new ArrayList<Cave>();
        id2Cave = new HashMap<Integer, Cave>();
    }

    // this for testing only
    public void testStub() {
        caves.get(0).setEvil(new Evil(0));
        caves.get(1).setEvil(new Evil(2));
        caves.get(2).setEvil(new Evil(2));
        caves.get(3).setEvil(null);
        caves.get(4).setEvil(null);
        caves.get(5).setEvil(new Evil(1));
        caves.get(6).setEvil(null);
        caves.get(7).setEvil(new Evil(2));
        caves.get(8).setEvil(new Evil(0));

        for (int i = 0; i < 9; i++) {
            id2Cave.put(i + 1, caves.get(i));
        }
    }

    public void addCave(Cave cave) {
        caves.add(cave);
        id2Cave.put(cave.getId(), cave);
    }

    public void loadFromFile(String file) {
        try {
            int totalLines = 0;
            int caveCount = 0;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while (true) {
                line = reader.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                String[] parts = line.split(",");
                totalLines++;
                if (parts.length != 5) {
                    System.out.println("Invalid format line, skip: " + line);
                } else {
                    int id = Integer.parseInt(parts[0]);
                    int north = Integer.parseInt(parts[1]);
                    int east = Integer.parseInt(parts[2]);
                    int south = Integer.parseInt(parts[3]);
                    int west = Integer.parseInt(parts[4]);
                    Cave cave = new Cave(id, north, east, south, west);
                    addCave(cave);
                    if (!cave.isMount()) {
                        caveCount++;
                    }
                }
            }
            System.out.println("  * total lines from file: " + totalLines);
            System.out.println("  * cave count: " + caveCount);
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error(s) when reading from file:\n" + e.toString());
        }
    }

    public Cave getCave(int curCaveId) {
        return id2Cave.get(curCaveId);
    }


    public List<Evil> getAllEvils() {
        ArrayList<Evil> res = new ArrayList<>();
        for (Cave cave : caves) {
            if (cave.getEvil() != null) {
                res.add(cave.getEvil());
            }
        }
        return res;
    }

    public void displayAllEvilsInCaves() {
        for (Cave cave : caves) {
            if (cave.getEvil() != null) {
                System.out.println("  cave " + cave.getId() + ": " + cave.getEvil().getNameStats());
            }
        }
    }
}
