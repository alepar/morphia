package com.github.jmkgreen.morphia.issue241;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.DatastoreImpl;
import com.github.jmkgreen.morphia.Morphia;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoURI;
import org.junit.Assert;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;


/**
 * Unit test for testing morphia mappings with generics.
 */
public class TestMapping {

    Morphia morphia = new Morphia();

    Mongo mongo;
    Datastore datastore;
    MongoURI uri = new MongoURI("mongodb://127.0.0.1:27017");


    @Before
    public void setUp() {
        try {
            mongo = new Mongo(uri);
            datastore = new DatastoreImpl(morphia, mongo, "MY_DB");
        } catch (UnknownHostException unknownHostException) {
        } catch (MongoException mongoException) {
        }
    }

    @After
    public void tearDown() {
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testMapping() {
        BasicDAO<Message, ObjectId> messageDAO = new BasicDAO<Message, ObjectId>(Message.class, datastore);
        Assert.assertNotNull(messageDAO);
    }

    @SuppressWarnings("unused")
    @Entity
    private static class Message<U extends User> {

        @Id
        private ObjectId id;
        private U user;

        public U getUser() {
            return user;
        }

        public void setUser(U user) {
            this.user = user;
        }
    }

    @Entity
    private static class User {
        @Id
        private ObjectId id;

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final User other = (User) obj;
            if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
            return hash;
        }
    }
}
