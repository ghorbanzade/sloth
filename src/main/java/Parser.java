//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import java.util.StringTokenizer;

/**
 * This interface is implemented by classes used by packet reader that
 * may parse a string into a packet object.
 *
 * @author Pejman Ghorbanzade
 * @see PacketReader
 * @see ActivityCode.Parser
 * @see RawPacket.Parser
 */
public interface Parser {

  /**
   * Return a packet object to be put on packet queue for processing by
   * packet processor. A packet object can either be an activity code
   * or a packet with raw acceleration data.
   *
   * @param tokens data components of data transmitted by a sensor node
   * @return a packet object to be given to packet processor
   * @throws Packet.ParseException if tokens cannot be parsed to a packet
   */
  public Packet parse(StringTokenizer tokens) throws Packet.ParseException;

}
