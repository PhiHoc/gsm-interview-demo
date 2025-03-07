package interview.googleapis;

import lombok.Getter;

/**
 * Represents the position (row and column indices) of a cell in a Google Sheet.
 */
@Getter
public class CellPosition {
    private final int row;
    private final int col;

    public CellPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
