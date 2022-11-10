/**
 A class to record books and other information related to them
 JPA Project: Books
 @author Jay, Caitlin, Edmund
 @version 1 11/1/2021
 */
package csulb.cecs323.app;
import csulb.cecs323.model.*;
import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class BookMain {
   /**
    * You will likely need the entityManager in a great many functions throughout your application.
    * Rather than make this a global variable, we will make it an instance variable within the BookMain
    * class, and create an instance of BookMain in the main.
    */
   private EntityManager entityManager;

   /**
    * The Logger can easily be configured to log to a file, rather than, or in addition to, the console.
    * We use it because it is easy to control how much or how little logging gets done without having to
    * go through the application and comment out/uncomment code and run the risk of introducing a bug.
    * Here also, we want to make sure that the one Logger instance is readily available throughout the
    * application, without resorting to creating a global variable.
    */
   private static final Logger LOGGER = Logger.getLogger(BookMain.class.getName());

   /**
    * The constructor for the BookMain class.  All that it does is stash the provided EntityManager
    * for use later in the application.
    * @param manager    The EntityManager that we will use.
    */
   public BookMain(EntityManager manager) {
      this.entityManager = manager;
   }

   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("BookMain");
      EntityManager manager = factory.createEntityManager();
      // Create an instance of BookMain and store our new EntityManager as an instance variable.
      BookMain bookmain = new BookMain(manager);
      // Any changes to the database need to be done within a transaction.
      // See: https://en.wikibooks.org/wiki/Java_Persistence/Transactions

      Scanner reader = new Scanner(System.in);
      boolean programRunning = true;
      System.out.println("Welcome!");
      while(programRunning)
      {
         int menuIntInput = bookmain.getUserInputInt("Main Menu\n\t1.Add Data\n\t2.Delete Book\n\t3.List information about a specific object\n\t4.List primary keys on a certain table\n\t5.Update a book\n\t6.Exit Program", 1, 6);
         if(menuIntInput == 1)
         {
            menuIntInput = bookmain.getUserInputInt("Where would you like to add to?:\n\t1.Publisher\n\t2.Books\n\t3.Authoring Entity", 1, 3);
            switch(menuIntInput)
            {
               case 1:
                  /** Add publisher */
                  bookmain.addPublishers();
                  break;
               case 2:
                  /** Add book */
                  bookmain.addBooks();
                  break;
               case 3:
                  /** Add authoring entity */
                  menuIntInput = bookmain.getUserInputInt("Enter the type of authoring entity.\n\t1. Writing Group\n\t2. Individual Author\n\t3. AdHocTeam\n\t4. Individual Author to Existing Ad Hoc Team ", 1, 4);
                  switch(menuIntInput){
                     case 1:
                        /** add writing group */
                        bookmain.addWritingGroup();
                        break;
                     case 2:
                        /** add individual author */
                        bookmain.addIndividualAuthors();
                        break;
                     case 3:
                        /** add AdHocTeam */
                        bookmain.addAdHocTeam();
                        break;
                     case 4:
                        /** Add author to a team */
                        bookmain.addAuthorToTeam();
                        break;
                  }
                  break;
                  //end of adding an authoring entity
            }
         }
         /** Delete a book*/
         else if (menuIntInput == 2)
         {
            bookmain.deleteBook();
         }
         /** List information about specific objects */
         else if (menuIntInput == 3)
         {
           bookmain.listInformation();
         }
         /** List the primary Keys of a certain table  */
         else if (menuIntInput == 4)
         {
            bookmain.listPublicKeys();
         }
         /** Update book with new author */
         else if(menuIntInput == 5)
         {
            /** update an object that's in the books table  */
            bookmain.updateBook();
         }
         /**Exits the program*/
         else if(menuIntInput == 6)
         {
            programRunning = false;
            break;
         }
         else{
            System.out.println("Invalid Input");
         }
      }
   } // End of the main method

   /**
    * Gets user string input with error handling
    * @param prompt the question asked to the user
    * @param length the length of the string that is accepted
    * @return the string that the user inputted
    */
   public String getUserInputString( String prompt, int length ){
      Scanner in = new Scanner(System.in);
      String userIn = "";
      boolean loop = true;
      while (loop) {
         System.out.println(prompt);
         try {
            userIn = in.nextLine();
            if( userIn.isEmpty() || userIn == "" || userIn.length() > length ){
               System.out.println("Invalid input try again.");
            }
            else{
               loop = false;
            }
         } catch (Exception e) {
            System.out.println("Invalid input try again.");
            in.nextLine();
         }
      }
      return userIn;
   }

   /**
    * Gets user int input from an acceptable range
    * @param prompt the question asked to the user
    * @param start the starting number accepted
    * @param end the ending number that is accepted
    * @return the number that was inputted by the user
    */
   public int getUserInputInt( String prompt, int start, int end ) {
      Scanner in = new Scanner(System.in);
      int userIn = -1;
      boolean loop = true;
      while (loop) {
         System.out.println(prompt);
         try {
            userIn = in.nextInt();
            if( userIn >= start && userIn <= end) {
               loop = false;
            }
            else{
               System.out.println("Number not within range");
            }
         } catch (Exception e) {
            System.out.println("Invalid input try again.");
            in.nextLine();
         }
      }
      return userIn;
   }

   /**
    * Persists the entity in the database
    * @param entity the entity to be stored
    * @param <E> a generic so that we can add any entity to the database
    */
   public <E> void persistEntity(E entity) {
      try {
         LOGGER.fine("Begin of Transaction");
         EntityTransaction tx = this.entityManager.getTransaction();
         tx.begin();
         this.entityManager.persist(entity);
         tx.commit();
         LOGGER.fine("End of Transaction");
      }
      catch (Exception e){
         System.out.println("Failed to persist.");
         System.out.println(e.getCause());
      }
   }

   /**
    * Updates the entity in the database
    * @param entity the entity to be updated
    * @param <E> describes entity
    */
   public <E> void updateEntity(E entity) {
      try {
         LOGGER.fine("Begin of Transaction");
         EntityTransaction tx = this.entityManager.getTransaction();
         tx.begin();
         this.entityManager.merge(entity);
         tx.commit();
         LOGGER.fine("End of Transaction");
      }
      catch (Exception e){
         System.out.println("Failed to update.");
         System.out.println(e.getCause());
      }
   }

   /**
    * Gets a list of entities based on a query
    * @param theClass the type of class that is returned from the query
    * @param query the query represented as a string
    * @param <E> describes the type of theClass
    * @return a list of entities based on the query
    */
   public <E> List<E> getEntityList(Class<E> theClass, String query){
      List<E> entities = this.entityManager.createNamedQuery( query, theClass).getResultList();
      if (entities.size() == 0) {
         return null;
      } else {
         return entities;
      }
   }

   /**
    * Gets a single entity based on a query
    * @param theClass the type of class that is returned from the query
    * @param param the parameter used to identify the entity in the query
    * @param query the sql query represented as a string
    * @param <E>  this describes theClass
    * @return an entity from the query
    */
   public <E> E getEntity(Class<E> theClass, String param, String query){
      List<E> entities = this.entityManager.createNamedQuery( query, theClass).setParameter(1, param).getResultList();
      if (entities.size() == 0) {
         return null;
      } else {
         return entities.get(0);
      }
   }

   /**
    * Checks if a publisher is already in the database using pairs of constraints
    * @param name the name of the publisher
    * @param email the email of the publisher
    * @param phone the phone of the publisher
    * @return true if the publisher exists and false otherwise
    */
   public boolean checkPublisher(String name, String email, String phone){
      List<Publisher> publishers = this.entityManager.createNamedQuery("ReturnPublisherWithName", Publisher.class).setParameter(1, name).getResultList();
      boolean test = false;
      String output = "";
      if( publishers.size() != 0 ){
         output += "A publisher exists with this name.\n";
         test = true;
      }
      publishers = this.entityManager.createNamedQuery("ReturnPublisherWithEmail", Publisher.class).setParameter(1, email).getResultList();
      if( publishers.size() != 0){
         output += "A publisher exists with this email.\n";
         test = true;
      }
      publishers = this.entityManager.createNamedQuery("ReturnPublisherWithPhone", Publisher.class).setParameter(1, phone).getResultList();
      if( publishers.size() != 0 ){
         output += "A publisher exists with this phone.\n";
         test = true;
      }
      System.out.println(output);
      return test;
   }

   /**
    * Check for duplicate book by checking with each candidate key
    * @param ISBN the unique identifier of the book
    * @param title the title used to refer to the book
    * @param author the name of the authoring enetity
    * @param publisher the name of the publisher who published the book
    * @return true if the book already exists
    */
   public boolean checkBook(String ISBN, String title, String author, String publisher ){
      List<Book> books = this.entityManager.createNamedQuery("ReturnBookWithISBN", Book.class).setParameter(1, ISBN).getResultList();
      boolean test = false;
      String output = "";
      if( books.size() != 0 ){
         output += "A book exists with this ISBN.\n";
         test = true;
      }
      books = this.entityManager.createNamedQuery("ReturnBookWithTitleAndPublisher", Book.class).setParameter(1, title).setParameter(2, publisher).getResultList();
      if( books.size() != 0){
         output += "A book exists with this title and publisher.\n";
         test = true;
      }
      books = this.entityManager.createNamedQuery("ReturnBookWithTitleAndAuthor", Book.class).setParameter(1, title).setParameter(2, author).getResultList();
      if( books.size() != 0 ){
         output += "A book exits with this title and author.\n";
         test = true;
      }
      System.out.println(output);
      return test;
   }

   /**
    * Checks whether there is an author with a given email in the database
    * @param email the email of the authoring enttiy
    * @return true if the author exists
    */
   public boolean checkAuthoringEntity(String email){
      AuthoringEntity ae = this.getEntity(AuthoringEntity.class, email, "ReturnAuthoringEntityWithEmail" );
      if( ae != null){
         System.out.println("An authoring entity exists with this email");
         return true;
      }
      return false;
   }

   /**
    * Prompts the user for information to add a publisher into the database
    */
   public void addPublishers(){
      String pubName = this.getUserInputString("Enter the name of the publisher:", 80);
      String pubEmail = this.getUserInputString("Enter the email address of the publisher:", 80);
      String pubPhone = this.getUserInputString("Enter the phone number of the publisher:", 24);
      if(!this.checkPublisher(pubName, pubEmail, pubPhone)) {
         this.persistEntity(new Publisher(pubName, pubEmail, pubPhone));
         System.out.println("Publisher added.");
      }
   }

   /**
    * Prompts the user for information about a book to add into the database
    */
   public void addBooks(){
      String ISBN = this.getUserInputString("Enter the ISBN of the book.", 17);
      String title = this.getUserInputString("Enter the title of the book.", 80);
      int yearPublished = this.getUserInputInt("Enter the year the book was published", 0, 9999);
      String authoringEntityEmail = this.getUserInputString("Enter the email of the authoring entity", 30);
      String publisherName = this.getUserInputString("Enter the name of the publisher", 80);
      Book b = new Book( ISBN, title, yearPublished );
      AuthoringEntity bAuthor = this.getEntity(AuthoringEntity.class, authoringEntityEmail, "ReturnAuthoringEntityWithEmail");
      Publisher bPublisher = this.getEntity(Publisher.class, publisherName, "ReturnPublisherWithName");
      if( null != bAuthor){
         if ( bPublisher != null){
            if(!this.checkBook(ISBN, title, authoringEntityEmail, publisherName )){
               bAuthor.addBook(b);
               bPublisher.addBook(b);
               this.persistEntity(b);
               System.out.println("Book added.");
            }
         }
         else{
            System.out.println("Publisher does not exist.");
         }
      }
      else{
         System.out.println("Author does not exist.");
      }
   }

   /**
    * Prompts the user for information about a writing group to add a writing group into the database
    */
   public void addWritingGroup(){
      String email = this.getUserInputString("Enter the email.", 30);
      String aeName = this.getUserInputString("Enter the name", 80);
      String headWriter = this.getUserInputString("Enter the head writer.", 80);
      int yearCreated = this.getUserInputInt("Enter the year formed.", 0, 9999);
      if( !this.checkAuthoringEntity(email) ){
         this.persistEntity(new WritingGroup(email, aeName, headWriter, yearCreated));
         System.out.println("Writing group added");
      }
   }

   /**
    * Prompts the user for information about an individual author to add to the database
    */
   public void addIndividualAuthors(){
      String email = this.getUserInputString("Enter the email.", 30);
      String aeName = this.getUserInputString("Enter the name", 80);
      if( !this.checkAuthoringEntity(email) ){
         this.persistEntity(new IndividualAuthor(email, aeName));
         System.out.println("Individual Author added");
      }
   }

   /**
    * Prompts the user for information about an ad hoc team to add to the database
    */
   public void addAdHocTeam(){
      String email = getUserInputString("Enter the email.", 30);
      String aeName = getUserInputString("Enter the name", 80);
      if( !this.checkAuthoringEntity(email) ){
         this.persistEntity(new AdHocTeam(email, aeName));
         System.out.println("Team added");
      }
   }

   /**
    * Prompts the user for information about an author and team to pair together
    */
   public void addAuthorToTeam(){
      String iaEmail = getUserInputString("Enter the email of the Individual Author", 30);
      String ahtEmail = getUserInputString("Enter the email of the adhoc Team", 30);
      AdHocTeam aht = getEntity(AdHocTeam.class, ahtEmail, "ReturnAdHocTeamWithEmail");
      IndividualAuthor ia = getEntity(IndividualAuthor.class, iaEmail, "ReturnIndividualAuthorWithEmail");
      if( ia != null ){
         if ( aht != null ){
            if( aht.addAuthor(ia) ){
               updateEntity(ia);
               updateEntity(aht);
               System.out.println("Author added to Team");
            }
            else{
               System.out.println("Author already part of team");
            }
         }
         else{
            System.out.println("AdHocTeam with that email does not exists");
         }
      }
      else{
         System.out.println("Individual author with that email does not exist.");
      }
   }

   /**
    * Prompts the user for a book to delete from the database
    */
   public void deleteBook(){
      String title = getUserInputString("Enter the title of the book.", 80);
      String author = getUserInputString("Enter the email of the author of the book.", 80);
      if(checkBook("",title, author, "")){
         List<Book> b = this.entityManager.createNamedQuery("ReturnBookWithTitleAndAuthor", Book.class ).setParameter(1, title).setParameter(2, author).getResultList();
         try {
            LOGGER.fine("Begin of Transaction");
            EntityTransaction tx = this.entityManager.getTransaction();
            tx.begin();

            b.get(0).removeAuthor();
            b.get(0).removePublisher();
            this.entityManager.remove(b.get(0));

            tx.commit();
            LOGGER.fine("End of Transaction");
            System.out.println("Book deleted.");
         }
         catch (Exception e){
            System.out.println("Failed to delete.");
            System.out.println(e.getCause());
         }
      }
      else{
         System.out.println("The book with the specified title and author does not exist");
      }
   }

   /**
    * Prompts the user for a book to update with a new author
    */
   public void updateBook(){
      String isbn = getUserInputString("Enter the ISBN of the book.", 17);
      Book b = getEntity(Book.class, isbn, "ReturnBookWithISBN");
      if( b != null ){
         String email = getUserInputString("Enter the email of the new author (could be the same one):", 30);
         AuthoringEntity ae = getEntity(AuthoringEntity.class, email, "ReturnAuthoringEntityWithEmail");
         if( ae != null ){
            b.removeAuthor();
            ae.addBook(b);
            updateEntity(b);
            updateEntity(ae);
            System.out.println("Author updated for book.");
         }
         else{
            System.out.println("Email does not map to any AuthoringEntity");
         }
      } else{ System.out.println("Book with entered ISBN does not exist"); }
   }

   /**
    * List the public keys for ll rows in publishers, books, or authoring entities
    */
   public void listPublicKeys(){
      int menuIntInput = getUserInputInt("Where would you like to see the primary keys from?\n\t1. Publishers\n\t2. Authoring Entities\n\t3. Books", 1, 3);
      switch(menuIntInput){
         case 1:
            List<Publisher> pubList = getEntityList(Publisher.class, "ReturnAllPublisher");
            if( pubList != null ) {
               for (Publisher pub : pubList) { System.out.println("PK (name): " + pub.getName()); }
            }
            break;
         case 2:
            List<AuthoringEntity> aeList = getEntityList(AuthoringEntity.class, "ReturnAllAuthoringEntity");
            if( aeList != null ) {
               for (AuthoringEntity ae : aeList) {
                  System.out.println("PK (email): " + ae.getEmail() + " Type: " + ae.getAuthoringEntityType());
               }
            }
            break;
         case 3:
            List<Book> books = getEntityList(Book.class, "ReturnAllBook");
            if( books != null ) {
               for (Book book : books) {
                  System.out.println("PK (ISBN): " + book.getISBN() + " Title: " + book.getTitle() );
               }
            }
            break;
      }
   }

   /**
    * List all the information about a specific object given the user provides a key for the object
    */
   public void listInformation(){
      int userInput = getUserInputInt("Select the object to list information from \n\t1. Publisher\n\t2. Book\n\t3. Writing Group", 1, 3);
      switch( userInput ){
         case 1:
            String name = getUserInputString("Enter the name of the publisher", 80);
            Publisher p = getEntity(Publisher.class, name, "ReturnPublisherWithName");
            if( p != null ){
               System.out.println(p);
            } else { System.out.println("Publisher with entered name doesn't exist"); }
            break;
         case 2:
            String isbn = getUserInputString("Enter the ISBN of the book", 17);
            Book b = getEntity(Book.class, isbn, "ReturnBookWithISBN");
            if( b != null ){
               System.out.println(b);
            } else { System.out.println("Book with entered ISBN doesn't exist"); }
            break;
         case 3:
            String email = getUserInputString("Enter the email of the writing group", 30);
            WritingGroup w = getEntity(WritingGroup.class, email, "ReturnWritingGroupWithEmail");
            if( w != null ){
               System.out.println(w);
            } else { System.out.println("Writing Group with entered email doesn't exist"); }
            break;
      }
   }


} // End of BookMain class
