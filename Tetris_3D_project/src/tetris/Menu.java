package tetris;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * trieda charakterizujuca hlavne menu
 *
 * @author Matej Kurinec
 * @version 1.0
 * @date 05.11.14
 */
public class Menu {

    private Game game;
    private Texture texture, whiteTexture, wallpaper;
    private boolean newGame = true, exit = false;

    /**
     * konstruktor na vytvorenie hlavneho menu + nacitavanie potrebnych textur
     *
     * pripade ze sa nejaka textura nenacita, program sa ukonci vynimkou
     *
     * @author Matej Kurinec
     * @version 1.0
     * @date 10.12.14
     */
    public Menu() {
        try {
            wallpaper = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/tetris-wallpaper.jpg"));
            texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/tetris.jpg"));
            whiteTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/white.jpg"));
        } catch (Exception ex) {
            System.err.println("Chyba pri nacitavani textury.");
            System.err.println("CHYBA: " + ex.getMessage());
        }
    }

    /**
     * metoda na vykreslenie hlavneho menu a zistovanie vstupu od pouzivatela
     */
    public void drawMenu() {

        while (!Display.isCloseRequested()) {
            //najprv sa musi bykreslit uvodna obrazovka s info o autorovi a predmete
            wallpaper.bind();
            glBegin(GL_QUADS);
            glTexCoord2f(0, 1);
            glVertex2f(0, 0);
            glTexCoord2f(1, 1);
            glVertex2f(800, 0);
            glTexCoord2f(1, 0);
            glVertex2f(800, 600);
            glTexCoord2f(0, 0);
            glVertex2f(0, 600);
            glEnd();
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                while (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                    Display.update();
                }
                break;
            }
            Display.update();
        }

        while (!Display.isCloseRequested()) {
            texture.bind();
            glBegin(GL_QUADS);
            glTexCoord2f(0, 1);
            glVertex2f(0, 0);
            glTexCoord2f(1, 1);
            glVertex2f(800, 0);
            glTexCoord2f(1, 0);
            glVertex2f(800, 600);
            glTexCoord2f(0, 0);
            glVertex2f(0, 600);
            glEnd();
            if (newGame) {
                drawLine(310, 500);
            } else if (exit) {
                drawLine(210, 280);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && newGame) {
                newGame = false;
                exit = true;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_UP) && exit) {
                newGame = true;
                exit = false;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                if (newGame) {
                    newGame();
                } else if (exit) {
                    System.exit(0);
                }
            }
            Display.update();
        }
        cleanUp();
    }

    /**
     * metoda na vytvorenie novej hry
     */
    public void newGame() {
        initGame();
        gameLoop();
    }

    /**
     * metoda na vykreslenie bielej viary pod text ciara charakterizuje aktualne
     * zvolenu polozku menu
     *
     * @param y y pozicia ciary
     * @param width sirka ciary
     */
    private void drawLine(int y, int width) {
        whiteTexture.bind();
        glLineWidth(8);
        glBegin(GL_LINES);
        glVertex2f((Display.getWidth() / 2) - (width / 2), y);
        glVertex2f((Display.getWidth() / 2) + (width / 2) + 20, y);
        glEnd();
    }

    /**
     * metoda, v ktorej sa vykonava hlavna slucka programu - hry
     */
    private void gameLoop() {
        while (!Display.isCloseRequested()) {
            getInput();
            update();
            render();
        }
        cleanUp();
        System.exit(0);
    }

    /**
     * inicializacia novej hry
     */
    private void initGame() {
        game = new Game(this);
    }

    /**
     * nacitanie vstupu od pouzivatela
     */
    private void getInput() {
        game.getInput();
    }

    /**
     * update-ovanie hry
     */
    private void update() {
        game.update();
    }

    /**
     * vymazanie instancii pre display a pre klavesnicu
     */
    private void cleanUp() {
        Display.destroy();
        Keyboard.destroy();
    }

    /**
     * prekreslenie obrazovky
     */
    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();

        game.render();

        Display.update();
        Display.sync(60);
    }
}
