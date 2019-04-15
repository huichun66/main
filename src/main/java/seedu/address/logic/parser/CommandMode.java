package seedu.address.logic.parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the mode for AddPersonCommand, EditPersonCommand and DeletePersonCommand.
 * @author Lookaz
 */
public enum CommandMode {

    HEALTH_WORKER,
    PATIENT,
    REQUEST,
    OTHERS,
    INVALID;

    public static final String MODE_HEALTHWORKER = "1";
    public static final String MODE_REQUEST = "2";

    private static final Set<String> healthWorkerModes = new HashSet<>(Arrays.asList("healthworker", "h", "1"));
    private static final Set<String> requestModes = new HashSet<>(Arrays.asList("request", "r", "2"));

    /**
     * Method that checks for the corresponding CommandMode given a string input.
     * Returns INVALID if input string does not match any of the given modes.
     */
    public static CommandMode checkMode(String input) {
        if (healthWorkerModes.contains(input)) {
            return CommandMode.HEALTH_WORKER;
        } else if (requestModes.contains(input)) {
            return CommandMode.REQUEST;
        }

        return CommandMode.INVALID;
    }
}
