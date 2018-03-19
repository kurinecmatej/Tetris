package tetris;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

/**
 * ALL RIGHTS RESERVED
 * mail: matej.krc10@gmail.com
 * 
 * @author Matej Kurinec
 * @version 1.0
 * @date 05.11.14
 */
public class Tetris {
    private static Menu menu;

    /**
     * hlavna funkcia, spustanie programu, inicializacia hlavneho menu
     * @param args argumenty hlavnej metody
     */
    public static void main(String[] args) {
        initDisplay();
        initGL();
        menu = new Menu();
        menu.drawMenu();
    }

    /**
     * inicializacia okna
     */
    private static void initGL() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 800, 0, 600, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
    }
    
    /**
     * inicializacia display-u
     */
    private static void initDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.setTitle("T E T R I S");
            Display.create();
            Display.setVSyncEnabled(true);
            Keyboard.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(Tetris.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
