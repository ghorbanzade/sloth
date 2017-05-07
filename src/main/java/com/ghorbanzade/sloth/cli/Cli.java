//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth.cli;

import com.ghorbanzade.sloth.Config;
import com.ghorbanzade.sloth.ConfigManager;
import com.ghorbanzade.sloth.parser.CommandFileParser;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Defines a command line interface for the Sloth program.
 *
 * @author Pejman Ghorbanzade
 * @see Instruction
 */
public final class Cli {

  private final Config cfg;
  private final History history;
  private final Scanner scanner = new Scanner(System.in, "UTF-8");
  private final Map<String, Command> commands = new HashMap<String, Command>();
  private static final Logger log = Logger.getLogger(Cli.class);

  /**
   * constructs the CLI with a set of recognizable commands.
   *
   * @param config main configuration parameters of the program
   */
  public Cli(Config config) {
    this.cfg = config;
    this.history = new History();
    this.initCommands();
  }

  /**
   * Initializes a list of commands recognized by the CLI.
   */
  private void initCommands() {
    CommandFileParser cfp = new CommandFileParser(
      cfg.getAsString("file.commands.conf")
    );
    cfp.parse();
    this.commands.put("classify", new ClassifyCommand());
    this.commands.put("history", new HistoryCommand());
    this.commands.put("learn", new LearnCommand());
    this.commands.put("exit", new ExitCommand());
  }

  /**
   * Takes an instruction, converts it to a command and executes it.
   *
   * @param instruction the instruction as given by the user
   * @throws Cli.Exception if the command fails to execute user request or
   *         the user explicitly asks for termination of the program.
   */
  public void execute(Instruction instruction) throws Cli.Exception {
    try {
      Command command = this.parse(instruction);
      this.history.add(instruction);
      command.check(instruction);
      command.execute(this, instruction);
    } catch (InvalidCommandException ex) {
      // intentionally left blank
    }
  }

  /**
   * Converts user instruction to a given executable command
   *
   * @return an executable command corresponding to the given instruction
   * @throws Cli.InvalidCommandException if given command is not recognized
   */
  private Command parse(Instruction instruction)
      throws Cli.InvalidCommandException {
    String name = instruction.getName();
    if (!this.commands.containsKey(name)) {
      throw new InvalidCommandException(instruction, "command not found");
    }
    return this.commands.get(name);
  }

  /**
   * Returns an organized structure for a given user input to the CLI.
   *
   * @return an organized structure for a given user input to the CLI.
   */
  public Instruction getInstruction() {
    while (true) {
      System.out.printf(this.getPrompt());
      String str = scanner.nextLine();
      if (!str.isEmpty()) {
        Instruction instruction = new Instruction(str);
        return instruction;
      }
    }
  }

  /**
   * Returns the command line prompt.
   *
   * @return the command line prompt
   */
  public String getPrompt() {
    return "$ ";
  }

  /**
   * Returns a list of recent instructions passed to the CLI.
   *
   * @return a list of recent isntructions passed to the CLI.
   */
  public List<Map.Entry<Integer, Instruction>> getInstructions() {
    return this.history.getInstructions();
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
    private int itemCounter = 1000;
    private final List<Map.Entry<Integer, Instruction>> list;

    /**
     * A history is simply a list of most recent instructions given by the user
     * to the Cli object. The list has a limited size which is determined based
     * on <pre>cli.history.size</pre> paramter in the configuration file.
     */
    public History() {
      this.limit = cfg.getAsInt("cli.history.size");
      this.list = new ArrayList<Map.Entry<Integer, Instruction>>(
          this.limit
      );
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
      this.list.add(
          new AbstractMap.SimpleImmutableEntry<Integer, Instruction>(
              this.itemCounter++, instruction
          )
      );
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
    public List<Map.Entry<Integer, Instruction>> getInstructions() {
      return this.list;
    }

  }

  /**
   * The superclass of all exceptions that can occur when using the CLI.
   *
   * @author Pejman Ghorbanzade
   * @see FailedCommandException
   * @see GracefulExitException
   * @see InvalidCommandException
   */
  @SuppressWarnings("serial")
  public abstract static class Exception extends RuntimeException {

    /**
     * Logs a debug message for every exception that is thrown.
     *
     * @param level the level with which the given message should be logged
     * @param message the message to be logged for a thrown exception.
     */
    public Exception(Level level, String message) {
      log.log(level, message);
    }

  }

  /**
   * Thrown when user enters an instruction which does not correspond to
   * any command recognized by the CLI.
   *
   * @author Pejman Ghorbanzade
   */
  @SuppressWarnings("serial")
  public static final class InvalidCommandException
      extends com.ghorbanzade.sloth.cli.Cli.Exception {

    /**
     * Prepares a debug message to be logged when this exception is thrown.
     *
     * @param instruction the instruction as given by the user
     * @param message the message to be printed alongside with the instruction
     *        name.
     */
    public InvalidCommandException(Instruction instruction, String message) {
      super(Level.INFO, String.format("%s: %s", instruction, message));
    }

  }

  /**
   * Thrown when user calls the exit command from the CLI.
   *
   * @author Pejman Ghorbanzade
   */
  @SuppressWarnings("serial")
  public static final class GracefulExitException
      extends com.ghorbanzade.sloth.cli.Cli.Exception {

    /**
     * Prepares a debug message to be logged when this exception is thrown.
     */
    public GracefulExitException() {
      super(Level.DEBUG, "terminating program per user request");
    }

  }

  /**
   * Thrown when a command encounters irrecoverable error during execution.
   *
   * @author Pejman Ghorbanzade
   * @see Cli.Exception
   */
  @SuppressWarnings("serial")
  public static final class FailedCommandException
      extends com.ghorbanzade.sloth.cli.Cli.Exception {

    /**
     * Prepares a debug message to be logged when this exception is thrown.
     *
     * @param instruction the instruction that failed to successfully execute
     * @param message the message to be logged when exception is thrown
     */
    public FailedCommandException(Instruction instruction, String message) {
      super(
          Level.INFO, String.format("%s: %s", instruction.getName(), message)
      );
    }

  }

}
