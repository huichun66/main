package seedu.address.logic.commands.request;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import seedu.address.model.CommandType;
import seedu.address.model.Model;

/**
 * Interface that represents a command involving a request
 *
 * @@author daviddl9
 */
public interface RequestCommand {

    String COMMAND_OPTION = "request";

    String ADD_COMMAND_PARAMETERS = PREFIX_NAME + "NAME "
            + PREFIX_NRIC + "NRIC "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_DATE + "DATETIME "
            + PREFIX_CONDITION + "CONDITION...\n";

    String ADD_COMMAND_EXAMPLE = PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "81234567 "
            + PREFIX_NRIC + "S9123456A "
            + PREFIX_ADDRESS + "123, Sengkang Ave 3, #04-12, 214632 "
            + PREFIX_DATE + "01-01-2019 08:00:00 "
            + PREFIX_CONDITION + "Physiotherapy";

    String EDIT_COMMAND_PARAMETERS = "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_STATUS + "STATUS] "
            + "[" + PREFIX_NRIC + "NRIC] "
            + "[" + PREFIX_CONDITION + "CONDITION]...\n"
            + "Note that the date format is dd-mm-yyyy HH:mm:ss in 24hour format.\n";

    String EDIT_COMMAND_EXAMPLE = PREFIX_PHONE + "91234567 "
            + PREFIX_CONDITION + "Physiotherapy "
            + PREFIX_CONDITION + "Dialysis";

    String MESSAGE_DUPLICATE_REQUEST = "This request already exists in the "
            + "request book.";

    default void commitRequestBook(Model model) {
        model.commit(CommandType.REQUEST_COMMAND);
    }
}
