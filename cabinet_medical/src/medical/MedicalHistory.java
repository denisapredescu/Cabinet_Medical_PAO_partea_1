package medical;

import person.Patient;

import java.util.ArrayList;
import java.util.List;

public class MedicalHistory {

    public Patient patient;
    public List<Appointment> appointments;

    public MedicalHistory(Patient patient, List<Appointment> appointments) {
        this.patient = patient;
        this.appointments = appointments;
    }

    public MedicalHistory(Patient patient) {
        this.patient = patient;
        this.appointments = new ArrayList<Appointment>();   // vida
    }

    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return "MedicalHistory {" +
                "patient=" + patient +
                ", appointments=" + appointments +
                '}';
    }
}
