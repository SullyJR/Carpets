package piece;

/**
 * A class to represent a piece of carpet.
 */

import java.util.*;

public class CarpetPiece2 {

    private String strip;
    private Set<CarpetPiece2> matches = new HashSet<>();
    private Set<CarpetPiece2> noMatches = new HashSet<>();
    private Map<Integer, Boolean> reversalMap = new HashMap<>();


    private int id;

    public CarpetPiece2(String strip, int id) {
        this.strip = strip.toLowerCase();
        this.id = id;
    }

    public String getStrip() {
        return this.strip;
    }

    public List<CarpetPiece2> getMatches() {
        return new ArrayList<>(matches);
    }

    // A method to see if two pieves of carpet match
    // public boolean piecesMatch(CarpetPiece2 other) {
    //     return false;
    // }

    public boolean piecesDoNotMatch(CarpetPiece2 other) {
        return noMatches.contains(other);
    }

    // A method to reverse the carpet piece
    public CarpetPiece2 reverse() {
        return new CarpetPiece2(new StringBuilder(strip).reverse().toString(), id);
    }

    /**
     * Check if two carpet pieces match in either direction
     * and if they don't match at all in any direction
     * 
     * @param other The other carpet piece to check against
     */
    public void checkMatch(CarpetPiece2 other) {
        String reversedOther = new StringBuilder(other.strip).reverse().toString();

        boolean exactMatchForward = strip.equals(other.strip);
        boolean exactMatchBackward = strip.equals(reversedOther);
        boolean noMatchFoundForward = true;
        boolean noMatchFoundBackward = true;

        for (int i = 0; i < strip.length(); i++) {
            if (strip.charAt(i) == other.strip.charAt(i)) {
                noMatchFoundForward = false;
                break; // No need to continue checking
            }
        }

        for (int i = 0; i < strip.length(); i++) {
            if (strip.charAt(i) == reversedOther.charAt(i)) {
                noMatchFoundBackward = false;
                break; // No need to continue checking
            }
        }

        if (exactMatchForward || exactMatchBackward) {
            Boolean shouldReverse = exactMatchForward ? false : true;
            reversalMap.put(other.id, shouldReverse);
            matches.add(other);

        }

        if (noMatchFoundForward || noMatchFoundBackward) {
            Boolean shouldReverse = noMatchFoundForward ? false : true;
            reversalMap.put(other.id, shouldReverse);
            noMatches.add(other);
        }
    }

    public int getId() {
        return this.id;
    }

    public String toStringDebug() {
        StringBuilder sb = new StringBuilder();
        sb.append("CarpetPiece2: " + this.strip + ", id: " + this.id + "\n");
        if (matches.size() > 0) {
            sb.append("Matches ID's: ");
            for (CarpetPiece2 piece : matches) {
                sb.append(piece.getId() + ", ");
            }
            sb.deleteCharAt(sb.length() - 2);
            sb.append("\n");
        }
        if (noMatches.size() > 0) {
            sb.append("No Matches ID's: ");
            for (CarpetPiece2 piece : noMatches) {
                sb.append(piece.getId() + ", ");
            }
            sb.deleteCharAt(sb.length() - 2);
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.strip;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CarpetPiece2)) {
            return false;
        }
        CarpetPiece2 other = (CarpetPiece2) obj;
        return this.strip.equals(other.strip);
    }


}