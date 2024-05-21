package piece;

public class CarpetPiece {
    private String strip;
    private String reversedStrip;
    private int id;

    public CarpetPiece(String strip, int id) {
        this.strip = strip;
        this.reversedStrip = new StringBuilder(strip).reverse().toString();
        this.id = id;
    }

    public String getStrip() {
        return strip;
    }

    public String getReversedStrip() {
        return reversedStrip;
    }

    public int getId() {
        return id;
    }

    public boolean piecesDoNotMatch(CarpetPiece other, boolean useReversed) {
        String otherStrip = useReversed ? other.getReversedStrip() : other.getStrip();
        for (int i = 0; i < strip.length(); i++) {
            if (strip.charAt(i) == otherStrip.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return strip;
    }

    
}