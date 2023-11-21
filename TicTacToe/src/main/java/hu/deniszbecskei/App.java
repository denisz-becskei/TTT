package hu.deniszbecskei;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class App extends Application {

    // Top Bar
    private final Rectangle tb = new Rectangle(750, 190, Color.WHITE);
    private final Rectangle rectX = new Rectangle(100, 100, Color.RED);
    private final Rectangle rectO = new Rectangle(100, 100, Color.BLUE);
    private final Rectangle rectXborder = new Rectangle(120, 120, Color.TRANSPARENT);
    private final Rectangle rectOborder = new Rectangle(120, 120, Color.TRANSPARENT);
    protected final Rectangle winnerX = new Rectangle(160, 80, Color.TRANSPARENT);
    protected final Rectangle winnerO = new Rectangle(160, 80, Color.TRANSPARENT);

    // Control
    protected static int currentTurn = 0;
    protected static boolean isPlaying = true;
    private final Control control = new Control(winnerX, winnerO, rectOborder, rectXborder);
    private final Pane rootMenu = new Pane();
    private final int WINDOW_WIDTH = 750;
    private final int WINDOW_HEIGHT = 950;
    private Scene gameScene;

    // Timers
    private static float TIME_FOR_MOVE = 10;
    protected static float movingTimerTurn;
    protected static Timer movingTimer;

    private float TIME_TO_END = 60;
    private float movingTimerEnd;
    protected static Timer endTimer;

    protected static boolean isTimerRunning = false;
    protected static boolean isAITimerRunning = false;
    protected static Timer aiTimer;

    @MethodClarification(
            methodName = "createMainMenu",
            parameters = "stage: stage the window uses",
            returnType = "Parent",
            additionalInfo = "Creates the Main Menu components and returns it in a container"
    )
    private Parent createMainMenu(Stage stage) {
        rootMenu.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        Rectangle logo = new Rectangle(150, 150, Color.WHITE);
        logo.setFill(new ImagePattern(new Image("https://i.imgur.com/3OpGL57.png", 150, 150, true, true)));
        logo.setTranslateX(WINDOW_WIDTH / 2.0 - 150.0 / 2);
        logo.setTranslateY(75);

        Label tableSize_l = new Label("Táblaméret");
        TextField tableSizeInput = new TextField();
        tableSizeInput.setMaxWidth(200);
        tableSize_l.setLabelFor(tableSizeInput);
        Label tableSizeLimit = new Label("[10 - 35]");

        Label timeForMove_l = new Label("Időkorlát egy lépéshez (mp)");
        TextField timeForMoveInput = new TextField();
        timeForMoveInput.setMaxWidth(200);
        timeForMove_l.setLabelFor(timeForMoveInput);
        Label timeForMoveLimit = new Label("[5.0 - 60.0]");

        Label timeToEnd_l = new Label("Időkorlát a meccs befejezéséig (mp)");
        TextField timeToEndInput = new TextField();
        timeToEndInput.setMaxWidth(200);
        timeToEnd_l.setLabelFor(timeToEndInput);
        Label timeToEndLimit = new Label("[45.0 - 300.0]");

        Label ff_option_1 = new Label("Kétjátékos-mód");
        FlipFlop ff = new FlipFlop();
        Label ff_option_2 = new Label("Egyjátékos-mód");

        HBox flipper = new HBox(10);
        flipper.setAlignment(Pos.CENTER);
        flipper.setPrefWidth(WINDOW_WIDTH);
        flipper.getChildren().addAll(ff_option_1, ff, ff_option_2);

        Button start = new Button();
        start.setText("Játék indítása!");
        start.setOnMouseClicked(mouseEvent -> {
            if (control.isNumeric(tableSizeInput.getText()) && control.isNumeric(timeForMoveInput.getText()) &&
                    control.isNumeric(timeToEndInput.getText())) {
                TABLE_SIZE = (int) control.limitValue(10, 36, Float.parseFloat(tableSizeInput.getText()));
                TIME_FOR_MOVE = control.limitValue(5, 61, Float.parseFloat(timeForMoveInput.getText()));
                TIME_TO_END = control.limitValue(45, 301, Float.parseFloat(timeToEndInput.getText()));
                movingTimerTurn = TIME_FOR_MOVE;
                movingTimerEnd = TIME_TO_END;
                System.out.println(TABLE_SIZE);
                if (ff.getFlipper() == 'L') {
                    gameScene = new Scene(createGame('M'));
                } else {
                    gameScene = new Scene(createGame('S'));
                }
                stage.setScene(gameScene);
            }
        });

        rootMenu.getChildren().add(logo);

        VBox vb = new VBox(10);
        vb.setAlignment(Pos.CENTER);
        vb.setPrefWidth(WINDOW_WIDTH);
        vb.setLayoutY(250);
        vb.getChildren().addAll(
                tableSize_l,
                tableSizeInput,
                tableSizeLimit,
                timeForMove_l,
                timeForMoveInput,
                timeForMoveLimit,
                timeToEnd_l,
                timeToEndInput,
                timeToEndLimit,
                flipper,
                start
        );

        rootMenu.getChildren().add(vb);
        return rootMenu;
    }

    // Table
    protected static int TABLE_SIZE;
    protected static Cell[][] cells;
    protected static Character[][] cellsCheck;
    protected static Integer[][] whenTheMoveHappened;
    private final Label turnTimer_l = new Label();
    private final Label endTimer_l = new Label();
    private final Pane rootGame = new Pane();
    private static int currentMoveNumber = 0;
    protected static final Label victoryLabel = new Label();
    protected static Button btnSaveParty;
    private final Label savedId = new Label();

    @MethodClarification(
            methodName = "updateTable",
            parameters = "x: horizontal position in array, y: vertical position in array, state: state to change cell to",
            returnType = "void",
            additionalInfo = "none"
    )
    protected static void updateTable(int x, int y, char state) {
        cellsCheck[y][x] = state;
        currentMoveNumber += 1;
        whenTheMoveHappened[y][x] = currentMoveNumber;
    }

    @MethodClarification(
            methodName = "createGame",
            parameters = "mode: Mode in which the game is played. Can we single or multiplayer",
            returnType = "Parent",
            additionalInfo = "Creates the Game components and returns it in a container"
    )
    private Parent createGame(char mode) {
        cells = new Cell[TABLE_SIZE][TABLE_SIZE];
        cellsCheck = new Character[TABLE_SIZE][TABLE_SIZE];
        whenTheMoveHappened = new Integer[TABLE_SIZE][TABLE_SIZE];

        for (int i = 0; i < TABLE_SIZE; i++) {
            for (int j = 0; j < TABLE_SIZE; j++) {
                whenTheMoveHappened[i][j] = 0;
            }
        }

        for (int i = 0; i < TABLE_SIZE; i++) {
            for (int j = 0; j < TABLE_SIZE; j++) {
                cells[i][j] = new Cell(75 + j * (int) Math.round(600.0 / TABLE_SIZE), 250 + i * (int) Math.round(600.0 / TABLE_SIZE), j, i, (int) Math.round(500.0 / TABLE_SIZE), (int) Math.round(500.0 / TABLE_SIZE), Color.RED);
                cells[i][j].setStroke(Color.BLACK);
                cells[i][j].setFill(Color.TRANSPARENT);
                cells[i][j].setOnMouseClicked(event -> {
                    if (isPlaying) {
                        System.out.println(event.getSource());
                        System.out.println(((Cell) event.getSource()).getState());

                        if (currentTurn == 0 || currentTurn == 1 && mode == 'M') {
                            if (((Cell) event.getSource()).getState() == null) {
                                // If X's turn
                                if (currentTurn == 0) {
                                    ((Cell) event.getSource()).changeState("X");
                                    currentTurn = 1;
                                    rectXborder.setOpacity(0);
                                    rectOborder.setOpacity(1);
                                    updateTable(((Cell) event.getSource()).getCoords().get(0), ((Cell) event.getSource()).getCoords().get(1), 'X');
                                    for (int k = 0; k < TABLE_SIZE; k++) {
                                        for (int l = 0; l < TABLE_SIZE; l++) {
                                            System.out.print(cellsCheck[k][l] + " ");
                                        }
                                        System.out.println();
                                    }
                                } else {
                                    if (mode == 'M') {
                                        ((Cell) event.getSource()).changeState("O");
                                        currentTurn = 0;
                                        rectXborder.setOpacity(1);
                                        rectOborder.setOpacity(0);
                                        updateTable(((Cell) event.getSource()).getCoords().get(0), ((Cell) event.getSource()).getCoords().get(1), 'O');
                                        for (int k = 0; k < TABLE_SIZE; k++) {
                                            for (int l = 0; l < TABLE_SIZE; l++) {
                                                System.out.print(cellsCheck[k][l] + " ");
                                            }
                                            System.out.println();
                                        }
                                    }
                                }

                                // Setting the moving timer to the start timer
                                resetTurnTimer();

                                // Checking for winner
                                if (isPlaying) {

                                    // Checking for victories
                                    if (control.checkHorizontal() != '§' || control.checkVertical() != '§' || control.checkLeftRightDiag() != '§' || control.checkRightLeftDiag() != '§') {
                                        if (control.checkHorizontal() == 'X')
                                            control.giveVictory('X', "Horizontális győzelem!");
                                        if (control.checkHorizontal() == 'O')
                                            control.giveVictory('O', "Horizontális győzelem!");

                                        if (control.checkVertical() == 'X')
                                            control.giveVictory('X', "Vertikális győzelem!");
                                        if (control.checkVertical() == 'O')
                                            control.giveVictory('O', "Vertikális győzelem!");

                                        if (control.checkLeftRightDiag() == 'X')
                                            control.giveVictory('X', "Diagonális győzelem!");
                                        if (control.checkLeftRightDiag() == 'O')
                                            control.giveVictory('O', "Diagonális győzelem!");

                                        if (control.checkRightLeftDiag() == 'X')
                                            control.giveVictory('X', "Diagonális győzelem!");
                                        if (control.checkRightLeftDiag() == 'O')
                                            control.giveVictory('O', "Diagonális győzelem!");
                                        isPlaying = false;
                                    }

                                    if (control.checkForFullBoard()) {
                                        control.giveVictory('-', "A tábla megtelt!");
                                        isPlaying = false;
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }

        // Filling table empty
        for (int i = 0; i < TABLE_SIZE; i++) {
            for (int j = 0; j < TABLE_SIZE; j++) {
                cellsCheck[i][j] = ' ';
            }
        }

        // Doing stuff with the timer
        Label turnTimerText = new Label("Következő lépés időkorlátja");
        turnTimerText.setTranslateY(WINDOW_HEIGHT - 75);
        turnTimerText.setTranslateX(WINDOW_WIDTH - 225);

        turnTimer_l.setTranslateY(WINDOW_HEIGHT - 50);
        turnTimer_l.setTranslateX(WINDOW_WIDTH - 155);
        turnTimer_l.setFont(Font.font("Arial", 32));

        Label endTimerText = new Label("Maximális idő a meccs végéig");
        endTimerText.setTranslateY(WINDOW_HEIGHT - 75);
        endTimerText.setTranslateX(75);

        endTimer_l.setTranslateY(WINDOW_HEIGHT - 50);
        endTimer_l.setTranslateX(75);
        endTimer_l.setFont(Font.font("Arial", 32));

        // Setting options for top bar
        // Setting X and O rectangles
        rectX.setFill(new ImagePattern(new Image("https://i.imgur.com/w891n5k.png", 100, 100, true, true)));
        rectO.setFill(new ImagePattern(new Image("https://i.imgur.com/yBTVo7q.png", 100, 100, true, true)));

        rectX.setTranslateX(80);
        rectX.setTranslateY(50);

        rectO.setTranslateX(WINDOW_WIDTH - 210);
        rectO.setTranslateY(50);

        // Setting X and O borders
        rectXborder.setFill(new ImagePattern(new Image("https://i.imgur.com/r9JptPY.png", 120, 120, true, true)));
        rectOborder.setFill(new ImagePattern(new Image("https://i.imgur.com/XLqfhgX.png", 120, 120, true, true)));

        rectXborder.setTranslateX(70);
        rectXborder.setTranslateY(40);

        rectOborder.setTranslateX(WINDOW_WIDTH - 220);
        rectOborder.setTranslateY(40);

        rectXborder.setOpacity(1);
        rectOborder.setOpacity(0);

        // Setting winner options
        winnerX.setFill(new ImagePattern(new Image("https://i.imgur.com/7VhitB2.png", 160, 80, true, true)));
        winnerX.setOpacity(0);
        winnerX.setTranslateX(50);

        winnerO.setFill(new ImagePattern(new Image("https://i.imgur.com/7VhitB2.png", 160, 80, true, true)));
        winnerO.setOpacity(0);
        winnerO.setTranslateX(510);

        // Victory text
        victoryLabel.setText("Lejárt az idő!");
        victoryLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 20));
        victoryLabel.setOpacity(0);

        // Victory text VBox for alignment
        VBox victoryLabelContainer = new VBox(20);
        victoryLabelContainer.setAlignment(Pos.CENTER);
        victoryLabelContainer.setPrefWidth(WINDOW_WIDTH);
        victoryLabelContainer.setLayoutY(200);
        victoryLabelContainer.getChildren().addAll(
          victoryLabel
        );

        // Save Party
        savedId.setOpacity(0);

        btnSaveParty = new Button();
        btnSaveParty.setOpacity(0);
        btnSaveParty.setDisable(true);
        btnSaveParty.setText("Party elmentése");
        btnSaveParty.setOnMouseClicked(event -> {
            System.out.println("Party elmentve");
            String partyId = control.saveMatch();
            savedId.setOpacity(1);
            savedId.setText("A meccs elmentve. Azonosító: " + partyId);
        });

        VBox alignBottom = new VBox(20);
        alignBottom.setAlignment(Pos.CENTER);
        alignBottom.setPrefWidth(WINDOW_WIDTH);
        alignBottom.setLayoutY(WINDOW_HEIGHT - 65);
        alignBottom.getChildren().addAll(
                btnSaveParty, savedId
        );

        // Add to Root Children
        rootGame.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        rootGame.getChildren().addAll(
                tb,
                rectXborder,
                rectOborder,
                rectX,
                rectO,
                winnerX,
                winnerO,
                victoryLabelContainer,
                turnTimerText,
                endTimerText,
                turnTimer_l,
                endTimer_l,
                alignBottom
        );

        // Add Cells to Children
        for (int i = 0; i < TABLE_SIZE; i++) {
            for (int j = 0; j < TABLE_SIZE; j++) {
                rootGame.getChildren().add(cells[i][j]);
            }
        }

        // Starting timers
        setMoveTimer();
        setEndTimer(mode);

        return rootGame;
    }

    @MethodClarification(
            methodName = "setMoveTimer",
            parameters = "void",
            returnType = "void",
            additionalInfo = "For starting the turn timer"
    )
    private void setMoveTimer() {
        movingTimer = new Timer();
        movingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isTimerRunning = true;
                if (movingTimerTurn > 0) {
                    Platform.runLater(() -> turnTimer_l.setText(String.valueOf(Math.round(movingTimerTurn * 100.0) / 100.0)));
                    movingTimerTurn -= 0.01;
                } else {
                    movingTimer.cancel();
                    endTimer.cancel();
                    if (currentTurn == 0) {
                        control.giveVictory('-', "Lépésidő lejárt!", 'O');
                    }
                    else if (currentTurn == 1) {
                        control.giveVictory('-', "Lépésidő lejárt!", 'X');
                    }
                    isTimerRunning = false;

                    control.emptyBoard();
                }
            }
        }, 10, 10);
    }

    @MethodClarification(
            methodName = "setEndTimer",
            parameters = "void",
            returnType = "void",
            additionalInfo = "For starting the end timer"
    )
    private void setEndTimer(char mode) {
        endTimer = new Timer();
        endTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (movingTimerEnd > 0) {
                    Platform.runLater(() -> endTimer_l.setText(String.valueOf(Math.round(movingTimerEnd * 100.0) / 100.0)));
                    movingTimerEnd -= 0.01;
                    if (mode == 'S' && currentTurn == 1) {
                        if (!isAITimerRunning) {
                            setAITimer();
                        }
                    }
                } else {
                    endTimer.cancel();
                    movingTimer.cancel();
                    control.giveVictory('-', "Lejárt a meccsidő!");
                    isTimerRunning = false;

                    control.emptyBoard();
                }
            }
        }, 10, 10);
    }

    @MethodClarification(
            methodName = "setAITimer",
            parameters = "void",
            returnType = "void",
            additionalInfo = "For starting an AI timer for a turn"
    )
    private void setAITimer() {
        isAITimerRunning = true;
        aiTimer = new Timer();
        final float[] aiMoveTimer = {(new Random().nextFloat( )) * TIME_FOR_MOVE / 5};
        System.out.println("Timer start: " + aiMoveTimer[0]);
        aiTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (aiMoveTimer[0] > 0) {
                    aiMoveTimer[0] -= 0.01;
                } else {
                    currentTurn = 0;
                    aiTimer.cancel();
                    isAITimerRunning = false;
                    System.out.println("Timer end: " + aiMoveTimer[0]);
                    control.AIStep();
                }
            }
        }, 10, 10);
    }

    @MethodClarification(
            methodName = "resetTurnTimer",
            parameters = "void",
            returnType = "void",
            additionalInfo = "Reset the turn timer"
    )
    public static void resetTurnTimer() {
        movingTimerTurn = TIME_FOR_MOVE;
    }

    @MethodClarification(
            methodName = "start",
            parameters = "stage: the first stage",
            returnType = "void",
            additionalInfo = "Starting the stage and setting the scene"
    )
    @Override
    public void start(Stage stage) {
        Scene mainMenuScene = new Scene(createMainMenu(stage));

        stage.setScene(mainMenuScene);
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> {
            if (isTimerRunning) {
                movingTimer.cancel();
                endTimer.cancel();
            }
            if (isAITimerRunning) {
                aiTimer.cancel();
            }
        });
    }

    @MethodClarification(
            methodName = "main",
            parameters = "args: input arguments (none for now)",
            returnType = "void",
            additionalInfo = "none"
    )
    public static void main(String[] args) {
        launch();
    }


}