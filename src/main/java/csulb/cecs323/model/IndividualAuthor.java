package csulb.cecs323.model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedNativeQuery(
        name = "ReturnIndividualAuthorWithEmail",
        query ="SELECT * " +
                "FROM  Authoring_entities " +
                "WHERE email = ? and authoring_entity_type = 'IndividualAuthor' ",
        resultClass = IndividualAuthor.class
)
@Entity
@DiscriminatorValue("IndividualAuthor")
/** Represents the individual author */
public class IndividualAuthor extends AuthoringEntity {

    /** Team that an author is a part of */
    @ManyToMany(mappedBy = "authors" )
    private List<AdHocTeam> adHocTeamList = new ArrayList<>();

    /** Default constructor */
    public IndividualAuthor() {}

    /**
     * Initializes the values of the author
     * @param email the author's email
     * @param name the author's name
     */
    public IndividualAuthor(String email, String name) {
        super(email, name, null, null);
        setAuthoringEntityType("IndividualAuthor");
    }

    /**
     * Add the author the ad hoc team
     * @param ah the ad hoc team that the author is being added to
     */
    public void addTeam( AdHocTeam ah ){
        adHocTeamList.add(ah);
    }


}
