package com.mylrajan.client;

import com.mylrajan.ParentalControl.AdditionalMessageCallback;
import com.mylrajan.ParentalControl.ParentalControlService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommandLineClientTest {

    @Mock
    private PrintStream out;
    @Mock
    private ParentalControlService service;

    private InputStream in;
    private CommandLineClient client;

    @Test
    public void start_shouldOrchestrateUserInteraction() throws Exception {
        // Set up fixture
        in = new ByteArrayInputStream(("" +
                "parental control level preference\n" +
                "requested movie id").getBytes());
        client = new CommandLineClient(out, in, service);

        // Set expectations
        when(service.isAllowedToWatchMovie(anyString(), anyString(), any(AdditionalMessageCallback.class)))
                .thenReturn(true);

        // Exercise SUT
        client.start();

        // Verify behaviour
        InOrder inOrder = inOrder(out, service);
        inOrder.verify(out).print("Welcome to the Parental Control Service!\n" +
                "Please enter your Parental Control Level preference: ");
        inOrder.verify(out).print("Please enter the movie id you would like to view: ");
        inOrder.verify(service).isAllowedToWatchMovie(
                eq("parental control level preference"), eq("requested movie id"), any(AdditionalMessageCallback.class));
        inOrder.verify(out).println(anyString());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void startWithWatchAllowed_shouldDisplayApproval() throws Exception {
        // Set up fixture
        in = new ByteArrayInputStream(("" +
                "parental control level preference\n" +
                "requested movie id").getBytes());
        client = new CommandLineClient(out, in, service);

        // Set expectations
        when(service.isAllowedToWatchMovie(anyString(), anyString(), any(AdditionalMessageCallback.class)))
                .thenReturn(true);

        // Exercise SUT
        client.start();

        // Verify behaviour
        verify(out).println("\nYou may watch this movie!");
    }

    @Test
    public void startWithWatchNotAllowed_shouldDisplayForbidden() throws Exception {
        // Set up fixture
        in = new ByteArrayInputStream(("" +
                "parental control level preference\n" +
                "requested movie id").getBytes());
        client = new CommandLineClient(out, in, service);

        // Set expectations
        when(service.isAllowedToWatchMovie(anyString(), anyString(), any(AdditionalMessageCallback.class)))
                .thenReturn(false);

        // Exercise SUT
        client.start();

        // Verify behaviour
        verify(out).println("\nYou are not allowed to watch this movie.");
    }

    @Test
    public void startWithMessageCallbackUsed_shouldPrintMessage() throws Exception {
        // Set up fixture
        in = new ByteArrayInputStream(("" +
                "parental control level preference\n" +
                "requested movie id").getBytes());
        client = new CommandLineClient(out, in, service);

        // Set expectations
        when(service.isAllowedToWatchMovie(anyString(), anyString(), any(AdditionalMessageCallback.class)))
                .thenAnswer(invocation -> {
                    final AdditionalMessageCallback callback = (AdditionalMessageCallback) invocation.getArguments()[2];
                    callback.showMessage("error message");
                    return false;
                });

        // Exercise SUT
        client.start();

        // Verify behaviour
        verify(out).println("error message");
    }


}