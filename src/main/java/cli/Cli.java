//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth.cli;

import com.ghorbanzade.sloth.Config;
import com.ghorbanzade.sloth.ConfigManager;
import com.ghorbanzade.sloth.FatalException;
import com.ghorbanzade.sloth.LearnCommand;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 *
 * @author Pejman Ghorbanzade
 * @see Instruction
 */
public final class Cli {

  private final Config cfg = ConfigManager.get("config/main.properties");
  private final History history = new History();
  private final Scanner scanner = new Scanner(System.in, "UTF-8");
  private final Map<String, Command> commands = new HashMap<String, Command>();
  private static final Logger log = Logger.getLogger(Cli.class);

  /**
   *
   */
  public Cli() {
    this.initCommands();
  }

  /**
   *
   */
  public void initCommands() {
    this.commands.put("learn", new LearnCommand());
    //this.commands.put("exit", new ExitCommand());
    //try {
    //  String[] names = {"learn"};
    //  for (String name: names) {
    //    Object obj = Class.forName(name).getConstructor().newInstance();
    //    this.commands.put(name, (Command) obj);
    //  }
    //} catch (Exception ex) {
    //}
  }

  /**
   *
   *
   * @param instruction
   * @throws FatalException
   */
  public void execute(Instruction instruction) throws FatalException {
    try {
      Command command = this.parse(instruction);
      command.check(instruction);
      command.execute(instruction);
    } catch (InvalidCommandException ex) {
      log.info(ex.getMessage());
    }
  }

  /**
   *
   *
   * @return an executable command object corresponding to the given instruction
   * @throws InvalidCommandException if given command is not known by the CLI.
   */
  private Command parse(Instruction instruction) throws InvalidCommandException {
    String name = instruction.getName();
    if (!this.commands.containsKey(name)) {
      throw new InvalidCommandException(instruction, "command not found");
    }
    return this.commands.get(name);
  }

  /**
   *
   *
   * @return
   */
  public Instruction getInstruction() {
    while (true) {
      System.out.printf(this.getPrompt());
      String str = scanner.nextLine();
      if (!str.isEmpty()) {
        Instruction instruction = new Instruction(str);
        this.history.add(instruction);
        return instruction;
      }
    }
  }

  /**
   *
   *
   * @return
   */
  public String getPrompt() {
    return "$ ";
  }

  /**
   * History of a CLI is simply a list of most recent instructions given
   * by the user. The size of the list can be configured by client using
   * the <pre>set cli.history.size</pre> command.
   *
   * @author Pejman Ghorbanzade
   * @see Cli
   */
  private final class History {

    private int limit;
    private final List<Instruction> list;

    /**
     * A history is simply a list of most recent instructions given by the user
     * to the Cli object. The list has a limited size which is determined based
     * on <pre>cli.history.size</pre> paramter in the configuration file.
     */
    public History() {
      this.limit = cfg.getAsInt("cli.history.size");
      this.list = new ArrayList<Instruction>(this.limit);
    }

    /**
     * Adds a new instruciton to the list of instructions recorded in history.
     *
     * @param instruction the instruction recently executed by cli
     */
    public void add(Instruction instruction) {
      if (this.list.size() == this.limit) {
        this.list.remove(0);
      }
      this.list.add(instruction);
    }

    /**
     * Updates the number of instructions that can be saved in the history.
     *
     * @param limit the number of instructions recorded in history
     */
    public void setSize(int limit) {
      this.limit = limit;
    }

    /**
     * Returns a list of instructions stored in the history.
     *
     * @return list of instructions recorded in history
     */
    public List<Instruction> getInstructions() {
      return this.list;
    }

  }

}
