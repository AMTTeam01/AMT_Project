package ch.heigvd.amt.mvcProject.application.comment;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
@EqualsAndHashCode
public class CommentCommand {

    private String description;
    private Date createDate;
}
