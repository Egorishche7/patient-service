package by.egorishche7.patientservice.service;

import by.egorishche7.patientservice.exception.PatientNotFoundException;
import by.egorishche7.patientservice.model.Patient;
import by.egorishche7.patientservice.payload.request.CreatePatientRequest;
import by.egorishche7.patientservice.payload.request.UpdatePatientRequest;
import by.egorishche7.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with id = " + id + " not found"));
    }

    public Patient createPatient(CreatePatientRequest createPatientRequest) {
        Patient newPatient = new Patient();
        newPatient.setName(createPatientRequest.getName());
        newPatient.setGender(createPatientRequest.getGender());
        newPatient.setBirthDate(createPatientRequest.getBirthDate());
        return patientRepository.save(newPatient);
    }

    public Patient updatePatient(UUID id, UpdatePatientRequest updatePatientRequest) {
        Patient existingPatient = getPatientById(id);
        existingPatient.setName(updatePatientRequest.getName());
        existingPatient.setGender(updatePatientRequest.getGender());
        existingPatient.setBirthDate(updatePatientRequest.getBirthDate());
        return patientRepository.save(existingPatient);
    }

    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient with id = " + id + " not found");
        }
        patientRepository.deleteById(id);
    }
}
