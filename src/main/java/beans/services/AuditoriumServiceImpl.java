package beans.services;

import beans.models.Auditorium;
import beans.repository.AuditoriumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 11:14 AM
 */
@Service
@Transactional
public class AuditoriumServiceImpl implements AuditoriumService {

    private final AuditoriumRepository auditoriumRepository;

    @Autowired
    public AuditoriumServiceImpl(AuditoriumRepository auditoriumRepository) {
        this.auditoriumRepository = auditoriumRepository;
    }

    @Override
    public List<Auditorium> getAuditoriums() {
        return auditoriumRepository.findAll();
    }

    @Override
    public Auditorium getByName(String name) {
        return auditoriumRepository.getByName(name);
    }

    @Override
    public int getSeatsNumber(String auditoriumName) {
        return auditoriumRepository.getByName(auditoriumName).getSeatsNumber();
    }

    @Override
    public List<Integer> getVipSeats(String auditoriumName) {
        return auditoriumRepository.getByName(auditoriumName).getVipSeatsList();
    }
}
