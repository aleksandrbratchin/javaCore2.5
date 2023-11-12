package ru.bratchin.javaCore25.util.validation;

import org.apache.commons.lang3.StringUtils;
import ru.bratchin.javaCore25.exception.validation.NameValidException;
import ru.bratchin.javaCore25.exception.validation.SurnameValidException;

public class StringValid {

    private static final String validCharactersForNameRu = "йцукенгшщзхъфывапролджэячсмитьбюё";
    private static final String validCharactersForNameEn = "qwertyuiopasdfghjklzxcvbnm";
    public static void nameValid(String str) {
        if (!StringUtils.isBlank(str)) {
            throw new NameValidException("Имя не должно быть пустым!");
        }
        if (!StringUtils.containsAnyIgnoreCase(str, validCharactersForNameEn) || !StringUtils.containsAnyIgnoreCase(str, validCharactersForNameRu)) {
            throw new NameValidException("Имя должно содержать только буквы!");
        }
    }

    public static void surnameValid(String str) {
        if (!StringUtils.isBlank(str)) {
            throw new SurnameValidException("Фамилия не должна быть пустой!");
        }
        if (!StringUtils.containsAnyIgnoreCase(str, validCharactersForNameEn) || !StringUtils.containsAnyIgnoreCase(str, validCharactersForNameRu)) {
            throw new NameValidException("Фамилия должна содержать только буквы!");
        }
    }
}
