//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

/**
 * This class defines a custom exception that is thrown to hint the packet
 * processor that the packet must be discarded.
 *
 * @author Pejman Ghorbanzade
 * @see Packet
 * @see PacketProcessor
 */
public final class CurruptPacketException extends RuntimeException {

  private static final Logger log = Logger.getLogger(CurruptPacketException.class);

  /**
   * A currupt packet exception is thrown if a packet is found severely
   * affected by dynamic accelaration. This exception is not fatal and
   * must be caught by packet processor.
   */
  public CurruptPacketException() {
    log.info("currupt packet detected");
  }

}
