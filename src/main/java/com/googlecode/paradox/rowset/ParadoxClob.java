package com.googlecode.paradox.rowset;

import com.googlecode.paradox.data.table.value.ClobDescriptor;
import com.googlecode.paradox.metadata.BlobTable;
import com.googlecode.paradox.utils.SQLStates;
import com.googlecode.paradox.utils.StringUtils;
import com.googlecode.paradox.utils.filefilters.TableFilter;

import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Clob for paradox file (mb).
 *
 * Created by Andre on 22.12.2014.
 */
public class ParadoxClob implements Clob {

    private long length;
    private long fieldLength;
    private long offset;
    private short modificator;
    private byte[] value;
    private BlobTable blob = null;
    private boolean parsed = false;

    public ParadoxClob(ClobDescriptor descriptor) {
        length = 0;
        fieldLength = descriptor.getLength();
        value = null;
        offset = -1;
        // If MB_Offset = 0 then the entire blob is contained in the leader.
        if (descriptor.getOffset() == 0) {
            if (descriptor.getLeader() != null) {
                value = descriptor.getLeader().getBytes();
                length = value.length;
            }
            parsed = true;
        } else {
            offset = descriptor.getOffset();
            modificator = descriptor.getModificator();
            blob = descriptor.getFile();
        }
    }

    @Override
    public long length() throws SQLException {
        parse();
        isValid();
        return length;
    }

    @Override
    public String getSubString(long pos, int length) throws SQLException {
        parse();
        isValid();
        if (pos < 1 || pos > this.length()) {
            throw new SQLException("Invalid position '" + pos + "' in Clob object set");
        }

        if ((pos-1) + length > this.length()) {
            throw new SQLException("Invalid position and substring length");
        }

        try {
            return new String(value, (int)pos - 1, length);

        } catch (StringIndexOutOfBoundsException e) {
            throw new SQLException("StringIndexOutOfBoundsException: " + e.getMessage());
        }
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        parse();
        isValid();
        return new InputStreamReader(new ByteArrayInputStream(value));
    }

    @Override
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        parse();
        isValid();
        if (pos < 1 || pos > length) {
            throw new SQLException("Invalid position in Clob object set");
        }

        if ((pos-1) + length > length) {
            throw new SQLException("Invalid position and substring length");
        }
        if (length <= 0) {
            throw new SQLException("Invalid length specified");
        }

        return new InputStreamReader(new ByteArrayInputStream(value, (int)pos, (int)length));
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
        return null;
    }

    @Override
    public long position(String searchstr, long start) throws SQLException {
        return 0;
    }

    @Override
    public long position(Clob searchstr, long start) throws SQLException {
        return 0;
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
        return 0;
    }

    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        return 0;
    }

    @Override
    public OutputStream setAsciiStream(long pos) throws SQLException {
        return null;
    }

    @Override
    public Writer setCharacterStream(long pos) throws SQLException {
        return null;
    }

    @Override
    public void truncate(long len) throws SQLException {
        parse();
        isValid();
        if (length > len) {
            throw new SQLException("Length more than what can be truncated");
        } else {
            len = length;
            // re-size the buffer

            if (len == 0) {
                value = new byte[] {};
            } else {
                value = (this.getSubString(1, (int)len)).getBytes();
            }
        }
    }

    @Override
    public void free() throws SQLException {
        if (value != null) {
            value = null;
        }
        if (blob != null) {
            blob.close();
        }
    }

/********************************* Private methods ****************************************************/

    private void parse() throws SQLException{
        if (!parsed) {
            try {
                value = blob.read(offset, fieldLength);
                parsed = blob.isParsed();
                length = value.length;
            } catch (Exception x) {
                throw new SQLException("Reading CLOB error", SQLStates.LOAD_DATA, x);
            }
        }
    }

    private void isValid() throws SQLException{
        if (!parsed && blob == null)
            throw new SQLException("Invalid CLOB descriptor.");
    }
}