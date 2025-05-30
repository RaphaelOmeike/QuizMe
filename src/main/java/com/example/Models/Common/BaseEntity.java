package com.example.Models.Common;

import java.time.OffsetDateTime;

public abstract class BaseEntity {
    public int Id;
//    private OffsetDateTime CreatedAt = OffsetDateTime.now();

//    OffsetDateTime getCreatedAt() {
//        return CreatedAt;
//    }

    public BaseEntity(int id) {
        Id = id;
    }
}
