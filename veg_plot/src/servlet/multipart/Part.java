// Copyright (C) 1999 by Jason Hunter <jhunter@acm.org>.  All rights reserved.
// Use of this class is limited.  Please see the LICENSE for more information.
 
//package com.oreilly.servlet.multipart;
package multipart;


/**
 * A <code>Part</code> is an abstract upload part which represents an 
 * <code>INPUT</code> form element in a <code>multipart/form-data</code> form 
 * submission.
 * 
 * @see FilePart
 * @see ParamPart
 * 
 * @author Geoff Soutter
 */
public abstract class Part {
  private String name;
  
  /**
   * Constructs an upload part with the given name.
   */
  Part(String name) {
    this.name = name;
  }
  
  /**
   * Returns the name of the form element that this Part corresponds to.
   * 
   * @return the name of the form element that this Part corresponds to.
   */
  public String getName() {
    return name;
  }
  
  /**
   * Returns true if this Part is a FilePart.
   * 
   * @return true if this is a FilePart.
   */
  public boolean isFile() {
    return false;
  }
  
  /**
   * Returns true if this Part is a ParamPart.
   * 
   * @return true if this is a ParamPart.
   */
  public boolean isParam() {
    return false;
  }
}
