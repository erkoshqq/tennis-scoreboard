package org.mserz_o.springcourse.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchForm {
    @NotEmpty(message = "Поле имя не может быть пустым")
    @Size(min = 2,max = 20, message = "Минимальный размер имени 2 символа, максимальный 20")
    @Pattern(
            regexp = "\\p{Lu}\\p{Ll}+(\\s\\p{Lu}\\p{Ll}+)?",
            message = "Введите имя или имя и фамилию с заглавной буквы"
    )
    private String firstName;
    @NotEmpty(message = "Поле имя не может быть пустым")
    @Size(min = 2,max = 20, message = "Минимальный размер имени 2 символа, максимальный 20")
    @Pattern(
            regexp = "\\p{Lu}\\p{Ll}+(\\s\\p{Lu}\\p{Ll}+)?",
            message = "Введите имя или имя и фамилию с заглавной буквы"
    )
    private String secondName;

}
