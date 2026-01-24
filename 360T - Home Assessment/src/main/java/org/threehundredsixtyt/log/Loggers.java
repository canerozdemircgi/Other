package org.threehundredsixtyt.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Centralized logging facade for the application.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Provide single point of access to the Log4j2 logger instance</li>
 *   <li>Simplify logger usage across the whole codebase</li>
 *   <li>Encapsulate logger configuration and initialization</li>
 * </ul>
 */
public final class Loggers {

	public static final Logger LOGGER = LogManager.getLogger("Journal");

}