/*
 * TokenType.java
 *
 * 03/12/2009
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
package com.googlecode.paradox.parser;

/**
 * SQL Tokens
 *
 * @author Leonardo Alves da Costa
 * @since 1.0
 * @version 1.2
 */
public enum TokenType {
    /**
     * And token.
     */
    AND,

    /**
     * AS token.
     */
    AS,

    /**
     * All fields token.
     */
    ASTERISK("*"),

    /**
     * Between token.
     */
    BETWEEN,

    /**
     * By token.
     */
    BY,

    /**
     * Character literal.
     */
    CHARACTER(null),

    /**
     * Comma token.
     */
    COMMA(","),

    /**
     * Delete token.
     */
    DELETE,

    /**
     * Distinct token.
     */
    DISTINCT,

    /**
     * Equals token.
     */
    EQUALS("="),

    /**
     * Exists token.
     */
    EXISTS,

    /**
     * From token.
     */
    FROM,

    /**
     * Having token.
     */
    HAVING,

    /**
     * Identifier token.
     */
    IDENTIFIER,

    /**
     * Inner token.
     */
    INNER,

    /**
     * Insert token.
     */
    INSERT,

    /**
     * Into token.
     */
    INTO,

    /**
     * Join token.
     */
    JOIN,

    /**
     * Left token.
     */
    LEFT,

    /**
     * Less token.
     */
    LESS("<"),

    /**
     * Left parenthesis token.
     */
    LPAREN("("),

    /**
     * Minus token.
     */
    MINUS("-"),

    /**
     * More token.
     */
    MORE(">"),

    /**
     * Not token.
     */
    NOT,

    /**
     * Not equals token.
     */
    NOTEQUALS("<>"),

    /**
     * Variant not equals token.
     */
    NOTEQUALS2("!="),

    /**
     * Null token.
     */
    NULL,

    /**
     * Numeric token.
     */
    NUMERIC(null),

    /**
     * ON token.
     */
    ON,

    /**
     * OR token.
     */
    OR,

    /**
     * Order token.
     */
    ORDER,

    /**
     * Outer token.
     */
    OUTER,

    /**
     * Period token.
     */
    PERIOD("."),

    /**
     * Plus token.
     */
    PLUS("+"),

    /**
     * Right token.
     */
    RIGHT,

    /**
     * Right parenthesis token.
     */
    RPAREN(")"),

    /**
     * Select token.
     */
    SELECT,

    /**
     * Semicolon token.
     */
    SEMI(";"),

    /**
     * Update token.
     */
    UPDATE,

    /**
     * Where token.
     */
    WHERE,

    /**
     * XOR token.
     */
    XOR;

    /**
     * Token value.
     */
    private String value;

    /**
     * Creates a new instance.
     */
    private TokenType() {
        value = name();
    }

    /**
     * Creates a new instance.
     * 
     * @param value
     *            the token value.
     */
    private TokenType(final String value) {
        this.value = value;
    }

    /**
     * Gets the token by value.
     * 
     * @param value
     *            the value to search on the token list.
     * @return the token by value.
     */
    public static TokenType get(final String value) {
        for (final TokenType token : TokenType.values()) {
            if (value.equals(token.value)) {
                return token;
            }
        }
        return null;
    }

}
