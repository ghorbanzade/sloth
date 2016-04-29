//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

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
public final class Packet {

  private final Node node;
  private final int[] data = new int[Data.COUNT.getValue()];
  private static final Logger log = Logger.getLogger(Packet.class);

  /**
   * This static method takes a number of tokens to check whether they can
   * construct a packet object. It is called by packet reader to see if the
   * received buffer is a raw unprocessed packet.
   *
   * @param st string tokens to be parsed
   * @return an array of packet components
   * @throws PacketMismatchException if fails to parse tokens to packet items
   */
  public static int[] parse(StringTokenizer st) throws PacketMismatchException {
    int[] components = new int[Data.COUNT.getValue()];
    if (st.countTokens() != components.length) {
      throw new PacketMismatchException();
    }
    try {
      for (int i = 0; i < components.length; i++) {
        components[i] = Integer.parseInt(st.nextToken());
      }
    } catch (NumberFormatException ex) {
      throw new PacketMismatchException();
    }
    return components;
  }

  /**
   * A packet is constructed by packet reader in case data received from
   * sensor node is not preprocessed and contains raw acceleration data.
   * It holds raw acceleration information as well as information about
   * the sensor node from which it is received.
   *
   * @param node the sensor node from which this packet is received
   * @param data raw data received from the sensor node
   */
  public Packet(Node node, int[] data) {
    this.node = node;
    for (int i = 0; i < Data.COUNT.getValue(); i++) {
      this.data[i] = data[i];
    }
  }

  /**
   * This method updates sensed values with calibration results fetched from
   * a previously stored calibration object.
   */
  public void calibrate() {
    // TODO
  }

  /**
   * This method gives the sensor node that transmitted this packet. It is
   * used by the packet processor to update the posture.
   *
   * @return the object representing the the sensor node that sent this packet
   */
  public Node getNode() {
    return this.node;
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
        this.node.getName(),
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
    private int getValue() {
      return this.value;
    }

  }

}
