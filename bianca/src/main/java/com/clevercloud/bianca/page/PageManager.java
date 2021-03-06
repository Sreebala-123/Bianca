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
package com.clevercloud.bianca.page;

import com.clevercloud.bianca.BiancaContext;
import com.clevercloud.bianca.parser.BiancaParser;
import com.clevercloud.bianca.program.BiancaProgram;
import com.clevercloud.util.L10N;
import com.clevercloud.util.LruCache;
import com.clevercloud.vfs.IOExceptionWrapper;
import com.clevercloud.vfs.Path;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Each "page" refers to a bianca file.
 */
public class PageManager {

   private static final Logger log = Logger.getLogger(PageManager.class.getName());
   protected static final L10N L = new L10N(PageManager.class);
   private final BiancaContext _bianca;
   //private Path _pwd;
   private boolean _isLazyCompile;
   private boolean _isCompile;
   private boolean _isCompileFailover = true;
   private boolean _isRequireSource = true;
   protected LruCache<Path, BiancaProgram> _programCache = new LruCache<Path, BiancaProgram>(1024);
   private boolean _isClosed;

   /**
    * Constructor.
    */
   public PageManager(BiancaContext bianca) {
      _bianca = bianca;
   }

   public BiancaContext getBianca() {
      return _bianca;
   }

   /**
    * Gets the owning directory.
    */
   public Path getPwd() {
      return _bianca.getPwd();
   }

   /**
    * true if the pages should be compiled.
    */
   public boolean isCompile() {
      return _isCompile;
   }

   /**
    * true if the pages should be compiled.
    */
   public void setCompile(boolean isCompile) {
      _isCompile = isCompile;
   }

   /**
    * * true if interpreted pages should be used if pages fail to compile.
    */
   public boolean isCompileFailover() {
      return _isCompileFailover;
   }

   /**
    * * true if interpreted pages should be used if pages fail to compile.
    */
   public void setCompileFailover(boolean isCompileFailover) {
      _isCompileFailover = isCompileFailover;
   }

   /**
    * true if the pages should be compiled lazily.
    */
   public boolean isLazyCompile() {
      return _isLazyCompile;
   }

   /**
    * true if the pages should be compiled.
    */
   public void setLazyCompile(boolean isCompile) {
      _isLazyCompile = isCompile;
   }

   /**
    * true if compiled pages require their source
    */
   public void setRequireSource(boolean isRequireSource) {
      _isRequireSource = isRequireSource;
   }

   /**
    * true if compiled pages require their source
    */
   public boolean isRequireSource() {
      return _isRequireSource;
   }

   /**
    * Gets the max size of the page cache.
    */
   public int getPageCacheSize() {
      return _programCache.getCapacity();
   }

   /**
    * Sets the max size of the page cache.
    */
   public void setPageCacheSize(int size) {
      if (size >= 0 && size != _programCache.getCapacity()) {
         _programCache = new LruCache<Path, BiancaProgram>(size);
      }
   }

   /**
    * true if the manager is active.
    */
   public boolean isActive() {
      return !_isClosed;
   }

   /**
    * Returns the relative path.
    */
   /*
   public String getClassName(Path path)
   {
   if (path == null)
   return "tmp.eval";

   String pathName = path.getFullPath();
   String pwdName = getPwd().getFullPath();

   String relPath;

   if (pathName.startsWith(pwdName))
   relPath = pathName.substring(pwdName.length());
   else
   relPath = pathName;

   return "_bianca." + JavaCompiler.mangleName(relPath);
   }
    */

   /**
    * Returns a parsed or compiled bianca program.
    *
    * @param path the source file path
    * @return the parsed program
    * @throws IOException
    */
   public BiancaPage parse(Path path)
      throws IOException {
      return parse(path, null, -1);
   }

   /**
    * Returns a parsed or compiled bianca program.
    *
    * @param path the source file path
    * @return the parsed program
    * @throws IOException
    */
   public BiancaPage parse(Path path, String fileName, int line)
      throws IOException {
      try {
         BiancaProgram program;

         program = _programCache.get(path);

         boolean isModified = false;

         if (program != null) {
            isModified = program.isModified();

            if (program.isCompilable()) {
            } else if (isModified) {
               program.setCompilable(true);
            } else {
               if (log.isLoggable(Level.FINE)) {
                  log.fine(L.l("Bianca[{0}] loading interpreted page", path));
               }

               return new InterpretedPage(program);
            }
         }

         if (program == null || isModified) {
            clearProgram(path, program);

            program = preloadProgram(path, fileName);

            if (program == null) {
               if (log.isLoggable(Level.FINE)) {
                  log.fine(L.l("Bianca[{0}] parsing page", path));
               }

               program = BiancaParser.parse(_bianca,
                  path,
                  _bianca.getScriptEncoding(),
                  fileName,
                  line);
            }

            _programCache.put(path, program);
         }

         if (program.getCompiledPage() != null) {
            return program.getCompiledPage();
         }

         return compilePage(program, path);
      } catch (IOException e) {
         throw e;
      } catch (RuntimeException e) {
         throw e;
      } catch (Throwable e) {
         throw new IOExceptionWrapper(e);
      }
   }

   public boolean precompileExists(Path path) {
      return false;
   }

   protected BiancaProgram preloadProgram(Path path, String fileName) {
      return null;
   }

   protected void clearProgram(Path path, BiancaProgram program) {
      _programCache.remove(path);

      // php/0b36
      if (program != null) {
         _bianca.clearDefinitionCache();
      }
   }

   protected BiancaPage compilePage(BiancaProgram program, Path path) {
      if (log.isLoggable(Level.FINE)) {
         log.fine(L.l("Bianca[{0}] loading interpreted page", path));
      }

      return new InterpretedPage(program);
   }

   public void close() {
      _isClosed = true;
   }
}
