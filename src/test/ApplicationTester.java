package test;

import domain.*;
import validation.*;
import infrastructure.*;
import business.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ApplicationTester {
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;

    public ApplicationTester() {
        setUp();
    }

    @BeforeEach
    public void setUp() {
        user1 = new User("Abdul" , "Fountain", LocalDate.of(1997, 5 , 26), "abdul_fountain@emvil.com"  , new Address("Strada Fabrica de Chibrituri 2", "Romania", "Cluj"           , "Cluj-Napoca"));
        user2 = new User("Donna" , "Meyer"   , LocalDate.of(1999, 8 , 10), "donna_meyer@mailcuk.com"   , new Address("Bulevardul Expozitiei 9"       , "Romania", "Mures"          , "Targu Mures"));
        user3 = new User("Tania" , "Whiteley", LocalDate.of(1991, 1 , 15), "tania_whiteley@nbobd.com"  , new Address("Strada Pascani 8"              , "Romania", "Suceava"        , "Suceava"));
        user4 = new User("Nathan", "Browne"  , LocalDate.of(2001, 5 , 30), "nathan_browne@pow-pows.com", new Address("Strada Brodina 3"              , "Romania", "Neamt"          , "Piatra Neamt"));
        user5 = new User("Aayat" , "Liu"     , LocalDate.of(1993, 11, 6) , "aayat_liu@wuupr.com"       , new Address("Strada Jean Louis Calderon 1-5", "Romania", "Bistrita-Nasaud", "Bistrita"));
    }

    @Test
    public void runTestsEntity() {
        Entity<Long> entity1 = new Entity<Long>();

        entity1.setId(0L);
        assertEquals(entity1.getId(), 0L);

        entity1.setId(1L);
        assertEquals(entity1.getId(), 1L);

        entity1.setId(-1L);
        assertEquals(entity1.getId(), -1L);

        entity1.setId(5L);
        assertEquals(entity1.getId(), 5L);

        entity1.setId(-5L);
        assertEquals(entity1.getId(), -5L);

        entity1.setId(10L);
        assertEquals(entity1.getId(), 10L);

        entity1.setId(-10L);
        assertEquals(entity1.getId(), -10L);

        Entity<Boolean> entity2 = new Entity<Boolean>();

        entity2.setId(false);
        assertEquals(entity2.getId(), false);

        entity2.setId(true);
        assertEquals(entity2.getId(), true);

        Entity<Character> entity3 = new Entity<Character>('@');
        assertEquals(entity3.getId(), '@');

        entity3.setId('a');
        assertEquals(entity3.getId(), 'a');

        entity3.setId('z');
        assertEquals(entity3.getId(), 'z');

        entity3.setId('0');
        assertEquals(entity3.getId(), '0');

        entity3.setId('9');
        assertEquals(entity3.getId(), '9');

        entity3.setId('_');
        assertEquals(entity3.getId(), '_');

        entity3.setId('-');
        assertEquals(entity3.getId(), '-');

        entity3.setId('#');
        assertEquals(entity3.getId(), '#');

        Entity<String> entity4 = new Entity<String>("foobar");
        assertEquals(entity4.getId(), "foobar");

        entity4.setId("foo bar");
        assertEquals(entity4.getId(), "foo bar");

        entity4.setId("bar foo");
        assertEquals(entity4.getId(), "bar foo");

        entity4.setId("barfoo");
        assertEquals(entity4.getId(), "barfoo");

        entity4.setId("");
        assertEquals(entity4.getId(), "");

        Entity<Double> entity5 = new Entity<Double>(3.25D);
        assertEquals(entity5.getId(), 3.25D);

        entity5.setId(-10.15D);
        assertEquals(entity5.getId(), -10.15D);

        entity5.setId(5.25D);
        assertEquals(entity5.getId(), 5.25D);

        entity5.setId(0D);
        assertEquals(entity5.getId(), 0D);

        entity5.setId(-135.425D);
        assertEquals(entity5.getId(), -135.425D);

        entity5.setId(715.35D);
        assertEquals(entity5.getId(), 715.35D);
    }

    @Test
    public void runTestsUser() {
        user1.setId(0L);
        assertEquals(user1.getId(), 0L);
        assertEquals(user1.getFirstName(), "Abdul");
        assertEquals(user1.getLastName(), "Fountain");
        assertEquals(user1.getBirthday(), LocalDate.of(1997, 5 , 26));
        assertEquals(user1.getEmail(), "abdul_fountain@emvil.com");
        assertTrue(user1.getFriendList().size() == 0);

        user2.setId(1L);
        assertEquals(user2.getId(), 1L);
        assertEquals(user2.getFirstName(), "Donna");
        assertEquals(user2.getLastName(), "Meyer");
        assertEquals(user2.getBirthday(), LocalDate.of(1999, 8 , 10));
        assertEquals(user2.getEmail(), "donna_meyer@mailcuk.com");
        assertTrue(user2.getFriendList().size() == 0);

        user3.setId(2L);
        assertEquals(user3.getId(), 2L);
        assertEquals(user3.getFirstName(), "Tania");
        assertEquals(user3.getLastName(), "Whiteley");
        assertEquals(user3.getBirthday(), LocalDate.of(1991, 1 , 15));
        assertEquals(user3.getEmail(), "tania_whiteley@nbobd.com");
        assertTrue(user3.getFriendList().size() == 0);

        user4.setId(3L);
        assertEquals(user4.getId(), 3L);
        assertEquals(user4.getFirstName(), "Nathan");
        assertEquals(user4.getLastName(), "Browne");
        assertEquals(user4.getBirthday(), LocalDate.of(2001, 5 , 30));
        assertEquals(user4.getEmail(), "nathan_browne@pow-pows.com");
        assertTrue(user4.getFriendList().size() == 0);

        user5.setId(4L);
        assertEquals(user5.getId(), 4L);
        assertEquals(user5.getFirstName(), "Aayat");
        assertEquals(user5.getLastName(), "Liu");
        assertEquals(user5.getBirthday(), LocalDate.of(1993, 11, 6));
        assertEquals(user5.getEmail(), "aayat_liu@wuupr.com");
        assertTrue(user5.getFriendList().size() == 0);
    }

    @Test
    public void runTestsFriendship() {

    }

    @Test
    public void runTestsSocialNetworkGraph() {

    }

    @Test
    public void runTestsDomain() {
        runTestsEntity();
        runTestsUser();
        runTestsFriendship();
        runTestsSocialNetworkGraph();
    }

    @Test
    public void runTestsValidation() {

    }

    @Test
    public void runTestsInfrastructure() {

    }

    @Test
    public void runTestsBusiness() {

    }

    @Test
    public void runAllTests() {
        runTestsDomain();
        runTestsValidation();
        runTestsInfrastructure();
        runTestsBusiness();
    }
}
