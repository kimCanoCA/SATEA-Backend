package com.example.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "estudiantes")
public class Estudiante {
    @Id
    
    @Column(name = "id_estudiante")
    private Long idEstudiante;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;
    
    @Column(name = "carrera")
    private String carrera; 

    // 🚨 CAMPO AGREGADO 1: Edad
    @Column(name = "edad") 
    private Integer edad;

    // 🚨 CAMPO AGREGADO 2: Nivel de Riesgo
    @Column(name = "nivel_riesgo") 
    private String nivelRiesgo;

    @Column(name = "anxiety_level", columnDefinition = "SMALLINT")
    private Integer anxietyLevel;

    @Column(name = "self_esteem", columnDefinition = "SMALLINT")
    private Integer selfEsteem;

    @Column(name = "mental_health_history", columnDefinition = "SMALLINT")
    private Integer mentalHealthHistory;

    @Column(name = "depression", columnDefinition = "SMALLINT")
    private Integer depression;

    @Column(name = "headache", columnDefinition = "SMALLINT")
    private Integer headache;

    @Column(name = "blood_pressure", columnDefinition = "SMALLINT")
    private Integer bloodPressure;

    @Column(name = "sleep_quality", columnDefinition = "SMALLINT")
    private Integer sleepQuality;

    @Column(name = "breathing_problem", columnDefinition = "SMALLINT")
    private Integer breathingProblem;

    @Column(name = "noise_level", columnDefinition = "SMALLINT")
    private Integer noiseLevel;

    @Column(name = "living_conditions", columnDefinition = "SMALLINT")
    private Integer livingConditions;

    @Column(name = "safety", columnDefinition = "SMALLINT")
    private Integer safety;

    @Column(name = "basic_needs", columnDefinition = "SMALLINT")
    private Integer basicNeeds;

    @Column(name = "academic_performance", columnDefinition = "SMALLINT")
    private Integer academicPerformance;

    @Column(name = "study_load", columnDefinition = "SMALLINT")
    private Integer studyLoad;

    @Column(name = "teacher_student_relationship", columnDefinition = "SMALLINT")
    private Integer teacherStudentRelationship;

    @Column(name = "future_career_concerns", columnDefinition = "SMALLINT")
    private Integer futureCareerConcerns;

    @Column(name = "social_support", columnDefinition = "SMALLINT")
    private Integer socialSupport;

    @Column(name = "peer_pressure", columnDefinition = "SMALLINT")
    private Integer peerPressure;

    @Column(name = "extracurricular_activities", columnDefinition = "SMALLINT")
    private Integer extracurricularActivities;

    @Column(name = "bullying", columnDefinition = "SMALLINT")
    private Integer bullying;

    @Column(name = "stress_level", columnDefinition = "SMALLINT")
    private Integer stressLevel;

    public Estudiante() {
        // Constructor vacío requerido por JPA
    }

    // Relación con Consejero (Muchos a Uno)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_consejero", referencedColumnName = "id_consejero")
    private Consejero consejero;

    // Relación con AnalisisRiesgo (Muchos a Uno)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_analisis", referencedColumnName = "id_analisis")
    private Analisis_Riesgo analisis_riesgo;

    // Relación Estudiante -> Recomendacion (Uno a Muchos)
    @OneToMany(mappedBy = "estudiantes")
    private List<Recomendacion> recomendaciones;

	public Estudiante(Long idEstudiante, String nombre, String email, String carrera, Integer edad, String nivelRiesgo,
			Integer anxietyLevel, Integer selfEsteem, Integer mentalHealthHistory, Integer depression, Integer headache,
			Integer bloodPressure, Integer sleepQuality, Integer breathingProblem, Integer noiseLevel,
			Integer livingConditions, Integer safety, Integer basicNeeds, Integer academicPerformance,
			Integer studyLoad, Integer teacherStudentRelationship, Integer futureCareerConcerns, Integer socialSupport,
			Integer peerPressure, Integer extracurricularActivities, Integer bullying, Integer stressLevel,
			Consejero consejero, Analisis_Riesgo analisis_riesgo, List<Recomendacion> recomendaciones) {
		super();
		this.idEstudiante = idEstudiante;
		this.nombre = nombre;
		this.email = email;
		this.carrera = carrera;
		this.edad = edad;
		this.nivelRiesgo = nivelRiesgo;
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

	public String getCarrera() {
		return carrera;
	}

	public void setCarrera(String carrera) {
		this.carrera = carrera;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getNivelRiesgo() {
		return nivelRiesgo;
	}

	public void setNivelRiesgo(String nivelRiesgo) {
		this.nivelRiesgo = nivelRiesgo;
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