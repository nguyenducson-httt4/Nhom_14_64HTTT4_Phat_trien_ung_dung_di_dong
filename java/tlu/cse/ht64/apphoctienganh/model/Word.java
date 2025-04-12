package tlu.cse.ht64.apphoctienganh.model;

import java.io.Serializable;

public class Word implements Serializable {
    private int id;
    private String english;
    private String phonetic;
    private String vietnamese;
    private String imageFile;
    private String audioFile;
    private boolean isLearned;
    private boolean isFavorite;
    private String category;

    public Word(int id, String english, String phonetic, String vietnamese, String imageFile, String audioFile, boolean isLearned, boolean isFavorite, String category) {
        this.id = id;
        this.english = english;
        this.phonetic = phonetic;
        this.vietnamese = vietnamese;
        this.imageFile = imageFile;
        this.audioFile = audioFile;
        this.isLearned = isLearned;
        this.isFavorite = isFavorite;
        this.category = category;
    }

    public int getId() { return id; }
    public String getEnglish() { return english; }
    public void setEnglish(String english) { this.english = english; }
    public String getPhonetic() { return phonetic; }
    public void setPhonetic(String phonetic) { this.phonetic = phonetic; }
    public String getVietnamese() { return vietnamese; }
    public void setVietnamese(String vietnamese) { this.vietnamese = vietnamese; }
    public String getImageFile() { return imageFile; }
    public void setImageFile(String imageFile) { this.imageFile = imageFile; }
    public String getAudioFile() { return audioFile; }
    public void setAudioFile(String audioFile) { this.audioFile = audioFile; }
    public boolean isLearned() { return isLearned; }
    public void setLearned(boolean learned) { isLearned = learned; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return english;
    }
}