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
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;

/**
 *
 *
 *
 * @author Pejman Ghorbanzade
 */
public final class CloudConnector implements Runnable {

  private Config cfg;
  private static final Logger log = Logger.getLogger(PacketProcessor.class);

  /**
   *
   */
  public CloudConnector() {
    this.cfg = ConfigManager.get("config/main.properties");
  }

  /**
   *
   */
  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(this.cfg.getAsInt("cloud.connector.sleep.interval"));
        try {
          this.upload();
        } catch (IOException ex) {
          ex.printStackTrace();
          log.error("unable to create directory for classified activities");
        }
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        log.info("sleep interrupted");
      }
    }
    log.info("cloud connector stopped by the main thread");
  }

  /**
   *
   *
   * @throws IOException
   */
  private void upload() throws IOException {
    HashMap<File, Activity> hm = this.load();
    if (hm.isEmpty()) {
      log.trace("no files to transmit");
    } else {
      if (this.post(hm.values())) {
        for (File file: hm.keySet()) {
          FileUtils.forceDelete(file);
        }
      } else {
        log.warn("failed to post classified activities to remote server");
      }
    }
  }

  /**
   *
   *
   * @return
   * @throws IOException
   */
  public HashMap<File, Activity> load() throws IOException {
    File dir = new File(this.cfg.getAsString("dir.classified.activities"));
    FileUtils.forceMkdir(dir);
    String[] exts = {"json"};
    Iterator<File> it = FileUtils.iterateFiles(dir, exts, true);
    HashMap<File, Activity> hm = new HashMap<File, Activity>();
    Gson gson = new Gson();
    while (it.hasNext()) {
      File file = it.next();
      String content = FileUtils.readFileToString(file, "UTF-8");
      Activity act = gson.fromJson(content, Activity.class);
      hm.put(file, act);
      if (hm.size() > this.cfg.getAsInt("cc.max.post.size")) {
        break;
      }
    }
    return hm;
  }

  /**
   *
   *
   * @param acts activity objects to be posted to the cloud
   * @return true if activities are successfully posted to cloud
   * @throws IOException if httpclient find error while connecting with remote
   */
  private boolean post(Collection<Activity> acts) throws IOException {
    String postUrl = this.cfg.getAsString("cc.post.url");
    String content = this.prepareToPost(acts);
    HttpClient httpClient = HttpClientBuilder.create().build();
    HttpPost post = new HttpPost(postUrl);
    post.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
    post.setHeader("Content-type", "application/json");
    HttpResponse response = httpClient.execute(post);
    return (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
  }

  /**
   *
   *
   * @param acts activity objects to be parsed to a json string
   * @return a json string ready to be posted to remote server
   */
  private String prepareToPost(Collection<Activity> acts) {
    JsonObject msg = new JsonObject();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    JsonObject user = new JsonObject();
    user.addProperty("token", this.cfg.getAsString("cc.user.token"));
    JsonArray array = new JsonArray();
    for (Activity act: acts) {
      JsonObject json = new JsonObject();
      json.addProperty("activity_name", act.getName());
      json.addProperty("recognition_accuracy", act.getAccuracy());
      json.addProperty("recognition_date", sdf.format(act.getDate()));
      array.add(json);
    }
    msg.add("user", user);
    msg.add("activities", array);
    Gson gson = new Gson();
    return gson.toJson(msg);
  }

}

