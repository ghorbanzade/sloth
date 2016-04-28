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
import java.nio.file.Path;

/**
 * This class defines an uploader as a runnable responsible to upload
 * files containing classified activities to the remote server.
 *
 * @author Pejman Ghorbanzade
 * @see FileQueue
 */
public final class Uploader implements Runnable {

  private Config cfgs;
  private final Config cfg = ConfigManager.get("config/main.properties");
  private final FTPClient ftp = new FTPClient();
  private final FileQueue fq;
  private static final Logger log = Logger.getLogger(Uploader.class);

  /**
   * An uploader is constructed based on the queue from which it should
   * upload the file.
   *
   * @param fq file queue from which files to be uploaded should be read
   */
  public Uploader(FileQueue fq) {
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
    String cfgsPath = this.cfg.getAsString("config.file.server");
    if (cfgsPath.isEmpty()) {
      log.error("configuration file for remote server is missing");
      throw new FatalException(this.getClass());
    }
    this.cfgs = ConfigManager.get(cfgsPath);
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
          if (!this.fq.isEmpty()) {
            this.upload();
          }
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
          log.info("sleep interrupted");
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
    while (!this.fq.isEmpty()) {
      this.uploadFile(this.fq.get());
    }
  }

  /**
   * This method contains the logic for uploading a given file to the server.
   *
   * @param path the path to the file that should be uploaded
   * @throws IOException if connection with remote server is disrupted
   */
  private void uploadFile(Path path) throws IOException {
    // TODO
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
