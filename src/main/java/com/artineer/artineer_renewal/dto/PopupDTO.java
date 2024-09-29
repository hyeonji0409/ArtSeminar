package com.artineer.artineer_renewal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class PopupDTO {
    private Long no;
    private String title;
    private String imageSrc;
    private String popupValue;
}
