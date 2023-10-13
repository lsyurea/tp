package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Person;

/**
 * UnMarks the attendance of an existing student in the taa.
 */
public class UnMarkAttendanceCommand extends Command {
    public static final String COMMAND_WORD = "unmarkAtd";
    public static final String ATTENDANCE_UNMARK_SUCCESS = "Attendance unmarked successfully!";
    public static final String ATTENDANCE_UNMARK_FAIL = "Attendance failed to unmark!";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks the attendance of the student identified\n"
            + "by the index number used in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer), "
            + "[" + PREFIX_TUTORIAL + "TutorialToUnmark] \n"
            + "Example: " + COMMAND_WORD + " 1 t/1 ";
    private final Index index;
    private final Index tut;

    /**
     * Constructs a new UnMarkAttendanceCommand to mark attendance for a student on a specific week.
     *
     * @param index The index of the student to unmark attendance for.
     * @param tut The index of the week to unmark attendance on.
     */
    public UnMarkAttendanceCommand(Index index, Index tut) {
        this.index = index;
        this.tut = tut;
    }

    /**
     * Executes the UnMarkAttendanceCommand to unmark attendance for a student on a specific week.
     *
     * @param model The model that the command operates on.
     * @return A CommandResult indicating the outcome of the execution.
     * @throws CommandException If there is an error in executing the command.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person studentToEdit = lastShownList.get(index.getZeroBased());
        Person editedStudent = new Person(
                studentToEdit.getName(), studentToEdit.getPhone(), studentToEdit.getEmail(),
                studentToEdit.getAddress(), studentToEdit.getAttendance(), studentToEdit.getTags(),
                studentToEdit.getComments());
        Attendance studentAtd = studentToEdit.getAttendance();
        if (!studentAtd.isMarkedWeek(this.tut.getZeroBased())) {
            return new CommandResult(Messages.MESSAGE_DUPLICATE_MARKINGS);
        }
        studentAtd.markAttendance(this.tut.getZeroBased());

        model.setPerson(studentToEdit, editedStudent);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(generateSuccessMessage(editedStudent));
    }

    /**
     * Generates a command execution success message based on whether
     * the attendance is unmarked.
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message = personToEdit.getAttendance().isMarkedWeek(this.tut.getZeroBased())
                ? ATTENDANCE_UNMARK_SUCCESS
                : ATTENDANCE_UNMARK_FAIL;
        return String.format(message, personToEdit);
    }
}
