package us.asciiroth.client.board;

/**
 * A visitor that is used to examine cells on the board through a 
 * number of methods on the Board object. If the visit method returns
 * a cell, the visitor search will immediately return that cell and
 * end, otherwise, it continues. 
 *
 */
public interface CellVisitor {
    /**
     * Visit this cell. Returning true will cause the visit method to immediately
     * return this cell to the caller.
     * @param cell
     * @return  false to abort visit.
     */
    public boolean visit(Cell cell, int range);
}
