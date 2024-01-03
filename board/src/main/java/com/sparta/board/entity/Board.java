package com.sparta.board.entity;


import com.sparta.board.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Board {
    private Long id;
    private String username;
    private String title;
    private String password;
    private String contents;
    private LocalDateTime localDateTime;

    public Board(BoardRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.title = requestDto.getTitle();
        this.password = requestDto.getPassword();
        this.contents = requestDto.getContents();
        this.localDateTime = requestDto.getLocalDateTime();
    }
}
