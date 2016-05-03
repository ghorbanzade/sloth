//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * A wireless sensor network is a collection of sensor nodes declared to
 * the software as eligible for transmission of data. A user can modify
 * the wireless sensor node and store the configuration in a file.
 *
 * @author Pejman Ghorbanzade
 * @see Node
 * @see WsnManager
 */
public final class Wsn {

  private final File file;
  private final HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
  private static final Logger log = Logger.getLogger(Wsn.class);

  /**
   * A sensor network is constructed by specifying a file path. If the file
   * exists, the content of the file is parsed and used to initialize the
   * sensor network. Otherwise, the filename is stored for later use during
   * the save procedure.
   *
   * @param filename the name of the file containing info about sensor network
   */
  public Wsn(String filename) {
    this.file = new File(filename);
    if (this.file.isFile()) {
      try {
        String content = Files.toString(this.file, Charsets.UTF_8);
        log.trace("retrieved content of wsn configuration file");
        Gson gson = new Gson();
        Node[] nds = gson.fromJson(content, Node[].class);
        log.trace("retrieved node objects from wsn configuration file");
        for (int i = 0; i < nds.length; i++) {
          this.add(nds[i]);
          log.debug("added node id " + nds[i].getId() + " to wsn");
        }
      } catch (IOException ex) {
        log.error("unable to read content of the given file");
      }
    }
  }

  /**
   * This method allows access to a node in the network via its id.
   *
   * @param id a unique identifier for the sensor node
   * @return the sensor node object with the given id
   * @throws NoSuchNodeException if given id is not found in sensor network
   */
  public Node getNode(int id) throws NoSuchNodeException {
    return this.nodes.get(id);
  }

  /**
   * This method allows user to introduce a sensor node to the network.
   * In case the new sensor node has an id similar to that of another
   * node in the network, the new node replaces the previous sensor node.
   *
   * @param node the new sensor node to be added to this sensor network
   */
  public void add(Node node) {
    this.nodes.put(node.getId(), node);
  }

  /**
   * This method allows user to save current configuration of the sensor network
   * in a file previously specified upon instantiation.
   */
  public void save() {
    // TODO
  }

  /**
   * Returns number of nodes in the network.
   *
   * @return size of the hashmap holding sensor data.
   */
  public int getSize() {
    return this.nodes.size();
  }

}
