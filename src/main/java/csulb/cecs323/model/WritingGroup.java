package csulb.cecs323.model;
import javax.persistence.*;

@NamedNativeQuery(
        name = "ReturnWritingGroupWithEmail",
        query ="SELECT * " +
                "FROM  Authoring_entities " +
                "WHERE email = ? and authoring_entity_type = 'WritingGroup' ",
        resultClass = WritingGroup.class
)
@Entity
@DiscriminatorValue("WritingGroup")
/** Represents a writing group */
public class WritingGroup extends AuthoringEntity {

    /** Default Constructor of Writing Group */
    public WritingGroup() {}

    /**
     * Initializes the writing group to the values need to identify it
     * @param email the email of the writing group
     * @param name the name of the writing group
     * @param headWriter the head_writer that leads the group
     * @param yearFormed the year the group was formed
     */
    public WritingGroup(String email, String name, String headWriter, int yearFormed) {
        super(email, name, headWriter, yearFormed);
        setAuthoringEntityType("WritingGroup");
    }

    /**
     * Converts the writing group into a string and just calls on the super class function
     * @return writing group in a string format
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
