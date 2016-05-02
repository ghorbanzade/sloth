//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

/**
 * This class defines a custom exception that is thrown to hint that client
 * tried to access a node with an unidentified id.
 *
 * @author Pejman Ghorbanzade
 * @see PacketReader
 * @see Wsn
 */
public final class NoSuchNodeException extends RuntimeException {

  private static final Logger log =
      Logger.getLogger(NoSuchNodeException.class);

  /**
   * An exception is thrown if a given node id is not found in the wireless
   * sensor network.
   */
  public NoSuchNodeException() {
    log.error("unidentified node requested");
  }

}
