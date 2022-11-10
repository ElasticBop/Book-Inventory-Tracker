package csulb.cecs323.model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedNativeQuery(
        name = "ReturnAuthoringEntityWithEmail",
        query ="SELECT * " +
                "FROM  Authoring_entities " +
                "WHERE email = ?",
        resultClass = AuthoringEntity.class
)

@NamedNativeQuery(
        name="ReturnAllAuthoringEntity",
        query = "SELECT * " +
                "FROM   Authoring_entities ",
        resultClass = AuthoringEntity.class
)

@Entity
@Table(name = "Authoring_entities")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//this might cause errors
@DiscriminatorColumn(name = "authoring_Entity_Type", discriminatorType = DiscriminatorType.STRING)
/** Represents an authoring entity and is a parent for the sub categories */
public class AuthoringEntity {

    /** The email of the authoring entity*/
    @Id
    @Column(nullable = false, length = 30)
    private String email;

    /** The name of the authoring entity*/
    @Column(nullable = false, length = 80)
    private String name;

    /** The type of the authoring entity*/
    @Column(length = 31)
    private String authoring_Entity_Type;

    /** The head writer the authoring entity*/
    @Column( length = 80)
    private String head_Writer;

    /** Year group was formed */
    @Column(length = 4)
    private Integer year_Formed;

    /** The books authored by the authoring entity*/
    @OneToMany(mappedBy = "authoringEntity", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

    /** Default Constructor */
    public AuthoringEntity() {}

    /**
     * Assigning values to the authoring entity
     * @param email the email of the entity
     * @param name the name of the entity
     * @param head_Writer if a writing group then the head writer of the group
     * @param year_Formed the year the group was formed
     */
    public AuthoringEntity(String email, String name, String head_Writer, Integer year_Formed){
        this.email = email;
        this.name = name;
        this.head_Writer = head_Writer;
        this.year_Formed = year_Formed;
    }

    /**
     * Adds a book associated with the entity
     * @param book the book being added
     */
    public void addBook( Book book ){
        books.add(book);
        book.setAuthoringEntity(this);
    }

    /**
     * Removes the book from the entity
     * @param book the book being removed
     */
    public void removeBook( Book book ){
        books.remove(book);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthoringEntityType() {
        return authoring_Entity_Type;
    }

    public void setAuthoringEntityType(String authoringEntityType) {
        this.authoring_Entity_Type = authoringEntityType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Converts the Author's info to a string
     * @return Author's info in a string format
     */
    @Override
    public String toString() {
        return "AuthoringEntity{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", authoring_Entity_Type='" + authoring_Entity_Type + '\'' +
                ", head_Writer='" + head_Writer + '\'' +
                ", year_Formed=" + year_Formed +
                '}';
    }

}
