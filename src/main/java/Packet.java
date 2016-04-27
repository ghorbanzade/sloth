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
 */
public final class Packet {

  private int region;
  private final Node node;
  private final int[] data = new int[RawData.COUNT.getValue()];
  private final Logger log = Logger.getLogger(this.getClass());

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
    for (int i = 0; i < RawData.COUNT.getValue(); i++) {
      this.data[i] = data[i];
    }
  }

  /**
   * Once a packet is queued for processing, packet processor calls this method
   * to calculate the region the packet points to and update the activity code
   * of the posture object.
   *
   * @throws CurruptPacketException if packet is affected by dynamic acceleration
   */
  public void process() throws CurruptPacketException {
    // TODO
  }

  /**
   * This method gives the region number of the recognition sphere this packet
   * is pointing to. If the packet is not processed, the return value will be
   * null.
   *
   * @return the region packet is pointing to or null if it is not processed
   */
  public int getRegion() {
    return this.region;
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

  public enum RawData {

    /**
     * Components of a packet in form of enum items.
     */
    ACC_X(0),
    ACC_Y(1),
    ACC_Z(2),
    GYR_X(3),
    GYR_Y(4),
    GYR_Z(5),
    COUNT(6);

    private final int value;

    /**
     * Each enum item has a value for easier access when inside an array
     * of integers.
     *
     * @param value assign a numeric value to each enum item
     */
    private RawData(int value) {
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
