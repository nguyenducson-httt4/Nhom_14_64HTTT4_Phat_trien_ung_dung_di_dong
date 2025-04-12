package tlu.cse.ht64.apphoctienganh.model;

import java.io.Serializable;

public class Tense implements Serializable {
    private String name;
    private String usage;
    private String structure;
    private String affirmative;
    private String negative;
    private String question;
    private String example;

    public Tense(String name, String usage, String structure, String affirmative, String negative, String question, String example) {
        this.name = name;
        this.usage = usage;
        this.structure = structure;
        this.affirmative = affirmative;
        this.negative = negative;
        this.question = question;
        this.example = example;
    }

    public String getName() { return name; }
    public String getUsage() { return usage; }
    public String getStructure() { return structure; }
    public String getAffirmative() { return affirmative; }
    public String getNegative() { return negative; }
    public String getQuestion() { return question; }
    public String getExample() { return example; }

    @Override
    public String toString() {
        return name;
    }
}