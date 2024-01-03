package com.sparta.board.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class BoardRequestDto {
    private String username;
    private String title;
    private String password;
    private String contents;
    private LocalDateTime localDateTime;

}
