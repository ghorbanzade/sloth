//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.lang.UnsupportedOperationException;
import java.util.Objects;

/**
 * This class wraps recognition window as an array of region numbers whose
 * elements are the number of packets occured on the region.
 *
 * @author Pejman Ghorbanzade
 * @see Classifier
 * @see PacketReader
 * @see PacketProcessor
 * @see Posture
 */
public final class ActivityCode {

  /**
   * An activity code is simply a wrapper for an array of integers whose
   * elements represent regions of the recognition sphere.
   */
  private int count = 0;
  private final int[] code;
  private static final Logger log = Logger.getLogger(ActivityCode.class);

  /**
   * the posture object is created only once and is updated every time
   * a packet is processed.
   */
  public ActivityCode() {
    Config cfg = ConfigManager.get("config/main.properties");
    Model model = ModelManager.get(cfg.getAsInt("recognition.model.segments"));
    this.code = new int[model.getTotalRegions()];
    log.info("creating activity code");
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
      this.count++;
    }
  }

  /**
   * This method gives a percentile view of the activity code. It is the basis
   * of comparing similarity of one activity code with another.
   *
   * @return array containing probabilistic view of an activity code
   */
  private double[] getCode() {
    double[] out = new double[this.code.length];
    for (int i = 0; i < this.code.length; i++) {
      out[i] = (double) this.code[i] / this.count;
    }
    return out;
  }

  /**
   * Compares similarity of this object with a given activity code.
   *
   * @param act the activity code whose similarity should be measured
   * @return a measure of similarity between this code with another
   */
  public double findSimilarity(ActivityCode act) {
    double sum = 0;
    for (int i = 0; i < this.code.length; i++) {
      sum += Math.exp(-(Math.abs(this.getCode()[i] - act.getCode()[i])));
    }
    return sum / this.code.length;
  }

  /**
   * An activity code simply prints number of packets received in each region
   * since it has been created.
   *
   * @return a string representation of this activity code
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.code.length; i++) {
      sb.append(this.code[i]);
      sb.append(' ');
    }
    sb.append('\n');
    return sb.toString();
  }

}
