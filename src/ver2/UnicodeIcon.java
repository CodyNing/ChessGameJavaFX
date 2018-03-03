package ver2;

/**
 * UnicodeIcon.class
 * Unicode icon of chess game.
 *
 * @author Zhuo (Cody) Ning
 * @version 2018
 */
public class UnicodeIcon implements MyIcon {

    /**
     * Unicode symbol for this icon.
     */
    private String symbol;

    /**
     * Constructs an object of type UnicodeIcon.
     * @param symbol
     */
    public UnicodeIcon(String symbol) {
        super();
        this.symbol = symbol;
    }

    /**
     * Return the symbol for this UnicodeIcon.
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the symbol for this UnicodeIcon.
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    
}
