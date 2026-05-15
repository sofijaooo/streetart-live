@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping
    public List<Event> searchEvents(@RequestParam(required = false) String search) {
        return service.searchEvents(search);
    }
}