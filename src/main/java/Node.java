//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

/**
 * This class defines a wireless sensor node object which represents a
 * sensor node whose packets are received.
 *
 * @author Pejman Ghorbanzade
 * @see Wsn
 * @see WsnManager
 */
public final class Node {

  private int id;
  private String name;
  private String description;

  /**
   * A wireless sensor node has an identifier, a short name and a description
   * that may describe its position on the user's body.
   *
   * @param id id of this sensor node
   * @param name name of this sensor node
   * @param description of this sensor node
   */
  public Node(int id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  /**
   * Getter method for id of this node. This method is widely used to access
   * a node inside a wireless sensor network based in its id.
   *
   * @return id of this sensor node
   */
  public int getId() {
    return this.id;
  }

  /**
   * Getter method for name of this node. This method is used to print
   * information about the node to the user.
   *
   * @return name of this sensor node
   */
  public String getName() {
    return this.name;
  }

  /**
   * Getter method for description of this node. This method is used to print
   * information about the node to the user.
   *
   * @return description about this sensor node
   */
  public String getDescription() {
    return this.description;
  }

}
