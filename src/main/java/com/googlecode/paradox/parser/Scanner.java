/*
 * Scanner.java 03/14/2009 Copyright (C) 2009 Leonardo Alves da Costa This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.googlecode.paradox.parser;

import com.googlecode.paradox.utils.SQLStates;

import java.nio.CharBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import static com.googlecode.paradox.utils.Utils.position;

/**
 * SQL Scanner (read tokens from SQL String).
 *
 * @author Leonardo Alves da Costa
 * @version 1.2
 * @since 1.0
 */
public class Scanner {

    /**
     * Separators char.
     */
    private static final char[] SEPARATORS = {' ', '\t', '\n', '\0', '\r'};

    /**
     * Special chars.
     */
    private static final char[] SPECIAL = {'(', ')', '+', '-', ',', '.', '=', ';'};

    /**
     * Character buffer used to parse the SQL.
     */
    private final CharBuffer buffer;

    /**
     * Read tokens.
     */
    private final ArrayList<Token> tokens = new ArrayList<>();

    /**
     * Value buffer.
     */
    private final StringBuilder value = new StringBuilder(299);

    /**
     * Creates a new instance.
     *
     * @param buffer the buffer to read of.
     * @throws SQLException in case of parse errors.
     */
    Scanner(final String buffer) throws SQLException {
        if (buffer == null) {
            throw new SQLException("NULL SQL Query.", SQLStates.INVALID_SQL.getValue());
        }
        this.buffer = CharBuffer.wrap(buffer.trim());
    }

    /**
     * Checks for maximum number dots allowed.
     *
     * @param dotCount the dot count.
     * @throws SQLException in case of invalid dot count.
     */
    private static void checkDotCount(final int dotCount) throws SQLException {
        if (dotCount > 1) {
            throw new SQLException("Invalid numeric format", SQLStates.INVALID_SQL.getValue());
        }
    }

    /**
     * Creates a token by value.
     *
     * @param value to convert.
     * @return a new {@link Token}.
     */
    private static Token getToken(final String value) {
        if (value.isEmpty()) {
            return null;
        }
        final TokenType token = TokenType.get(value.toUpperCase(Locale.US));
        if (token != null) {
            return new Token(token, value);
        }
        return new Token(TokenType.IDENTIFIER, value);
    }

    /**
     * Check if is a character or a string.
     *
     * @param c the char to verify.
     * @return <code>true</code> if c is a char.
     */
    private static boolean isCharacters(final char c) {
        boolean characters = false;
        if (c == '\'') {
            // characters
            characters = true;
        }
        return characters;
    }

    /**
     * If the char is a separator.
     *
     * @param value the char to identify.
     * @return true if the char is a separator.
     */
    private static boolean isSeparator(final char value) {
        for (final char c : Scanner.SEPARATORS) {
            if (c == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * if the value is a special char.
     *
     * @param value the value to identify.
     * @return true if the value is a special char.
     */
    private static boolean isSpecial(final char value) {
        for (final char c : Scanner.SPECIAL) {
            if (c == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the next value in buffer.
     *
     * @return the next char.
     */
    private char nextChar() {
        return this.buffer.get();
    }

    /**
     * Parses identifier tokens.
     *
     * @return if this token is an character token.
     * @throws SQLException in case of parser errors.
     */
    private boolean parseIdentifier() throws SQLException {
        while (this.hasNext()) {
            final char c = this.nextChar();

            // Ignore separators.
            if (!Scanner.isSeparator(c)) {
                if (Scanner.isSpecial(c)) {
                    this.value.append(c);
                    return false;
                } else if ((c == '"') || (c == '\'')) {
                    // identifiers with special chars
                    final boolean characters = Scanner.isCharacters(c);
                    this.parseString(c);
                    return characters;
                } else {
                    this.parseNumber(c);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Parses a numeric char.
     *
     * @param start the char to start of.
     * @throws SQLException in case of parse errors.
     */
    private void parseNumber(final char start) throws SQLException {
        char c = start;
        boolean numeric = false;
        int dotCount = 0;
        while (!Scanner.isSeparator(c) && ((numeric && (c == '.')) || !Scanner.isSpecial(c))) {
            this.value.append(c);
            if (this.value.length() == 1) {
                numeric = Character.isDigit(this.value.charAt(0));
            } else if (c == '.') {
                dotCount++;

                // Only one dot per numeric value
                Scanner.checkDotCount(dotCount);
            }
            if (this.hasNext()) {
                c = this.nextChar();
            } else {
                break;
            }
        }
        if (Scanner.isSeparator(c) || Scanner.isSpecial(c)) {
            this.pushBack();
        }
    }

    /**
     * Parses a {@link String} value.
     *
     * @param type the string type (special char used).
     */
    private void parseString(final char type) {
        char c;
        do {
            if (this.hasNext()) {
                c = this.nextChar();
            } else {
                return;
            }
            if (c == type) {
                if (this.hasNext()) {
                    c = this.nextChar();
                } else {
                    return;
                }
                if (c == type) {
                    this.value.append(c);
                    // prevent breaking
                    c = ' ';
                } else {
                    this.pushBack();
                    return;
                }
            } else {
                this.value.append(c);
            }
        } while (c != type);
    }

    /**
     * Push back the read char.
     */
    private void pushBack() {
        position(buffer, this.buffer.position() - 1);
    }

    /**
     * If buffer has tokens.
     *
     * @return true if the buffer still have tokens.
     */
    boolean hasNext() {
        return !this.tokens.isEmpty() || this.buffer.hasRemaining();
    }

    /**
     * Gets the next {@link Token} in buffer.
     *
     * @return the next {@link Token}.
     * @throws SQLException in case of parse errors.
     */
    Token nextToken() throws SQLException {
        final int size = this.tokens.size();
        if (size > 0) {
            final Token token = this.tokens.get(size - 1);
            this.tokens.remove(size - 1);
            return token;
        }
        if (!this.hasNext()) {
            throw new SQLException("Unexpected end of SELECT statement.", SQLStates.INVALID_SQL.getValue());
        }
        this.value.delete(0, this.value.length());
        final boolean characters = this.parseIdentifier();
        if (characters) {
            return new Token(TokenType.CHARACTER, this.value.toString());
        } else if (Character.isDigit(this.value.charAt(0))) {
            return new Token(TokenType.NUMERIC, this.value.toString());
        }
        return Scanner.getToken(this.value.toString());
    }

    /**
     * Push back the given token in buffer.
     *
     * @param token the token to push back.
     */
    void pushBack(final Token token) {
        this.tokens.add(token);
    }
}
