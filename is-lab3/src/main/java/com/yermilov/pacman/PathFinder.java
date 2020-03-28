package com.yermilov.pacman;

import java.util.*;

public class PathFinder {
    private PathFinder() {
    }

    public static Player.Position getGhostNextPosition(Player pacMan, Player ghost, NodeType[][] maze) {
        Node startNode = new Node();
        Node targetNode = new Node();
        startNode.setColumn(ghost.getCurrentColumn());
        startNode.setRow(ghost.getCurrentRow());

        targetNode.setColumn(pacMan.getCurrentColumn());
        targetNode.setRow(pacMan.getCurrentRow());

        return calculateAStar(startNode, targetNode, maze);
    }

    public static Player.Position getNextPositionTo(Player pacMan, Node target, NodeType[][] maze) {
        Node startNode = new Node();
        startNode.setColumn(pacMan.getCurrentColumn());
        startNode.setRow(pacMan.getCurrentRow());
        if (startNode.equals(target))
            target = getAdjacentNodes(target.getColumn(), target.getRow(), maze).get(0);
        //return calculateAStar(startNode, target, maze);
        //return calculateBFS(startNode, target, maze).get(0);
        //return calculateHeuristic(startNode, target, maze);
        return calculateDfs(startNode, target, maze);
    }

    public static Player.Position calculateAStar(Node startNode, Node targetNode, NodeType[][] maze) {
        List<Node> nodesToVisit = new ArrayList<>();
        List<Node> visitedNodes = new ArrayList<>();
        long start = System.currentTimeMillis();
        nodesToVisit.add(startNode);
        while (nodesToVisit.size() > 0 && !visitedNodes.contains(targetNode)) {
            Node currentNode = getLowestDistanceNode(nodesToVisit);
            visitedNodes.add(currentNode);
            nodesToVisit.remove(currentNode);

            List<Node> adjacentNodes = getAdjacentNodes(currentNode.getColumn(), currentNode.getRow(), maze);
            for (Node adjacentNode : adjacentNodes) {
                if (!visitedNodes.contains(adjacentNode)) {
                    adjacentNode.setParentColumn(currentNode.getColumn());
                    adjacentNode.setParentRow(currentNode.getRow());
                    if (!nodesToVisit.contains(adjacentNode)) {
                        adjacentNode.setDistance(calculateDistance(adjacentNode.getColumn(), adjacentNode.getRow(), targetNode.getColumn(), targetNode.getRow()));
                        nodesToVisit.add(adjacentNode);
                    }
                }
            }
        }
        System.out.println("Visited = " + visitedNodes.size() + ", time = " + (System.currentTimeMillis() - start));
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println(usedBytes / 1048576);
        return getNextPosition(startNode, getNextMove(startNode, targetNode, visitedNodes));
    }

    public static Player.Position calculateDfs(Node startNode, Node targetNode, NodeType[][] maze) {
        Node[] stack = new Node[maze.length * maze[0].length * 100];
        Player.Position[][] directions = new Player.Position[maze.length][maze[0].length];
        for (int i = 0; i < directions.length; i++) {
            Arrays.fill(directions[i], null);
        }
        long start = System.currentTimeMillis();
        Set<Node> visitedNodes = new HashSet<>();
        int h = 1;
        stack[0] = startNode;
        int visited = 1;
        visitedNodes.add(startNode);
        while (h > 0) {
            visited++;
            Node currentNode = stack[h - 1];
            h--;
            if (!visitedNodes.contains(currentNode)) {
                visitedNodes.add(currentNode);
                if (currentNode == targetNode) {
                    System.out.println("Visited = " + visited + ", time = " + (System.currentTimeMillis() - start));
                    long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    System.out.println(usedBytes / 1048576);
                    return directions[targetNode.getRow()][targetNode.getColumn()];
                }
            }
            List<Node> adjacentNodes = getAdjacentNodes(currentNode.getColumn(), currentNode.getRow(), maze);
            for (Node adjacentNode : adjacentNodes) {
                if (!visitedNodes.contains(adjacentNode)) {
                    stack[h++] = adjacentNode;
                    Player.Position direction = directions[currentNode.getRow()][currentNode.getColumn()];
                    if (direction == null) {
                        direction = getNextPosition(currentNode, adjacentNode);
                    }
                    directions[adjacentNode.getRow()][adjacentNode.getColumn()] = direction;
                    visited++;
                    if (adjacentNode == targetNode) {
                        System.out.println("Visited = " + visited + ", time = " + (System.currentTimeMillis() - start));
                        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        System.out.println(usedBytes / 1048576);
                        return directions[targetNode.getRow()][targetNode.getColumn()];
                    }
                }
            }

        }
        System.out.println("Visited = " + visited + ", time = " + (System.currentTimeMillis() - start));
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println(usedBytes / 1048576);
        return directions[targetNode.getRow()][targetNode.getColumn()];
    }

    static int distance(Node a, Node b) {
        return Math.abs(a.getColumn() - b.getColumn()) + Math.abs(a.getRow() - b.getRow());
    }

