//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

/**
 * This class defines a packet as a container of unprocessed raw acceleration
 * data that is queued by packet reader for processing and processed by packet
 * processor to update the activity code of the posture object.
 *
 * @author Pejman Ghorbanzade
 * @see PacketReader
 * @see PacketProcessor
 * @see PacketQueue
 * @see Parser
 */
public abstract class Packet {

  private final Node node;

  /**
   * A packet is constructed by packet reader in case data received from
   * sensor node is not preprocessed and contains raw acceleration data.
   * It holds raw acceleration information as well as information about
   * the sensor node from which it is received.
   *
   * @param node the sensor node from which this packet is received
   */
  public Packet(Node node) {
    this.node = node;
  }

  /**
   * This method is used to update the posture.
   *
   * @return the object representing the the sensor node that sent this packet
   */
  public Node getNode() {
    return this.node;
  }

  /**
   * Custom exception that thrown when ${@link Parser} failes to create
   * a packet object using the given string tokenizer. This exception is
   * not fatal and must be caught by packet reader.
   *
   * @author Pejman Ghorbanzade
   * @see Packet
   */
  public static final class ParseException extends RuntimeException {

    /**
     * Custom exception thrown if a given string tokenizer cannot be converted
     * to components of a packet.
     */
    public ParseException() {
      // intentionally left blank
    }

  }

}
