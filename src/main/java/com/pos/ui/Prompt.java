package com.pos.ui;

import javax.swing.*;
import java.util.function.BiConsumer;

/**
 *
 * @author peter john
 */
public class Prompt {

    /**
     * Function that shows a custom message dialog.
     * Checks if the prompt is for error or for information message.
     * If the prompt is for error, it shows a message dialog that informs the user what is the error.
     * If the prompt is for information message, the message will be based on the methods that invoked the Function.
     */
    public final BiConsumer<String, Boolean> show = (message, isError) ->
            JOptionPane.showMessageDialog(
                    null,
                    message,
                    (isError ? "SOMETHING WENT WRONG" : "SUCCESS"),
                    (isError ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE)
            );

}
