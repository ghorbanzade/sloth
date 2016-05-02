//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

import java.util.StringTokenizer;

/**
 *
 *
 * @author Pejman Ghorbanzade
 * @see ActivityCodeParser
 * @see PacketReader
 * @see RawPacketParser
 */
public abstract class Parser {

  protected Wsn wsn;
  private static final Logger log = Logger.getLogger(Parser.class);

  /**
   *
   */
  public Parser() {
    Config cfg = ConfigManager.get("config/main.properties");
    this.wsn = WsnManager.getWsn(cfg.getAsString("config.file.wsn"));
  }

  /**
   *
   *
   * @param tokens
   * @return
   * @throws
   */
  public abstract Packet parse(StringTokenizer tokens)
      throws PacketFormatException;

}
