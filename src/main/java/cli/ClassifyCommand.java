//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth.cli;

import com.ghorbanzade.sloth.Classifier;
import com.ghorbanzade.sloth.Config;
import com.ghorbanzade.sloth.ConfigManager;
import com.ghorbanzade.sloth.PacketProcessor;
import com.ghorbanzade.sloth.PacketQueue;
import com.ghorbanzade.sloth.PacketReader;
import com.ghorbanzade.sloth.Posture;
import com.ghorbanzade.sloth.ResourceManager;
import com.ghorbanzade.sloth.SerialQueue;
import com.ghorbanzade.sloth.SerialReader;

import com.ghorbanzade.sloth.cli.Cli;
import com.ghorbanzade.sloth.cli.Command;
import com.ghorbanzade.sloth.cli.Instruction;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the command that classifies an activity in real-time.
 *
 * @author Pejman Ghorbanzade
 */
public final class ClassifyCommand extends Command {

  private static final Logger log = Logger.getLogger(ClassifyCommand.class);

  /**
   * Classifies the activity being performed in real-time. Reads packets
   * from serial port, constructs a posture and classifies it based on
   * previously learned models.
   *
   * @param instruction the instruction as given by user
   * @throws Cli.Exception if an error occurs during classification
   */
  @Override
  public void execute(Instruction instruction) throws Cli.Exception {
    Config cfg = ConfigManager.get("config/main.properties");
    ResourceManager rm = new ResourceManager();
    SerialQueue sq = new SerialQueue();
    PacketQueue pq = new PacketQueue();
    Posture posture = new Posture();
    SerialReader sr = new SerialReader(sq);
    rm.add(sr);
    List<Thread> threads = new ArrayList<Thread>();
    threads.add(new Thread(new PacketReader(sq, pq)));
    threads.add(new Thread(new PacketProcessor(pq, posture)));
    threads.add(new Thread(new Classifier(posture)));
    try {
      Runtime.getRuntime().addShutdownHook(new Thread(rm));
      for (Thread thread: threads) {
        thread.start();
      }
      sr.open(cfg.getAsString("serial.name"));
      Thread.sleep(cfg.getAsInt("serial.listening.time"));
    } catch (InterruptedException ex) {
      log.debug("learning was interrupted");
    } finally {
      for (Thread thread: threads) {
        thread.interrupt();
      }
    }
  }

}
