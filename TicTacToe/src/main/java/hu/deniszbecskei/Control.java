package hu.deniszbecskei;

import hu.deniszbecskei.dao.Party;
import hu.deniszbecskei.dao.PartyDAOImpl;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Random;

import static hu.deniszbecskei.App.*;

public class Control {

    private final Rectangle winnerXimg, winnerOimg, borderO, borderX;
    private final PartyDAOImpl partyDAO = new PartyDAOImpl();

    public Control(Rectangle winnerXimg, Rectangle winnerOimg, Rectangle borderO, Rectangle borderX) {
        this.winnerXimg = winnerXimg;
        this.winnerOimg = winnerOimg;
        this.borderO = borderO;
        this.borderX = borderX;
    }

    @MethodClarification(
            methodName = "giveVictory",
            parameters = "who: who won, why: why the player won",
            returnType = "void",
            additionalInfo = "Initiating win sequence"
    )
    protected void giveVictory(char who, String why, char... extra) {
        isPlaying = false;
        this.borderX.setOpacity(0);
        this.borderO.setOpacity(0);

        if (isTimerRunning) {
            movingTimer.cancel();
            endTimer.cancel();
        }
        if (isAITimerRunning) {
            aiTimer.cancel();
        }

        System.out.println(who + " won! Cause: \"" + why + "\"");
        if (who != '-') {
            victoryLabel.setText(who + " nyert! " + why);
        }

        switch (who) {
            case 'X':
                this.winnerXimg.setOpacity(1);
                victoryLabel.setTextFill(Color.RED);
                break;
            case 'O':
                this.winnerOimg.setOpacity(1);
                victoryLabel.setTextFill(Color.LIME);
                break;
            default:
                victoryLabel.setTextFill(Color.GRAY);
                break;
        }

        if (extra.length != 0) {
            switch (extra[0]) {
                case 'X':
                    this.winnerXimg.setOpacity(1);
                    victoryLabel.setTextFill(Color.RED);
                    break;
                case 'O':
                    this.winnerOimg.setOpacity(1);
                    victoryLabel.setTextFill(Color.LIME);
                    break;
                default:
                    victoryLabel.setTextFill(Color.GRAY);
                    break;
            }
        }
        victoryLabel.setOpacity(1);

        btnSaveParty.setDisable(false);
        btnSaveParty.setOpacity(1);
    }

