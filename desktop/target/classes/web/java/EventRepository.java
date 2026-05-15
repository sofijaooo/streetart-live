@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // поиск по месту проведения
    List<Event> findByPlaceContainingIgnoreCase(String place);

    // поиск по user_id (например, чтобы фильтровать по автору события)
    List<Event> findByUserId(Integer userId);

    // поиск по дате события
    List<Event> findByEventDate(LocalDate eventDate);
}