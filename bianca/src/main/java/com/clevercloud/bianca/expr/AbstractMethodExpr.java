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
import com.clevercloud.bianca.env.StringValue;
import com.clevercloud.bianca.env.Value;


/**
 * Represents a PHP function expression.
 */
abstract public class AbstractMethodExpr extends Expr {

   protected AbstractMethodExpr(Location location) {
      super(location);
   }

   /**
    * Evaluates the expression as a copy
    *
    * @param env the calling environment.
    * @return the expression value.
    */
   @Override
   public Value evalCopy(Env env) {
      return eval(env).copy();
   }

   /**
    * Evaluates the expression as a copy
    *
    * @param env the calling environment.
    * @return the expression value.
    */
   @Override
   public Value evalArg(Env env, boolean isTop) {
      return eval(env).copy();
   }

   /**
    * Evaluates the expression.
    *
    * @param env the calling environment.
    * @return the expression value.
    */
   protected Value eval(Env env, Value qThis,
                        StringValue methodName, int hashCode,
                        Expr[] argExprs) {
      Value[] args = evalArgs(env, argExprs);

      env.pushCall(this, qThis, args);

      try {
         return qThis.callMethod(env, methodName, hashCode, args);
      } finally {
         env.popCall();
      }
   }
}
