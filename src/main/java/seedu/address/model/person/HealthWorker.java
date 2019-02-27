package seedu.address.model.person;

import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Skills;
import seedu.address.model.tag.Specialisation;
import seedu.address.model.tag.Tag;

/**
 * Represents a Health Worker class that can handle requests.
 * Guarantees: details are present and not null, and field values are validated.
 */
public class HealthWorker extends Person {

    private Organization organization;
    private Skills skills;

    public HealthWorker(Name name, Phone phone, Email email, Nric nric, Address
                        address, Set<Tag> tags, Organization organization) {
        super(name, phone, email, nric, address, tags);
        this.organization = organization;
        this.skills = new Skills();
    }

    public HealthWorker(Name name, Phone phone, Email email, Nric nric, Address address,
                        Set<Tag> tags, Organization organization, Skills skills) {
        super(name, phone, email, nric, address, tags);
        this.organization = organization;
        this.skills = skills;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Skills getSkills() {
        return skills;
    }

    /**
     * Checks if the current HealthWorker object has the specified
     * specialisation
     * @param specialisation to check for
     * @return true if the HealthWorker object contains the specialisation in
     * Skills, false otherwise.
     */
    public boolean hasSkill(Specialisation specialisation) {
        return this.skills.contains(specialisation);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Nric: ")
                .append(getNric())
                .append(" Organization: ")
                .append(getOrganization().toString())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Skills: ")
                .append(getSkills());
        return builder.toString();
    }

    /**
     * Returns true if both HealthWorkers have the same name, nric, phone and
     * organization.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameHealthWorker(HealthWorker other) {
        if (other == this) {
            return true;
        }

        return other != null
                && other.getName().equals(this.getName())
                && other.getNric().equals(this.getNric())
                && other.getPhone().equals(this.getPhone())
                && other.getOrganization().equals(this.getOrganization());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof HealthWorker)) {
            return false;
        }

        HealthWorker otherHealthWorker = (HealthWorker) other;
        return otherHealthWorker.getName().equals(getName())
                && otherHealthWorker.getPhone().equals(getPhone())
                && otherHealthWorker.getNric().equals(getNric())
                && otherHealthWorker.getTags().equals(getTags())
                && otherHealthWorker.getEmail().equals(getEmail())
                && otherHealthWorker.getAddress().equals(getAddress())
                && otherHealthWorker.getOrganization().equals(getOrganization())
                && otherHealthWorker.getSkills().equals(getSkills());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getNric(), getAddress(), getPhone(),
                getOrganization(), getEmail(), getTags(), getSkills());
    }
}
