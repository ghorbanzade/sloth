//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

/**
 * This class defines the recognition model which will be used by packet
 * processor to update the body posture based on received packets.
 *
 * @author Pejman Ghorbanzade
 * @see ModelManager
 */
public final class Model {

  private int[] prsm;
  private int[] aprm;
  private int segments;
  private int totalRegions;
  private static final Logger log = Logger.getLogger(Model.class);

  /**
   * Information about a recognition model is calculated based on the number
   * of segments of the sphere in zenith direction.
   *
   * @param segments the number of segments on the sphere in zenith direction
   */
  public Model(int segments) {
    this.segments = segments;
    double[] trsm = new double[segments + 1];
    double[] strm = new double[segments];
    this.prsm = new int[segments];
    this.aprm = new int[segments];
    for (int i = 0; i < segments + 1; i++) {
      trsm[i] = i * Math.PI / segments;
    }
    for (int i = 0; i < segments; i++) {
      strm[i] = Math.cos(trsm[i]) - Math.cos(trsm[i + 1]);
      this.prsm[i] = (int) Math.round(strm[i] / strm[0]);
    }
    this.aprm[0] = 0;
    for (int i = 1; i < segments; i++) {
      this.aprm[i] = aprm[i - 1] + prsm[i - 1];
    }
    this.totalRegions = this.aprm[segments - 1] + this.prsm[segments - 1];
    log.info("model constructed with " + this.totalRegions + " total regions");
  }

  /**
   * To construct an activity code, information about the total number of
   * regions on the sphere is needed.
   *
   * @return the number of total regions on the recognition sphere
   */
  public int getTotalRegions() {
    return this.totalRegions;
  }

  /**
   * This method calculates the number assigned to the region of recognition
   * sphere to which a packet with given accelrations poitn to.
   *
   * @param accx acceleration in x
   * @param accy acceleration in y
   * @param accz acceleration in z
   * @return the number assigned to the region of sphere this packet points to
   */
  public int getRegion(int accx, int accy, int accz) {
    double phi = Math.atan2(accy, accx) * 180.0 / Math.PI + 180.0;
    double theta = Math.atan2(
            Math.sqrt(Math.pow(accx, 2) + Math.pow(accy, 2)), accz
        ) * 180.0 / Math.PI;
    int regionTheta = (int) Math.floor(theta * this.segments / 180.0) + 1;
    int regionPhi;
    if (phi >= 360.0 - 180.0 / this.prsm[regionTheta - 1]) {
      regionPhi = 1;
    } else {
      regionPhi = 1 + (int) Math.floor(
          phi * this.prsm[regionTheta - 1] / 360.0 + 0.5
      );
    }
    return this.aprm[regionTheta - 1] + regionPhi;
  }

}
