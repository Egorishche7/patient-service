package by.egorishche7.patientservice.controller;

import by.egorishche7.patientservice.exception.PatientNotFoundException;
import by.egorishche7.patientservice.model.Patient;
import by.egorishche7.patientservice.payload.request.CreatePatientRequest;
import by.egorishche7.patientservice.payload.request.UpdatePatientRequest;
import by.egorishche7.patientservice.payload.response.ErrorResponse;
import by.egorishche7.patientservice.payload.response.SuccessResponse;
import by.egorishche7.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PatientController {
    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/patients")
    @Operation(summary = "Get all patients", description = "Retrieve a list of all patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PreAuthorize("hasRole('PRACTITIONER') or hasRole('PATIENT')")
    public ResponseEntity<?> getAllPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Patients retrieved successfully", patients));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @GetMapping("/patients/{id}")
    @Operation(summary = "Get patient by ID", description = "Retrieve a patient by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PreAuthorize("hasRole('PRACTITIONER') or hasRole('PATIENT')")
    public ResponseEntity<?> getPatientById(@PathVariable("id") UUID id) {
        try {
            Patient patient = patientService.getPatientById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Patient retrieved successfully", patient));
        } catch (PatientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/patients")
    @Operation(summary = "Create a new patient", description = "Add a new patient to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PreAuthorize("hasRole('PRACTITIONER')")
    public ResponseEntity<?> createPatient(@Valid @RequestBody CreatePatientRequest createPatientRequest) {
        try {
            Patient newPatient = patientService.createPatient(createPatientRequest);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Patient created successfully", newPatient));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PutMapping("/patients/{id}")
    @Operation(summary = "Update an existing patient", description = "Update the details of an existing patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PreAuthorize("hasRole('PRACTITIONER')")
    public ResponseEntity<?> updatePatient(@PathVariable("id") UUID id,
                                           @Valid @RequestBody UpdatePatientRequest updatePatientRequest) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, updatePatientRequest);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Patient updated successfully", updatedPatient));
        } catch (PatientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @DeleteMapping("/patients/{id}")
    @Operation(summary = "Delete a patient", description = "Delete a patient by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PreAuthorize("hasRole('PRACTITIONER')")
    public ResponseEntity<?> deletePatient(@PathVariable("id") UUID id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Patient deleted successfully", id));
        } catch (PatientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }
}
