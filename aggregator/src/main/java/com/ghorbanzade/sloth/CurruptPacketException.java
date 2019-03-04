//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

/**
 * Custom exception thrown to hint that packet processor should discard
 * the packet.
 *
 * @author Pejman Ghorbanzade
 * @see Packet
 * @see PacketProcessor
 */
public final class CurruptPacketException extends RuntimeException {

  private static final Logger log =
      Logger.getLogger(CurruptPacketException.class);

  /**
   * Exception is thrown if a packet is found severely affected by dynamic
   * accelaration. This exception is not fatal and must be caught by packet
   * processor.
   */
  public CurruptPacketException() {
    log.info("currupt packet detected");
  }

}
