package engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Art {

    private static final int SPRITE_WIDTH = 16;
    private static final int SPRITE_SHEET_ROWS = 16;
    private static final int SPRITE_SHEET_COLUMNS = 16;

    // alpha color(s)

    public static final BufferedImage SPRITES[][];

    static {
        SPRITES = new BufferedImage[SPRITE_SHEET_ROWS][SPRITE_SHEET_COLUMNS];
        BufferedImage image;

        try {
            image = ImageIO.read(new File("./res/Sprites.png"));

            for (int i = 0; i < SPRITE_SHEET_ROWS; i++) {
                for (int j = 0; j < SPRITE_SHEET_COLUMNS; j++) {

                    SPRITES[i][j] = image.getSubimage(j * SPRITE_WIDTH, i * SPRITE_WIDTH, SPRITE_WIDTH, SPRITE_WIDTH);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
