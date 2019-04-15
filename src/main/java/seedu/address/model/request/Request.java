package seedu.address.model.request;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Phone;
import seedu.address.model.person.healthworker.HealthWorker;
import seedu.address.model.tag.Condition;

/**
 * Represents a request made by a patient in the request book.
 *
 * @@author daviddl9
 */
public class Request {

    private final Name name;
    private final Nric nric;
    private final Address address;
    private final Phone phone;
    private final RequestDate requestDate;
    private Set<Condition> conditions;
    private String healthWorker = null;
    private RequestStatus requestStatus;

    /**
     * Overloaded constructor that takes in differing arguments for the patient.
     */
    public Request(Name name, Nric nric, Phone phone, Address address, RequestDate requestDate,
                   Set<Condition> conditions, RequestStatus status) {
        requireAllNonNull(name, phone, nric, address, requestDate, conditions, status);
        this.phone = phone;
        this.conditions = conditions;
        this.requestStatus = status;
        this.requestDate = requestDate;
        this.name = name;
        this.nric = nric;
        this.address = address;
    }

    /**
     * Constructor that takes in a {@code Request} object and copies over it's values.
     */
    public Request(Request toCopy) {
        requireNonNull(toCopy);
        this.phone = toCopy.getPhone();
        this.conditions = toCopy.getConditions();
        this.requestStatus = toCopy.getRequestStatus();
        this.requestDate = toCopy.getRequestDate();
        this.name = toCopy.getName();
        this.nric = toCopy.getNric();
        this.address = toCopy.getAddress();
        this.healthWorker = toCopy.getHealthStaff();
    }

    /**
     * Simplified constructor that takes in the minimal arguments to form a {@code Request}
     * <p>
     * Sets the {@code requestStatus} to pending
     */
    public Request(Name name, Nric nric, Phone phone, Address address, RequestDate requestDate,
                   Set<Condition> conditions) {
        requireAllNonNull(name, nric, address, requestDate, conditions, phone);
        this.phone = phone;
        this.conditions = conditions;
        this.requestStatus = new RequestStatus("PENDING");
        this.name = name;
        this.nric = nric;
        this.requestDate = requestDate;
        this.address = address;
    }

    /**
     * Overloaded constructor to represent a request.
     */
    public Request(Name name, Nric nric, Phone phone, Address address, RequestDate requestDate,
                   Set<Condition> conditions, RequestStatus status, String healthWorker) {
        requireAllNonNull(name, address, phone, requestDate, conditions, status);
        this.phone = phone;
        this.conditions = conditions;
        this.requestStatus = status;
        this.requestDate = requestDate;
        this.healthWorker = healthWorker;
        this.name = name;
        this.nric = nric;
        this.address = address;
    }


    /**
     * Returns true if both requests of the same ID and date have at least one other
     * property field that is the same.
     * This defines a weaker notion of equality between two requests.
     */
    public boolean isSameRequest(Request otherRequest) {
        if (otherRequest == this) {
            return true;
        }

        if (otherRequest == null) {
            return false;
        }

        return otherRequest.nric.equals(this.nric)
            && otherRequest.getRequestDate().equals(this.requestDate)
            && otherRequest.getConditions().equals(this.conditions)
            && otherRequest.address.equals(this.address);
    }

    public Name getName() {
        return this.name;
    }

    public Address getAddress() {
        return this.address;
    }

    public Nric getNric() {
        return this.nric;
    }

    @Override
    public String toString() {
        String hw = (healthWorker == null) ? "Unassigned" : healthWorker;
        final StringBuilder builder = new StringBuilder();
        builder.append("\n----------Request----------\n")
                .append("Name: ")
                .append(getName() + "\n")
                .append("Nric: ")
                .append(getNric() + "\n")
                .append("Phone: ")
                .append(getPhone() + "\n")
                .append("Address: ")
                .append(getAddress() + "\n")
                .append("Assigned staff: ")
                .append(hw + "\n")
                .append("Request Date: ")
                .append(getRequestDate() + "\n")
                .append("Condition(s): ");
        builder.append(getConditions() + "\n")
                .append("Status: ")
                .append(getRequestStatus() + "\n")
                .append("----------End of Request----------\n");
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Request)) {
            return false;
        }

        Request otherRequest = (Request) other;
        return (otherRequest.getRequestDate().equals(this.requestDate))
            && (otherRequest.name.equals(this.name))
            && (otherRequest.address.equals(this.address))
            && (otherRequest.phone.equals(this.phone))
            && (otherRequest.nric.equals(this.nric))
            && (otherRequest.getConditions().equals(this.conditions))
            && (Objects.equals(this.healthWorker, otherRequest.getHealthStaff()))
            && (otherRequest.getRequestStatus().equals(this.requestStatus));
    }

    public Set<Condition> getConditions() {
        return this.conditions;
    }

    public RequestStatus getRequestStatus() {
        return this.requestStatus;
    }

    public RequestDate getRequestDate() {
        return this.requestDate;
    }

    public String getHealthStaff() {
        return healthWorker;
    }

    public void setRequestStatus(RequestStatus status) {
        requireNonNull(status);
        this.requestStatus = new RequestStatus(status.toString());
    }

    public void setHealthStaff(String healthStaff) {
        requireNonNull(healthStaff);
        this.healthWorker = healthStaff;
        this.requestStatus = new RequestStatus("ONGOING");
    }

    public void setHealthWorker(HealthWorker healthWorker) {
        requireNonNull(healthWorker);
        this.healthWorker = healthWorker.getNric().toString();
        this.requestStatus = new RequestStatus("ONGOING");
    }

    public boolean isOngoingStatus() {
        return this.requestStatus.isOngoingStatus();
    }

    public boolean isCompleted() {
        return this.requestStatus.isCompletedStatus();
    }

    public Phone getPhone() {
        return phone;
    }

    public void complete() {
        this.requestStatus = new RequestStatus("COMPLETED");
    }
}
