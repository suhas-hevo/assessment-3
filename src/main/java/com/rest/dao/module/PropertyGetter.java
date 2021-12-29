package com.rest.dao.module;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.net.URL;

public class PropertyGetter {

	public Properties getProperties() {

		Properties prop = new Properties();

		try (InputStream input = PropertyGetter.class.getResourceAsStream("/config.properties");) {
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return prop;

	}

}
