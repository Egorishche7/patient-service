package by.egorishche7.patientservice.payload.request;

import by.egorishche7.patientservice.model.EGender;

import java.time.LocalDateTime;

public class UpdatePatientRequest extends CreatePatientRequest {

    public UpdatePatientRequest(String name, EGender gender, LocalDateTime birthDate) {
        super(name, gender, birthDate);
    }
}
