public abstract class ChessTile{
    int tileCoordinates; // the current tile coordinates on the chess board

    public ChessTile(int coordinates) {
        this.tileCoordinates = coordinates;
    }
    // isTileOccupied() checks if the current tile is occupied by a chess piece.
    public abstract boolean isTileOccupied();
    // getPiece() gets the information of the chess piece on the current tile.
    public abstract ChessPiece getPiece();

    // Gives the property of an empty tile to the class ChessTile.
    public static final class emptyTile extends ChessTile{

        public emptyTile(int coordinate) {
            super(coordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }
        // There is no piece on an empty tile to return.
        @Override
        public ChessPiece getPiece() {
            return null;
        }
    }

    // Gives the property of an occupied tile to the class ChessTile.
    public static final class occupiedTile extends ChessTile{
        ChessPiece currentPiece; // ChessPiece on the current tile.
        public occupiedTile(int coordinate, ChessPiece curPiece) {
            super(coordinate);
            this.currentPiece = curPiece;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public ChessPiece getPiece() {
            return this.currentPiece;
        }
    }
}
