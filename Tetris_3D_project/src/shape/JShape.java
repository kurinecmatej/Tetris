package shape;

import tetris.Board;

/**
 * Trieda charakterizujuca tvar pismena J
 * 
 * @author Matej Kurinec
 * @version 1.0
 * @date 20.11.14
 * 
 * 
 * java.lang.Object
java.lang.Enum<Shape.Rotation>
shape.Shape.Rotation

Všetky implementované interfejsy:
	java.io.Serializable 
java.lang.Comparable<Shape.Rotation>

Zapuzdrujúca trieda: 
	Shape

Enumeračné konštanty:
	NORTH, SOUTH, WEST, EAST


Metódy zdedené od triedy java.lang.Enum:
clone, compareTo, equals, finalize, getDeclaringClass, hashCode, name, ordinal, toString, valueOf 

Metódy zdedené od triedy java.lang.Object:
	getClass, notify, notifyAll, wait 


 */
public class JShape extends Shape {

    Board board;

    /**
     * konstruktor triedy JShape. Vytvrenie noveho tvaru
     * @param board instancia aktualnej hracej plochy
     */
    public JShape(Board board) {
        rotation = Shape.Rotation.NORTH;
        setBoard(board);
        setCenterX(4);
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
                if (!(getCenterY() + 2 < board.getBoardHeight()
                        && board.getBoard()[getCenterY() + 2][getCenterX()] == 0
                        && board.getBoard()[getCenterY() + 2][getCenterX() - 1] == 0)) {
                    return false;
                }
                break;
            case SOUTH:
                if (!(getCenterY() + 2 < board.getBoardHeight()
                        && board.getBoard()[getCenterY() + 2][getCenterX()] == 0
                        && board.getBoard()[getCenterY()][getCenterX() + 1] == 0)) {
                    return false;
                }
                break;
            case EAST:
                if (!(getCenterY() + 1 < board.getBoardHeight()
                        && board.getBoard()[getCenterY() + 1][getCenterX() - 1] == 0
                        && board.getBoard()[getCenterY() + 1][getCenterX() + 1] == 0
                        && board.getBoard()[getCenterY() + 1][getCenterX()] == 0)) {
                    return false;
                }
                break;
            case WEST:
                if (!(getCenterY() + 2 < board.getBoardHeight()
                        && board.getBoard()[getCenterY() + 1][getCenterX() - 1] == 0
                        && board.getBoard()[getCenterY() + 1][getCenterX()] == 0
                        && board.getBoard()[getCenterY() + 2][getCenterX() + 1] == 0)) {
                    return false;
                }
                break;
        }
        nullShape();
        setCenterY(getCenterY() + 1);
        createShape();
        setBoard(board);
        board.render();
        return true;
    }

    @Override
    public void moveLeft() {
        board = getBoard();
        switch (rotation) {
            case NORTH:
                if (!(getCenterX() - 1 > 0 && board.getBoard()[getCenterY() - 1][getCenterX() - 1] == 0
                        && board.getBoard()[getCenterY()][getCenterX() - 1] == 0
                        && board.getBoard()[getCenterY() + 1][getCenterX() - 2] == 0)) {
                    return;
                }
                break;
            case SOUTH:
                if (!(getCenterX() > 0 && board.getBoard()[getCenterY() - 1][getCenterX() - 1] == 0
                        && board.getBoard()[getCenterY()][getCenterX() - 1] == 0
                        && board.getBoard()[getCenterY() + 1][getCenterX() - 1] == 0)) {
                    return;
                }
                break;
            case EAST:
                if (!(getCenterX() - 1 > 0 && board.getBoard()[getCenterY() + 1][getCenterX() - 2] == 0
                        && board.getBoard()[getCenterY()][getCenterX() - 2] == 0)) {
                    return;
                }
                break;
            case WEST:
                if (!(getCenterX() - 1 > 0 && board.getBoard()[getCenterY() + 1][getCenterX()] == 0
                        && board.getBoard()[getCenterY()][getCenterX() - 2] == 0)) {
                    return;
                }
                break;
        }
        nullShape();
        setCenterX(getCenterX() - 1);
        createShape();
        setBoard(board);
        board.render();
    }

    @Override
    public void moveRight() {
        board = getBoard();
        switch (rotation) {
            case NORTH:
                if (!(getCenterX() + 1 < board.getBoardWidth() && board.getBoard()[getCenterY() - 1][getCenterX() + 1] == 0
                        && board.getBoard()[getCenterY()][getCenterX() + 1] == 0
                        && board.getBoard()[getCenterY() + 1][getCenterX() + 1] == 0)) {
                    return;
                }
                break;
            case SOUTH:
                if (!(getCenterX() + 2 < board.getBoardWidth() && board.getBoard()[getCenterY() - 1][getCenterX() + 2] == 0
                        && board.getBoard()[getCenterY()][getCenterX() + 1] == 0
                        && board.getBoard()[getCenterY() + 1][getCenterX() + 1] == 0)) {
                    return;
                }
                break;
            case EAST:
                if (!(getCenterX() + 2 < board.getBoardWidth() && board.getBoard()[getCenterY() - 1][getCenterX()] == 0
                        && board.getBoard()[getCenterY()][getCenterX() + 2] == 0)) {
                    return;
                }
                break;
            case WEST:
                if (!(getCenterX() + 2 < board.getBoardWidth() && board.getBoard()[getCenterY()][getCenterX() + 2] == 0
                        && board.getBoard()[getCenterY() + 1][getCenterX() + 2] == 0)) {
                    return;
                }
                break;
        }
        nullShape();
        setCenterX(getCenterX() + 1);
        createShape();
        setBoard(board);
        board.render();
    }

    @Override
    public void rotateLeft() {
        board = getBoard();
        switch (rotation) {
            case NORTH:
                if (getCenterX() + 1 < board.getBoardWidth() && board.getBoard()[getCenterY()][getCenterX() - 1] == 0
                        && board.getBoard()[getCenterY()][getCenterX() + 1] == 0
                        && board.getBoard()[getCenterY() + 1][getCenterX() + 1] == 0) {
                    nullShape();
                    rotation = Shape.Rotation.WEST;
                    createShape();
                    setBoard(board);
                    board.render();
                }
                break;
            case SOUTH:
                if(getCenterX() > 0 && board.getBoard()[getCenterY() + 1][getCenterX() - 1] == 0 &&
                        board.getBoard()[getCenterY()][getCenterX() - 1] == 0 &&
                        board.getBoard()[getCenterY()][getCenterX() + 1] == 0){
                nullShape();
                rotation = Shape.Rotation.EAST;
                createShape();
                setBoard(board);
                board.render();
                }
                break;
            case EAST:
                if(getCenterY() + 1 < board.getBoardHeight() && board.getBoard()[getCenterY() - 1][getCenterX()] == 0 &&
                        board.getBoard()[getCenterY() + 1][getCenterX() - 1] == 0 &&
                        board.getBoard()[getCenterY() + 1][getCenterX()] == 0){
                nullShape();
                rotation = Shape.Rotation.NORTH;
                createShape();
                setBoard(board);
                board.render();
                }
                break;
            case WEST:
                if(board.getBoard()[getCenterY() - 1][getCenterX()] == 0 &&
                        board.getBoard()[getCenterY() - 1][getCenterX() + 1] == 0 &&
                        board.getBoard()[getCenterY() + 1][getCenterX()] == 0){
                nullShape();
                rotation = Shape.Rotation.SOUTH;
                createShape();
                setBoard(board);
                board.render();
                }
                break;
        }
    }

    @Override
    public void rotateRight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void nullShape() {
        board = getBoard();
        switch (rotation) {
            case NORTH:
                board.getBoard()[getCenterY()][getCenterX()] = 0;
                board.getBoard()[getCenterY() - 1][getCenterX()] = 0;
                board.getBoard()[getCenterY() + 1][getCenterX()] = 0;
                board.getBoard()[getCenterY() + 1][getCenterX() - 1] = 0;
                break;
            case SOUTH:
                board.getBoard()[getCenterY()][getCenterX()] = 0;
                board.getBoard()[getCenterY() - 1][getCenterX()] = 0;
                board.getBoard()[getCenterY() + 1][getCenterX()] = 0;
                board.getBoard()[getCenterY() - 1][getCenterX() + 1] = 0;
                break;
            case EAST:
                board.getBoard()[getCenterY()][getCenterX()] = 0;
                board.getBoard()[getCenterY()][getCenterX() + 1] = 0;
                board.getBoard()[getCenterY()][getCenterX() - 1] = 0;
                board.getBoard()[getCenterY() - 1][getCenterX() - 1] = 0;
                break;
            case WEST:
                board.getBoard()[getCenterY()][getCenterX() - 1] = 0;
                board.getBoard()[getCenterY()][getCenterX()] = 0;
                board.getBoard()[getCenterY()][getCenterX() + 1] = 0;
                board.getBoard()[getCenterY() + 1][getCenterX() + 1] = 0;
                break;
        }
        setBoard(board);
    }

    @Override
    public void createShape() {
        board = getBoard();
        switch (rotation) {
            case NORTH:
                board.getBoard()[getCenterY()][getCenterX()] = 1;
                board.getBoard()[getCenterY() - 1][getCenterX()] = 1;
                board.getBoard()[getCenterY() + 1][getCenterX()] = 1;
                board.getBoard()[getCenterY() + 1][getCenterX() - 1] = 1;
                break;
            case SOUTH:
                board.getBoard()[getCenterY()][getCenterX()] = 1;
                board.getBoard()[getCenterY() - 1][getCenterX()] = 1;
                board.getBoard()[getCenterY() + 1][getCenterX()] = 1;
                board.getBoard()[getCenterY() - 1][getCenterX() + 1] = 1;
                break;
            case EAST:
                board.getBoard()[getCenterY()][getCenterX() - 1] = 1;
                board.getBoard()[getCenterY()][getCenterX() + 1] = 1;
                board.getBoard()[getCenterY()][getCenterX()] = 1;
                board.getBoard()[getCenterY() - 1][getCenterX() - 1] = 1;
                break;
            case WEST:
                board.getBoard()[getCenterY()][getCenterX() - 1] = 1;
                board.getBoard()[getCenterY()][getCenterX()] = 1;
                board.getBoard()[getCenterY()][getCenterX() + 1] = 1;
                board.getBoard()[getCenterY() + 1][getCenterX() + 1] = 1;
                break;
        }
        setBoard(board);
    }

    @Override
    public boolean isFirstFree() {
        board = getBoard();
        return (board.getBoard()[getCenterY()][getCenterX()] == 0 &&
                board.getBoard()[getCenterY() - 1][getCenterX()] == 0 &&
                board.getBoard()[getCenterY() + 1][getCenterX()] == 0 &&
                board.getBoard()[getCenterY() + 1][getCenterX() - 1] == 0);
    }
}
