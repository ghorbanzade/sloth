//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 *
 *
 * @author Pejman Ghorbanzade
 */
public abstract class Activity {

  private final Date date;
  private final String name;
  private final double accuracy;
  private final HashMap<Node, ActivityCode> posture;
  private static final Logger log = Logger.getLogger(Activity.class);

  /**
   *
   *
   * @param name name of this activity
   * @param posture
   */
  public Activity(String name, HashMap<Node, ActivityCode> posture) {
    this.name = name;
    this.accuracy = 100;
    this.date = new Date();
    this.posture = posture;
  }

  /**
   *
   * @param name
   * @param posture
   * @param date
   */
  public Activity(
      String name, HashMap<Node, ActivityCode> posture, Date date
  ) {
    this.name = name;
    this.date = (Date) date.clone();
    this.accuracy = 100;
    this.posture = posture;
  }

  /**
   *
   *
   * @param name name of this activity
   * @param accuracy accuracy with which the activity is classified
   */
  public Activity(String name, double accuracy) {
    this.name = name;
    this.posture = null;
    this.accuracy = accuracy;
    this.date = new Date();
  }

  /**
   * Writes data regarding this activity into a json file.
   */
  public abstract void log();

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
   * Returns the activity code the describes body posture during this activity
   *
   * @return a map of the codes received from sensor nodes
   */
  public HashMap<Node, ActivityCode> getPosture() {
    return this.posture;
  }

  /**
   * Returns a string representation of this activity, suitable for printing
   * to standard output.
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

  /**
   *
   *
   * @author Pejman Ghorbanzade
   */
  public static class Classified extends Activity {

    /**
     *
     *
     * @param name
     * @param accuracy
     */
    public Classified(String name, double accuracy) {
      super(name, accuracy);
    }

    /**
     *
     */
    @Override
    public void log() {
    }

  }

  /**
   *
   *
   * @author Pejman Ghorbanzade
   */
  public static class Learned extends Activity {

    private final Config cfg = ConfigManager.get("config/main.properties");

    /**
     *
     *
     * @param name
     * @param posture
     */
    public Learned(String name, HashMap<Node, ActivityCode> posture) {
      super(name, posture);
    }

    /**
     *
     *
     * @param name
     * @param posture
     * @param date
     */
    public Learned(
        String name, HashMap<Node, ActivityCode> posture, Date date
    ) {
      super(name, posture, date);
    }

    /**
     *
     */
    @Override
    public void log() {
      try {
        File dir = new File(this.cfg.getAsString("dir.learned.activities"));
        FileUtils.forceMkdir(dir);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss'.json'");
        File file = FileUtils.getFile(dir, sdf.format(new Date()));
        String content = this.prepareContent();
        FileUtils.writeStringToFile(file, content, "UTF-8");
      } catch (IOException ex) {
        log.error("unable to store the activity instance");
      }
    }

    /**
     * Returns a string representation of the activity which will be
     * the content of the file to be created for that learned activity
     * instance.
     *
     * @return a string representation of the activity to be written in file
     */
    private String prepareContent() {
      JsonArray posture = new JsonArray();
      for (Node node: this.getPosture().keySet()) {
        JsonObject bodyPart = new JsonObject();
        bodyPart.addProperty("node", node.getId());
        JsonArray code = new JsonArray();
        for (double num: this.getPosture().get(node).getCode()) {
          code.add(Math.round(num * 1000) / 1000.0);
        }
        bodyPart.add("code", code);
        posture.add(bodyPart);
      }
      JsonObject msg = new JsonObject();
      msg.addProperty("name", this.getName());
      msg.add("posture", posture);
      Gson gson = new Gson();
      return gson.toJson(msg);
    }

  }

}
