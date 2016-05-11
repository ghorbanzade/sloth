//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth.cli;

/**
 *
 *
 * @author Pejman Ghorbanzade
 * @see Cli
 * @see Command
 */
public class InvalidCommandException extends RuntimeException {

  private final String msg;

  /**
   *
   *
   * @param instruction the instruction that was failed to be parsed
   * @param message the reason this instruction was identified as invalid
   */
  public InvalidCommandException(Instruction instruction, String message) {
    this.msg = String.format("%s: %s", instruction.getName(), message);
  }

  /**
   * Returns a ready to print error message to notify user as to why the
   * command was identified as invalid.
   *
   * @return a ready-to-print error message
   */
  public String getMessage() {
    return this.msg;
  }

}
