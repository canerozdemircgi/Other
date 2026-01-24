package org.threehundredsixtyt.config;

import org.threehundredsixtyt.log.Loggers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Function;

/**
 * Central configuration holder for the application.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Load configuration values from properties file</li>
 *   <li>Manage default configuration values</li>
 *   <li>Provide static access to important constants and settings</li>
 *   <li>Centralize configuration-related values used across the application</li>
 * </ul>
 */
public final class Config {

	public static final int SERVER_PORT;

	public static final String CLIENT_IP;
	public static final int CLIENT_PORT;
	public static final long CLIENT_RECONNECT_TIMEOUT_MS;

	public static final String MESSAGE;
	public static final int MESSAGE_MAX;

	static {
		final String configFileName = "config.properties";
		final Properties properties = new Properties();

		boolean success = false;
		final Path configPath = Paths.get(configFileName);
		if (Files.exists(configPath) && Files.isReadable(configPath)) {
			try (final InputStream inputStream = Files.newInputStream(configPath)) {
				properties.load(inputStream);
				success = true;
			} catch (final IOException ex) {
				Loggers.LOGGER.error(ex);
			}
		}

		if (!success) {
			try (final InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(configFileName)) {
				properties.load(inputStream);
			} catch (final IOException ex) {
				Loggers.LOGGER.error(ex);
			}
		}

		SERVER_PORT = Config.getInt(properties, "server.port", 59_837);

		CLIENT_IP = Config.getString(properties, "client.ip", "127.0.0.1");
		CLIENT_PORT = Config.getInt(properties, "client.port", 59_837);
		CLIENT_RECONNECT_TIMEOUT_MS = Config.getLong(properties, "client.reconnect.timeout.ms", 1_000L);

		MESSAGE = Config.getString(properties, "message", "Hello from Player: {} counter: {}");
		MESSAGE_MAX = Config.getInt(properties, "message.max", 10);
	}

	private static String getString(final Properties properties, final String key, final String defaultValue) {
		final String string = properties.getProperty(key);
		if (string != null) {
			final String result = string.trim();
			if (!result.isEmpty()) {
				return result;
			}
		}
		Loggers.LOGGER.warn("Key not found: {}, using default value: {}", key, defaultValue);
		return defaultValue;
	}

	private static <T> T getNumber(final Properties properties, final String key, final T defaultValue, final Function<String, T> parser) {
		final String string = Config.getString(properties, key, String.valueOf(defaultValue));
		try {
			return parser.apply(string);
		} catch (final RuntimeException ex) {
			Loggers.LOGGER.error(ex);
		}
		Loggers.LOGGER.warn("Exception emerged for key: {}, using default value: {}", key, defaultValue);
		return defaultValue;
	}

	private static int getInt(final Properties properties, final String key, final int defaultValue) {
		return Config.getNumber(properties, key, defaultValue, Integer::parseInt);
	}

	private static long getLong(final Properties properties, final String key, final long defaultValue) {
		return Config.getNumber(properties, key, defaultValue, Long::parseLong);
	}

}