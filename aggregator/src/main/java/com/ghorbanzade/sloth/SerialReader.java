//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TooManyListenersException;

/**
 * A serial reader is the first worker thread that listens to serial port
 * for incoming data and puts the data in serial queue once it is available
 * on the buffer.
 *
 * @author Pejman Ghorbanzade
 * @see SerialQueue
 */
public final class SerialReader implements SerialPortEventListener, Closeable {

  private Config cfg;
  private SerialPort port;
  private SerialQueue sq;
  private BufferedReader input;
  private static final Logger log = Logger.getLogger(SerialReader.class);

  /**
   * A serial reader is constructed based on the configuration file
   * and the serial queue to which new data should be written.
   *
   * @param sq serial queue to which new data should be written
   */
  public SerialReader(SerialQueue sq) {
    this.sq = sq;
    this.cfg = ConfigManager.get("config/main.properties");
  }

  /**
   * This method configures the serial port and opens it and starts listening
   * to it for incoming data.
   *
   * @param portName the path to the serial port that should be opened
   * @throws FatalException if serial port cannot be configured or opened
   */
  public void open(String portName) throws FatalException {
    try {
      this.port = (SerialPort) CommPortIdentifier
          .getPortIdentifier(portName)
          .open(this.getClass().getName(), this.cfg.getAsInt("serial.timeout"));
      this.port.setSerialPortParams(
          this.cfg.getAsInt("serial.baudrate"),
          SerialPort.DATABITS_8,
          SerialPort.STOPBITS_1,
          SerialPort.PARITY_NONE
      );
      this.input = new BufferedReader(
          new InputStreamReader(this.port.getInputStream(), "UTF-8")
      );
      this.port.addEventListener(this);
      this.port.notifyOnDataAvailable(true);
    } catch (NoSuchPortException ex) {
      log.fatal("specified port does not exist");
      throw new FatalException(this.getClass());
    } catch (PortInUseException ex) {
      log.fatal("port is in use by another application");
      throw new FatalException(this.getClass());
    } catch (UnsupportedCommOperationException ex) {
      log.fatal("failed to set serial parameters");
      throw new FatalException(this.getClass());
    } catch (IOException ex) {
      log.fatal("cannot access serial port buffer");
      throw new FatalException(this.getClass());
    } catch (TooManyListenersException ex) {
      log.fatal("unable to add serial port as event listener");
      throw new FatalException(this.getClass());
    }
  }

  /**
   * Removes event listener hook and closes serial port. This method
   * is called as part of the shutdown thread.
   */
  public void close() {
    if (this.port != null) {
      this.port.removeEventListener();
      this.port.close();
      log.debug("serial port closed");
    }
  }

  /**
   * This method implements what should happen in case an event occured.
   * If data is found on buffer, it will be fetched and put on serial queue.
   *
   * @param event the event that triggered event listener
   */
  public void serialEvent(SerialPortEvent event) {
    if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
      try {
        String rawInput = this.input.readLine();
        log.trace("read packet from serial port");
        this.sq.put(rawInput);
      } catch (IOException ex) {
        log.error("serial reader encountered exception while reading data");
      }
    }
  }

}
