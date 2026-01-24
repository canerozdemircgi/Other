package org.threehundredsixtyt;

import org.threehundredsixtyt.log.Loggers;
import org.threehundredsixtyt.mode.IMode;
import org.threehundredsixtyt.mode.ModeFactory;
import org.threehundredsixtyt.argument.ArgumentParser;

import java.io.IOException;
import java.util.Map;

/**
 * Main entry point of the application.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Parse command-line arguments</li>
 *   <li>Determine execution mode using ModeFactory</li>
 *   <li>Start the selected communication mode</li>
 *   <li>Provide basic start/stop logging</li>
 * </ul>
 */
public final class Main {

	public static void main(final String[] arguments) throws IOException {
		Loggers.LOGGER.info("Program is starting");

		final Map<String, String> params = ArgumentParser.parse(arguments);
		final IMode mode = ModeFactory.create(params.getOrDefault("mode", "both"));
		mode.conduct();

		Loggers.LOGGER.info("Program is stopping");
	}

}