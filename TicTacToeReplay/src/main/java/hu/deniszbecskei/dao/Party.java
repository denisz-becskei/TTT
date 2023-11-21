package hu.deniszbecskei.dao;

import java.time.LocalDate;

public class Party {
    private String id;
    private int tableSize;
    private Integer[][] moves;
    private LocalDate playedOn;

    public Party() {
    }

    public int getTableSize() {
        return tableSize;
    }

    public String getId() {
        return id;
    }

    public Coords getMove(int moveNumber) {
        for (int i = 0; i < tableSize; i++) {
            for (int j = 0; j < tableSize; j++) {
                if (moves[i][j] == moveNumber)
                    return new Coords(i, j);
            }
        }
        return null;
    }

    public int getNumberOfMoves() {
        int max = 0;
        for (int i = 0; i < tableSize; i++) {
            for (int j = 0; j < tableSize; j++) {
                if (moves[i][j] > max)
                    max = moves[i][j];
            }
        }
        return max;
    }

    public LocalDate getPlayedOn() {
        return playedOn;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    public void setMoves(Integer[][] moves) {
        this.moves = moves;
    }

    public void setPlayedOn(LocalDate playedOn) {
        this.playedOn = playedOn;
    }

    // For Debugging
    @Override
    public String toString() {
        StringBuilder movesAsString = new StringBuilder();
        for (int i = 0; i < this.tableSize; i++) {
            for (int j = 0; j < this.tableSize; j++) {
                movesAsString.append(this.moves[i][j]).append(" ");
            }
            movesAsString.append('\n');
        }

        return "Party{" +
                "id='" + id + '\'' +
                ", tableSize=" + tableSize +
                ", moves=\n" + movesAsString +
                ", playedOn=" + this.playedOn +
                '}';
    }
}
