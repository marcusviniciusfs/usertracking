import com.rd.persistence.database.DatabaseService;
import com.rd.persistence.database.Transaction;
import com.rd.persistence.database.exception.TransactionException;
import com.rd.persistence.model.Contact;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class TestCRUD {

    private DatabaseService databaseService;
    private ServerHelp serverHelper;
    private List<Contact> contacts = new ArrayList<Contact>();

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getMethodName());
        }
    };

    @Test
    public void testCRUD() throws  Exception {
        databaseService = serverHelper.startDatabaseService();

        serverHelper = new ServerHelp();
        serverHelper.startServer(databaseService);

        //Persisting
        final Contact contact = databaseService.submit(new Transaction<Contact>() {
            @Override
            public final Contact execute(final EntityManager entityManager) throws TransactionException {

                final Contact contact1 = new Contact("teste@teste.com", "empresaTeste.com.br", "yyyyDDmm HHmmss");
                entityManager.persist(contact1);
                contacts.add(contact1);

                final Contact contact2 = new Contact("teste2@teste.com", "empresaTeste2.com.br", "yyyyDDmm HHmmss");
                entityManager.persist(contact2);
                contacts.add(contact2);

                return contact1;
            }
        });

        //Reading
        databaseService.submit(new Transaction<Contact>() {
        @Override
        public Contact execute(EntityManager entityManager) throws TransactionException {
            final Query nativeQuery = entityManager.createNativeQuery("select * from contact", Contact.class);
            final List resultList = nativeQuery.getResultList();
            Contact returnContact = (Contact) resultList.get(0);

            assertFalse(resultList.isEmpty());
            assertEquals(contacts.get(0).getEmail(), returnContact.getEmail());
            assertEquals(contacts.get(0).getUrl(), returnContact.getUrl());
            assertEquals(contacts.get(0).getDatetime(), returnContact.getDatetime());
            assertEquals(resultList.size(), contacts.size());

            return returnContact;
        }});

        //removing
        databaseService.submit(new Transaction<Contact>() {
        @Override
        public Contact execute(EntityManager entityManager) throws TransactionException {
            final Query nativeQuery = entityManager.createNativeQuery("select * from contact", Contact.class);
            List resultList = nativeQuery.getResultList();
            entityManager.remove(resultList.get(0));

            resultList = nativeQuery.getResultList();
            assertEquals(resultList.size(), 1);

            return (Contact) resultList.get(0);
        }});

        //updating
        databaseService.submit(new Transaction<Contact>() {
        @Override
        public Contact execute(EntityManager entityManager) throws TransactionException {
            final Query nativeQuery = entityManager.createNativeQuery("select * from contact", Contact.class);
            List resultList = nativeQuery.getResultList();
            Contact returnContact = (Contact) resultList.get(0);
            returnContact.setEmail("newemail@teste.com");
            entityManager.merge(returnContact);

            resultList = nativeQuery.getResultList();
            assertThat(((Contact) resultList.get(0)).getEmail(), not(contacts.get(0).getEmail()));

            return returnContact;
        }});

        serverHelper.stopUndertowServer();
    }
}
