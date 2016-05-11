//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import com.ghorbanzade.sloth.cli.Command;
import com.ghorbanzade.sloth.cli.Instruction;
import com.ghorbanzade.sloth.cli.InvalidCommandException;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Pejman Ghorbanzade
 */
public final class LearnCommand implements Command {

  private static final Logger log = Logger.getLogger(LearnCommand.class);

  @Override
  public void check(Instruction instruction) throws InvalidCommandException {
    if (instruction.getArguments().isEmpty()) {
      throw new InvalidCommandException(instruction, "argument is missing");
    }
  }

  @Override
  public void execute(Instruction instruction) throws FatalException {
    Config cfg = ConfigManager.get("config/main.properties");
    ResourceManager rm = new ResourceManager();
    String actName = instruction.getArguments().get(0);
    SerialQueue sq = new SerialQueue();
    PacketQueue pq = new PacketQueue();
    Posture posture = new Posture();
    SerialReader sr = new SerialReader(sq);
    rm.add(sr);
    List<Thread> threads = new ArrayList<Thread>();
    threads.add(new Thread(new PacketReader(sq, pq)));
    threads.add(new Thread(new PacketProcessor(pq, posture)));
    threads.add(new Thread(new Learner(posture, actName)));
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

	/**
   *
   */
  public LearnCommand() {
		// intentionally left empty
  }

}
