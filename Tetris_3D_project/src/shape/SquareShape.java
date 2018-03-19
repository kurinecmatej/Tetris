package shape;

import tetris.Board;

/**
 * Trieda charakterizujuca tvar kocky
 * 
 * @author Matej Kurinec
 * @version 1.0
 * @date 20.11.14
 */
public class SquareShape extends Shape {

    Board board;

    /**
     * konstruktor triedy SquareShape. Vytvrenie noveho tvaru
     * @param board instancia aktualnej hracej plochy
     */
    public SquareShape(Board board) {
        rotation = Shape.Rotation.NORTH;
        setBoard(board);
        setCenterX(5);
        setCenterY(1);
        if(isFirstFree()){
            createShape();
            board.setCanCreate(true);
        }
        else board.setCanCreate(false);
    }

    @Override
    public boolean oneLineDown(Board board) {
        switch (rotation) {
            case NORTH:
                if (getCenterY() + 1 < board.getBoardHeight()
                        && board.getBoard()[getCenterY() + 1][getCenterX() - 1] == 0
                        && board.getBoard()[getCenterY() + 1][getCenterX()] == 0) {
                    nullShape();
                    setCenterY(getCenterY() + 1);
                    createShape();
                    setBoard(board);
                    board.render();
                    return true;
                }
                break;
            case SOUTH:
                break;
            case EAST:
                break;
            case WEST:
                break;

        }
        return false;
    }

    @Override
    public void moveLeft() {
        board = getBoard();
        if (getCenterX() - 1 > 0 && board.getBoard()[getCenterY() - 1][getCenterX() - 2] == 0
                && board.getBoard()[getCenterY()][getCenterX() - 2] == 0) {
            nullShape();
            setCenterX(getCenterX() - 1);
            createShape();
            setBoard(board);
            board.render();
        }
    }

    @Override
    public void moveRight() {
        board = getBoard();
        if (getCenterX() + 1 < board.getBoardWidth() && board.getBoard()[getCenterY() - 1][getCenterX() + 1] == 0
                && board.getBoard()[getCenterY()][getCenterX() + 1] == 0) {
            nullShape();
            setCenterX(getCenterX() + 1);
            createShape();
            setBoard(board);
            board.render();
        }
    }

    @Override
    public void rotateLeft() {
        return;
    }

    @Override
    public void rotateRight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void nullShape() {
        board = getBoard();
        board.getBoard()[getCenterY() - 1][getCenterX()] = 0;
        board.getBoard()[getCenterY() - 1][getCenterX() - 1] = 0;
        board.getBoard()[getCenterY()][getCenterX()] = 0;
        board.getBoard()[getCenterY()][getCenterX() - 1] = 0;
        setBoard(board);
    }

    @Override
    public void createShape() {
        board = getBoard();
        board.getBoard()[getCenterY() - 1][getCenterX()] = 5;
        board.getBoard()[getCenterY() - 1][getCenterX() - 1] = 5;
        board.getBoard()[getCenterY()][getCenterX()] = 5;
        board.getBoard()[getCenterY()][getCenterX() - 1] = 5;
        setBoard(board);
    }

    @Override
    public boolean isFirstFree() {
        board = getBoard();
        return (board.getBoard()[getCenterY() - 1][getCenterX()] == 0
                && board.getBoard()[getCenterY() - 1][getCenterX() - 1] == 0
                && board.getBoard()[getCenterY()][getCenterX()] == 0
                && board.getBoard()[getCenterY()][getCenterX() - 1] == 0);
    }

}
