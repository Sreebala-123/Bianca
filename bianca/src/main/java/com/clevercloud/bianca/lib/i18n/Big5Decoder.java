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
 * @author Marc-Antoine Perennou <Marc-Antoine@Perennou.com>
 */
package com.clevercloud.bianca.lib.i18n;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;


public class Big5Decoder
   extends GenericDecoder {

   public Big5Decoder(String charsetName) {
      super(charsetName);
   }

   private boolean isMalformed(CoderResult coder, ByteBuffer in) {
      if (coder.isMalformed() || coder.isUnmappable()) {
         int errorPosition = in.position();

         if (errorPosition + 1 < in.limit()
            && in.get(errorPosition) == '\u00a3'
            && in.get(errorPosition + 1) == '\u00e1') {
            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   protected boolean fill(StringBuilder sb, ByteBuffer in,
                          CharBuffer out, CoderResult coder) {
      int len = out.position();

      if (len > 0) {
         int offset = out.arrayOffset();
         sb.append(out.array(), offset, len);
      }

      if (coder.isMalformed() || coder.isUnmappable()) {
         int errorPosition = in.position();

         if (errorPosition + 1 < in.limit()
            && (in.get(errorPosition) & 0xFF) == '\u00a3'
            && (in.get(errorPosition + 1) & 0xFF) == '\u00e1') {

            sb.append('\u20AC');
            in.position(errorPosition + 2);

            return true;
         }

         _hasError = true;
         in.position(errorPosition + 1);

         if (_isIgnoreErrors) {
         } else if (_replacement != null) {
            sb.append(_replacement);
         } else if (_isAllowMalformedOut) {
            sb.append((char) in.get(errorPosition));
         } else {
            return false;
         }
      }

      return true;
   }
}
