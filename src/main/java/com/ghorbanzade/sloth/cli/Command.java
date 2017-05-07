//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth.cli;

/**
 * Defines the process to be initiated when user enters a corresponding
 * instruction.
 *
 * @author Pejman Ghorbanzade
 * @see Cli
 */
public abstract class Command {

  private ArrayList<CommandSyntax> syntaxes;

  /**
   * Creates a command with the syntax used by the client,
   * Creates a command with a list of arguments and the syntax from which
   * they were passed, whose validity and format has already been verified by CLI.
   *
   * @param args arguments given to this command
   */
  public Command(ArrayList<CommandSyntax> syntaxes) {
    this.args = args;
  }

  /**
   * Returns a help string that contains general description, list of
   * possible syntaxes, format and range of arguments etc for this command.
   *
   * @return a help string for this command ready to be printed on screen
   */
  public StringBuilder help() {
    StringBuilder sb = new StringBuilder();
    sb.add("help");
    return sb.toString();
  }

  /**
   * Defines the effect that a given command execution will have.
   *
   * @param cli the cli that is executing the given command
   * @param syntax the instruction given by the user
   * @throws Cli.Exception case an error occurs during command execution
   *         or user requests program to be terminated.
   */
  public abstract void execute(Cli cli, Syntax syntax) throws Cli.Exception;

}
