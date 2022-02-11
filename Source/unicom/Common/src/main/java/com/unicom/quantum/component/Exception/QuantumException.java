package com.unicom.quantum.component.Exception;

import com.unicom.quantum.component.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantumException extends Exception {

    private Result result;

    public QuantumException(String message) {
        super(message);
    }

}
