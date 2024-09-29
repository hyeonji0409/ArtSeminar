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
    private String popupValue;
    private String title;
    private String description;
    private String imageSrc;
}
