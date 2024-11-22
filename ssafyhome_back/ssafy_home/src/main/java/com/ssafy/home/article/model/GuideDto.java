package com.ssafy.home.article.model;

public class GuideDto {
    private int id;
    private String category;
    private String content;
    private String url;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
        

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "GuideDto [id=" + id + ", category=" + category + ", content=" + content + ", url=" + url + "]";
	}
    
    
}
