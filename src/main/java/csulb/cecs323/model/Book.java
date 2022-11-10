package csulb.cecs323.model;
import javax.persistence.*;

@NamedNativeQuery(
        name = "ReturnBookWithTitleAndPublisher",
        query = "SELECT * " +
                "FROM   BOOKs " +
                "WHERE  TITLE = ?1 AND PUBLISHER_NAME = ?2",
        resultClass = Book.class
)

@NamedNativeQuery(
        name = "ReturnBookWithTitleAndAuthor",
        query = "SELECT * " +
                "FROM   BOOKs " +
                "WHERE  TITLE = ? AND AUTHORING_ENTITY_NAME = ?",
        resultClass = Book.class
)

@NamedNativeQuery(
        name = "ReturnBookWithISBN",
        query = "SELECT * " +
                "FROM   BOOKs " +
                "WHERE  ISBN = ?",
        resultClass = Book.class
)

@NamedNativeQuery(
        name="ReturnAllBook",
        query = "SELECT * " +
                "FROM BOOKs",
        resultClass = Book.class
)

@Entity
@Table(name = "Books", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "publisher_Name"}),
        @UniqueConstraint(columnNames = {"title", "authoring_Entity_Name"})
})
/** Represents a book */
public class Book {

    /** Book's ISBN */
    @Id
    @Column(nullable = false, length = 17)
    private String ISBN;

    /** Book's title */
    @Column(nullable = false, length = 80)
    private String title;

    /** Year book was published */
    @Column(nullable = false, length = 4)
    private int year_Published;

    /** Authoring Entity of the book */
    @ManyToOne
    @JoinColumn(name = "authoring_Entity_Name", referencedColumnName = "email")
    private AuthoringEntity authoringEntity;

    /** The publisher who published the book */
    @ManyToOne
    @JoinColumn(name = "publisher_Name", referencedColumnName = "name")
    private Publisher publisher;

    /** Default constructor */
    public Book() {}

    /**
     * Initialize the basic values needed to identify a book
     * @param ISBN used to identify the book uniquely
     * @param title the title of the book
     * @param yearPublished the year the book was published
     */
    public Book(String ISBN, String title, int yearPublished)
    {
        this.ISBN = ISBN;
        this.title = title;
        this.year_Published = yearPublished;
    }

    /**
     * Removes the reference to the current book from the author's list
     */
    public void removeAuthor(){
        authoringEntity.removeBook(this);
        authoringEntity = null;
    }

    /**
     * Removes the reference to the current book from the publisher's list
     */
    public void removePublisher(){
        publisher.removeBook(this);
        publisher = null;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYearPublished() {
        return year_Published;
    }

    public void setYearPublished(int yearPublished) {
        this.year_Published = yearPublished;
    }

    public AuthoringEntity getAuthoringEntity() {
        return authoringEntity;
    }

    public void setAuthoringEntity(AuthoringEntity authoringEntity) {
        this.authoringEntity = authoringEntity;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Converts the book into a string format
     * @return the string format of the book
     */
    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", yearPublished=" + year_Published +
                ", authoringEntityName=" + authoringEntity.getEmail() +
                ", publisherName=" + publisher.getName() +
                '}';
    }
}
