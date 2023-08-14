//package com.example.email.writer;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import org.slf4j.Logger ;
//
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class myWriterTest {
//
//    @Test
//    public void testWrite() throws Exception {
//        // Mock the logger
//        Logger logger = mock(Logger.class);
//        myWriter myWriter = new myWriter(logger);
//
//        // Call the write() method
//        myWriter.write(null);
//
//        // Verify that the logger methods were called with the expected messages
//        verify(logger).info("Sending emails...");
//        verify(logger).info("Emails sent successfully.");
//    }
//}
//
//
