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
 * @author Nam Nguyen
 */
package com.clevercloud.bianca.lib.reflection;

import com.clevercloud.bianca.annotation.Optional;
import com.clevercloud.bianca.env.BiancaClass;
import com.clevercloud.bianca.env.Env;
import com.clevercloud.bianca.env.ObjectValue;
import com.clevercloud.bianca.env.Value;

public class ReflectionObject extends ReflectionClass {

   private void __clone() {
   }

   protected ReflectionObject(BiancaClass cls) {
      super(cls);
   }

   public static ReflectionObject __construct(Env env, Value val) {
      if (!val.isObject()) {
         throw new ReflectionException("parameter must be an object");
      }

      ObjectValue obj = (ObjectValue) val.toObject(env);

      return new ReflectionObject(obj.getBiancaClass());
   }

   public static String export(Env env,
                               Value object,
                               @Optional boolean isReturn) {
      return null;
   }

   @Override
   public String toString() {
      return "ReflectionObject[" + getName() + "]";
   }
}
