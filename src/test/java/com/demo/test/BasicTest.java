package com.demo.test;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class BasicTest {

    private Timer timer;
    private TestTask taskMock;

    @Before
    public void setup() {
        timer = new TimerImpl();
        taskMock = new TestTask();
    }

    @Test
    public void singleTimerFiresOnce() {
        timer.schedule(taskMock, 100, TimeUnit.MILLISECONDS);
        taskMock.timeout(110).times(1);
    }

    @Test
    public void singleTimerDoesNotFirePrematurely() {
        timer.schedule(taskMock, 200, TimeUnit.MILLISECONDS);
        taskMock.timeout(195).times(0);
        taskMock.timeout(15).times(1);
    }

    @Test
    public void multipleTimersFireSequentially() {
        timer.schedule(taskMock, 100, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 200, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 300, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 400, TimeUnit.MILLISECONDS);

        taskMock.timeout(110).times(1);
        taskMock.timeout(110).times(2);
        taskMock.timeout(110).times(3);
        taskMock.timeout(110).times(4);
    }
    
    @Test
    public void multipleTimersFireSequentiallyDescOrder() {
        timer.schedule(taskMock, 400, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 400, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 400, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 400, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 300, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 300, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 300, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 200, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 200, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 100, TimeUnit.MILLISECONDS);
        
        taskMock.timeout(110).times(1);
        taskMock.timeout(110).times(3);
        taskMock.timeout(110).times(6);
        taskMock.timeout(110).times(10);
    }
    
    //@Test
    // Hackerrank is down if the number of tests are more than 5.
    public void checkOrder() {
        timer.schedule(taskMock, 200, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 100, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 100, TimeUnit.MILLISECONDS);
        taskMock.timeout(105).times(2);
        taskMock.timeout(105).times(3);
    }
    
    public void testManySchedules() {
        for (int i = 0; i < 100; i++) {
            timer.schedule(taskMock, 100, TimeUnit.MILLISECONDS);
        }
        
        taskMock.timeout(90).times(0);
        taskMock.timeout(15).times(100);
    }
    
    public void testHugeSchedules() {
        // hackerrank is down after this test
        /*
        for (int i = 0; i < 1000000; i++) {
            timer.schedule(taskMock, i%1000, TimeUnit.MILLISECONDS);
        }
        
        taskMock.timeout(1000).times(1000000);
        */
    }
    
    @Test 
    public void testDelayBetween() {
        timer.schedule(taskMock, 300, TimeUnit.MILLISECONDS);
        timer.schedule(taskMock, 600, TimeUnit.MILLISECONDS);
        taskMock.timeout(305).times(1);
        taskMock.timeout(50).times(1);
        timer.schedule(taskMock, 50, TimeUnit.MILLISECONDS);
        taskMock.timeout(55).times(2);
        taskMock.timeout(305).times(3);
    }
}
