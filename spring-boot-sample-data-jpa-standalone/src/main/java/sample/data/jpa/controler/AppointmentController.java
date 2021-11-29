package sample.data.jpa.controler;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.data.jpa.domain.Appointment;
import sample.data.jpa.service.AppointmentDAO;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@RestControllerAdvice
public class AppointmentController {

    @Autowired
    private AppointmentDAO appointmentDAO;

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

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get every appointment known")
    public ResponseEntity<List<Appointment>> getAll(){
        return ResponseEntity.ok(appointmentDAO.findAll());
    }

}