    private static Player.Position calculateHeuristic(Node startNode, Node targetNode, NodeType[][] maze) {
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(o -> distance(o, targetNode)));
        queue.add(startNode);
        Node[][] cameFrom = new Node[maze.length][maze[0].length];
        for (int i = 0; i < cameFrom.length; i++) {
            for (int j = 0; j < cameFrom[i].length; j++) {
                cameFrom[i][j] = null;
            }
        }
        int queueSize = 1;
        cameFrom[startNode.getRow()][startNode.getColumn()] = startNode;
        long start = System.currentTimeMillis();
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.equals(targetNode)) {
                break;
            }

            for (Node node : getAdjacentNodes(current.getColumn(), current.getRow(), maze)) {
                int r = node.getRow();
                int c = node.getColumn();
                if (cameFrom[r][c] == null) {
                    queue.add(node);
                    queueSize++;
                    cameFrom[r][c] = current;
                }
            }
        }
        Node curr = targetNode;
        Player.Position position = null;
        while (!curr.equals(startNode)) {
            Node prevNode = cameFrom[curr.getRow()][curr.getColumn()];
            position = getNextPosition(prevNode, curr);
            curr = prevNode;
        }
        System.out.println("Visited = " + queueSize + ", time = " + (System.currentTimeMillis() - start));
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println(usedBytes / 1048576);
        return position;
    }

    public static List<Player.Position> calculateBFS(Node startNode, Node targetNode, NodeType[][] maze) {
        List<Node> queue = new ArrayList<>();
        List<List<Player.Position>> directions = new ArrayList<>();
        int[][] costs = new int[maze.length][maze[0].length];
        for (int i = 0; i < costs.length; i++) {
            for (int j = 0; j < costs[i].length; j++) {
                costs[i][j] = Integer.MAX_VALUE;
            }
        }
        long start = System.currentTimeMillis();
        costs[startNode.getRow()][startNode.getColumn()] = 0;
        queue.add(startNode);
        directions.add(new ArrayList<>());
        int h = 0;
        int t = 1;
        while (h < t) {
            Node head = queue.get(h);
            List<Player.Position> path = directions.get(h);
            h++;
            List<Node> adjacentNodes = getAdjacentNodes(head.getColumn(), head.getRow(), maze);
            for (Node node : adjacentNodes) {
                List<Player.Position> newPath = new ArrayList<>(path);
                if (costs[node.getRow()][node.getColumn()] > costs[head.getRow()][head.getColumn()] + 1) {
                    Player.Position nextPosition = getNextPosition(head, node);
                    newPath.add(nextPosition);
                    queue.add(node);
                    t++;
                    directions.add(newPath);
                    costs[node.getRow()][node.getColumn()] = costs[head.getRow()][head.getColumn()] + 1;
                }
                if (node.equals(targetNode)) {
                    System.out.println("Visited = " + queue.size() + ", time = " + (System.currentTimeMillis() - start));
                    long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    System.out.println(usedBytes / 1048576);
                    return newPath;
                }
            }
        }
        return null;
    }

    private static Player.Position getNextPosition(Node startNode, Node nextNode) {
        Player.Position position = null;
        if (nextNode != null) {
            if (startNode.getColumn() > nextNode.getColumn()) {
                position = Player.Position.LEFT;
            } else if (startNode.getColumn() < nextNode.getColumn()) {
                position = Player.Position.RIGHT;
            } else if (startNode.getRow() > nextNode.getRow()) {
                position = Player.Position.UP;
            } else if (startNode.getRow() < nextNode.getRow()) {
                position = Player.Position.DOWN;
            }
        }
        return position;
    }

    private static Node getNextMove(Node startNode, Node targetNode, List<Node> visitedNodes) {
        int column = targetNode.getColumn();
        int row = targetNode.getRow();
        Node node;
        while ((node = findNode(column, row, visitedNodes)) != null) {
            if (node.getParentColumn() == startNode.getColumn()
                    && node.getParentRow() == startNode.getRow()) {
                break;
            } else {
                column = node.getParentColumn();
                row = node.getParentRow();
            }
        }
        return node;
    }

    private static Node findNode(int column, int row, List<Node> visitedNodes) {
        for (Node node : visitedNodes) {
            if (node.getColumn() == column && node.getRow() == row) {
                return node;
            }
        }
        return null;
    }

    private static List<Node> getAdjacentNodes(int column, int row, NodeType[][] maze) {
        List<Node> nodeList = new ArrayList<>();
        if (column + 1 < maze[row].length && !isBlockedTerrain(column + 1, row, maze)) {
            Node node = new Node();
            node.setColumn(column + 1);
            node.setRow(row);
            nodeList.add(node);
        }

        if (column - 1 >= 0 && !isBlockedTerrain(column - 1, row, maze)) {
            Node node = new Node();
            node.setColumn(column - 1);
            node.setRow(row);
            nodeList.add(node);
        }

        if (row + 1 < maze.length && !isBlockedTerrain(column, row + 1, maze)) {
            Node node = new Node();
            node.setColumn(column);
            node.setRow(row + 1);
            nodeList.add(node);
        }

        if (row - 1 >= 0 && !isBlockedTerrain(column, row - 1, maze)) {
            Node node = new Node();
            node.setColumn(column);
            node.setRow(row - 1);
            nodeList.add(node);
        }
        Collections.shuffle(nodeList);
        return nodeList;
    }

    private static boolean isBlockedTerrain(int column, int row, NodeType[][] maze) {
        boolean blocked = false;
        try {
            if (maze[row][column] == NodeType.WALL) {
                blocked = true;
            }
        } catch (Exception ex) {
            //blocked = true;
        }
        return blocked;
    }

    private static int calculateDistance(int srcColumn, int srcRow, int destColumn, int destRow) {
        return (Math.abs(srcColumn - destColumn)) + (Math.abs(srcRow - destRow));
    }

    private static Node getLowestDistanceNode(List<Node> nodesToVisit) {
        Node lowestDistanceNode = null;
        if (nodesToVisit.size() > 0) {
            lowestDistanceNode = nodesToVisit.get(0);
            for (int i = 1; i < nodesToVisit.size(); i++) {
                Node n = nodesToVisit.get(i);
                if (n.getDistance() < lowestDistanceNode.getDistance()) {
                    lowestDistanceNode = n;
                }
            }
        }
        return lowestDistanceNode;
    }
}
