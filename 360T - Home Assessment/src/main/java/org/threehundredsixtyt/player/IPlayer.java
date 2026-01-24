package org.threehundredsixtyt.player;

/**
 * Interface defining player behavior contract.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Define the core player execution method</li>
 *   <li>Provide uniform way to interact with players regardless of role</li>
 * </ul>
 */
@FunctionalInterface
public interface IPlayer {

	void play();

}