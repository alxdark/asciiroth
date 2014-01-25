package us.asciiroth.client.core;

/**
 * Immutability has been a big success in this game, in terms of keeping the 
 * bugs due to behavior under control. But when it comes time to render and 
 * you have to combine these symbols in to one, we need an object to keep 
 * track of the union of the parts. This is it. Pieces should not use this. 
 */
public class ModifiableSymbol {

    private String entity;
    private Color color;
    private Color background;
    
    /**
     * Overwrite any of the components of the symbol so far with any 
     * that are defined for the supplied symbol. 
     * @param sym
     * @param outside
     */
    public void addSymbol(Symbol sym, boolean outside) {
        if (sym == null) {
            return;
        }
        if (sym.getAdjustedEntity() != null) {
            this.entity = sym.getAdjustedEntity();
        }
        if (sym.getColor(outside) != null) {
            this.color = sym.getColor(outside);
        }
        if (sym.getBackground(outside) != null) {
            this.background = sym.getBackground(outside);
        }
    }
    /**
     * Overwrite any of the components of the symbol so far with any 
     * that are defined for the supplied symbol. 
     * @param piece
     * @param outside
     */
    public void addPiece(Piece piece, boolean outside) {
        if (piece == null || piece.getSymbol() == null) {
            return;
        }
        Symbol sym = piece.getSymbol();
        if (sym == null) {
            return;
        }
        if (sym.getAdjustedEntity() != null) {
            this.entity = sym.getAdjustedEntity();
        }
        if (sym.getColor(outside) != null) {
            this.color = sym.getColor(outside);
        }
        if (sym.getBackground(outside) != null) {
            this.background = sym.getBackground(outside);
        }
    }
    
    public String getEntity() {
        return this.entity;
    }
    public String getColor() {
        return (this.color == null) ? "" : this.color.toString();
    }
    public String getBackground() {
        return (this.background == null) ? "" : this.background.toString();
    }
    
}
