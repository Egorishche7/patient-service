package by.egorishche7.patientservice.payload.request;

import by.egorishche7.patientservice.model.EGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CreatePatientRequest {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Gender is mandatory")
    private EGender gender;

    @NotNull(message = "Birth date is mandatory")
    @Past(message = "Birth date must be in the past")
    private LocalDateTime birthDate;

    public CreatePatientRequest() {
    }

    public CreatePatientRequest(String name, EGender gender, LocalDateTime birthDate) {
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EGender getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender = gender;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }
}
