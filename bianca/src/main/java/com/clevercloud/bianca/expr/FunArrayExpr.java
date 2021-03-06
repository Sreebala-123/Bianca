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
import com.clevercloud.bianca.env.ArrayValue;
import com.clevercloud.bianca.env.ArrayValueImpl;
import com.clevercloud.bianca.env.Env;
import com.clevercloud.bianca.env.Value;

import java.util.ArrayList;

/**
 * Represents the array function
 */
public class FunArrayExpr extends Expr {

   protected final Expr[] _keys;
   protected final Expr[] _values;

   public FunArrayExpr(Location location,
                       ArrayList<Expr> keyList,
                       ArrayList<Expr> valueList) {
      super(location);

      _keys = new Expr[keyList.size()];
      keyList.toArray(_keys);

      _values = new Expr[valueList.size()];
      valueList.toArray(_values);
   }

   public FunArrayExpr(Location location, Expr[] keys, Expr[] values) {
      super(location);
      _keys = keys;
      _values = values;
   }

   public FunArrayExpr(ArrayList<Expr> keyList, ArrayList<Expr> valueList) {
      this(Location.UNKNOWN, keyList, valueList);
   }

   public FunArrayExpr(Expr[] keys, Expr[] values) {
      this(Location.UNKNOWN, keys, values);
   }

   /**
    * Returns true if the expression evaluates to an array.
    */
   @Override
   public boolean isArray() {
      return true;
   }

   /**
    * Returns true for a constant array.
    */
   @Override
   public boolean isConstant() {
      for (int i = 0; i < _keys.length; i++) {
         if (_keys[i] != null && !_keys[i].isConstant()) {
            return false;
         }
      }

      for (int i = 0; i < _values.length; i++) {
         if (_values[i] != null && !_values[i].isConstant()) {
            return false;
         }
      }

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
      ArrayValue array = new ArrayValueImpl();

      for (int i = 0; i < _values.length; i++) {
         Expr keyExpr = _keys[i];

         Value value = _values[i].evalArg(env, true);
         // php/0471
         value = value.toRefValue();

         if (keyExpr != null) {
            Value key = keyExpr.evalArg(env, true).toLocalValue();

            array.put(key, value);
         } else {
            array.put(value);
         }
      }

      return array;
   }

   @Override
   public String toString() {
      return "array()";
   }
}
