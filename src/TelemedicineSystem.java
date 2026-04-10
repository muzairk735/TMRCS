import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelemedicineSystem {
    private Map<String, Doctor> doctors = new HashMap<>();
    private Map<String, Patient> patients = new HashMap<>();
    private List<Appointment> appointments = new ArrayList<>();

    public void registerDoctor(Doctor d) {}
    public void registerPatient(Patient p) {}
    public Appointment bookAppointment(String patientId, String doctorId, LocalDateTime time) {}
    public void cancelAppointment(String appointmentId) {}
    public List<Doctor> searchDoctors(String specialization) {}
    public List<Appointment> getPatientAppointments(String patientId) {}
}
