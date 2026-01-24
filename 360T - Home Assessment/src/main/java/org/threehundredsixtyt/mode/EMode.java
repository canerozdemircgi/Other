package org.threehundredsixtyt.mode;

/**
 * Enum representing possible execution modes of the application.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Define supported operating modes (both, client, server)</li>
 *   <li>Provide clear, type-safe way to select communication pattern</li>
 * </ul>
 */
public enum EMode {

	BOTH,
	SERVER,
	CLIENT;

	public static EMode valueOfIgnoreCase(final String string) {
		try {
			return EMode.valueOf(string.toUpperCase());
		} catch (final IllegalArgumentException ex) {
			throw new IllegalArgumentException("Mode: %s is unknown. Use: --mode=both|server|client".formatted(string));
		}
	}

}