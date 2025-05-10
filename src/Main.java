import controller.UserController;
import service.UserService;
import view.UserView;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        UserView userView = new UserView();
        UserController controller = new UserController(userService, userView);
        controller.start();
    }
}