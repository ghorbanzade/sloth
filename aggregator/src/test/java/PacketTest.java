// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
* Unit test suit for packet class and its subclasses.
*
* @author Pejman Ghorbanzade
*/
public class PacketTest {

  /**
   * Checks numeric values assigned to enum items of raw packet data components.
   */
  @Test
  public void valuesOfrawPacketComponents() {
    assertThat(RawPacket.Data.ACC_X.getValue(), is(0));
    assertThat(RawPacket.Data.ACC_Y.getValue(), is(1));
    assertThat(RawPacket.Data.ACC_Z.getValue(), is(2));
  }

  /**
   * Creates a raw packet and checks its initialized components.
   */
  @Test
  public void buildPacket() {
    Node node = new Node(1, "node-name", "node-desc");
    int[] data = new int[]{1, 2, 3};
    RawPacket packet = new RawPacket(node, data);
    assertThat(packet.getComponent(RawPacket.Data.ACC_X), is(1));
    assertThat(packet.getComponent(RawPacket.Data.ACC_Y), is(2));
    assertThat(packet.getComponent(RawPacket.Data.ACC_Z), is(3));
    assertThat(packet.toString(), is("node-name 1 2 3"));
  }

}
