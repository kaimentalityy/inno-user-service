package server.util.exceptions.notfound;

public class EntityNotFoundException extends CustomNotFoundException {
    public EntityNotFoundException(String entity, Object obj, Object name) {
        super(entity + " with " + obj + name +  " not found");
    }
}