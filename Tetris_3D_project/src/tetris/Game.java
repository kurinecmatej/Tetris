package tetris;


import org.lwjgl.input.Keyboard;

/**
 * Trieda game, v ktorej sa odohrava logika celej hry
 * @author Matej Kurinec
 * @version 1.0
 * @date 15.11.14
 */
public class Game {
    private final Menu menu;
    private final Board board;
    private long curTime;
    
    /**
     * konstruktor triedy Game
     * @param menu instancia menu, ktora sa odovzdava objektu GameOver
     */
    public Game(Menu menu) {
        this.menu = menu;
        board = new Board();
        board.start();
        curTime = 0;
    }

    /**
     * metoda pre ziskanie vstupu od pouzivatela
     */
    public void getInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && System.currentTimeMillis() - curTime > 100){
            board.getCurrentShape().moveRight();
            curTime = System.currentTimeMillis();
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && System.currentTimeMillis() - curTime > 100){
            board.getCurrentShape().moveLeft();
            curTime = System.currentTimeMillis();
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) 
            board.getCurrentShape().oneLineDown(board);
        else if (Keyboard.isKeyDown(Keyboard.KEY_UP) && System.currentTimeMillis() - curTime > 100){ 
            board.getCurrentShape().rotateLeft();
            curTime = System.currentTimeMillis();
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_P)){
            new Pause(menu, this);
        }
    }

    /**
     * metoda na prekreslenie obrazovky
     */
    public void render() {
        board.drawPlayground();
        board.render();
    }

    /**
     * metoda na update-ovanie hry
     */
    public void update() {
        if(!board.getGameOver())
            board.update();
        else new GameOver(menu, this);
    }
    
}
