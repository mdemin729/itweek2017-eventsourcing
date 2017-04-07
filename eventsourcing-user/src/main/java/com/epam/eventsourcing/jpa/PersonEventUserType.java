package com.epam.eventsourcing.jpa;

import com.epam.eventsourcing.event.PersonEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public class PersonEventUserType implements UserType {

    private static final int[] SQL_TYPES = new int[]{Types.VARCHAR};
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Return the SQL type codes for the columns mapped by this type. The
     * codes are defined on <tt>java.sql.Types</tt>.
     *
     * @return int[] the typecodes
     * @see Types
     */
    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    /**
     * The class returned by <tt>nullSafeGet()</tt>.
     *
     * @return Class
     */
    @Override
    public Class returnedClass() {
        return PersonEvent.class;
    }

    /**
     * Compare two instances of the class mapped by this type for persistence "equality".
     * Equality of the persistent state.
     *
     * @param x
     * @param y
     * @return boolean
     */
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    /**
     * Get a hashcode for the instance, consistent with persistence "equality"
     *
     * @param x
     */
    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hashCode(x);
    }

    /**
     * Retrieve an instance of the mapped class from a JDBC resultset. Implementors
     * should handle possibility of null values.
     *
     * @param rs      a JDBC result set
     * @param names   the column names
     * @param session
     * @param owner   the containing entity  @return Object
     * @throws HibernateException
     * @throws SQLException
     */
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {

        String data = rs.getString(names[0]);
        if (data == null) {
            return null;
        }

        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, PersonEvent.class);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to PersonEvent: " + ex.getMessage(), ex);
        }
    }

    /**
     * Write an instance of the mapped class to a prepared statement. Implementors
     * should handle possibility of null values. A multi-column type should be written
     * to parameters starting from <tt>idx</tt>.
     *
     * @param ps      a JDBC prepared statement
     * @param value   the object to write
     * @param idx   statement parameter idx
     * @param session
     * @throws HibernateException
     * @throws SQLException
     */
    @Override
    public void nullSafeSet(PreparedStatement ps, Object value, int idx, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            ps.setNull(idx, Types.VARCHAR);
            return;
        }
        try {
            String s = mapper.writeValueAsString(value);
            ps.setObject(idx, s, Types.VARCHAR);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert PersonEvent to String: " + ex.getMessage(), ex);
        }
    }

    /**
     * Return a deep copy of the persistent state, stopping at entities and at
     * collections. It is not necessary to copy immutable objects, or null
     * values, in which case it is safe to simply return the argument.
     *
     * @param value the object to be cloned, which may be null
     * @return Object a copy
     */
    @Override
    public Object deepCopy(Object value) throws HibernateException {

        try {
            String s = mapper.writeValueAsString(value);
            return mapper.readValue(s, PersonEvent.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * Are objects of this type mutable?
     *
     * @return boolean
     */
    @Override
    public boolean isMutable() {
        return true;
    }

    /**
     * Transform the object into its cacheable representation. At the very least this
     * method should perform a deep copy if the type is mutable. That may not be enough
     * for some implementations, however; for example, associations must be cached as
     * identifier values. (optional operation)
     *
     * @param value the object to be cached
     * @return a cachable representation of the object
     * @throws HibernateException
     */
    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * Reconstruct an object from the cacheable representation. At the very least this
     * method should perform a deep copy if the type is mutable. (optional operation)
     *
     * @param cached the object to be cached
     * @param owner  the owner of the cached object
     * @return a reconstructed object from the cachable representation
     * @throws HibernateException
     */
    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        try {
            String s = (String) cached;
            return mapper.readValue(s, PersonEvent.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * During merge, replace the existing (target) value in the entity we are merging to
     * with a new (original) value from the detached entity we are merging. For immutable
     * objects, or null values, it is safe to simply return the first parameter. For
     * mutable objects, it is safe to return a copy of the first parameter. For objects
     * with component values, it might make sense to recursively replace component values.
     *
     * @param original the value from the detached entity being merged
     * @param target   the value in the managed entity
     * @param owner
     * @return the value to be merged
     */
    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }
}
