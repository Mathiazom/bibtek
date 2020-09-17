module bibtek {
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires com.google.gson;
	requires java.base;

	
	exports bibtek.core;
	exports bibtek.json;
	exports bibtek.ui;

	opens bibtek.core to com.google.gson;

	opens bibtek.ui to javafx.fxml;
}