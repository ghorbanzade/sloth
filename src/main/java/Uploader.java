//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * This class defines an uploader as a runnable responsible to upload
 * files containing classified activities to the remote server.
 *
 * @author Pejman Ghorbanzade
 * @see FileQueue
 */
public final class Uploader implements Runnable {

  /**
   * Uploader needs the main configuration file and the queue that holds
   * the files to be uploaded.
   */
  private final ConfigManager cfg;
  private ConfigManager cfgs;
  private final FTPClient ftp = new FTPClient();
  private final FileQueue fq;
  private final Logger log = Logger.getLogger(this.getClass());

  /**
   * An uploader is constructed based on the configuration file and the queue
   * from which it should upload the file.
   */
  public Uploader(ConfigManager cfg, FileQueue fq) {
    this.cfg = cfg;
    this.fq = fq;
  }

  /**
   * This method loads the server-specific configuration file, using the
   * filename specified in the main configuration file. This method should
   * be called before passing the object to a thread.
   *
   * @throws FatalException if the server configuration file is not specified
   */
  public void init() throws FatalException {
    String cfgsPath = this.cfg.getAsString("server.filename");
    if (cfgsPath.isEmpty()) {
      log.error("configuration file for remote server is missing");
      throw new FatalException(this.getClass());
    }
    this.cfgs = new ConfigManager(cfgsPath);
    this.cfgs.init();
  }

  /**
   * When an uploader is passed to a thread, it first connects to the web
   * server and after each sleep interval, uploads all the files that are
   * in the file queue. It continues to do so until it is interrupted by
   * the main thread.
   */
  @Override
  public void run() {
    try {
      this.connect();
      while (!Thread.currentThread().isInterrupted()) {
        try {
          Thread.sleep(this.cfg.getAsInt("server.upload.interval"));
          if (!this.fq.getQueue().isEmpty()) {
            this.upload();
          }
        } catch (InterruptedException ex) {
          log.warn("sleep interrupted");
        }
      }
    } catch (IOException ex) {
      log.error("connection disrupted with remote serve");
    } finally {
      this.disconnect();
    }
  }

  /**
   * This method fetches files queued by the classifier and uploads them
   * to the remote serve one by one.
   *
   * @throws IOException if connection with remote server is disrupted
   */
  private void upload() throws IOException {
  }

  /**
   * An uploader fetches login credentials from server configuration file
   * and tries to connect the remote server.
   *
   * @throws IOException if connection with remote server is not successful
   */
  private void connect() throws IOException {
    this.ftp.connect(this.cfgs.getAsString("server.address"));
    this.ftp.login(
        this.cfgs.getAsString("server.user"),
        this.cfgs.getAsString("server.pass")
    );
    if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
      log.info("connected to remote server");
    } else {
      this.ftp.disconnect();
      log.warn("remote server refused connection");
    }
  }

  /**
   * Uploader disconnects from remote server, once it is signaled to terminate.
   *
   * @throws IOException if uploader cannot disconnect from remote server
   */
  private void disconnect() {
    if (this.ftp.isConnected()) {
      try {
        this.ftp.disconnect();
      } catch (IOException ex) {
        log.warn("failed to disconnect from remote server");
      }
    }
  }

}