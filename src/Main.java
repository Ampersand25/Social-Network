import business.UserService;
import domain.User;
import infrastructure.IRepository;
import infrastructure.memory.InMemoryRepo;
import presentation.UI;
import validation.IValidator;
import validation.UserValidator;

public class Main {
    public static void main(String[] args) {
        IValidator<User> userValidator = new UserValidator();
        IRepository<Long, User> userIRepository = new InMemoryRepo<>();
        UserService userService = new UserService(userValidator, userIRepository);
        UI ui = new UI(userService);
        ui.run();
    }
}