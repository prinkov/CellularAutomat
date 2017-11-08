package xyz.prinkov.mmlab3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Pair;
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
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Ввод исходных данных");
        dialog.setHeaderText("Введите шаблон соседства и начальное состояние");


        ButtonType startBtn = new ButtonType("Поехали!", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(startBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField templateField = new TextField();
        templateField.setPromptText("i, j");
        templateField.setText("0, 1, 1, 0, 0, 0, 1, 1, 1, 1," +
                " 1, 0, 0, 0,  0, 1, 1, 1, 1, 1, 0, 0, 1, " +
                "1, 0, 0, 0, 0, 0, 1, 1, 1");
        TextField nuField = new TextField();
        nuField.setText("7,5; 0,2; 2,8; 1,0; 1,2; 3,2; 3,4; 4,1; 3,3; 2,6");
        nuField.setPromptText("i, j; k, v");

        grid.add(new Label("Шаблон соседства:"), 0, 0);
        grid.add(templateField, 1, 0);
        grid.add(new Label("Начальное состояние:"), 0, 1);
        grid.add(nuField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> templateField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == startBtn) {
                return new Pair<>(templateField.getText(), nuField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        if (result.isPresent()){
            Pair<String, String> res = result.get();
            String t1 = res.getKey().replaceAll(" ", "");
            int[] template = Arrays.asList(t1.split(",")).
                    stream().
                    mapToInt(Integer::parseInt).
                    toArray();

            String t2 = res.getValue().replaceAll(" ", "");
            String[] nu = t2.split(";");
            //-----------NU--------------
            for(int k = 0; k < nu.length; k++) {
                nu[k] = nu[k].replaceAll(" ", "");
                int x = Integer.parseInt(nu[k].split(",")[0]);
                int y = Integer.parseInt(nu[k].split(",")[1]);
                table.table[x][y] = 1;
            }

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
