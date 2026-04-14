package org.example.dto;

public class RecipeSummaryResponse {

    private String id;
    private String name;

    public RecipeSummaryResponse() {
    }

    public RecipeSummaryResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}