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
import com.clevercloud.bianca.env.Var;
import com.clevercloud.bianca.parser.BiancaParser;
import com.clevercloud.util.L10N;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a PHP field reference.
 */
public class ThisFieldVarExpr extends AbstractVarExpr {

   private static final L10N L = new L10N(ObjectFieldVarExpr.class);
   protected final ThisExpr _qThis;
   protected final Expr _nameExpr;

   public ThisFieldVarExpr(ThisExpr qThis, Expr nameExpr) {
      _qThis = qThis;

      _nameExpr = nameExpr;
   }

   //
   // function call creation
   //

   /**
    * Creates a function call expression
    */
   @Override
   public Expr createCall(BiancaParser parser,
                          Location location,
                          ArrayList<Expr> args)
      throws IOException {
      ExprFactory factory = parser.getExprFactory();

      return factory.createThisMethod(location, _qThis, _nameExpr, args);
   }

   /**
    * Evaluates the expression.
    *
    * @param env the calling environment.
    * @return the expression value.
    */
   @Override
   public Value evalArg(Env env, boolean isTop) {
      Value value = env.getThis();

      return value.getThisFieldArg(env, _nameExpr.evalStringValue(env));
   }

   /**
    * Evaluates the expression.
    *
    * @param env the calling environment.
    * @return the expression value.
    */
   @Override
   public Var evalVar(Env env) {
      // bianca/0d1k
      Value value = env.getThis();

      return value.getThisFieldVar(env, _nameExpr.evalStringValue(env));
   }

   /**
    * Evaluates the expression.
    *
    * @param env the calling environment.
    * @return the expression value.
    */
   @Override
   public Value eval(Env env) {
      Value obj = env.getThis();

      return obj.getThisField(env, _nameExpr.evalStringValue(env));
   }

   /**
    * Evaluates the expression.
    *
    * @param env the calling environment.
    * @return the expression value.
    */
   @Override
   public Value evalAssignValue(Env env, Value value) {
      Value obj = env.getThis();

      obj.putThisField(env, _nameExpr.evalStringValue(env), value);

      return value;
   }

   /**
    * Evaluates the expression.
    *
    * @param env the calling environment.
    * @return the expression value.
    */
   @Override
   public Value evalAssignRef(Env env, Value value) {
      Value obj = env.getThis();

      obj.putThisField(env, _nameExpr.evalStringValue(env), value);

      return value;
   }

   /**
    * Evaluates as an array index assign ($a[index] = value).
    */
   @Override
   public Value evalArrayAssign(Env env, Value index, Value value) {
      Value obj = env.getThis();

      StringValue name = _nameExpr.evalStringValue(env);

      Value fieldVar = obj.getThisFieldVar(env, name);

      // php/03mn
      return fieldVar.put(index, value);
   }

   /**
    * Evaluates the expression, creating an array if the field is unset.
    *
    * @param env the calling environment.
    * @return the expression value.
    */
   @Override
   public Value evalArray(Env env) {
      Value obj = env.getThis();

      return obj.getThisFieldArray(env, _nameExpr.evalStringValue(env));
   }

   /**
    * Evaluates the expression, creating an object if the field is unset.
    *
    * @param env the calling environment.
    * @return the expression value.
    */
   @Override
   public Value evalObject(Env env) {
      Value obj = env.getThis();

      return obj.getThisFieldObject(env, _nameExpr.evalStringValue(env));
   }

   /**
    * Evaluates the expression.
    *
    * @param env the calling environment.
    * @return the expression value.
    */
   @Override
   public void evalUnset(Env env) {
      Value obj = env.getThis();

      obj.unsetThisField(_nameExpr.evalStringValue(env));
   }

   @Override
   public String toString() {
      return "$this->{" + _nameExpr + "}";
   }
}
