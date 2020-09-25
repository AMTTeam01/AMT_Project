package ch.heigvd.amt.mvcProject.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Question {
    private String title;
    private String description;
    private List<String> tags;
}
