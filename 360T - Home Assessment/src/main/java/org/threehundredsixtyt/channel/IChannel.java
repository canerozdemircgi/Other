package org.threehundredsixtyt.channel;

import java.io.IOException;

/**
 * Interface defining the contract for communication between two players.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Send messages to the other player</li>
 *   <li>Receive messages from the other player</li>
 *   <li>Properly release resources when communication is finished</li>
 * </ul>
 * <p>
 * Different implementations support same-process (memory) and separate-process (TCP) communication.
 */
public interface IChannel extends AutoCloseable {

	void send(String message) throws IOException;
	String receive() throws IOException;

	@Override
	void close() throws IOException;
}