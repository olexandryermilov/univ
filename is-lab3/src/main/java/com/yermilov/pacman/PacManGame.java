package com.yermilov.pacman;

import java.util.ArrayList;
import java.util.List;

public class PacManGame {
    private static final int SCORE = 10;
    private Player pacMan;
    private List<Player> ghosts = new ArrayList<>();
    private MovementEngine movementEngine;
    private int nDots;
    private Game game;
    private int totalScore;
    private NodeType[][] maze = {
        // 0
        {
            NodeType.DOT,
            //NodeType.GHOST,
                NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT,
                NodeType.DOT
                //NodeType.GHOST
        },
        // 1
        { NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL, NodeType.DOT,
            NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.DOT },
        // 2
        { NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL, NodeType.DOT,
            NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.DOT },
        // 3
        {
            NodeType.DOT,
            //NodeType.GHOST,
                NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT,
                NodeType.DOT,
                //NodeType.GHOST
        },
        // 4
        { NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.WALL,
            NodeType.WALL, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.DOT },
        // 5
        { NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT },
        // 6
        { NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL, NodeType.DOT,
            NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.DOT },
        // 7
        { NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.DOT },
        // 8
        { NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.DOT },
        // 9
        { NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.WALL,
            NodeType.WALL, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.DOT },
        // 10
        { NodeType.WALL, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.WALL,
            NodeType.WALL, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.WALL },
        // 11
        { NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.WALL,
            NodeType.WALL, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.DOT },
        // 12
        { NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.DOT },
        // 13
        { NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.WALL,
            NodeType.WALL, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.DOT },
        // 14
        { NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, },
        // 15
        { NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL, NodeType.DOT,
            NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.WALL, NodeType.DOT },
        // 16
        { NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.DOT },
        // 17
        { NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.DOT },
        // 18
        { NodeType.WALL, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.WALL,
            NodeType.WALL, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.WALL },
        // 19
        { NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.WALL,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.WALL, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, },
        // 20
        { NodeType.DOT, NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.WALL,
            NodeType.WALL, NodeType.WALL, NodeType.DOT, NodeType.WALL, NodeType.DOT,
            NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.WALL, NodeType.WALL,
            NodeType.WALL, NodeType.DOT },
        // 21
        { NodeType.PACMAN, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT, NodeType.DOT,
            NodeType.DOT, NodeType.DOT } };

    private NodeType[][] harderMaze = {{}};

    public PacManGame(Game game) {
        this.game = game;

        pacMan = new Player(Player.PlayerType.PACMAN);
        pacMan.setCurrentColumn(0);
        pacMan.setCurrentRow(maze.length - 1);
        pacMan.setPosition(Player.Position.RIGHT);
        pacMan.setNumDotsEaten(0);

        Node target = new Node();
        target.setColumn(10);
        target.setRow(maze.length - 1);

        movementEngine = new MovementEngine(pacMan, maze, target);

        start();
    }

    public void start() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == NodeType.DOT) {
                    nDots++;
                } else if (maze[i][j] == NodeType.GHOST) {
                    Player ghost = new Player(Player.PlayerType.GHOST);
                    ghost.setCurrentRow(i);
                    ghost.setCurrentColumn(j);
                    ghost.setPosition(Player.Position.RIGHT);
                    ghosts.add(ghost);
                }
            }
        }
    }

    public void movePacMan() {
        move(pacMan);
        totalScore = pacMan.getNumDotsEaten() * SCORE;
        if (pacMan.getNumDotsEaten() == nDots) {
            game.win();
        } else {
            if (pacMan.isDead()) {
                game.gameOver();
            }
        }
    }

    public void moveGhost() {
        for (Player ghost : ghosts) {
            move(ghost);
        }
    }

    private void move(Player player) {
        if (player.getPosition() == Player.Position.LEFT) {
            movementEngine.left(player);
        } else if (player.getPosition() == Player.Position.RIGHT) {
            movementEngine.right(player);
        } else if (player.getPosition() == Player.Position.UP) {
            movementEngine.up(player);
        } else if (player.getPosition() == Player.Position.DOWN) {
            movementEngine.down(player);
        }
    }

    public Player getPacMan() {
        return pacMan;
    }

    public NodeType[][] getMaze() {
        return maze;
    }

    public List<Player> getGhosts() {
        return ghosts;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
