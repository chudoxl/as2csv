package com.github.chudoxl.as2csv;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;


@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class Item {
	@XStreamAsAttribute
	@XStreamAlias("name")
	public String name;

	@XStreamAsAttribute
	@XStreamAlias("translatable")
	public String translatable;

	public String value;
}
