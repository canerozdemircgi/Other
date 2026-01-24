package org.threehundredsixtyt.mode;

import java.io.IOException;

/**
 * Interface defining the contract for different execution modes.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Encapsulate the complete execution logic of a communication pattern</li>
 *   <li>Provide uniform way to start any selected mode</li>
 * </ul>
 */
@FunctionalInterface
public interface IMode {

	void conduct() throws IOException;

}