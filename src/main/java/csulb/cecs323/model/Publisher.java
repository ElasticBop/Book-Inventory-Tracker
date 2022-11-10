package csulb.cecs323.model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedNativeQuery(
        name = "ReturnPublisherWithName",
        query = "SELECT * " +
                "FROM   Publishers " +
                "WHERE  name = ?",
        resultClass = Publisher.class
)
@NamedNativeQuery(
        name = "ReturnPublisherWithPhone",
        query = "SELECT * " +
                "FROM   Publishers " +
                "WHERE  phone = ?",
        resultClass = Publisher.class
)
@NamedNativeQuery(
        name = "ReturnPublisherWithEmail",
        query = "SELECT * " +
                "FROM   Publishers " +
                "WHERE email = ?",
        resultClass = Publisher.class
)
@NamedNativeQuery(
        name="ReturnAllPublisher",
        query = "SELECT * " +
                "FROM   PUBLISHERs ",
        resultClass = Publisher.class
)
@Entity
@Table(name = "Publishers")
/** Represents a Publisher */
public class Publisher {

    /** The name of the publisher */
    @Id
    @Column(nullable = false, length = 80)
    private String name;

    /** The publishers email address */
    @Column(unique = true, nullable = false, length = 80)
    private String email;

    /** The publishers phone number */
    @Column(unique = true, nullable = false, length = 24)
    private String phone;

    /** List of books published by publisher */
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

    /** Default Constructor */
    public Publisher() {}

    /**
     * Initializes the publisher with basic values
     * @param name the name of the publisher
     * @param email the email of the publisher
     * @param phone the phone number of the publisher
     */
    public Publisher(String name, String email, String phone)
    {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Adds a book to the publisher's list of books
     * @param book the book to be added
     */
    public void addBook( Book book ){
        books.add(book);
        book.setPublisher(this);
    }

    /**
     * Remove the book from the list
     * @param book the book to be removed
     */
    public void removeBook( Book book ){
        books.remove(book);
    }

    public void setName(String name) { this.name = name; }

    public String getName()
    {
        return name;
    }

    public void setEmail(String email) { this.email = email; }

    public String getEmail()
    {
        return email;
    }

    public void setPhone(String phone) { this.phone = phone; }

    public String getPhone()
    {
        return phone;
    }

    /**
     * Converts the publisher into a string that includes the information you can get from the databse
     * @return the publisher formatted as a string
     */
    @Override
    public String toString() {
        return "Publisher{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone +
                '}';
    }
}//end of publisher class
