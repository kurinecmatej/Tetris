package tetris;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Trieda charakterizujuca pauzu hry
 * 
 * @author Matej Kurinec
 * @version 1.0
 * @date 10.12.14
 */
public class Pause {
    private Texture texture, whiteTexture;
    private boolean returnToGame = true, mainMenu = false;
    private Menu menu;
    private Game game;
    
    /**
     * konstruktor na vytvorenie instancie triedy Pause
     * 
     * V pripade ze sa nejaka textura nenacita, program sa ukonci vynimkou
     * 
     * @param menu instancia triedy menu
     * @param game instancia triedy game
     * 
     */
    public Pause(Menu menu, Game game){
        this.menu = menu;
        this.game = game;
        try {
            texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/tetris_pause.jpg"));
            whiteTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/white.jpg"));
        } catch (Exception ex) {
            System.err.println("Chyba pri nacitavani textury.");
            System.err.println("CHYBA: " + ex.getMessage());
        }
        drawPause();
    }
    
    /**
     * metoda na vykreslenie obrazovky pre pripad ked je pauza
     */
    private void drawPause(){
        while(Keyboard.isKeyDown(Keyboard.KEY_P))Display.update();
        
        while(!Display.isCloseRequested()){
            texture.bind();
            glBegin(GL_QUADS);
                    glTexCoord2f(0, 1);glVertex2f(0, 0);
                    glTexCoord2f(1, 1);glVertex2f(800, 0);
                    glTexCoord2f(1, 0);glVertex2f(800, 600);
                    glTexCoord2f(0, 0);glVertex2f(0, 600);
            glEnd();

            if(returnToGame)
                drawLine(500, 300);
            else if(mainMenu)
                drawLine(405, 380);
            if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && returnToGame){
                returnToGame = false;
                mainMenu = true;
            }
            else if(Keyboard.isKeyDown(Keyboard.KEY_UP) && mainMenu){
                returnToGame = true;
                mainMenu = false;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
                if(returnToGame){
                    return;
                }
                else if(mainMenu){
                    game = null;
                    //pocka sa kym nepustim enter
                    while(Keyboard.isKeyDown(Keyboard.KEY_RETURN))Display.update();
                    menu.drawMenu();
                }
            }
            Display.update();
        }
        cleanUp();
    }
    
    /**
     * metoda na vykreslenie bielej viary pod text
     * ciara charakterizuje aktualne zvolenu polozku menu
     * @param y y pozicia ciary
     * @param width sirka ciary
     */
    private void drawLine(int y, int width){
        whiteTexture.bind();
        glLineWidth(8);
        glBegin(GL_LINES);
            glVertex2f((Display.getWidth()/2)-(width/2) - 10, y);
            glVertex2f((Display.getWidth()/2)+(width/2) - 10, y);
        glEnd();
    }
    
    /**
     * vymazanie instancii pre display a pre klavesnicu
     */
    private void cleanUp() {
        Display.destroy();
        Keyboard.destroy();
        System.exit(0);
    }
}
