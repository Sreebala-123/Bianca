/*
* Copyright (c) 1998-2010 Caucho Technology -- all rights reserved
 * Copyright (c) 2011-2012 Clever Cloud SAS -- all rights reserved
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
* @author Emil Ong
*/

package com.clevercloud.xml.stream.events;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.EntityReference;
import java.io.Writer;

public class EntityReferenceImpl extends XMLEventImpl
   implements EntityReference {
   private final String _name;
   private final EntityDeclaration _declaration;

   public EntityReferenceImpl(String name, EntityDeclaration declaration) {
      _name = name;
      _declaration = declaration;
   }

   public EntityDeclaration getDeclaration() {
      return _declaration;
   }

   public String getName() {
      return _name;
   }

   public int getEventType() {
      return ENTITY_REFERENCE;
   }

   public void writeAsEncodedUnicode(Writer writer)
      throws XMLStreamException {
      // XXX
   }

   public boolean equals(Object o) {
      if (!(o instanceof EntityReference))
         return false;
      if (o == null)
         return false;
      if (this == o)
         return true;

      EntityReference entity = (EntityReference) o;

      return getName().equals(entity.getName()) &&
         getDeclaration().equals(entity.getDeclaration());
   }
}

