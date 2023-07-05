package sh.hutch.taponoff;

import java.time.LocalDateTime;

/**
 * Represents a single tap event from the input source
 */
public record TapEvent
    (Integer id,
     LocalDateTime timestamp,
     TapType type,
     String stopId,
     String companyId,
     String busId,
     String pan) { }
