package interview.googleapis;

import lombok.Getter;

@Getter
public enum CellColor {
    GRAY(1.0f, 0.75f, 0.005f, 1.0f),
    RED(1.0f, 0.005f, 0.005f, 0.005f),
    ORANGE(1.0f, 0.64f, 0.005f, 0.005f),
    YELLOW(1.0f, 1.0f, 0.005f, 0.005f),
    BLUE(0.4f, 0.6f, 0.9f, 1.0f),
    PINK(1.0f, 0.005f, 1.0f, 1.0f),
    BLACK(0.6f, 0.6f, 0.6f, 1.0f),
    WHITE(1.0f, 1.0f, 1.0f, 1.0f),
    GREEN(0.005f, 1.0f, 0.005f, 1.0f);
    private final float red;
    private final float green;
    private final float blue;
    private final float alpha;

    CellColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

}
