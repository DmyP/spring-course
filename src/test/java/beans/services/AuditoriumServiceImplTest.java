package beans.services;

import beans.configuration.AppConfiguration;
import beans.configuration.db.DataSourceConfiguration;
import beans.configuration.db.DbSessionFactory;
import beans.models.Auditorium;
import beans.repository.AuditoriumRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 1:23 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfiguration.class, DataSourceConfiguration.class, DbSessionFactory.class, beans.configuration.TestAuditoriumConfiguration.class})
@Transactional
public class AuditoriumServiceImplTest {

    public static final int AUDITORIUMS_COUNT = 2;
    @Autowired
    private AuditoriumService auditoriumService;
    @Autowired
    private AuditoriumRepository auditoriumRepository;
    @Autowired
    private Auditorium testHall1;
    @Autowired
    private Auditorium testHall2;

    @After
    public void cleanup() {
        auditoriumRepository.deleteAll();
    }

    @Test
    public void testGetAuditoriums() throws Exception {
        List<Auditorium> auditoriums = auditoriumService.getAuditoriums();
        assertEquals("Auditoriums number should match", AUDITORIUMS_COUNT, auditoriums.size());
    }

    @Test
    public void testGetByName() throws Exception {
        checkTestHall(testHall1);
        checkTestHall(testHall2);
    }

    @Test(expected = RuntimeException.class)
    public void testGetByName_Exception() throws Exception {
        auditoriumService.getSeatsNumber("bla-bla-bla");
    }

    private void checkTestHall(Auditorium testHall) {
        int seatsNumber = auditoriumService.getSeatsNumber(testHall.getName());
        List<Integer> vipSeats = auditoriumService.getVipSeats(testHall.getName());
        assertEquals("Auditorium seats number should match. Auditorium: [" + testHall + "]", testHall.getSeatsNumber(),
                     seatsNumber);
        assertEquals("Auditorium vip seats should match. Auditorium: [" + testHall + "]", testHall.getVipSeatsList(),
                     vipSeats);
    }
}
