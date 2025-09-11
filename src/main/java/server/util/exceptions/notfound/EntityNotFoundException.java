package server.util.exceptions.notfound;

public class EntityNotFoundException extends CustomNotFoundException {
    public EntityNotFoundException(String entity, Long id) {
        super(entity + " with ID " + id + " not found");
    }
}