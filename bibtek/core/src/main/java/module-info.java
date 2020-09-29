module bibtek.core {

	requires com.google.gson;
	requires java.base;

	exports bibtek.core;
	exports bibtek.json;

	opens bibtek.core to com.google.gson;

}