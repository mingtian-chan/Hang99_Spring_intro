package com.sparta.board.controller;


import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardController {

    private final JdbcTemplate jdbcTemplate;

    public BoardController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @PostMapping("/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {

        // requestDto -> Entity
        Board board = new Board(requestDto);

        // DB저장

        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Get the current LocalDateTime
        LocalDateTime currentDateTime = LocalDateTime.now();

        String sql = "INSERT INTO board (username, title, password, contents, `date`) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, board.getUsername());
                    preparedStatement.setString(2, board.getTitle());
                    preparedStatement.setString(3, board.getPassword());
                    preparedStatement.setString(4, board.getContents());

                    // Convert LocalDateTime to Timestamp
                    Timestamp timestamp = Timestamp.valueOf(currentDateTime);
                    preparedStatement.setTimestamp(5, timestamp);

                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        board.setId(id);



        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(board, currentDateTime);

        return boardResponseDto;
    }

    @GetMapping("/board")
    public List<BoardResponseDto> getBoard() {
        // DB 조회
        String sql = "SELECT * FROM board ORDER BY date DESC";

        return jdbcTemplate.query(sql, new RowMapper<BoardResponseDto>() {
            @Override
            public BoardResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 board 데이터들을 BoardResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String title = rs.getString("title");
                String contents = rs.getString("contents");

                // Use getObject to get the timestamp
                Timestamp timestamp = rs.getTimestamp("date");

                // Convert Timestamp to LocalDateTime
                LocalDateTime localDateTime = (timestamp != null) ? timestamp.toLocalDateTime() : LocalDateTime.now();

                return new BoardResponseDto(id, username, title, contents, localDateTime);
            }
        });
    }

    @GetMapping("/board/{id}")
    public BoardResponseDto getSpecificBoard(@PathVariable Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Board board = findById(id);
        if (board != null) {
            // Board가 존재할 경우, 해당 Board를 BoardResponseDto로 변환하여 반환
            LocalDateTime localDateTime = getLocalDateTimeFromDatabase(id); // Adjust this method as needed
            return new BoardResponseDto(board.getId(), board.getUsername(), board.getTitle(), board.getPassword(), board.getContents(), localDateTime);
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }


    @PutMapping("/board/{id}/{password}")
    public BoardResponseDto updateBoard(
            @PathVariable Long id,
            @PathVariable String password,
            @RequestBody BoardRequestDto requestDto
    ) {
        // 해당 메모가 DB에 존재하는지 확인
        Board board = findById(id);
        if (board != null && (board.getPassword() == null || board.getPassword().equals(password))) {
            // board 내용 수정
            String sql = "UPDATE board SET username = ?, title = ?, contents = ? WHERE id = ?";
            jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getTitle(), requestDto.getContents(), id);

            LocalDateTime localDateTime = getLocalDateTimeFromDatabase(id); // Adjust this method as needed
            return new BoardResponseDto(id, requestDto.getUsername(), requestDto.getTitle(), requestDto.getContents(), localDateTime);
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }


    @DeleteMapping("/board/{id}")
    public Long deleteBoard(@PathVariable Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Board board = findById(id);
        if (board != null) {
            // board 삭제
            String sql = "DELETE FROM board WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }


    // Helper method to retrieve LocalDateTime from the database
    private LocalDateTime getLocalDateTimeFromDatabase(Long id) {
        // Implement the logic to retrieve LocalDateTime based on the provided ID
        // Example: Fetch the "date" column from the database using jdbcTemplate
        String sql = "SELECT date FROM board WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, LocalDateTime.class, id);
    }

    private Board findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM board WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Board board = new Board();
                board.setId(resultSet.getLong("id"));
                board.setUsername(resultSet.getString("username"));
                board.setTitle(resultSet.getString("title"));
                board.setContents(resultSet.getString("contents"));

                // Check for null before converting to LocalDateTime
                Timestamp timestamp = resultSet.getTimestamp("date");
                LocalDateTime date = (timestamp != null) ? timestamp.toLocalDateTime() : null;
                board.setLocalDateTime(date);

                return board;
            } else {
                return null;
            }
        }, id);
    }

}


