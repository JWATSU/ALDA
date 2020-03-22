//Jesper Westin, jeve9726, jeve9726@student.su.se
public class ChessBoard
{
    static private class Queen
    {
        int row;
        int column;

        Queen(int row, int column)
        {
            this.row = row;
            this.column = column;
        }

        @Override
        public String toString()
        {
            return "row " + row + "column" + column;
        }
    }

    private final Queen[] queens;
    private final int boardSize;

    public ChessBoard(int boardSize)
    {
        queens = new Queen[boardSize];
        this.boardSize = boardSize;
    }

    public void getBoard()
    {
        boolean hasSolution = generateBoard(0);
        if (hasSolution)
        {
            printBoard();
        } else
        {
            System.out.println("NO SOLUTION FOUND");
        }
    }

    private boolean generateBoard(int row)
    {
        if (row == boardSize)
        {
            return true;
        }
        for (int column = 0; column < boardSize; column++)
        {
            boolean legalCoordinate = true;

            for (Queen queen : queens)
            {
                if (queen != null && (queen.column == column || Math.abs(queen.row - row) == Math.abs(queen.column - column)))
                {
                    legalCoordinate = false;
                    break;
                }
            }
            if (legalCoordinate)
            {
                queens[row] = new Queen(row, column);
                if (generateBoard(row + 1))
                {
                    return true;
                }
                // We need to backtrack, so the last placed queen needs to be removed.
                queens[row] = null;
            }
        }
        return false;
    }

    private void printBoard()
    {
        String[][] chessBoard = new String[boardSize][boardSize];
        for (Queen queen : queens)
        {
            chessBoard[queen.row][queen.column] = "Q";
        }

        for (int row = 0; row < boardSize; row++)
        {
            for (int column = 0; column < boardSize; column++)
                if (chessBoard[row][column] == "Q")
                {
                    System.out.print("Q" + "\t");
                } else
                {
                    System.out.print("*" + "\t");
                }
            System.out.print("\n");
        }
    }

    public static void main(String[] args)
    {
        ChessBoard board = new ChessBoard(35);
        board.getBoard();
    }
}