package ru.javaops.masterjava.persist.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class City extends BaseEntity {
    private final @NonNull String key;

    private final @NonNull String name;

    @Builder
    public City(Integer id, String key, String name) {
        super(id);
        this.key = key;
        this.name = name;
    }
}
