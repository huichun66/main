package seedu.address.model.request;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.request.exceptions.DuplicateRequestException;
import seedu.address.model.request.exceptions.RequestNotFoundException;

/**
 * A list of requests that enforces uniqueness between its elements and does not allow nulls.
 * An order is considered unique by comparison using {@code Request#isSameRequest(Request)}. As such, adding and
 * updating of orders uses Request#isSameRequest(Request) for equality so as to ensure that the Request being added or
 * updated is unique in terms of identity in the UniqueRequestList. However, the removal of a Request uses
 * Request#equals(Object) so as to ensure that the request with the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Request#isSameRequest(Request)
 *
 * @@author daviddl9
 */
public class UniqueRequestList implements Iterable<Request> {

    private final ObservableList<Request> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent order as the given argument.
     */
    public boolean contains(Request toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameRequest);
    }

    /**
     * Adds an order to the list.
     * The order must not already exist in the list.
     */
    public void add(Request toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateRequestException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the order {@code target} in the list with {@code editedRequest}.
     * {@code target} must exist in the list.
     * {@code editedRequest} must not be the same as any other request in the list (by Request#isSameRequest(Request)
     * comparison).
     */
    public void setRequest(Request target, Request editedRequest) {
        requireAllNonNull(target, editedRequest);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new RequestNotFoundException();
        }

        if (!target.isSameRequest(editedRequest) && contains(editedRequest)) {
            throw new DuplicateRequestException();
        }

        internalList.set(index, editedRequest);
    }

    public void setRequests(UniqueRequestList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code requests}.
     * {@code requests} must not contain duplicate persons.
     */
    public void setRequests(List<Request> requests) {
        requireAllNonNull(requests);
        if (!requestsAreUnique(requests)) {
            throw new DuplicateRequestException();
        }
        internalList.setAll(requests);
    }

    /**
     * Returns true if all the contents in {@code requests} are unique, false otherwise.
     */
    private boolean requestsAreUnique(List<Request> requests) {
        for (int i = 0; i < requests.size() - 1; i++) {
            for (int j = i + 1; j < requests.size(); j++) {
                if (requests.get(i).isSameRequest(requests.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if 2 request lists are the same.
     */
    public boolean areRequestsSame(UniqueRequestList requests) {
        if (this.internalList.size() != requests.internalList.size()) {
            return false;
        }
        for (int i = 0; i < requests.internalList.size(); i++) {
            if (!(this.internalList.get(i).isSameRequest(requests.internalList.get(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes the equivalent order from the list.
     * The request must exist in the list.
     */
    public void remove(Request toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new RequestNotFoundException();
        }
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Request> asUnmodifiableObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    /**
     * Checks if any Request in the UniqueRequestList contains the assigned HealthWorker nric.
     */
    public boolean isAssigned(String nric) {
        return this.internalList.stream().anyMatch(x -> Objects.equals(nric, x.getHealthStaff())
                && x.getRequestStatus().isOngoingStatus());
    }

    /**
     * Updates the healthStaff field in Request object when the name of the correposnding HealthWorker object is
     * updated.
     */
    public void updateHealthWorker(String oldNric, String newNric) {
        for (Request request : this.internalList) {
            if (Objects.equals(request.getHealthStaff(), oldNric)) {
                Request editedRequest = new Request(request);
                RequestStatus status = request.getRequestStatus();
                editedRequest.setHealthStaff(newNric);
                editedRequest.setRequestStatus(status);
                setRequest(request, editedRequest);
            }
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Request> iterator() {
        return internalList.iterator();
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueRequestList // instanceof handles nulls
                && internalList.equals(((UniqueRequestList) other).internalList));
    }

    @Override
    public String toString() {
        return this.internalList.toString();
    }
}
