module fr.zeteox.javachatroom {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.zeteox.javachatroom to javafx.fxml;
    exports fr.zeteox.javachatroom;
    exports fr.zeteox.javachatroom.ui;
    opens fr.zeteox.javachatroom.ui to javafx.fxml;
    exports fr.zeteox.javachatroom.client;
    opens fr.zeteox.javachatroom.client to javafx.fxml;
    exports fr.zeteox.javachatroom.protocol;
    opens fr.zeteox.javachatroom.protocol to javafx.fxml;
}