package hu.deniszbecskei;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FlipFlop extends Parent {
    private char flipper = 'L';
    private final Rectangle front;

    public FlipFlop() {
        Rectangle back = new Rectangle(60, 30, Color.DARKGRAY);
        back.setArcHeight(12);
        back.setArcWidth(12);
        this.front = new Rectangle(20, 20, Color.GRAY);
        this.front.setTranslateY(5);
        this.front.setTranslateX(5);
        this.front.setOnMouseClicked(event -> flipFlipper());

        this.getChildren().addAll(back, front);
    }

    public void flipFlipper() {
        if (this.flipper == 'L') {
            this.flipper = 'R';
        } else {
            this.flipper = 'L';
        }

        if (this.flipper == 'L') {
            this.front.setTranslateY(5);
            this.front.setTranslateX(5);
        } else {
            this.front.setTranslateY(5);
            this.front.setTranslateX(60 - 25);
        }
    }

    public char getFlipper() {
        return flipper;
    }
}
