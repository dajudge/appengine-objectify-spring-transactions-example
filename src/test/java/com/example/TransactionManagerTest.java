package com.example;

import com.example.entities.Car;
import com.example.facade.TestBCI;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 * Created by romanl on 03.02.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:appengine-context.xml")
public class TransactionManagerTest {

    @Resource
    private TestBCI testBCI;

    private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setup() {
        // Objectify wants to know all entities
        ObjectifyService.register(Car.class);
        // Appengine integration test setup
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void not_persists_modification_on_rollback() {
        testBCI.setFailOnCreate(true);

        try {
            testBCI.createCar("series3");
        } catch (TestBCI.LolCatsException e) {
        }
        assertNull(ofy().load().type(Car.class).id("series3").now());
    }

    @Test
    public void persists_modification_on_commit() {
        testBCI.setFailOnCreate(false);

        testBCI.createCar("series1");
        assertNotNull(ofy().load().type(Car.class).id("series1").now());

        testBCI.createCar("series2");
        assertNotNull(ofy().load().type(Car.class).id("series2").now());
    }

    @Test(expected = TestBCI.LolCatsException.class)
    public void throws_correct_exception_on_rollback() {
        testBCI.setFailOnCreate(true);
        testBCI.createCar("series4");
    }
}
