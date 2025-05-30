package com.example.Models;

import com.example.Models.Common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class Option extends BaseEntity {
    public String Content;
    //public List<QuestionOption> QuestionOptions = new ArrayList<>();

    //remove navigation properties lazy-loading seems better
    public Option(int id, String content) {
        super(id);
        Content = content;
    }
}
