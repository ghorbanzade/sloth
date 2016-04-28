//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

/**
 * This class defines a custom exception that is thrown to hint a given
 * string tokenizer object cannot be converted to packet components.
 *
 * @author Pejman Ghorbanzade
 * @see Packet
 * @see PacketReader
 */
public final class PacketMismatchException extends RuntimeException {

  private static final Logger log =
      Logger.getLogger(PacketMismatchException.class);

  /**
   * An exception is thrown if a given string tokenizer cannot be converted
   * to components of a packet. This exception is not fatal and must be caught
   * by packet reader.
   */
  public PacketMismatchException() {
    // intentionally left blank
  }

}
