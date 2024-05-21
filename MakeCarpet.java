
/**
 * MakeCarpet
 */

import java.util.*;
import java.util.stream.Collectors;

import piece.CarpetPiece;
import piece.CarpetPiece2;

public class MakeCarpet {

    private static Map<Integer, CarpetPiece2> carpetPieces = new HashMap<>();
    private static Map<String, Integer> uniqueStripsCount = new HashMap<>();
    private static List<CarpetPiece2> bestCarpet = new ArrayList<>();
    private static int bestScore = 0;
    static int count = 0;

    public static void main(String[] args) {

        String type = args.length > 0 ? args[1] : "-m";
        int numPieces = args.length > 1 ? Integer.parseInt(args[0]) : 3;
        initialiseCarpet();
        List<CarpetPiece2> carpet = new ArrayList<>();

        switch (type) {
            case "-m":
                mostMatches(numPieces, new ArrayList<>(carpetPieces.values()), new ArrayList<>(), 
                    bestCarpet);
                break;
            case "-n":
                carpet = noMatches(numPieces, new ArrayList<>(carpetPieces.values()), new ArrayList<>());
                break;
            case "-b":
                carpet = balancedMatches(numPieces);
                break;
            default:
                System.out.println("Invalid type");
        }

        // List<CarpetPiece2> testList = new ArrayList<>(carpetPieces.values());
        // System.out.println(calculateScore(testList));

        if (bestCarpet.isEmpty()) {
            System.out.println("Not Possible");
        } else {
            for (CarpetPiece2 piece : bestCarpet) {
                System.out.println(piece);
            }
        }
        System.out.println("Score: " + calculateScore(bestCarpet));
        System.out.println("\n\n");

    }

    public static void initialiseCarpet() {

        Scanner scanner = new Scanner(System.in);
        int id = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                carpetPieces.put(id, new CarpetPiece2(line, id));
                id++;
            }
        }

        for (CarpetPiece2 piece : carpetPieces.values()) {
            // Add pieces to the countmap
            uniqueStripsCount.put(piece.getStrip(), uniqueStripsCount.getOrDefault(piece.getStrip(), 0) + 1);
            for (CarpetPiece2 other : carpetPieces.values()) {
                if (piece.getId() != other.getId()) {
                    piece.checkMatch(other);
                }
            }
        }

        scanner.close();

        // System.out.println("Score: " + calculateScore(carpet));
    }

    /**
     * Find the carpet arrangement with the most matches
     * 
     * @param size The number of carpet pieces to use in total
     */
    public static void mostMatches(int size, List<CarpetPiece2> stock, List<CarpetPiece2> carpet, List<CarpetPiece2> bestCarpet) {

        if (carpet.size() == size) {
            int score = calculateScore(carpet);
            if (score > bestScore) {
                bestScore = score;
                bestCarpet.clear();
                bestCarpet.addAll(carpet);
            }
            return;
        }

        //pruning if not possible to get a better score from current position
        if(!carpet.isEmpty()){
            if (calculateScore(carpet) + (size-carpet.size())* carpet.get(0).getStrip().length() < bestScore) {
                return;
            }
        }

        // debugging best carpet
        if (carpet.size() == size) {
            if (!carpet.get(0).getStrip().equals("rbr")) {
                System.out.println("current Carpet: " + count++);
                for (CarpetPiece2 piece : carpet) {
                    System.out.println(piece);
                }
                System.out.println();
            }
        }

        for (CarpetPiece2 piece : stock) {
            List<CarpetPiece2> newStock = new ArrayList<>(stock);
            newStock.remove(piece);
            carpet.add(piece);
            mostMatches(size, newStock, carpet, bestCarpet);
            carpet.remove(carpet.size() - 1);
        }

    }

    public static void mostMatches2(int size, List<CarpetPiece2> stock, List<CarpetPiece2> carpet, List<CarpetPiece2> bestCarpet) {

        if (carpet.size() == size) {
            int score = calculateScore(carpet);
            if (score > bestScore) {
                bestScore = score;
                bestCarpet.clear();
                bestCarpet.addAll(carpet);
            }
            return;
        }

        //pruning if not possible to get a better score from current position
        if(!carpet.isEmpty()){
            if (calculateScore(carpet) + (size-carpet.size())* carpet.get(0).getStrip().length() < bestScore) {
                return;
            }
        }

        // Sort the remaining pieces based on their frequency in descending order
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (CarpetPiece2 piece : stock) {
            frequencyMap.put(piece.getStrip(), frequencyMap.getOrDefault(piece.getStrip(), 0) + 1);
        }
        List<Map.Entry<String, Integer>> sortedFrequencies = new ArrayList<>(frequencyMap.entrySet());
        sortedFrequencies.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Try placing the most frequent pieces first
        for (Map.Entry<String, Integer> entry : sortedFrequencies) {
            String strip = entry.getKey();
            int frequency = entry.getValue();

            for (int i = 0; i < frequency; i++) {
                CarpetPiece2 piece = stock.stream()
                        .filter(p -> p.getStrip().equals(strip))
                        .findFirst()
                        .orElse(null);

                if (piece != null) {
                    List<CarpetPiece2> newStock = new ArrayList<>(stock);
                    newStock.remove(piece);
                    carpet.add(piece);
                    mostMatches2(size, newStock, carpet, bestCarpet);
                    carpet.remove(carpet.size() - 1);
                }
            }
        }
    }

    public static List<CarpetPiece2> noMatches(int size, List<CarpetPiece2> stock, List<CarpetPiece2> carpet) {
        if (carpet.size() == size) {
            return carpet;
        }
        if (size > stock.size() + carpet.size()) {
            return null;
        }

        for (CarpetPiece2 piece : stock) {

            // if impossible to form a full size carpet from this position: skip

            boolean canAddPiece = carpet.isEmpty() || carpet.get(carpet.size() - 1).piecesDoNotMatch(piece);

            if (canAddPiece) {
                List<CarpetPiece2> newStock = new ArrayList<>(stock);
                newStock.remove(piece);
                carpet.add(piece);
                List<CarpetPiece2> result = noMatches(size, newStock, carpet);
                if (result != null) {
                    return result;
                }
                carpet.remove(carpet.size() - 1);

            }
        }

        return null;
    }

    public static List<CarpetPiece2> balancedMatches(int size) {
        return null;
    }

    public static String getMostCommonStrip() {
        String strip = carpetPieces.get(0).getStrip();
        int count = uniqueStripsCount.get(strip);
        for (String key : uniqueStripsCount.keySet()) {
            if (uniqueStripsCount.get(key) > count) {
                strip = key;
                count = uniqueStripsCount.get(key);
            }
        }

        return strip;

    }

    // Helper method to calculate the total number of matches in a carpet
    // arrangement
    // one match is when two indivudal components of the carpet match
    private static int calculateScore(List<CarpetPiece2> carpet) {
        int score = 0;
        for (int i = 0; i < carpet.size() - 1; i++) {
            CarpetPiece2 piece1 = carpet.get(i);
            CarpetPiece2 piece2 = carpet.get(i + 1);
            for (int j = 0; j < piece1.getStrip().length(); j++) {
                if (piece1.getStrip().charAt(j) == piece2.getStrip().charAt(j)) {
                    score++;
                }
            }
        }
        return score;
    }

}