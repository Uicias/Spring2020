package sample.data.jpa.controler;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.data.jpa.domain.Slot;
import sample.data.jpa.service.SlotDAO;
import sample.data.jpa.service.TeacherDAO;

import java.util.List;

@RestController
@RequestMapping("/api/slot")
@RestControllerAdvice
@CrossOrigin(origins = "http://localhost:8080")
public class SlotController {

    @Autowired
    private SlotDAO slotDAO;

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
        return ResponseEntity.ok("Slot controller is OK!");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get every slots known")
    public ResponseEntity<List<Slot>> getAll() {
        return ResponseEntity.ok(slotDAO.findAll());
    }

    @GetMapping(value = "/teacher/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get every slots for a given teacher")
    public ResponseEntity<List<Slot>> getSlotsByTeacher(@PathVariable(value = "id") Long id) {
        // fetch slots if wanted teacher exists
        if (teacherDAO.findById(id).isPresent())
            return ResponseEntity.ok(this.slotDAO.findAllByTeacherId(id));
        else
            // return a 404 as teacher doesn't exist
            return ResponseEntity.notFound().build();
    }

}
