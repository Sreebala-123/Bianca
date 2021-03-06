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
 *   Free SoftwareFoundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Scott Ferguson
 */

package com.clevercloud.vfs.i18n;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Reader to handle any encoding by passing it to the JDK.
 */
public class JDKReader extends EncodingReader {
   /**
    * Null-arg constructor for instantiation by com.clevercloud.vfs.Encoding only.
    */
   public JDKReader() {
   }

   /**
    * Create a JDK-based reader.
    *
    * @param is           the input stream providing the bytes.
    * @param javaEncoding the JDK name for the encoding.
    * @return an InputStreamReader
    */
   public Reader create(InputStream is, String javaEncoding)
      throws UnsupportedEncodingException {
      if (Charset.isSupported(javaEncoding)) {
         Charset charset = Charset.forName(javaEncoding);

         return new InputStreamReader(is, charset);
      } else {
         // RSN-274, Charset doesn't support all java.io encoding
         return new InputStreamReader(is, javaEncoding);
      }
   }

   /**
    * Reads into a character buffer using the correct encoding.
    */
   public int read()
      throws IOException {
      throw new UnsupportedOperationException();
   }
}
