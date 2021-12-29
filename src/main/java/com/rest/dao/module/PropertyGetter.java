package com.rest.dao.module;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.net.URL;

public class PropertyGetter {

	public Properties getProperties() {

		Properties properties = new Properties();

		try (InputStream input = PropertyGetter.class.getResourceAsStream("/config.properties");) {
			properties.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return properties;

	}

}
