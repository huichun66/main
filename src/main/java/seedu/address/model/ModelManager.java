package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.healthworker.HealthWorker;
import seedu.address.model.request.Request;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedAddressBook versionedAddressBook;
    private final VersionedHealthWorkerBook versionedHealthWorkerBook;
//    private final VersionedRequest
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<HealthWorker> filteredHealthWorkers;
    // TODO make the relevant changes to the model manager
    // TODO get versionedAddressBook tests to pass
//    private final FilteredList<Request> filteredRequests;
    private final SimpleObjectProperty<Person> selectedPerson = new SimpleObjectProperty<>();

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyHealthWorkerBook healthWorkerBook,
                        ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        versionedAddressBook = new VersionedAddressBook(addressBook);
        versionedHealthWorkerBook = new VersionedHealthWorkerBook(healthWorkerBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(versionedAddressBook.getPersonList());
        filteredHealthWorkers = new FilteredList<>(versionedHealthWorkerBook.getHealthWorkerList());
        filteredPersons.addListener(this::ensureSelectedPersonIsValid);
        // TODO: listener for healthworker
        filteredHealthWorkers.addListener(this::ensureSelectedPersonIsValid);
    }

    public ModelManager() {
        this(new AddressBook(), new HealthWorkerBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        versionedAddressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return versionedAddressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedAddressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        versionedAddressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        versionedAddressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        versionedAddressBook.setPerson(target, editedPerson);
    }

    // ======================== Implemented methods for HealthWorker through Model Interface =========================
    // @author: Lookaz

    @Override
    public boolean hasHealthWorker(HealthWorker healthWorker) {
        requireNonNull(healthWorker);
        return this.versionedHealthWorkerBook.hasHealthWorker(healthWorker);
    }

    @Override
    public void deleteHealthWorker(HealthWorker target) {
        this.versionedAddressBook.removeHealthWorker(target);
    }

    @Override
    public void addHealthWorker(HealthWorker healthWorker) {
        this.versionedHealthWorkerBook.addHealthWorker(healthWorker);
        updateFilteredHealthWorkerList(PREDICATE_SHOW_ALL_HEALTHWORKERS);
    }

    @Override
    public void setHealthWorker(HealthWorker target, HealthWorker editedWorker) {
        requireAllNonNull(target, editedWorker);

        this.versionedHealthWorkerBook.setHealthWorker(target, editedWorker);
    }

    @Override
    public ObservableList<HealthWorker> getFilteredHealthWorkerList() {
        return this.filteredHealthWorkers;
    }

    @Override
    public void updateFilteredHealthWorkerList(Predicate<HealthWorker> predicate) {
        requireNonNull(predicate);
        this.filteredHealthWorkers.setPredicate(predicate);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================
    // TODO: Modify to do redo/undo for HealthWorkerBook. Suggestion: Use a state to maintain previous type of op.

    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    @Override
    public void undoAddressBook() {
        versionedAddressBook.undo();
    }

    @Override
    public void redoAddressBook() {
        versionedAddressBook.redo();
    }

    @Override
    public void commitAddressBook() {
        versionedAddressBook.commit();
    }

    //=========== Selected person ===========================================================================

    @Override
    public ReadOnlyProperty<Person> selectedPersonProperty() {
        return selectedPerson;
    }

    @Override
    public Person getSelectedPerson() {
        return selectedPerson.getValue();
    }

    @Override
    public void setSelectedPerson(Person person) {
        if (person != null && !filteredPersons.contains(person)) {
            throw new PersonNotFoundException();
        }
        selectedPerson.setValue(person);
    }

    /**
     * Returns the user prefs' request book file path.
     */
    @Override
    public Path getRequestBookFilePath() {
        return null;
    }

    /**
     * Sets the user prefs' request book file path.
     *
     * @param requestBookFilePath
     */
    @Override
    public void setRequestBookFilePath(Path requestBookFilePath) {

    }

    /**
     * Replaces request book data with the data in {@code requestBook}.
     *
     * @param requestBook
     */
    @Override
    public void setRequestBook(ReadOnlyRequestBook requestBook) {

    }

    /**
     * Returns the RequestBook
     */
    @Override
    public ReadOnlyRequestBook getRequestBook() {
        return null;
    }

    /**
     * Returns true if a request with the same identity as {@code request} exists in the address
     * book.
     *
     * @param request
     */
    @Override
    public boolean hasRequest(Request request) {
        return false;
    }

    /**
     * Deletes the given request.
     * The request must exist in the request book.
     *
     * @param target
     */
    @Override
    public void deleteRequest(Request target) {

    }

    /**
     * Adds the given request.
     * {@code request} must not already exist in the request book.
     *
     * @param request
     */
    @Override
    public void addRequest(Request request) {

    }

    /**
     * Replaces the given request {@code target} with {@code editedRequest}.
     * {@code target} must exist in the request book.
     * The request identity of {@code editedRequest} must not be the same as another existing
     * request in the request book.
     *
     * @param target
     * @param editedRequest
     */
    @Override
    public void setRequest(Request target, Request editedRequest) {

    }

    /**
     * Ensures {@code selectedPerson} is a valid person in {@code filteredPersons}.
     */
    private void ensureSelectedPersonIsValid(ListChangeListener.Change<? extends Person> change) {
        while (change.next()) {
            if (selectedPerson.getValue() == null) {
                // null is always a valid selected person, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedPersonReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedPerson.getValue());
            if (wasSelectedPersonReplaced) {
                // Update selectedPerson to its new value.
                int index = change.getRemoved().indexOf(selectedPerson.getValue());
                selectedPerson.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedPersonRemoved = change.getRemoved().stream()
                    .anyMatch(removedPerson -> selectedPerson.getValue().isSamePerson(removedPerson));
            if (wasSelectedPersonRemoved) {
                // Select the person that came before it in the list,
                // or clear the selection if there is no such person.
                selectedPerson.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedAddressBook.equals(other.versionedAddressBook)
                && versionedHealthWorkerBook.equals(other.versionedHealthWorkerBook)
                && userPrefs.equals(other.userPrefs)
                && filteredPersons.equals(other.filteredPersons)
                && Objects.equals(selectedPerson.get(), other.selectedPerson.get());
    }

}
