package ru.javaops.masterjava.xml.projectParticipant.finder;

import ru.javaops.masterjava.xml.schema.CityType;
import ru.javaops.masterjava.xml.schema.FlagType;
import ru.javaops.masterjava.xml.schema.User;

import java.util.Arrays;
import java.util.List;

public class Util {
    public static final List<User> TOPJAVA_PARTICIPANTS = Arrays.asList(
        newUser(
            "Admin",
            "admin@javaops.ru",
            FlagType.SUPERUSER,
            newCity("Санкт-Петербург", "spb")
        ),
        newUser(
            "Full Name",
            "gmail@gmail.com",
            FlagType.ACTIVE,
            newCity("Киев", "kiv")
        )
    );
    
    public static final List<User> BASEJAVA_PARTICIPANTS = Arrays.asList(
        newUser(
            "Deleted",
            "mail@yandex.ru",
            FlagType.DELETED,
            newCity("Санкт-Петербург", "spb")
        )
    );
    
    private static User newUser(
        String fullName,
        String email,
        FlagType flagType,
        CityType city
    ) {
        final User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setFlag(flagType);
        user.setCity(city);
        
        return user;
    }
    
    private static CityType newCity(String value, String id) {
        final CityType cityType = new CityType();
        cityType.setValue(value);
        cityType.setId(id);
        
        return cityType;
    }
}
