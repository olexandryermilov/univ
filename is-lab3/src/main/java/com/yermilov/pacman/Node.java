package com.yermilov.pacman;

public class Node implements Comparable<Node> {
    private int column;
    private int row;
    private int parentColumn;
    private int parentRow;
    private int distance;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getParentColumn() {
        return parentColumn;
    }

    public void setParentColumn(int parentColumn) {
        this.parentColumn = parentColumn;
    }

    public int getParentRow() {
        return parentRow;
    }

    public void setParentRow(int parentRow) {
        this.parentRow = parentRow;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public int compareTo(Node node) {
        if (node.getDistance() == this.getDistance()) {
            return 0;
        } else if (this.getDistance() > node.getDistance()) {
            return 1;
        }
        return -1;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Node)) {
            return false;
        }
        Node node = (Node) object;
        if (this.column == node.getColumn() && this.row == node.getRow()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.column;
        hash = 47 * hash + this.column;
        hash = 47 * hash + this.parentColumn;
        hash = 47 * hash + this.parentRow;
        hash = 47 * hash + this.distance;
        return hash;
    }

    @Override
    public String toString() {
        return "Row = " + getRow() + " " + "Column = " + getColumn();
    }
}
