module bibtek.fxui {

	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires transitive bibtek.core;
    requires org.kordamp.ikonli.javafx;

    exports bibtek.ui;

	opens bibtek.ui to javafx.fxml;
}