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
 * @author Marc-Antoine Perennou <Marc-Antoine@Perennou.com>
 */
package com.clevercloud.bianca.lib.file;

import com.clevercloud.bianca.env.Env;
import com.clevercloud.bianca.env.StringValue;
import com.clevercloud.bianca.resources.StreamResource;
import com.clevercloud.vfs.Path;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents a Bianca open file
 */
public class FileValue extends StreamResource {

   private Path _path;

   public FileValue(Path path) {
      _path = path;
   }

   /**
    * Returns the path.
    */
   public Path getPath() {
      return _path;
   }

   /**
    * Reads a character from a file, returning -1 on EOF.
    */
   @Override
   public int read()
      throws IOException {
      return -1;
   }

   /**
    * Reads a line from a file, returning null.
    */
   @Override
   public StringValue readLine(Env env)
      throws IOException {
      StringValue sb = new StringValue();
      int ch;

      while ((ch = read()) >= 0) {
         sb.append((char) ch);

         if (ch == '\n') {
            return sb;
         }
         // TODO: issues with mac
      }

      if (sb.length() > 0) {
         return sb;
      } else {
         return null;
      }
   }

   /**
    * Read a maximum of <i>length</i> bytes from the file and write
    * them to the outputStream.
    *
    * @param os     the {@link OutputStream}
    * @param length the maximum number of bytes to read
    */
   public void writeToStream(OutputStream os, int length)
      throws IOException {
   }

   /**
    * Prints a string to a file.
    */
   @Override
   public void print(String v)
      throws IOException {
   }

   /**
    * Closes the file.
    */
   @Override
   public void close() {
   }

   /**
    * Converts to a string.
    */
   @Override
   public String toString() {
      return "File[" + _path + "]";
   }
}
