package org.threehundredsixtyt.mode;

import org.threehundredsixtyt.log.Loggers;

/**
 * Factory responsible for creating appropriate mode implementations.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Create correct IMode implementation based on requested mode</li>
 *   <li>Centralize mode creation logic</li>
 *   <li>Provide default/fallback mode when needed</li>
 * </ul>
 */
public final class ModeFactory {

	public static IMode create(final String mode) {
		return ModeFactory.create(EMode.valueOfIgnoreCase(mode));
	}

	public static IMode create(final EMode mode) {
		Loggers.LOGGER.info("Program mode is: " + mode);

		return switch (mode) {
			case BOTH -> new ModeBoth();
			case SERVER -> new ModeServer();
			case CLIENT -> new ModeClient();
			default -> {
				Loggers.LOGGER.warn("Mode is unknown. Using the default mode");
				yield new ModeBoth();
			}
		};
	}

}