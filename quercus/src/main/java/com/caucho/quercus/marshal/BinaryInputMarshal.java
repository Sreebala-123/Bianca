/*
 * Copyright (c) 1998-2010 Caucho Technology -- all rights reserved
 *
 * This file is part of Resin(R) Open Source
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Resin Open Source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Resin Open Source is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, or any warranty
 * of NON-INFRINGEMENT.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Resin Open Source; if not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Scott Ferguson
 */
package com.caucho.quercus.marshal;

import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.JavaValue;
import com.caucho.quercus.env.Value;
import com.caucho.quercus.expr.Expr;
import com.caucho.quercus.lib.file.BinaryInput;
import com.caucho.quercus.lib.file.ReadStreamInput;

import java.io.InputStream;

public class BinaryInputMarshal extends Marshal {

   public static final Marshal MARSHAL = new BinaryInputMarshal();

   @Override
   public boolean isReadOnly() {
      return true;
   }

   @Override
   public Object marshal(Env env, Expr expr, Class expectedClass) {
      return marshal(env, expr.eval(env), expectedClass);
   }

   @Override
   public Object marshal(Env env, Value value, Class expectedClass) {
      if (value == null) {
         return null;
      } else if (value instanceof BinaryInput) {
         return (BinaryInput) value;
      }

      Object javaObj = value.toJavaObject();

      if (javaObj instanceof BinaryInput) {
         return (BinaryInput) javaObj;
      } else if (javaObj instanceof InputStream) {
         return new ReadStreamInput(env, (InputStream) javaObj);
      } else {
         return new ReadStreamInput(env, value.toInputStream());
      }
   }

   public static BinaryInput marshal(Env env, Value value) {
      if (value == null) {
         return null;
      } else if (value instanceof BinaryInput) {
         return (BinaryInput) value;
      }

      Object javaObj = value.toJavaObject();

      if (javaObj instanceof BinaryInput) {
         return (BinaryInput) javaObj;
      } else if (javaObj instanceof InputStream) {
         return new ReadStreamInput(env, (InputStream) javaObj);
      } else {
         return new ReadStreamInput(env, value.toInputStream());
      }
   }

   @Override
   public Value unmarshal(Env env, Object value) {
      return (Value) value;
   }

   @Override
   protected int getMarshalingCostImpl(Value argValue) {
      if (argValue instanceof JavaValue
              && InputStream.class.isAssignableFrom(argValue.toJavaObject().getClass())) {
         return Marshal.ZERO;
      } else {
         return Marshal.FOUR;
      }
   }

   @Override
   public Class getExpectedClass() {
      return BinaryInput.class;
   }
}
