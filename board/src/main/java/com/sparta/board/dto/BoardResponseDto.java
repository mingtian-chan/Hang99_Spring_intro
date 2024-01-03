package com.sparta.board.dto;

import com.sparta.board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class BoardResponseDto {
    private Long id;
    private String username;
    private String title;
    private String password;
    private String contents;
    private LocalDateTime localDateTime;



    public BoardResponseDto(Board board, LocalDateTime localDateTime) {
        this.id = board.getId();
        this.username = board.getUsername();
        this.title = board.getTitle();
        this.password = board.getPassword();
        this.contents = board.getContents();
        this.localDateTime = localDateTime;
    }

    public BoardResponseDto(Long id, String username, String title, String password, String contents, LocalDateTime localDateTime) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.password = password;
        this.contents = contents;
        this.localDateTime = localDateTime;
    }

    public BoardResponseDto(Long id, String username, String title, String contents, LocalDateTime localDateTime) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.localDateTime = localDateTime;
    }
}