    protected String saveMatch() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            for (int j = 0; j < TABLE_SIZE; j++) {
                System.out.print(whenTheMoveHappened[i][j] + " ");
            }
            System.out.println();
        }

        Party party = new Party(TABLE_SIZE, whenTheMoveHappened);
        partyDAO.save(party);
        return party.getId();
    }

    @MethodClarification(
            methodName = "checkHorizontal",
            parameters = "void",
            returnType = "char",
            additionalInfo = "checking if someone won in the horizontals"
    )
    protected char checkHorizontal() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            for (int j = 0; j < TABLE_SIZE; j++) {
                int count = 0;
                if (cellsCheck[i][j] == ' ') {
                    continue;
                } else if (cellsCheck[i][j] == 'X') {
                    count++;
                    try {
                        for (int k = 1; k < 5; k++) {
                            if (cellsCheck[i][j + k] == 'X') {
                                count++;
                            }
                        }
                    } catch (IndexOutOfBoundsException | NullPointerException e) {
                        break;
                    }
                    if (count >= 5) {
                        return 'X';
                    }
                } else {
                    count++;
                    try {
                        for (int k = 1; k < 5; k++) {
                            if (cellsCheck[i][j + k] == 'O') {
                                count++;
                            }
                        }
                    } catch (IndexOutOfBoundsException | NullPointerException e) {
                        break;
                    }
                    if (count >= 5) {
                        return 'O';
                    }
                }
            }
        }
        return '§';
    }

    @MethodClarification(
            methodName = "checkVertical",
            parameters = "void",
            returnType = "char",
            additionalInfo = "checking if someone won in the verticals"
    )
    protected char checkVertical() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            for (int j = 0; j < TABLE_SIZE; j++) {
                int count = 0;
                if (cellsCheck[j][i] == ' ') {
                    continue;
                } else if (cellsCheck[j][i] == 'X') {
                    count++;
                    try {
                        for (int k = 1; k < 5; k++) {
                            if (cellsCheck[j + k][i] == 'X') {
                                count++;
                            }
                        }
                    } catch (IndexOutOfBoundsException | NullPointerException e) {
                        break;
                    }
                    if (count >= 5) {
                        return 'X';
                    }
                } else {
                    count++;
                    try {
                        for (int k = 1; k < 5; k++) {
                            if (cellsCheck[j + k][i] == 'O') {
                                count++;
                            }
                        }
                    } catch (IndexOutOfBoundsException | NullPointerException e) {
                        break;
                    }
                    if (count >= 5) {
                        return 'O';
                    }
                }
            }
        }
        return '§';
    }

    @MethodClarification(
            methodName = "checkLeftRightDiag",
            parameters = "void",
            returnType = "char",
            additionalInfo = "checking if someone won in the diagonals going from left to right"
    )
    protected char checkLeftRightDiag() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            for (int j = 0; j < TABLE_SIZE; j++) {
                int count = 0;
                if (cellsCheck[j][i] == ' ') {
                    continue;
                } else if (cellsCheck[j][i] == 'X') {
                    count++;
                    try {
                        for (int k = 1; k < 5; k++) {
                            if (cellsCheck[j + k][i + k] == 'X') {
                                count++;
                            }
                        }
                    } catch (IndexOutOfBoundsException | NullPointerException e) {
                        break;
                    }
                    if (count >= 5) {
                        return 'X';
                    }
                } else {
                    count++;
                    try {
                        for (int k = 1; k < 5; k++) {
                            if (cellsCheck[j + k][i + k] == 'O') {
                                count++;
                            }
                        }
                    } catch (IndexOutOfBoundsException | NullPointerException e) {
                        break;
                    }
                    if (count >= 5) {
                        return 'O';
                    }
                }
            }
        }
        return '§';
    }

    @MethodClarification(
            methodName = "checkRightLeftDiag",
            parameters = "void",
            returnType = "char",
            additionalInfo = "checking if someone won in the diagonals going from right to left"
    )
    protected char checkRightLeftDiag() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            for (int j = 0; j < TABLE_SIZE; j++) {
                int count = 0;
                if (cellsCheck[j][i] == ' ') {
                    continue;
                } else if (cellsCheck[j][i] == 'X') {
                    count++;
                    try {
                        for (int k = 1; k < 5; k++) {
                            if (cellsCheck[j + k][i - k] == 'X') {
                                count++;
                            }
                        }
                    } catch (IndexOutOfBoundsException | NullPointerException e) {
                        break;
                    }
                    if (count >= 5) {
                        return 'X';
                    }
                } else {
                    count++;
                    try {
                        for (int k = 1; k < 5; k++) {
                            if (cellsCheck[j + k][i - k] == 'O') {
                                count++;
                            }
                        }
                    } catch (IndexOutOfBoundsException | NullPointerException e) {
                        break;
                    }
                    if (count >= 5) {
                        return 'O';
                    }
                }
            }
        }
        return '§';
    }

    @MethodClarification(
            methodName = "checkForFullBoard",
            parameters = "void",
            returnType = "boolean",
            additionalInfo = "Checks if the players filled the entire board"
    )
    protected boolean checkForFullBoard() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            for (int j = 0; j < TABLE_SIZE; j++) {
                if (cellsCheck[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    @MethodClarification(
            methodName = "limitValue",
            parameters = "lower: lower limit (the value to limit the value if not in range, upper: upper limit, value: value to limit",
            returnType = "float",
            additionalInfo = "none"
    )
    protected float limitValue(float lower, float upper, float value) {
        if (value >= lower && value < upper) {
            return value;
        } else {
            return lower;
        }
    }

    @MethodClarification(
            methodName = "randomTuple",
            parameters = "coords: tuple of random coords or null",
            returnType = "Tuple<Integer>",
            additionalInfo = "recursive function to generate random table coordinates"
    )
    private Tuple<Integer> randomTuple(Tuple<Integer> coords) {
        if (coords == null) {
            Random r = new Random();
            return randomTuple(new Tuple<>(r.nextInt(TABLE_SIZE), r.nextInt(TABLE_SIZE)));
        } else if (cellsCheck[coords.get(0)][coords.get(1)] == ' ') {
            return coords;
        } else {
            Random r = new Random();
            return randomTuple(new Tuple<>(r.nextInt(TABLE_SIZE), r.nextInt(TABLE_SIZE)));
        }
    }

    @MethodClarification(
            methodName = "AIStep",
            parameters = "void",
            returnType = "void",
            additionalInfo = "make a random step"
    )
    protected void AIStep() {
        Tuple<Integer> aiStepLocation = randomTuple(null);
        cells[aiStepLocation.get(0)][aiStepLocation.get(1)].changeState("O");
        currentTurn = 0;
        borderX.setOpacity(1);
        borderO.setOpacity(0);
        resetTurnTimer();
        updateTable(aiStepLocation.get(1), aiStepLocation.get(0), 'O');
        for (int k = 0; k < TABLE_SIZE; k++) {
            for (int l = 0; l < TABLE_SIZE; l++) {
                System.out.print(cellsCheck[k][l] + " ");
            }
            System.out.println();
        }
    }

    @MethodClarification(
            methodName = "emptyBoard",
            parameters = "void",
            returnType = "void",
            additionalInfo = "if the board is empty, then makes it not possible to save a match"
    )
    protected void emptyBoard() {
        boolean breakable = false;
        for (int i = 0; i < TABLE_SIZE; i++) {
            for (int j = 0; j < TABLE_SIZE; j++) {
                if (cellsCheck[i][j] != ' ') {
                    breakable = true;
                    break;
                }
            }
            if (breakable) break;
        }
        if (!breakable) {
            btnSaveParty.setOpacity(0);
            btnSaveParty.setDisable(true);
        }
    }

    @MethodClarification(
            methodName = "isNumeric",
            parameters = "str: string to test if input is numeric",
            returnType = "boolean",
            additionalInfo = "checks if string can be converted into a number"
    )
    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
