//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A packet reader is a worker thread whose job is to take strings read
 * from the buffer, parse them to retrieve their information and put them
 * on packet queue for updating the body posture.
 *
 * @author Pejman Ghorbanzade
 * @see Packet
 * @see PacketQueue
 * @see Parser
 * @see SerialQueue
 */
public final class PacketReader implements Runnable {

  private final Config cfg;
  private final PacketQueue pq;
  private final SerialQueue sq;
  private final ArrayList<Parser> parsers = new ArrayList<Parser>();
  private static final Logger log = Logger.getLogger(PacketReader.class);

  /**
   * A packet reader is constructed based on the serial queue from which
   * it should read data for parsing and the packet queue on which it
   * put parsed packets.
   *
   * @param sq queue from which sensor data should be read
   * @param pq queue to which packets should be written
   */
  public PacketReader(SerialQueue sq, PacketQueue pq) {
    this.sq = sq;
    this.pq = pq;
    this.cfg = ConfigManager.get("config/main.properties");
    this.parsers.add(new ActivityCode.Parser());
    this.parsers.add(new RawPacket.Parser());
  }

  /**
   * A packet reader should read sensor data from serial queue, parse them
   * into packets and put the packets on the packet queue.
   */
  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(this.cfg.getAsInt("packet.reader.sleep.interval"));
        while (!this.sq.isEmpty()) {
          String data = this.sq.get();
          try {
            Packet packet = this.parse(data);
            this.pq.put(packet);
          } catch (CurruptPacketException ex) {
            log.info("currupt packet discarded");
          }
        }
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        log.info("sleep interrupted");
      }
    }
    log.info("packet reader stopped by the main thread");
  }

  /**
   * This method parses received data to either a raw packet object or
   * an activity code object. It throws exception if performing this task
   * is not possible.
   *
   * @param data the buffer data received from a sensor node
   * @return either a raw packet object or an activity code object
   * @throws CurruptPacketException if data is missing expected information
   */
  private Packet parse(String data) throws CurruptPacketException {
    for (Parser parser: this.parsers) {
      StringTokenizer tokens = new StringTokenizer(data, "|");
      try {
        Packet packet = parser.parse(tokens);
        return packet;
      } catch (Packet.ParseException ex) {
        // it is okay if a parser fails to parse a packet
      }
    }
    throw new CurruptPacketException();
  }

}
