package tetris;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
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
import shape.*;

/**
 * Trieda charakterizujuca hracu plochu. V tejto triede sa vykonava cela
 * graficka cast hry.
 *
 * @author Matej Kurinec
 * @version 1.0
 * @date 15.11.14
 * 
 */
public class Board {

    private final int width = 8;
    private final int height = 22;
    private int score = 0;
    private Shape curPiece;
    private final int[][] board;
    private final int[][] nextPieceBoard;
    private int next;
    private final Random piece;
    private boolean canCreate, gameOver;
    private final int dlzkaBocnychStran;
    private final long startTime;
    private long curTime;
    private final Texture[] texture;
    private int delay, level;
    private String highScore;

    /**
     * Konstruktor triedy Board Nastavenie hracej plochy a inicializacia
     * casovacov
     */
    public Board() {
        gameOver = false;
        delay = 1000;
        texture = new Texture[11];
        piece = new Random();
        next = piece.nextInt(7) + 1;
        board = new int[height][width];
        nextPieceBoard = new int[4][3];
        dlzkaBocnychStran = (int) (getCubeWidth() / 3);
        startTime = System.currentTimeMillis();
        level = 1;
        readHighScore();
    }

    /**
     * vycisti hraciu plochu vsetky polozky pole sa nastavia na hodnotu 0
     */
    public void clearBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = 0;
            }
        }
        clearNextBoard();
    }

    /**
     * vycisti plochu vykreslovania nasledujuceho tvaru vsetky polozky pole sa
     * nastavia na hodnotu 0
     */
    private void clearNextBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                nextPieceBoard[i][j] = 0;
            }
        }
    }

    /**
     * nacitanie vsetkych potrebnych textur V pripade ze sa nejaka textura
     * nenacita, program sa ukonci vynimkou -> napise sa aj chybova sprava
     * zahrnujuca nazov prvej chybajucej textury
     *
     * @exception V pripade ze sa nejaka textura nenacita, program sa ukonci
     * vynimkou
     */
    private void loadTextures() {
        try {
            texture[0] = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/background.jpg"));
            texture[1] = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/blue.jpg"));
            texture[2] = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/green.jpg"));
            texture[3] = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/pink.jpg"));
            texture[4] = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/purple.jpg"));
            texture[5] = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/turquoise.jpg"));
            texture[6] = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/red.jpg"));
            texture[7] = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/yellow.jpg"));
            texture[8] = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/white.jpg"));
            texture[9] = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/bricks.jpg"));
            texture[10] = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/black.jpg"));
        } catch (Exception ex) {
            System.err.println("Chyba pri nacitavani textury.");
            System.err.println("CHYBA: " + ex.getMessage());
        }
    }

    /**
     * zacatie hry
     */
    public void start() {
        loadTextures();
        clearBoard();
        newPiece();
    }

    /**
     * metoda na zistenie vysky hracej plochy v bodoch
     *
     * @return vyska hracej plochy v bodoch
     */
    public int getBoardHeight() {
        return height;
    }

    /**
     * metoda na zistenie sirky hracej plochy v bodoc
     *
     * @return sirka hracej plochy v bodoch
     */
    public int getBoardWidth() {
        return width;
    }

    /**
     * metoda na nastavenie prislusnej bool premennej, pomocou ktorej sa
     * zistuje. ci sa da vytvorit novy tvar este alebo sa uz neda
     *
     * @param canCreate true ak sa da, false ak sa neda
     */
    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    /**
     * metoda sluziaca na ziskanie aktualne padajuceho objektu
     *
     * @return aktualne padajuci objekt
     */
    public Shape getCurrentShape() {
        return curPiece;
    }

    /**
     * metoda na zistenie ci sa hra uz prehrala alebo sa este hra dalej
     *
     * @return true, ak je zaplnene cele hracie pole - hra je skoncena, false ak
     * sa hra este neskoncila
     */
    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * metoda na ziskanie hracieho pola
     *
     * @return pole hracej plochy
     */
    public int[][] getBoard() {
        return this.board;
    }

    /**
     * metoda na ziskanie hodnoty sirky jednej kocky
     *
     * @return sirka jednej kocky
     */
    private int getCubeWidth() {
        return (int) ((Display.getHeight() - 100) / height);
    }

    /**
     * metoda na nahodne generovanie nasledujuceho tvaru v tejto metoda sa
     * pouziva metoda nextInt triedy Random na nahodne generovanie cisel
     */
    private void newPiece() {
        switch (next) {
            case 1:
                curPiece = new JShape(this);
                break;
            case 2:
                curPiece = new LShape(this);
                break;
            case 3:
                curPiece = new LineShape(this);
                break;
            case 4:
                curPiece = new SShape(this);
                break;
            case 5:
                curPiece = new SquareShape(this);
                break;
            case 6:
                curPiece = new TShape(this);
                break;
            case 7:
                curPiece = new ZShape(this);
                break;
        }
        next = piece.nextInt(7) + 1;

        switch (next) {
            case 1:
                nextPieceBoard[0][0] = 1;
                nextPieceBoard[0][1] = 1;
                nextPieceBoard[1][1] = 1;
                nextPieceBoard[2][1] = 1;
                break;
            case 2:
                nextPieceBoard[0][0] = 2;
                nextPieceBoard[0][1] = 2;
                nextPieceBoard[1][0] = 2;
                nextPieceBoard[2][0] = 2;
                break;
            case 3:
                nextPieceBoard[0][1] = 3;
                nextPieceBoard[1][1] = 3;
                nextPieceBoard[2][1] = 3;
                nextPieceBoard[3][1] = 3;
                break;
            case 4:
                nextPieceBoard[0][0] = 4;
                nextPieceBoard[0][1] = 4;
                nextPieceBoard[1][1] = 4;
                nextPieceBoard[1][2] = 4;
                break;
            case 5:
                nextPieceBoard[0][0] = 5;
                nextPieceBoard[0][1] = 5;
                nextPieceBoard[1][0] = 5;
                nextPieceBoard[1][1] = 5;
                break;
            case 6:
                nextPieceBoard[0][0] = 6;
                nextPieceBoard[0][1] = 6;
                nextPieceBoard[0][2] = 6;
                nextPieceBoard[1][1] = 6;
                break;
            case 7:
                nextPieceBoard[0][1] = 7;
                nextPieceBoard[0][2] = 7;
                nextPieceBoard[1][0] = 7;
                nextPieceBoard[1][1] = 7;
                break;
        }
        render();
    }

    /**
     * metoda na vykreslenie jednej kocky
     *
     * @param xPosition x pozicia laveho dolneho rohu
     * @param yPosition y pozicia laveho dolneho rohu
     * @param width sirka prednej strany kocky
     * @param color farba kocky
     */
    public void drawCube(int xPosition, int yPosition, int width, int color) {
        //dlzkaBocnychStran = (int)(width / 4);
        texture[color].bind();

        //vykreslenie kocky
        glBegin(GL_QUADS);
        //predna strana
        glVertex2f(xPosition, yPosition);
        glTexCoord2f(0, 0);
        glVertex2f(xPosition, yPosition + width);
        glTexCoord2f(1, 0);
        glVertex2f(xPosition + width, yPosition + width);
        glTexCoord2f(1, 1);
        glVertex2f(xPosition + width, yPosition);
        glTexCoord2f(0, 1);

        //horna strana
        glVertex2f(xPosition, yPosition + width);
        glTexCoord2f(0, 0);
        glVertex2f(xPosition + width, yPosition + width);
        glTexCoord2f(1, 0);
        glVertex2f(xPosition + width + dlzkaBocnychStran, yPosition + width + dlzkaBocnychStran);
        glTexCoord2f(1, 1);
        glVertex2f(xPosition + dlzkaBocnychStran, yPosition + width + dlzkaBocnychStran);
        glTexCoord2f(0, 1);

        //prava bocna strana
        glVertex2f(xPosition + width, yPosition);
        glTexCoord2f(0, 0);
        glVertex2f(xPosition + width, yPosition + width);
        glTexCoord2f(1, 0);
        glVertex2f(xPosition + width + dlzkaBocnychStran, yPosition + width + dlzkaBocnychStran);
        glTexCoord2f(1, 1);
        glVertex2f(xPosition + width + dlzkaBocnychStran, yPosition + dlzkaBocnychStran);
        glTexCoord2f(0, 1);
        glEnd();

        texture[8].bind();
        glLineWidth(2.5f);
        //vykreslenie ciar
        glBegin(GL_LINES);
        //obvod prednej strany
        glVertex2f(xPosition, yPosition);
        glVertex2f(xPosition, yPosition + width);
        glVertex2f(xPosition, yPosition + width);
        glVertex2f(xPosition + width, yPosition + width);
        glVertex2f(xPosition + width, yPosition + width);
        glVertex2f(xPosition + width, yPosition);
        glVertex2f(xPosition, yPosition);
        glVertex2f(xPosition + width, yPosition);

        //obvod hornej strany
        glVertex2f(xPosition, yPosition + width);
        glVertex2f(xPosition + width, yPosition + width);
        glVertex2f(xPosition + width, yPosition + width);
        glVertex2f(xPosition + width + dlzkaBocnychStran, yPosition + width + dlzkaBocnychStran);
        glVertex2f(xPosition + width + dlzkaBocnychStran, yPosition + width + dlzkaBocnychStran);
        glVertex2f(xPosition + dlzkaBocnychStran, yPosition + width + dlzkaBocnychStran);
        glVertex2f(xPosition, yPosition + width);
        glVertex2f(xPosition + dlzkaBocnychStran, yPosition + width + dlzkaBocnychStran);

        //obvod pravej bocnej strany
        glVertex2f(xPosition + width, yPosition);
        glVertex2f(xPosition + width, yPosition + width);
        glVertex2f(xPosition + width, yPosition + width);
        glVertex2f(xPosition + width + dlzkaBocnychStran, yPosition + width + dlzkaBocnychStran);
        glVertex2f(xPosition + width + dlzkaBocnychStran, yPosition + width + dlzkaBocnychStran);
        glVertex2f(xPosition + width + dlzkaBocnychStran, yPosition + dlzkaBocnychStran);
        glVertex2f(xPosition + width, yPosition);
        glVertex2f(xPosition + width + dlzkaBocnychStran, yPosition + dlzkaBocnychStran);
        glEnd();
    }

    /**
     * metoda na vykreslenie hracej plochy
     * v tejto metode sa vykresli komplet cela hracia plocha so vsetkymi texturami
     */
    public void drawPlayground() {
        //drae background
        texture[0].bind();
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

        //draw grey place in playground
        texture[10].bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0, 1);
        glVertex2f(Display.getWidth() / 3 - 5 + dlzkaBocnychStran, 50 + dlzkaBocnychStran);
        glTexCoord2f(1, 1);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 5, 50);
        glTexCoord2f(1, 0);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 30, Display.getHeight() - 45);
        glTexCoord2f(0, 0);
        glVertex2f(Display.getWidth() / 3 - 5 + dlzkaBocnychStran, Display.getHeight() - 45);
        glEnd();

        //draw bricks arround playground
        texture[9].bind();
        glBegin(GL_QUADS);
        //left
        glTexCoord2f(0, 0);
        glVertex2f(Display.getWidth() / 3 - 30, 50);
        glTexCoord2f(1, 0);
        glVertex2f(Display.getWidth() / 3 - 5, 50);
        glTexCoord2f(1, 15);
        glVertex2f(Display.getWidth() / 3 - 5, Display.getHeight() - 50);
        glTexCoord2f(0, 15);
        glVertex2f(Display.getWidth() / 3 - 30, Display.getHeight() - 50);

        glTexCoord2f(0, 0);
        glVertex2f(Display.getWidth() / 3 - 5, 50);
        glTexCoord2f(1, 0);
        glVertex2f(Display.getWidth() / 3 - 5 + dlzkaBocnychStran, 50 + dlzkaBocnychStran);
        glTexCoord2f(1, 15);
        glVertex2f(Display.getWidth() / 3 - 5 + dlzkaBocnychStran, Display.getHeight() - 50 + dlzkaBocnychStran);
        glTexCoord2f(0, 15);
        glVertex2f(Display.getWidth() / 3 - 5, Display.getHeight() - 50);

        glTexCoord2f(0, 0);
        glVertex2f(Display.getWidth() / 3 - 30, Display.getHeight() - 50);
        glTexCoord2f(1, 0);
        glVertex2f(Display.getWidth() / 3 - 5, Display.getHeight() - 50);
        glTexCoord2f(1, 15);
        glVertex2f(Display.getWidth() / 3 - 5 + dlzkaBocnychStran, Display.getHeight() - 50 + dlzkaBocnychStran);
        glTexCoord2f(0, 15);
        glVertex2f(Display.getWidth() / 3 - 30 + dlzkaBocnychStran, Display.getHeight() - 50 + dlzkaBocnychStran);

        //bottom
        glTexCoord2f(0, 0);
        glVertex2f(Display.getWidth() / 3 - 30, 25);
        glTexCoord2f(10, 0);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 30, 25);
        glTexCoord2f(10, 1);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 30, 50);
        glTexCoord2f(0, 1);
        glVertex2f(Display.getWidth() / 3 - 30, 50);

        glTexCoord2f(0, 0);
        glVertex2f(Display.getWidth() / 3 - 5, 50);
        glTexCoord2f(10, 0);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 5, 50);
        glTexCoord2f(10, 1);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 5 + dlzkaBocnychStran, 50 + dlzkaBocnychStran);
        glTexCoord2f(0, 1);
        glVertex2f(Display.getWidth() / 3 - 5 + dlzkaBocnychStran, 50 + dlzkaBocnychStran);

        //right
        glTexCoord2f(0, 0);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 5, 50);
        glTexCoord2f(1, 0);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 30, 50);
        glTexCoord2f(1, 15);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 30, Display.getHeight() - 50);
        glTexCoord2f(0, 15);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 5, Display.getHeight() - 50);

        glTexCoord2f(0, 0);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 30, 25);
        glTexCoord2f(1, 0);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 30 + dlzkaBocnychStran, 25 + dlzkaBocnychStran);
        glTexCoord2f(1, 15);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 30 + dlzkaBocnychStran, Display.getHeight() - 50 + dlzkaBocnychStran);
        glTexCoord2f(0, 15);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 30, Display.getHeight() - 50);

        glTexCoord2f(0, 0);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 5, Display.getHeight() - 50);
        glTexCoord2f(1, 0);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 30, Display.getHeight() - 50);
        glTexCoord2f(1, 15);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 30 + dlzkaBocnychStran, Display.getHeight() - 50 + dlzkaBocnychStran);
        glTexCoord2f(0, 15);
        glVertex2f(Display.getWidth() / 3 + width * getCubeWidth() + dlzkaBocnychStran + 5 + dlzkaBocnychStran, Display.getHeight() - 50 + dlzkaBocnychStran);
        glEnd();
    }

    /**
     * metoda na vymazanie celeho riadku, ak je zaplneny. Rekurzivne sa
     * prehladava cele hracie pole a hladaju sa plne riadky
     *
     * @param actLine aktualna pozicia riadku
     */
    private void removeFullLines(int actLine) {
        for (int i = actLine - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                if (board[i][j] == 0) {
                    break;
                }
                if (j == width - 1) {
                    for (int y = i; y > 0; y--) {
                        System.arraycopy(board[y - 1], 0, board[y], 0, width);
                    }
                    score += 100;
                    removeFullLines(i + 1); //lebo ak sa vymaze jeden plny riadok, moze sa nahradit opat plnym riadkom
                }
            }
        }
    }

    /**
     * metoda na posunutie tvaru o jeden riadok nizsie
     *
     * @return true ak sa tvar posunul, false ak sa neposunul lebo uz je
     * najnizsie ako moze byt
     */
    private boolean oneLineDown() {
        return curPiece.oneLineDown(this);
    }

    /**
     * metoda na prekreslenie okna
     */
    public void render() {
        GL11.glTranslatef(Display.getWidth() / 3, 50, 0);
        for (int i = height - 1; i >= 0; --i) {
            for (int j = 0; j < width; ++j) {
                if (board[i][j] != 0) {
                    drawCube(j * getCubeWidth(), Display.getHeight() - 135 - i * getCubeWidth(), getCubeWidth(), board[i][j]);
                }
            }
        }
        GL11.glTranslatef(-(Display.getWidth() / 3), -50, 0);
        drawNextPiece();
        SimpleText.drawString("Score " + score, 550, 250);
        SimpleText.drawString("Level " + level, 550, 300);
        SimpleText.drawString("HighScore " + highScore, 550, 200);
    }

    /**
     * metoda na vykreslenie nasledujuceho tvaru
     */
    private void drawNextPiece() {
        GL11.glTranslatef(Display.getWidth() - 250, Display.getHeight() - 200, 0);
        SimpleText.drawString("Next Piece", 0, 110);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (nextPieceBoard[i][j] != 0) {
                    drawCube(j * getCubeWidth(), i * getCubeWidth(), getCubeWidth(), nextPieceBoard[i][j]);
                }
            }
        }
        GL11.glTranslatef(-(Display.getWidth() - 250), -(Display.getHeight() - 200), 0);
    }

    /**
     * metoda na update celej hry. Kontroluje sa cas za ktory ma spadnut jedna
     * kocka, kontroluje sa ci su zaplnene riadky, kontroluje sa ci nenastal
     * koniec hry.
     */
    public void update() {
        curTime = System.currentTimeMillis() - startTime;
        //prepocitanie casu aby kocky zastavovali na okamih podla dosiahnuteho levelu
        //na zaciatku sa ma cakat jednu sekundu
        //18 je maximalny cas trvanie jedneho casoveho cyklu po dosiahnutie tejto metody 
        //(aspon tak mi to testovanim vyslo :-D )
        if (curTime / 100 > 1 && curTime % delay < 17) {
            if (!oneLineDown()) {
                score += 15;
                removeFullLines(height);
                if (canCreate) {
                    clearNextBoard();
                    newPiece();
                } else {
                    score -= 15;
                    gameOver = true;
                    if (score > Integer.parseInt(highScore)) {
                        writeNewHighScore();
                    }
                }
            }
        }
        if (score / (100 * level) >= 1 && delay > 200) {
            level++;
            delay -= 100;
        }
    }

    /**
     * metoda na nacitanie high score zo suboru
     *
     * @exception v pripade ze sa nepodarilo nacitat subor vyhodi sa vynimka
     * FileNotFoundException
     */
    private void readHighScore() {
        try {
            highScore = new Scanner(new File("score.txt")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException ex) {
            highScore = "0";
        }
    }

    /**
     * metoda na zapisanie noveho high score do suboru
     *
     * @exception v pripade ze sa nepodarilo otvorit subor vyhodi sa vynimka
     * IOException
     */
    private void writeNewHighScore() {
        try {
            FileWriter fw = new FileWriter("score.txt");
            fw.write(Integer.toString(score));
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
