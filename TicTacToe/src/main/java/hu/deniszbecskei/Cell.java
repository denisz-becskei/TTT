package hu.deniszbecskei;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import static hu.deniszbecskei.App.TABLE_SIZE;

public class Cell extends Rectangle {
    Tuple<Integer> coords;
    String state = null;

    @MethodClarification(
            methodName = "Cell",
            parameters = "x: positionX, y: positionY, bx: positionX on the board, by: positionY on the board, w: cell width, h: cell height, c: cell color",
            returnType = "void",
            additionalInfo = "none"
    )
    Cell(int x, int y, int bx, int by, int w, int h, Color c) {
        super(w, h, c);
        this.coords = new Tuple<>(bx, by);

        setTranslateX(x);
        setTranslateY(y);
    }

    @MethodClarification(
            methodName = "changeState",
            parameters = "newState: new state to change the current cell to",
            returnType = "void",
            additionalInfo = "none"
    )
    public void changeState(String newState) {
        if (this.state == null) {
            this.state = newState;
            if (newState.equals("X")) {
                this.setFill(new ImagePattern(new Image("https://i.imgur.com/w891n5k.png", (int)Math.round(500.0 / TABLE_SIZE), (int)Math.round(500.0 / TABLE_SIZE), true, true)));
            } else if (newState.equals("O")) {
                this.setFill(new ImagePattern(new Image("https://i.imgur.com/yBTVo7q.png", (int)Math.round(500.0 / TABLE_SIZE), (int)Math.round(500.0 / TABLE_SIZE), true, true)));
            }
            //update(this.coords.get(1), this.coords.get(0));
        }
    }

    @MethodClarification(
            methodName = "getState",
            parameters = "void",
            returnType = "String",
            additionalInfo = "Getting the state of the current cell"
    )
    public String getState() { return state; }

    @MethodClarification(
            methodName = "getCoords",
            parameters = "void",
            returnType = "Tuple<Integer>",
            additionalInfo = "returns cells current position in the array in a tuple"
    )
    public Tuple<Integer> getCoords() {
        return this.coords;
    }

    @MethodClarification(
            methodName = "toString",
            parameters = "void",
            returnType = "String",
            additionalInfo = "was made to check whether the coordinate system works"
    )
    @Override
    public String toString() {
        return "Cell at Position: " + this.coords.get(0) + ", " + this.coords.get(1);
    }

}