package com.example.demo.Datos;

public class Recommendation {
	 private String title;
	    private String summary;
	    private String suggestedInterventionist;

	    public Recommendation(String title, String summary, String suggestedInterventionist) {
	        this.title = title;
	        this.summary = summary;
	        this.suggestedInterventionist = suggestedInterventionist;
	    }

	    // Getters y Setters
	    public String getTitle() { return title; }
	    public void setTitle(String title) { this.title = title; }
	    public String getSummary() { return summary; }
	    public void setSummary(String summary) { this.summary = summary; }
	    public String getSuggestedInterventionist() { return suggestedInterventionist; }
	    public void setSuggestedInterventionist(String suggestedInterventionist) { this.suggestedInterventionist = suggestedInterventionist; }
}
