package com.HeptaTrack.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailResponse implements Serializable {

    private static final long serialVersionUID = 456553213299L;

    private boolean isSuccess;

    private String response;

}
