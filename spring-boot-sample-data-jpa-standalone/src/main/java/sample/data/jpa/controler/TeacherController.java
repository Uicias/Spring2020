package sample.data.jpa.controler;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.data.jpa.domain.Teacher;
import sample.data.jpa.exceptions.TeacherException;
import sample.data.jpa.service.TeacherDAO;

import java.util.Optional;

@RestController
@RequestMapping("/api/teacher")
@RestControllerAdvice
public class TeacherController {

    @Autowired
    private TeacherDAO teacherDAO;

    /**
     * Endpoint to test the controller with a postman
     *
     * @return A {@link ResponseEntity} with status code 200 and a {@link String}
     */
    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Endpoint to test the connection to the service")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Teacher controller is OK!");
    }

    /**
     * Return if any teacher with given id
     *
     * @param id teacher id
     * @return {@link ResponseEntity} with status code 200 and the wanted teacher in body
     * @throws TeacherException
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a teacher from his id")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable(value = "id") Long id) {
        Optional<Teacher> teacher = this.teacherDAO.findById(id);
        return teacher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates and returns a teacher
     *
     * @param teacher {@link Teacher} Teacher data sent in request body
     * @return the created {@link Teacher}
     * @throws TeacherException
     */
    @PostMapping("/create")
    @Operation(summary = "Create a teacher from given data")
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) throws TeacherException {
        // The couple first and last name shall be unique
        if (!this.teacherDAO.findTeacherByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName()).isPresent()) {
            // we save the teacher
            teacherDAO.save(teacher);
            // we find the newly created teacher (to get the generated id)
            Optional<Teacher> teacherRes = teacherDAO.findTeacherByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName());
            if (teacherRes.isPresent())
                // we found the teacher, everything is OK
                return ResponseEntity.ok(teacherRes.get());
            else
                // no teacher found, something went wrong
                throw new TeacherException(String.format("An unexpected error appended while creating teacher: %s", teacher));
        } else {
            // the couple first/last name is already used
            // we send a code HTTP 422 UnprocessableEntity
            return ResponseEntity.unprocessableEntity().build();
        }
    }


}
