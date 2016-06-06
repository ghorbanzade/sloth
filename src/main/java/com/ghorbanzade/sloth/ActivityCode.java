//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * This class wraps recognition window as an array of region numbers whose
 * elements are the number of packets mapped to that region.
 *
 * @author Pejman Ghorbanzade
 * @see ActivityCode.Parser
 * @see Classifier
 * @see Packet
 * @see PacketReader
 * @see Posture
 */
public final class ActivityCode extends Packet {

  private final double[] code;
  private static final Config cfg = ConfigManager.get("config/main.properties");
  private static final Logger log = Logger.getLogger(ActivityCode.class);

  /**
   * Creates an activity code with an empty region array for a given sensor
   * node.
   *
   * @param node the node for which activity code is created
   */
  public ActivityCode(Node node) {
    super(node);
    Model model = ModelManager.get(cfg.getAsInt("recognition.model.segments"));
    this.code = new double[model.getTotalRegions()];
    Arrays.fill(code, 0);
  }

  /**
   * Creates an activity code based on contents of a parsed packet. This
   * constructor is used by {@link ActivityCode.Parser}.
   *
   * @param node the node for which activity code is created
   * @param code the array of regions that is received from sensor nodes
   */
  public ActivityCode(Node node, double[] code)
      throws UnsupportedOperationException {
    super(node);
    Model model = ModelManager.get(cfg.getAsInt("recognition.model.segments"));
    if (code.length < model.getTotalRegions()) {
      throw new UnsupportedOperationException();
    } else {
      this.code = Arrays.copyOf(code, model.getTotalRegions());
    }
  }

  /**
   * This method allows packet processor to update activity code for a sensor
   * node based on region of a packet recently received from that node.
   *
   * @param region the region number that needs to be incremented
   * @throws UnsupportedOperationException if packet is not processed
   */
  public void update(int region) throws UnsupportedOperationException {
    if (this.code.length < region) {
      log.error("region number exceeds activity code size");
      throw new UnsupportedOperationException();
    } else {
      this.code[region - 1]++;
    }
  }

  /**
   * Adds all the packets of an activity code to this code preserving their
   * distribution in different regions of the sphere.
   *
   * @param code the activity code whose packets should be added to this code
   * @throws UnsupportedOperationException if size of given code does not match
   */
  public void update(ActivityCode code) throws UnsupportedOperationException {
    if (this.size() != code.size()) {
      log.error("incompatible activity code");
      throw new UnsupportedOperationException();
    } else {
      for (int i = 0; i < this.code.length; i++) {
        this.code[i] += code.getCode(i);
      }
    }
  }

  /**
   * This method gives a percentile view of the activity code. It is the basis
   * of comparing similarity of one activity code with another.
   *
   * @return array containing probabilistic view of an activity code
   */
  public double[] getCode() {
    double count = 0;
    double[] out = new double[this.code.length];
    for (int i = 0; i < this.code.length; i++) {
      count += this.code[i];
    }
    for (int i = 0; i < this.code.length; i++) {
      out[i] = this.code[i] / count;
    }
    return out;
  }

  /**
   * Returns the number of packets mapped to the region whose number is
   * given as parameter.
   *
   * @param index index of the element asked for
   * @return number of packets mapped to the given region number
   */
  private double getCode(int index) {
    return this.code[index];
  }

  /**
   * Returns the number of total regions of the model based on which this
   * activity code is constructed.
   *
   * @return the length of the array
   */
  private int size() {
    return this.code.length;
  }

  /**
   * Returns how much this activity code is similar to another given activity
   * code.
   *
   * @param act the activity code whose similarity should be measured
   * @return a measure of similarity between this code with the given code
   */
  public double findSimilarity(ActivityCode act) {
    double sum = 0;
    for (int i = 0; i < this.code.length; i++) {
      sum += Math.exp(-(Math.abs(this.getCode()[i] - act.getCode()[i])));
    }
    return sum / this.size();
  }

  /**
   * Returns a string representation of the activity code as compared with
   * other activity codes.
   *
   * @return a string representation of this activity code
   */
  @Override
  public String toString() {
    double[] code = this.getCode();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.size(); i++) {
      sb.append(String.format("%1.2f ", code[i]));
    }
    return sb.toString();
  }

  /**
   * This class implements {@link Parser} to define how received data from
   * a sensor node can be parsed a preprocessed activity code.
   *
   * @author Pejman Ghorbanzade
   * @see Parser
   */
  public static class Parser implements com.ghorbanzade.sloth.Parser {

    private Wsn wsn;
    private Model model;
    private static final Logger log
        = Logger.getLogger(ActivityCode.class);

    /**
     * Prepares the wireless sensor network and model objects to perform
     * faster parsing.
     */
    public Parser() {
      this.wsn = WsnManager.getWsn(cfg.getAsString("config.file.wsn"));
      this.model = ModelManager.get(cfg.getAsInt("recognition.model.segments"));
    }

    /**
     * Checks whether a given set of string tokens can be parsed to an
     * activity queue. It is called by packet reader to see if the
     * received buffer is a preprocessed array describing distribution of
     * packets over the recognition sphere.
     *
     * @param st string tokens to be parsed
     * @return an activity code object to be applied to posture
     * @throws Packet.ParseException if fails to parse tokens into a packet
     */
    @Override
    public Packet parse(StringTokenizer st) throws Packet.ParseException {
      try {
        Node node = this.wsn.getNode(Integer.parseInt(st.nextToken()));
        double[] components = new double[this.model.getTotalRegions()];
        if (st.countTokens() != components.length) {
          throw new Packet.ParseException();
        }
        for (int i = 0; i < components.length; i++) {
          components[i] = (double) Integer.parseInt(st.nextToken(), 16);
        }
        return new ActivityCode(node, components);
      } catch (NoSuchElementException
          | NumberFormatException
          | Wsn.NoSuchNodeException ex
      ) {
        throw new Packet.ParseException();
      }
    }

  }

}
