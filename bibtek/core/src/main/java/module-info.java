module bibtek.core {

	requires transitive com.google.gson;

	exports bibtek.core;
	exports bibtek.json;

    requires jersey.client;
    requires jersey.core;

    opens bibtek.core to com.google.gson;
	opens bibtek.json to com.google.gson;

}