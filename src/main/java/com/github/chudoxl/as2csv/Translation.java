package com.github.chudoxl.as2csv;

import java.nio.file.Path;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

class Translation {
	private final boolean isMain;
	private final String code;
	private final ResList resList;

	Translation(Path path) {
		String parentDir = path.getParent().getFileName().toString();
		isMain = !parentDir.contains("-");
		code = App.getTranslationCode(parentDir);

		XStream xstream = new XStream(new StaxDriver());
		xstream.processAnnotations(ResList.class);
		xstream.ignoreUnknownElements();
		resList = (ResList)xstream.fromXML(path.toFile());
	}

	boolean isMain() {
		return isMain;
	}

	String getCode() {
		return code;
	}

	List<Item> getItems() {
		return resList.items;
	}
}
