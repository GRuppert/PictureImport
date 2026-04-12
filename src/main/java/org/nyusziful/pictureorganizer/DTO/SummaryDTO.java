package org.nyusziful.pictureorganizer.DTO;


import javafx.beans.property.BooleanProperty;

public interface SummaryDTO {
    boolean isSelected();
    void setSelectedValue(boolean selected);
    BooleanProperty getSelected();
}
