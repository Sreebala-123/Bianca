/*
 * Copyright (c) 1998-2010 Caucho Technology -- all rights reserved
 * Copyright (c) 2011-2012 Clever Cloud SAS -- all rights reserved
 *
 * This file is part of Bianca(R) Open Source
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Bianca Open Source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Bianca Open Source is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, or any warranty
 * of NON-INFRINGEMENT.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Bianca Open Source; if not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Scott Ferguson
 */
package com.clevercloud.bianca.expr;

import com.clevercloud.bianca.Location;
import com.clevercloud.bianca.env.Env;
import com.clevercloud.bianca.env.Value;

/**
 * Represents a PHP multiplication expression.
 */
public class BinaryMulExpr extends AbstractBinaryExpr {

   public BinaryMulExpr(Location location, Expr left, Expr right) {
      super(location, left, right);
   }

   public BinaryMulExpr(Expr left, Expr right) {
      super(left, right);
   }

   /**
    * Return true for a double value
    */
   @Override
   public boolean isDouble() {
      return _left.isDouble() || _right.isDouble();
   }

   /**
    * Return true for a long value
    */
   @Override
   public boolean isLong() {
      return _left.isLong() && _right.isLong();
   }

   /**
    * Return true for a number
    */
   @Override
   public boolean isNumber() {
      return true;
   }

   /**
    * Evaluates the expression.
    *
    * @param env the calling environment.
    * @return the expression value.
    */
   @Override
   public Value eval(Env env) {
      Value lValue = _left.eval(env);
      Value rValue = _right.eval(env);

      return lValue.mul(rValue);
   }

   @Override
   public String toString() {
      return "(" + _left + " * " + _right + ")";
   }
}
