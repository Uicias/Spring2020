package sample.data.jpa.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sample.data.jpa.domain.User;
import sample.data.jpa.exceptions.UserException;
import sample.data.jpa.service.UserDao;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserDao userDao;

    /**
     * Endpoint to test the controller with a postman
     *
     * @return A {@link ResponseEntity} with status code 200 and a {@link String}
     */
    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("User controller is OK!");
    }

    /**
     * Return if any user with given id
     *
     * @param id user id
     * @return {@link ResponseEntity} with status code 200 and the wanted user in body
     * @throws UserException
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id) {
        Optional<User> user = this.userDao.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates and returns a user
     *
     * @param user {@link User} User data sent in request body
     * @return the created {@link User}
     * @throws UserException
     */
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) throws UserException {
        // The couple first and last name shall be unique
        if (!userDao.findUserByFirstNameAndLastName(user.getFirstName(), user.getLastName()).isPresent()) {
            // we save the user
            userDao.save(user);
            // we find the newly created user (to get the generated id)
            Optional<User> userRes = userDao.findUserByFirstNameAndLastName(user.getFirstName(), user.getLastName());
            if (userRes.isPresent())
                // we found the user, everything is OK
                return ResponseEntity.ok(userRes.get());
            else
                // no user found, something went wrong
                throw new UserException(String.format("An unexpected error appended while creating user: %s", user));
        } else {
            // the couple first/last name is already used
            // we send a code HTTP 422 UnprocessableEntity
            return ResponseEntity.unprocessableEntity().build();
        }
    }

}