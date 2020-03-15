package org.nyusziful.pictureorganizer.UI.Elements;

import com.sun.javafx.scene.control.skin.DatePickerContent;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DateRangePicker extends DatePicker {
    private DateCell iniCell=null;
    private DateCell endCell=null;

    private LocalDate iniDate;
    private LocalDate endDate;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.uuuu", Locale.ENGLISH);

    public void start(Stage primaryStage) {
        DatePicker datePicker=new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setConverter(new StringConverter<LocalDate>() {

            @Override
            public String toString(LocalDate object) {
                if(iniDate!=null && endDate!=null){
                    return iniDate.format(formatter)+" - "+endDate.format(formatter);
                }
                return object.format(formatter);
            }

            @Override
            public LocalDate fromString(String string) {
                if(string.contains("-")){
                    try{
                        iniDate=LocalDate.parse(string.split("-")[0].trim(), formatter);
                        endDate=LocalDate.parse(string.split("-")[1].trim(), formatter);
                    } catch(DateTimeParseException dte){
                        return LocalDate.parse(string, formatter);
                    }
                    return iniDate;
                }
                return LocalDate.parse(string, formatter);
            }
        });
        Scene scene = new Scene(new AnchorPane(datePicker), 300, 250);

        primaryStage.setScene(scene);
        primaryStage.show();

        datePicker.showingProperty().addListener((obs,b,b1)->{
            if(b1){
                DatePickerContent content = (DatePickerContent)((DatePickerSkin)datePicker.getSkin()).getPopupContent();

                List<DateCell> cells = content.lookupAll(".day-cell").stream()
                        .filter(ce->!ce.getStyleClass().contains("next-month"))
                        .map(n->(DateCell)n)
                        .collect(Collectors.toList());

                // select initial range
                if(iniDate!=null && endDate!=null){
                    int ini=iniDate.getDayOfMonth();
                    int end=endDate.getDayOfMonth();
                    cells.stream()
                            .forEach(ce->ce.getStyleClass().remove("selected"));
                    cells.stream()
                            .filter(ce->Integer.parseInt(ce.getText())>=ini)
                            .filter(ce->Integer.parseInt(ce.getText())<=end)
                            .forEach(ce->ce.getStyleClass().add("selected"));
                }
                iniCell=null;
                endCell=null;
                content.setOnMouseDragged(e->{
                    Node n=e.getPickResult().getIntersectedNode();
                    DateCell c=null;
                    if(n instanceof DateCell){
                        c=(DateCell)n;
                    } else if(n instanceof Text){
                        c=(DateCell)(n.getParent());
                    }
                    if(c!=null && c.getStyleClass().contains("day-cell") &&
                            !c.getStyleClass().contains("next-month")){
                        if(iniCell==null){
                            iniCell=c;
                        }
                        endCell=c;
                    }
                    if(iniCell!=null && endCell!=null){
                        int ini=(int)Math.min(Integer.parseInt(iniCell.getText()),
                                Integer.parseInt(endCell.getText()));
                        int end=(int)Math.max(Integer.parseInt(iniCell.getText()),
                                Integer.parseInt(endCell.getText()));
                        cells.stream()
                                .forEach(ce->ce.getStyleClass().remove("selected"));
                        cells.stream()
                                .filter(ce->Integer.parseInt(ce.getText())>=ini)
                                .filter(ce->Integer.parseInt(ce.getText())<=end)
                                .forEach(ce->ce.getStyleClass().add("selected"));
                    }
                });
                content.setOnMouseReleased(e->{
                    if(iniCell!=null && endCell!=null){
                        iniDate=LocalDate.of(datePicker.getValue().getYear(),
                                datePicker.getValue().getMonth(),
                                Integer.parseInt(iniCell.getText()));
                        endDate=LocalDate.of(datePicker.getValue().getYear(),
                                datePicker.getValue().getMonth(),
                                Integer.parseInt(endCell.getText()));
                        System.out.println("Selection from "+iniDate+" to "+endDate);

                        datePicker.setValue(iniDate);
                        int ini=iniDate.getDayOfMonth();
                        int end=endDate.getDayOfMonth();
                        cells.stream()
                                .forEach(ce->ce.getStyleClass().remove("selected"));
                        cells.stream()
                                .filter(ce->Integer.parseInt(ce.getText())>=ini)
                                .filter(ce->Integer.parseInt(ce.getText())<=end)
                                .forEach(ce->ce.getStyleClass().add("selected"));
                    }
                    endCell=null;
                    iniCell=null;
                });
            }
        });
    }
}
