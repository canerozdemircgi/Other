package org.threehundredsixtyt.argument;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class responsible for parsing command-line arguments.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Convert string array of arguments into key-value map</li>
 *   <li>Support both --key=value and --key formats</li>
 *   <li>Provide simple, predictable argument access</li>
 * </ul>
 */
public final class ArgumentParser {

	public static Map<String, String> parse(final String... arguments) {
		final Map<String, String> map = new HashMap<>(16);
		for (final String argument : arguments) {
			if (argument.startsWith("--")) {
				final String[] parts = argument.substring(2).split("=", 2);
				final String key = parts[0];
				final String value = parts.length > 1 ? parts[1] : "true";
				map.put(key, value);
			}
		}
		return map;
	}

}