package seedu.address.model.client;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.model.ReadOnlyClient;
import seedu.address.model.poc.Poc;
import seedu.address.model.poc.UniquePocList;
import seedu.address.model.tag.Tag;
import seedu.address.model.transaction.Transaction;
import seedu.address.model.transaction.TransactionLog;

/**
 * Represents a Client in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Client implements ReadOnlyClient {

    // Identity fields
    private final Name name;

    // Data fields
    private final Address address;
    private final ClientPhone phone;
    private final ClientEmail email;
    private final Set<Tag> tags = new HashSet<>();
    private final UniquePocList pocs;
    private final TransactionLog transactions;

    /**
     * Every field must be present and not null.
     */
    public Client(Name name, Address address, ClientPhone phone, ClientEmail email, Set<Tag> tags) {
        this(name, address, phone, email, tags, new UniquePocList(), new TransactionLog());
    }

    /**
     * Overloaded Constructor take in all fields.
     * @param name client name.
     * @param address address of client.
     * @param phone number of client.
     * @param email email of client.
     * @param tags tags of client.
     * @param pocs list of unique pocs.
     * @param transactions list of transactions.
     */
    public Client(Name name, Address address, ClientPhone phone, ClientEmail email,
                  Set<Tag> tags, UniquePocList pocs, TransactionLog transactions) {
        requireAllNonNull(name, address, tags, pocs);
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.tags.addAll(tags);
        this.pocs = pocs;
        this.transactions = transactions;
    }

    public Name getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public ClientPhone getPhone() {
        return phone;
    }

    public ClientEmail getEmail() {
        return email;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public UniquePocList getPocs() {
        return pocs;
    }

    public TransactionLog getTransactions() {
        return transactions;
    }

    /**
     * Adds a poc to the unique list in the client.
     * @param poc to be added to the list.
     */
    public void addPoc(Poc poc) {
        this.pocs.add(poc);
    }

    /**
     * Returns true if poc is in the list of pocs
     * @param poc to be checked
     * @return true if contains
     */
    public boolean hasPoc(Poc poc) {
        requireNonNull(poc);
        return pocs.contains(poc);
    }

    public double getTotalTransacted() {
        return transactions.calculateNetTransacted();
    }

    /**
     * Adds a transaction to the transaction log in the client.
     * @param transaction to be added to the list.
     */
    public void addTransaction(Transaction transaction) {
        this.transactions.addTransaction(transaction);
    }

    /**
     * Returns true if both client have the same name.
     * This defines a weaker notion of equality between two clients.
     */
    public boolean isSameClient(Client otherClient) {
        if (otherClient == this) {
            return true;
        }

        return otherClient != null
                && otherClient.getName().equals(getName());
    }

    /**
     * Returns true if both clients have the same identity and data fields.
     * This defines a stronger notion of equality between two clients.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Client)) {
            return false;
        }

        Client otherClient = (Client) other;
        return otherClient.getName().equals(getName())
                && otherClient.getAddress().equals(getAddress())
                && otherClient.getPhone().equals(getPhone())
                && otherClient.getEmail().equals(getEmail())
                && otherClient.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Address: ")
                .append(getAddress())
                .append("; Phone: ")
                .append(getPhone())
                .append("; Email: ")
                .append(getEmail());

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }

        UniquePocList pocs = getPocs();
        builder.append("; POCs: ");
        Iterator<Poc> itr = pocs.iterator();
        String prefix = "";
        while (itr.hasNext()) {
            Poc poc = itr.next();
            builder.append(prefix);
            prefix = ", ";
            builder.append(poc.getName());
        }

        TransactionLog transactions = getTransactions();

        builder.append("; Total transactions: $");
        if (!isNull(transactions) && !transactions.isEmpty()) {
            builder.append(transactions.calculateNetTransacted());
        } else {
            builder.append("0");
        }
        return builder.toString();
    }

    @Override
    public ObservableList<Poc> getPocList() {
        return pocs.asUnmodifiableObservableList();
    }

}
