package com.github.funthomas424242.rezeptsammlung.nitrite;

/*-
 * #%L
 * rezeptsammlung
 * %%
 * Copyright (C) 2019 PIUG
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.dizitart.no2.Nitrite;

public class NitriteTemplate {

    protected final Nitrite nitriteInstanz;

    public NitriteTemplate(final Nitrite nitrite) {
        nitriteInstanz = nitrite;
    }

    public void init() {
        //        nitriteInstanz.
        //        map.put("host", "mail.example.com");
        //        map.put("port", "25");
        //        map.put("from", "example@boraji.com");
        //        System.out.println("Inside init method - "+map);
    }

    public void destroy() {
        /**
         * Shutdown Hook
         *
         * While opening the database, Nitrite registers itself to a JVM shutdown hook, which before exiting will close the
         * database without persisting any unsaved changes to the disk. This shutdown hook protects the data file from
         * corruption due to JVM shutdown before properly closing the database.
         */
    }




}
