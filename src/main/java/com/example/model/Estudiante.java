package com.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @Column(name = "id_estudiante")
    private Long idEstudiante;  // <-- SIN @GeneratedValue

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "anxiety_level")
    private Integer anxietyLevel;

    @Column(name = "self_esteem")
    private Integer selfEsteem;

    @Column(name = "mental_health_history")
    private Integer mentalHealthHistory;

    @Column(name = "depression")
    private Integer depression;

    @Column(name = "headache")
    private Integer headache;

    @Column(name = "blood_pressure")
    private Integer bloodPressure;

    @Column(name = "sleep_quality")
    private Integer sleepQuality;

    @Column(name = "breathing_problem")
    private Integer breathingProblem;

    @Column(name = "noise_level")
    private Integer noiseLevel;

    @Column(name = "living_conditions")
    private Integer livingConditions;

    @Column(name = "safety")
    private Integer safety;

    @Column(name = "basic_needs")
    private Integer basicNeeds;

    @Column(name = "academic_performance")
    private Integer academicPerformance;

    @Column(name = "study_load")
    private Integer studyLoad;

    @Column(name = "teacher_student_relationship")
    private Integer teacherStudentRelationship;

    @Column(name = "future_career_concerns")
    private Integer futureCareerConcerns;

    @Column(name = "social_support")
    private Integer socialSupport;

    @Column(name = "peer_pressure")
    private Integer peerPressure;

    @Column(name = "extracurricular_activities")
    private Integer extracurricularActivities;

    @Column(name = "bullying")
    private Integer bullying;

    @Column(name = "stress_level")
    private Integer stressLevel;

    @ManyToOne
    @JoinColumn(name = "id_Consejero")
    @JsonBackReference(value = "consejero-estudiantes")
    private Consejero consejero;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analisis", referencedColumnName = "id_analisis")
    private Analisis_Riesgo analisis_riesgo;

    @OneToMany(mappedBy = "estudiante")
    @JsonManagedReference
    private List<Recomendacion> recomendaciones;

    // Constructor vacío
    public Estudiante() {}

    // Constructor completo
    public Estudiante(Long idEstudiante, String nombre, String email, Integer anxietyLevel, Integer selfEsteem,
                      Integer mentalHealthHistory, Integer depression, Integer headache, Integer bloodPressure,
                      Integer sleepQuality, Integer breathingProblem, Integer noiseLevel, Integer livingConditions,
                      Integer safety, Integer basicNeeds, Integer academicPerformance, Integer studyLoad,
                      Integer teacherStudentRelationship, Integer futureCareerConcerns, Integer socialSupport,
                      Integer peerPressure, Integer extracurricularActivities, Integer bullying, Integer stressLevel,
                      Consejero consejero, Analisis_Riesgo analisis_riesgo, List<Recomendacion> recomendaciones) {
        this.idEstudiante = idEstudiante;
        this.nombre = nombre;
        this.email = email;
        this.anxietyLevel = anxietyLevel;
        this.selfEsteem = selfEsteem;
        this.mentalHealthHistory = mentalHealthHistory;
        this.depression = depression;
        this.headache = headache;
        this.bloodPressure = bloodPressure;
        this.sleepQuality = sleepQuality;
        this.breathingProblem = breathingProblem;
        this.noiseLevel = noiseLevel;
        this.livingConditions = livingConditions;
        this.safety = safety;
        this.basicNeeds = basicNeeds;
        this.academicPerformance = academicPerformance;
        this.studyLoad = studyLoad;
        this.teacherStudentRelationship = teacherStudentRelationship;
        this.futureCareerConcerns = futureCareerConcerns;
        this.socialSupport = socialSupport;
        this.peerPressure = peerPressure;
        this.extracurricularActivities = extracurricularActivities;
        this.bullying = bullying;
        this.stressLevel = stressLevel;
        this.consejero = consejero;
        this.analisis_riesgo = analisis_riesgo;
        this.recomendaciones = recomendaciones;
    }

    // Getters y Setters
    public Long getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Long idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAnxietyLevel() {
        return anxietyLevel;
    }

    public void setAnxietyLevel(Integer anxietyLevel) {
        this.anxietyLevel = anxietyLevel;
    }

    public Integer getSelfEsteem() {
        return selfEsteem;
    }

    public void setSelfEsteem(Integer selfEsteem) {
        this.selfEsteem = selfEsteem;
    }

    public Integer getMentalHealthHistory() {
        return mentalHealthHistory;
    }

    public void setMentalHealthHistory(Integer mentalHealthHistory) {
        this.mentalHealthHistory = mentalHealthHistory;
    }

    public Integer getDepression() {
        return depression;
    }

    public void setDepression(Integer depression) {
        this.depression = depression;
    }

    public Integer getHeadache() {
        return headache;
    }

    public void setHeadache(Integer headache) {
        this.headache = headache;
    }

    public Integer getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(Integer bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Integer getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(Integer sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    public Integer getBreathingProblem() {
        return breathingProblem;
    }

    public void setBreathingProblem(Integer breathingProblem) {
        this.breathingProblem = breathingProblem;
    }

    public Integer getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(Integer noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public Integer getLivingConditions() {
        return livingConditions;
    }

    public void setLivingConditions(Integer livingConditions) {
        this.livingConditions = livingConditions;
    }

    public Integer getSafety() {
        return safety;
    }

    public void setSafety(Integer safety) {
        this.safety = safety;
    }

    public Integer getBasicNeeds() {
        return basicNeeds;
    }

    public void setBasicNeeds(Integer basicNeeds) {
        this.basicNeeds = basicNeeds;
    }

    public Integer getAcademicPerformance() {
        return academicPerformance;
    }

    public void setAcademicPerformance(Integer academicPerformance) {
        this.academicPerformance = academicPerformance;
    }

    public Integer getStudyLoad() {
        return studyLoad;
    }

    public void setStudyLoad(Integer studyLoad) {
        this.studyLoad = studyLoad;
    }

    public Integer getTeacherStudentRelationship() {
        return teacherStudentRelationship;
    }

    public void setTeacherStudentRelationship(Integer teacherStudentRelationship) {
        this.teacherStudentRelationship = teacherStudentRelationship;
    }

    public Integer getFutureCareerConcerns() {
        return futureCareerConcerns;
    }

    public void setFutureCareerConcerns(Integer futureCareerConcerns) {
        this.futureCareerConcerns = futureCareerConcerns;
    }

    public Integer getSocialSupport() {
        return socialSupport;
    }

    public void setSocialSupport(Integer socialSupport) {
        this.socialSupport = socialSupport;
    }

    public Integer getPeerPressure() {
        return peerPressure;
    }

    public void setPeerPressure(Integer peerPressure) {
        this.peerPressure = peerPressure;
    }

    public Integer getExtracurricularActivities() {
        return extracurricularActivities;
    }

    public void setExtracurricularActivities(Integer extracurricularActivities) {
        this.extracurricularActivities = extracurricularActivities;
    }

    public Integer getBullying() {
        return bullying;
    }

    public void setBullying(Integer bullying) {
        this.bullying = bullying;
    }

    public Integer getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(Integer stressLevel) {
        this.stressLevel = stressLevel;
    }

    public Consejero getConsejero() {
        return consejero;
    }

    public void setConsejero(Consejero consejero) {
        this.consejero = consejero;
    }

    public Analisis_Riesgo getAnalisis_riesgo() {
        return analisis_riesgo;
    }

    public void setAnalisis_riesgo(Analisis_Riesgo analisis_riesgo) {
        this.analisis_riesgo = analisis_riesgo;
    }

    public List<Recomendacion> getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(List<Recomendacion> recomendaciones) {
        this.recomendaciones = recomendaciones;
    }
}
