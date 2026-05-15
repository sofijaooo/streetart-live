@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    // поиск по месту
    public List<Event> searchEvents(String query) {
        if (query == null || query.isBlank()) {
            return repository.findAll();
        }
        return repository.findByPlaceContainingIgnoreCase(query);
    }
}