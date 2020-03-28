package com.yermilov.pacman;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MazeBuilder {
    private static final int START_X = 4;
    private static final int START_Y = 50;
    private int blockHeight = 0;
    private int blockWidth = 0;
    private static final int DOT_HEIGHT = 3;
    private static final int DOT_WIDTH = 3;
    private static int NUM_X_BLOCK = 17;
    private static int NUM_Y_BLOCK = 23;
    private Graphics graphics;
    private Image pacManWholeImage;
    private Image pacManRightImage;
    private Image pacManLeftImage;
    private Image pacManUpImage;
    private Image pacManDownImage;
    private Image ghostImageLeft;
    // private Image ghostImageRight;
    private Player pacMan;
    private List<Player> ghosts;

    public MazeBuilder(int width, int height, Player pacMan, List<Player> ghosts) {
        this.blockWidth = Math.abs(width / NUM_X_BLOCK);
        this.blockHeight = Math.abs(height / NUM_Y_BLOCK - 1);
        this.pacMan = pacMan;
        this.ghosts = ghosts;

        try {
            if (pacManWholeImage == null) {
                pacManWholeImage = ImageIO.read(getClass().getResource(
                    "/res/PacMan1.gif"));
                pacManRightImage = ImageIO.read(getClass().getResource(
                    "/res/PacMan3right.gif"));
                pacManLeftImage = ImageIO.read(getClass().getResource(
                    "/res/PacMan3left.gif"));
                pacManUpImage = ImageIO
                    .read(getClass().getResource("/res/PacMan3up.gif"));
                pacManDownImage = ImageIO.read(getClass().getResource(
                    "/res/PacMan3down.gif"));
                ghostImageLeft = ImageIO.read(getClass().getResource("/res/Ghost2.gif"));
                // ghostImageRight = ImageIO.read(getClass().getResource("/res/Ghost1.gif"));
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void draw(NodeType[][] maze) {
        int y = START_Y;
        for (int i = 0; i < maze.length; i++) {
            int x = START_X;
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == NodeType.WALL) {
                    getGraphics().setColor(Color.BLUE);
                    getGraphics().fillRect(x, y, blockWidth, blockHeight);
                } else if (maze[i][j] == NodeType.DOT) {
                    getGraphics().setColor(Color.BLACK);
                    getGraphics().fillRect(x, y, blockWidth, blockHeight);
                    getGraphics().setColor(Color.YELLOW);
                    getGraphics().fillRect(x + (blockWidth / 2), y + (blockHeight / 2),
                        DOT_WIDTH, DOT_HEIGHT);
                } else if (maze[i][j] == NodeType.PACMAN) {
                    getGraphics().setColor(Color.BLACK);
                    getGraphics().fillRect(x, y, blockWidth, blockHeight);
                    getGraphics().setColor(Color.YELLOW);
                    animate(getGraphics(), pacMan, x, y);
                } else if (maze[i][j] == NodeType.GHOST) {
                    getGraphics().setColor(Color.BLACK);
                    getGraphics().fillRect(x, y, blockWidth, blockHeight);
                    getGraphics().setColor(Color.YELLOW);
                    for (Player ghost : ghosts) {
                        animate(getGraphics(), ghost, x, y);
                    }
                } else if (maze[i][j] == NodeType.BLANK) {
                    getGraphics().setColor(Color.BLACK);
                    getGraphics().fillRect(x, y, blockWidth, blockHeight);
                }
                x += blockWidth;
            }
            y += blockHeight;
        }
    }

    private void animate(Graphics graphics, Player player, int x, int y) {
        Image image = null;
        if (player.getPlayerType() == Player.PlayerType.PACMAN) {
            if (player.getPosition() == Player.Position.LEFT) {
                image = pacManLeftImage;
            } else if (player.getPosition() == Player.Position.RIGHT) {
                image = pacManRightImage;
            } else if (player.getPosition() == Player.Position.UP) {
                image = pacManUpImage;
            } else if (player.getPosition() == Player.Position.DOWN) {
                image = pacManDownImage;
            }
        } else if (player.getPlayerType() == Player.PlayerType.GHOST) {
            image = ghostImageLeft;
        }

        if (player.getPosition() == Player.Position.RIGHT) {
            graphics.drawImage(image, x, y, blockWidth, blockHeight, null);
        } else if (player.getPosition() == Player.Position.LEFT) {
            graphics.drawImage(image, x, y, blockWidth, blockHeight, null);
        } else if (player.getPosition() == Player.Position.DOWN) {
            graphics.drawImage(image, x, y, blockWidth, blockHeight, null);
        } else if (player.getPosition() == Player.Position.UP) {
            graphics.drawImage(image, x, y, blockWidth, blockHeight, null);
        }
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }
}
