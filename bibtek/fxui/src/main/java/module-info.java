module bibtek.fxui {

	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires transitive bibtek.core;

	exports bibtek.ui;

	opens bibtek.ui to javafx.fxml;
}