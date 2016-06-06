//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * This class defines a packet as a container of unprocessed raw acceleration
 * data that is queued by packet reader for processing and processed by packet
 * processor to update the activity code of the posture object.
 *
 * @author Pejman Ghorbanzade
 * @see PacketReader
 * @see PacketProcessor
 * @see PacketQueue
 */
public final class RawPacket extends Packet {

  private final int[] data = new int[Data.COUNT.getValue()];

  /**
   * A packet is constructed by packet reader in case data received from
   * sensor node is not preprocessed and contains raw acceleration data.
   * It holds raw acceleration information as well as information about
   * the sensor node from which it is received.
   *
   * @param node the sensor node from which this packet is received
   * @param data raw data received from the sensor node
   */
  public RawPacket(Node node, int[] data) {
    super(node);
    for (int i = 0; i < Data.COUNT.getValue(); i++) {
      this.data[i] = data[i];
    }
  }

  /**
   * This method gives one of the components of the packet object whose type
   * matches the given parameter.
   *
   * @param component a sensed parameter whose value is called for
   * @return the value of the given sensed parameter type
   */
  public int getComponent(Data component) {
    return this.data[component.getValue()];
  }

  /**
   * This method describes how information about a packet should be printed.
   *
   * @return a string containing useful information about the received packet
   */
  @Override
  public String toString() {
    return String.format("%s %d %d %d",
        this.getNode().getName(),
        this.data[Data.ACC_X.getValue()],
        this.data[Data.ACC_Y.getValue()],
        this.data[Data.ACC_Z.getValue()]
    );
  }

  public enum Data {

    /**
     * Components of a packet in form of enum items.
     */
    ACC_X(0),
    ACC_Y(1),
    ACC_Z(2),
    COUNT(3);

    private final int value;

    /**
     * Each enum item has a value for easier access when inside an array
     * of integers.
     *
     * @param value assign a numeric value to each enum item
     */
    private Data(int value) {
      this.value = value;
    }

    /**
     * Getter method to get value assigned to an enum which is rquivalent
     * to its index.
     *
     * @return numeric value assigned to enum item
     */
    public int getValue() {
      return this.value;
    }

  }

  /**
   * This class implements {@link Parser} to define how received data from
   * a sensor node can be parsed into a raw packet.
   *
   * @author Pejman Ghorbanzade
   * @see Parser
   */
  public static class Parser implements com.ghorbanzade.sloth.Parser {

    private Wsn wsn;

    /**
     * Prepares the wireless sensor network object to perform faster parsing.
     */
    public Parser() {
      Config cfg = ConfigManager.get("config/main.properties");
      this.wsn = WsnManager.getWsn(cfg.getAsString("config.file.wsn"));
    }

    /**
     * Check whether a given set of string tokens can be parsed to a raw
     * packet. It is called by packet reader to see if the received buffer
     * is a raw unprocessed packet.
     *
     * @param st string tokens to be parsed
     * @return an raw packet object ready to be processed
     * @throws Packet.ParseException if fails to parse tokens into a packet
     */
    @Override
    public Packet parse(StringTokenizer st) throws Packet.ParseException {
      try {
        Node node = this.wsn.getNode(Integer.parseInt(st.nextToken()));
        int[] components = new int[RawPacket.Data.COUNT.getValue()];
        if (st.countTokens() != components.length) {
          throw new Packet.ParseException();
        }
        for (int i = 0; i < components.length; i++) {
          components[i] = Integer.parseInt(st.nextToken());
        }
        return new RawPacket(node, components);
      } catch (NoSuchElementException
          | NumberFormatException
          | Wsn.NoSuchNodeException ex
      ) {
        throw new Packet.ParseException();
      }
    }

  }

}
