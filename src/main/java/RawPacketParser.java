//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.lang.NumberFormatException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 *
 *
 * @author Pejman Ghorbanzade
 * @see Parser
 */
public class RawPacketParser extends Parser {

  private static final Logger log = Logger.getLogger(RawPacketParser.class);

  /**
   *
   */
  public RawPacketParser() {
    // intentionally left blank.
  }

  /**
   * This method takes a number of tokens to check whether they can
   * construct a packet object. It is called by packet reader to see if the
   * received buffer is a raw unprocessed packet.
   *
   * @param st string tokens to be parsed
   * @return an raw packet object ready to be processed
   * @throws PacketFormatException if fails to parse tokens into a packet
   */
  @Override
  public Packet parse(StringTokenizer st) throws PacketFormatException {
    try {
      Node node = this.wsn.getNode(Integer.parseInt(st.nextToken()));
      int[] components = new int[RawPacket.Data.COUNT.getValue()];
      if (st.countTokens() != components.length) {
        throw new PacketFormatException();
      }
      for (int i = 0; i < components.length; i++) {
        components[i] = Integer.parseInt(st.nextToken());
      }
      return new RawPacket(node, components);
    } catch (NoSuchElementException
        | NumberFormatException
        | NoSuchNodeException ex
    ) {
      throw new PacketFormatException();
    }
  }

}
