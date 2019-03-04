//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

package com.ghorbanzade.sloth;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
* Unit test suit for wsn, wsn manager and node classes.
*
* @author Pejman Ghorbanzade
*/
public class NodeTest {

  /**
   * It should be possible to create a wsn object that points to non-existant
   * configuration file.
   */
  @Test
  public void createWsnWithNonExistantFile() {
    Wsn wsn = WsnManager.getWsn("nonexist.properties");
    assertThat(wsn, is(not(nullValue())));
  }

  /**
   * It should be possible to create a wsn object that points to an existing
   * configuration file.
   */
  @Test
  public void createWsnWithExistingFile() {
    Wsn wsn = WsnManager.getWsn("src/test/resources/wsn-config.json");
    assertThat(wsn, is(not(nullValue())));
  }

  /**
   * Wsn constructor should load the json configuration file and construct
   * nodes with file parameters.
   */
  @Test
  public void wsnConstructorMustLoadFile() {
    Wsn wsn = WsnManager.getWsn("src/test/resources/wsn-config.json");
    assertThat(wsn.getNode(1), is(not(nullValue())));
    assertThat(wsn.getNode(2), is(not(nullValue())));
    assertThat(wsn.getNode(3), is(nullValue()));
  }

  /**
   * nodes should be constructed based on configurations in the file.
   */
  @Test
  public void checkNodeAttributes() {
    Wsn wsn = WsnManager.getWsn("src/test/resources/wsn-config.json");
    assertThat(wsn.getNode(2), is(not(nullValue())));
    assertThat(wsn.getNode(2).getId(), is(2));
    assertThat(wsn.getNode(2).getName(), is("name2"));
    assertThat(wsn.getNode(2).getDescription(), is("name of id 2"));
  }

  /**
   * It should be possible to manually construct a node with given attributes.
   */
  @Test
  public void manuallyConstructNode() {
    Node node = new Node(1, "name", "description");
    assertThat(node, is(not(nullValue())));
    assertThat(node.getId(), is(1));
    assertThat(node.getName(), is("name"));
    assertThat(node.getDescription(), is("description"));
  }

  /**
   * constructor of wsn manager is private.
   */
  @Test
  public void privateConstructor() {
    Constructor<?>[] constructors = WsnManager.class.getDeclaredConstructors();
    for (Constructor<?> constructor : constructors) {
      assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
    }
  }

}
