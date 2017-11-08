package xyz.prinkov.mmlab3;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Optional;

public class Main extends Application{
    static GridPane field = new GridPane();
    static Table table = new Table(10, 10);
    static Rectangle[][] cells;

    int WIDTH = 600;
    int HEIGHT = 750;
    public static void main(String[] args) {

        launch(args);
    }
    public void start(Stage stage) throws Exception {
        TextInputDialog dialog = new TextInputDialog
                ("0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, " +
                        "0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, " +
                        "0, 0, 1, 1, 1");
        dialog.setTitle("Введите значения");
        dialog.setHeaderText("Введите шаблон соседства");
        dialog.setContentText("f:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String res = result.get();
            res = res.replaceAll(" ", "");
            int[] template = Arrays.asList(res.split(",")).
                    stream().
                    mapToInt(Integer::parseInt).
                    toArray();
            //-----------NU--------------
            table.table[7][5] = 1;
            table.table[0][2] = 1;
            table.table[2][8] = 1;
            table.table[1][0] = 1;
            table.table[1][2] = 1;
            table.table[3][2] = 1;
            table.table[3][4] = 1;
            table.table[4][1] = 1;
            table.table[3][3] = 1;

            //-------------

            VBox vbox = new VBox();
            vbox.setSpacing(5);
            field.setPrefSize(600, 600);
            Scene scene = new Scene(vbox);
            vbox.setPrefSize(WIDTH, HEIGHT);
            Button nextBtn = new Button("Далее");
            nextBtn.setOnMouseClicked(e -> {
                nextIteration();
            });

            cells = new Rectangle[10][10];



            for(int i = 0; i < 10; i++)
                for(int j = 0; j < 10; j++) {
                    cells[i][j] = new Rectangle((WIDTH) / 10,
                            (HEIGHT - 150) / 10);
                    field.add(cells[i][j], i, 10 - j);
//                field.add(new Label(i  +" " + j), i, j);
                    if (table.table[i][j] == 1)
                        cells[i][j].setFill(Color.BLACK);
                    else
                        cells[i][j].setFill(Color.WHITE);
                    cells[i][j].setStroke(Color.GRAY);

                }

            vbox.setAlignment(Pos.CENTER);
            String polynom = table.computePolynom(template);
//        String polynom = table.computePolynom(
//                new int[]{0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0,
//                        0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0,
//                        1, 1, 1});
            Label lblP = new Label(polynom);
            lblP.setWrapText(true);
            TeXFormula formula = new TeXFormula(polynom);
            Image timg = SwingFXUtils.toFXImage((BufferedImage)formula.createBufferedImage(TeXConstants.STYLE_DISPLAY, 40,
                    java.awt.Color.BLACK, java.awt.Color.WHITE), null);
            ScrollPane sc = new ScrollPane();
            sc.setContent(new ImageView(timg));
            sc.setPrefSize(600, 150);
            vbox.getChildren().addAll(sc,  field, nextBtn);

            stage.setScene(scene);
            stage.show();
        }
    }

    public static void nextIteration() {
        table.computeIteration();
        for (int i = 0; i < table.table.length; i++)
            for (int j = 0; j < table.table[0].length; j++) {
                if (table.table[i][j] == 1)
                    cells[i][j].setFill(Color.BLACK);
                else
                    cells[i][j].setFill(Color.WHITE);
            cells[i][j].setStroke(Color.GRAY);
        }
    }
}
