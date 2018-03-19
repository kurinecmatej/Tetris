package shape;

import tetris.Board;

/**
 * abstraktna trieda obsahujuca zakladne metody sluziace na zakladne manipulacie s objektami hry
 * 
 * @author Matej Kurinec
 * @version 1.0
 * @date 20.11.14
 */
public abstract class Shape {

    public enum Rotation {NORTH, SOUTH, WEST, EAST};

    private Board board;
    private int centerX = 0;
    private int centerY = 0;
    protected Rotation rotation;
        
    /**
     * metoda na ziskanie instancie aktualnej hracej plochy
     * @return gameboard
     */
    public Board getBoard() {
        return board;
    }

    /**
     * metoda na nastavenie novej hracej plochy
     * @param board aktualna hracia plocha
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * metoda na zistenie Y suradnice stredovej kocky tvaru
     * @return stredova Y pozicia
     */
    public int getCenterY() {
        return centerY;
    }

    /**
     * metoda na nastavenie Y suradnice stredovej kocky tvaru
     * @param centerY stredova Y pozicia
     */
    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
    
    /**
     * metoda na zistenie X suradnice stredovej kocky tvaru
     * @return stredova X pozicia
     */
    protected int getCenterX(){
      return centerX;  
    }
    
    /**
     * metoda na nastavenie X suradnice stredovej kocky tvaru
     * @param centerX stredova X pozicia
     */
    protected void setCenterX(int centerX){
        this.centerX = centerX;
    }

    /**
     * metoda na posunutie tvaru o jeden riadok nizsie
     * @param board aktualna instancia hracej plochy
     * @return true ak sa kocka posunula o jeden riadok nizsie, false ak sa kocka nemoze posunut nizsie
     */
    public abstract boolean oneLineDown(Board board);
    
    /**
     * metoda na vynulovanie starej pozicie tvaru, ak sa s nim hybalo
     */
    public abstract void nullShape();
    
    /**
     * metoda na vytvorenie (nastavenie) novej pozicie tvaru
     */
    public abstract void createShape();
    
    /**
     * metoda na posun tvaru dolava
     */
    public abstract void moveLeft();
    
    /**
     * metoda na posun tvaru doprava
     */
    public abstract void moveRight();
    
    /**
     * metoda na rotaciu tvaru proti smeru hodinovych ruciciek
     */
    public abstract void rotateLeft();
    
    /**
     * metoda na rotaciu tvaru v smere hodinovych ruciciek
     */
    public abstract void rotateRight();
    
    /**
     * metoda na skontrolovanie hracej plochy. Zistuje sa, ci sa da este vykreslit novy tvar alebo sa uz neda
     * @return true, ak sa da vykreslit novy tvar, false ak sa neda 
     */
    public abstract boolean isFirstFree();

}
