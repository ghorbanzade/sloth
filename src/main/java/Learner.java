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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;

/**
 *
 *
 * @author Pejman Ghorbanzade
 * @see ActivityQueue
 */
public final class Learner implements Runnable {

  private final Config cfg;
  private final Wsn wsn;
  private final Model model;
  private final String name;
  private final ActivityQueue aq;
  private final Posture posture;
  private static final Logger log = Logger.getLogger(Learner.class);

  /**
   *
   *
   * @param posture the posture that should be learned
   * @param aq the queue on which classified activity should be put
   * @param name name of the activity to be learned
   */
  public Learner(Posture posture, ActivityQueue aq, String name) {
    this.aq = aq;
    this.name = name;
    this.posture = posture;
    this.cfg = ConfigManager.get("config/main.properties");
    this.wsn = WsnManager.getWsn(this.cfg.getAsString("config.file.wsn"));
    this.model = ModelManager.get(cfg.getAsInt("recognition.model.segments"));
  }

  /**
   *
   */
  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(this.cfg.getAsInt("learner.sleep.interval"));
        this.learn();
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        log.info("sleep interrupted");
      }
    }
    log.info("learner stopped by the main thread");
  }

  /**
   *
   */
  private void learn() {
    HashMap<Node, ActivityCode> code = new HashMap<Node, ActivityCode>();
    for (Node node: this.posture.getNodes()) {
      code.put(node, this.posture.get(node));
    }
    Activity act = new Activity(this.name, code);
    this.posture.reset();
    this.log(act);
  }

  /**
   *
   *
   * @param act the activity to be logged
   */
  private void log(Activity act) {
    try {
      File dir = new File(this.cfg.getAsString("dir.learned.activities"));
      FileUtils.forceMkdir(dir);
      SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss'.json'");
      File file = FileUtils.getFile(dir, sdf.format(new Date()));
      String content = this.prepareContent(act);
      FileUtils.writeStringToFile(file, content, "UTF-8");
    } catch (IOException ex) {
      log.error("unable to store the activity instance");
    }
  }

  /**
   * Returns a string representation of the activity which will be the content
   * of the file to be created for that learned activity instance.
   *
   * @param act the activity to be described
   * @return a string representation of the activity as to be written in file
   */
  private String prepareContent(Activity act) {
    JsonArray posture = new JsonArray();
    for (Node node: act.getPosture().keySet()) {
      JsonObject bodyPart = new JsonObject();
      bodyPart.addProperty("node", node.getId());
      JsonArray code = new JsonArray();
      for (double num: act.getPosture().get(node).getCode()) {
        code.add(Math.round(num * 1000) / 1000.0);
      }
      bodyPart.add("code", code);
      posture.add(bodyPart);
    }
    JsonObject msg = new JsonObject();
    msg.addProperty("name", act.getName());
    msg.add("posture", posture);
    Gson gson = new Gson();
    return gson.toJson(msg);
  }

}
