//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.util.Date;

/**
 *
 *
 * @author Pejman Ghorbanzade
 */
public final class Activity {

  private final Date date = new Date();
  private final String name;
  private boolean classified = true;
  private final double accuracy;
  private final ActivityCode code;
  private static final Logger log = Logger.getLogger(Activity.class);

  /**
   *
   *
   * @param name
   * @param code
   */
  public Activity(String name, ActivityCode code) {
    this.name = name;
    this.code = code;
    this.accuracy = 100;
    this.classified = false;
  }

  /**
   *
   *
   * @param name name of this activity
   * @param accuracy
   */
  public Activity(String name, double accuracy) {
    this.code = null;
    this.name = name;
    this.accuracy = accuracy;
  }

  /**
   * Returns a string representation of this activity, suitable for printing
   * on standard output.
   *
   * @return a string representation of this activity
   */
  @Override
  public String toString() {
    return this.name;
  }

}
