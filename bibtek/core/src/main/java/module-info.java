module bibtek.core {

	requires transitive com.google.gson;
	requires java.base;

	exports bibtek.core;
	exports bibtek.json;

	requires java.net.http;

	opens bibtek.core to com.google.gson;
	opens bibtek.json to com.google.gson;

}