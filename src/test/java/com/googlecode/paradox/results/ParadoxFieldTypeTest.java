/*
 * ParadoxFieldTypeTest.java
 *
 * 07/17/2016
 * Copyright (C) 2016 Leonardo Alves da Costa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.googlecode.paradox.results;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Unit test for {@link ParadoxFieldType} class.
 *
 * @author Leonardo Alves da Costa
 * @version 1.0
 * @since 1.3
 */
public class ParadoxFieldTypeTest {

    /**
     * Test for SQL type.
     *
     * @throws SQLException in case of errors.
     */
    @Test
    public void testSQLType() throws SQLException {
        Assert.assertEquals("Test for get SQL type.", ParadoxFieldType.AUTO_INCREMENT.getSQLType(),
                ParadoxFieldType.getSQLType(ParadoxFieldType.AUTO_INCREMENT.getType()));
    }

    /**
     * Test for invalid type.
     *
     * @throws SQLException if there is no errors.
     */
    @Test(expected = SQLException.class)
    public void getType() throws SQLException {
        ParadoxFieldType.getSQLType(-1);
    }

}