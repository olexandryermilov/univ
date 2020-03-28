package com.yermilov.pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;


public class UI extends JFrame implements Game {
    private static final long serialVersionUID = 1L;
    private static int FRAME_HEIGHT = 500;
    private static int FRAME_WIDTH = 400;
    private boolean gameOver = false;
    private boolean win = false;
    private PacManGame pacManGame;
    private MazeBuilder mazeBuilder;
    private Timer pacManTimer;
    private int pacManTimerDelay = 180;
    private Timer ghostTimer;
    private int ghostTimerDelay = 280;
    private Image buffer;

    @Override
    public void gameOver() {
        gameOver = true;
        ghostTimer.stop();
        pacManTimer.stop();
    }

    @Override
    public void win() {
        win = true;
        ghostTimer.stop();
        pacManTimer.stop();
    }

    class PacManActionListener implements ActionListener {
        private PacManGame pacManGame;

        public PacManActionListener(PacManGame pacManGame) {
            this.pacManGame = pacManGame;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            pacManGame.movePacMan();
            repaint();
        }
    }

    class GhostActionListener implements ActionListener {
        private PacManGame pacManGame;

        public GhostActionListener(PacManGame pacManGame) {
            this.pacManGame = pacManGame;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            pacManGame.moveGhost();
            repaint();
        }
    }

    public UI() {
        init();
    }

    public void init() {
        setTitle("Pac-Man");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        //addKeyListener(new PacManKeyListener());
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        pacManGame = new PacManGame(this);
        mazeBuilder = new MazeBuilder(getWidth(), getHeight(), pacManGame.getPacMan(),
            pacManGame.getGhosts());
        pacManTimer = new Timer(pacManTimerDelay, new PacManActionListener(pacManGame));
        ghostTimer = new Timer(ghostTimerDelay, new GhostActionListener(pacManGame));
        ghostTimer.start();
        pacManTimer.start();
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics g = buffer.getGraphics();
        drawScore(g);
        drawMaze(g);
        if (gameOver) {
            g.setColor(Color.YELLOW);
            g.drawString("GAME OVER!", FRAME_WIDTH / 2 - ("GAME OVER!".length()) - 26,
                FRAME_HEIGHT / 2);
        } else {
            if (win) {
                g.setColor(Color.YELLOW);
                g.drawString("YOU WIN!", FRAME_WIDTH / 2 - ("WIN!".length()) - 24,
                    FRAME_HEIGHT / 2);
            }
        }
        graphics.drawImage(buffer, 0, 0, this);
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, getWidth(), getHeight());
        int totalScore = pacManGame.getTotalScore();
        g.setColor(Color.YELLOW);
        g.drawString("Score: " + totalScore, 10, 45);
    }

    private void drawMaze(Graphics g) {
        mazeBuilder.setGraphics(g);
        mazeBuilder.draw(pacManGame.getMaze());
    }

    @Override
    public void update(Graphics graphics) {
        super.update(graphics);
    }

    public class PacManKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!win && !gameOver) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    pacManGame.getPacMan().setPosition(Player.Position.LEFT);
                    if (!pacManTimer.isRunning()) {
                        pacManTimer.start();
                    }
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    pacManGame.getPacMan().setPosition(Player.Position.RIGHT);
                    if (!pacManTimer.isRunning()) {
                        pacManTimer.start();
                    }
                } else if (keyCode == KeyEvent.VK_UP) {
                    pacManGame.getPacMan().setPosition(Player.Position.UP);
                    if (!pacManTimer.isRunning()) {
                        pacManTimer.start();
                    }
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    pacManGame.getPacMan().setPosition(Player.Position.DOWN);
                    if (!pacManTimer.isRunning()) {
                        pacManTimer.start();
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            UI pacMan = new UI();
            pacMan.setVisible(true);
        });
    }
}
