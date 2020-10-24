package ch.heigvd.amt.mvcProject.domain.answer;

import ch.heigvd.amt.mvcProject.domain.Id;

import java.util.UUID;

public class AnswerId extends Id {

    public AnswerId() {
    }

    public AnswerId(String id) {
        super(id);
    }

    public AnswerId(UUID id) {
        super(id);
    }
}
