package com.github.chudoxl.as2csv;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("resources")
public class ResList {
	@XStreamImplicit(itemFieldName = "string")
	public List<Item> items;
}
