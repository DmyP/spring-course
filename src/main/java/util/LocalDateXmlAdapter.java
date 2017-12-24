package util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate> {

	@Override
	public String marshal(LocalDate arg0) throws Exception {
		return arg0.toString();
	}

	@Override
	public LocalDate unmarshal(String arg) throws Exception {
		return LocalDate.parse(arg);
	}

}
