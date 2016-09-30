/*
 * ProcedureAS.java
 *
 * 03/14/2009
 * Copyright (C) 2009 Leonardo Alves da Costa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.googlecode.paradox.procedures;

import com.googlecode.paradox.procedures.math.Average;
import com.googlecode.paradox.procedures.math.Count;
import com.googlecode.paradox.procedures.math.Max;
import com.googlecode.paradox.procedures.math.Min;
import com.googlecode.paradox.procedures.math.Sum;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the procedures in this driver.
 *
 * @author Leonardo Alves da Costa
 * @version 1.1
 * @since 1.0
 */
public final class ProcedureAS {

    /**
     * Default instance.
     */
    private static final ProcedureAS INSTANCE = new ProcedureAS();

    /**
     * All registered procedures.
     */
    private final ArrayList<AbstractCallableProcedure> procedures = new ArrayList<>();

    /**
     * Register the default procedures.
     */
    private ProcedureAS() {
        register(new Average());
        register(new Count());
        register(new Max());
        register(new Min());
        register(new Sum());
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static ProcedureAS getInstance() {
        return INSTANCE;
    }

    /**
     * Gets the procedure by name.
     *
     * @param name
     *         the procedure name.
     * @return the procedure.
     */
    public AbstractCallableProcedure get(final String name) {
        for (final AbstractCallableProcedure procedure : procedures) {
            if (procedure.getName().equalsIgnoreCase(name)) {
                return procedure;
            }
        }
        return null;
    }

    /**
     * Gets the procedures list.
     *
     * @return the procedures list.
     */
    public List<AbstractCallableProcedure> list() {
        return procedures;
    }

    /**
     * Registers a new procedure.
     *
     * @param procedure
     *         the procedure to register.
     */
    private void register(final AbstractCallableProcedure procedure) {
        procedures.add(procedure);
    }
}
