package com.healthlx.smartonfhir.sample;

import com.healthlx.smartonfhir.core.SmartOnFhirContext;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatientController {

  private SmartOnFhirContext smartOnFhirContext;

  @Autowired
  public PatientController(SmartOnFhirContext smartOnFhirContext) {this.smartOnFhirContext = smartOnFhirContext;}

  @GetMapping("/get-patient")
  public Patient getPatient() {
    String patientId = smartOnFhirContext.getPatient();
    Patient patient = new Patient();
    patient.setId(patientId);
    return patient;
  }

}
