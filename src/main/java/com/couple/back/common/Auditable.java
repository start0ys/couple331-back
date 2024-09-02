package com.couple.back.common;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Auditable {
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
