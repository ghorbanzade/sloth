//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import org.apache.log4j.Logger;

/**
 * This class defines a custom exception that is thrown to hint the main
 * thread that the program should be aborted.
 *
 * @author Pejman Ghorbanzade
 */
public final class FatalException extends RuntimeException {

  private static final Logger log = Logger.getLogger(FatalException.class);

  /**
   * A fatal exception updates the log file with the class name that caused it.
   *
   * @param cls the class from which this exception is thrown
   */
  public FatalException(Class<?> cls) {
    log.fatal("fatal exception occured in " + cls.getSimpleName() + " class");
  }

}
