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
   * Returns name of this activity.
   *
   * @return name of this activity
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the accuracy in percent with which this activity has been
   * classified.
   *
   * @return recognition accuracy of this activity
   */
  public double getAccuracy() {
    return this.accuracy;
  }

  /**
   * Returns the date this activity was constructed.
   *
   * @return the date this activity was constructed
   */
  public Date getDate() {
    return (Date) this.date.clone();
  }


  /**
   * Returns a string representation of this activity, suitable for printing
   * on standard output.
   *
   * @return a string representation of this activity
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.date.toString());
    sb.append(' ');
    sb.append(this.name);
    return sb.toString();
  }

}
