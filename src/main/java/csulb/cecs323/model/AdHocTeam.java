package csulb.cecs323.model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedNativeQuery(
        name = "ReturnAdHocTeamWithEmail",
        query ="SELECT * " +
                "FROM  Authoring_entities " +
                "WHERE email = ? and authoring_entity_type = 'AdHocTeam' ",
        resultClass = AdHocTeam.class
)
@Entity
@DiscriminatorValue("AdHocTeam")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"individual_Authors_Email", "AD_HOC_TEAMS_EMAIL"})
})
/** Represents ad hoc team */
public class AdHocTeam extends AuthoringEntity {

    /** The individuals that are part of the ad hoc team */
    @ManyToMany
    @JoinTable(
        name = "Ad_Hoc_Teams_Member",
        joinColumns = @JoinColumn(nullable = false, name = "AD_HOC_TEAMS_EMAIL"), inverseJoinColumns = @JoinColumn(nullable = false, name = "individual_Authors_Email" )
    )
    private List<IndividualAuthor> authors = new ArrayList<>();

    /** Default Constructor */
    public AdHocTeam() {}

    /**
     * Constructor to build AdHocTeam
     * @param email the email of the entity
     * @param name the name of the entity
     */
    public AdHocTeam(String email, String name) {
        super(email, name, null, null);
        setAuthoringEntityType("AdHocTeam");
    }

    /**
     * Adds an individual author to the team
     * @param ia the entity being added
     * @return true if the addition was successful
     */
    public boolean addAuthor( IndividualAuthor ia){
        if( !authors.contains(ia) ){
            authors.add(ia);
            ia.addTeam(this);
            return true;
        }
        return false;
    }

}
