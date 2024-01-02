package com.sparta.springprepare;


import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class memo {
    private final String username;
    private String contents;


}

class Main {
    public static void main(String[] args) {
        memo memo = new memo("chan");
        System.out.println(memo.getUsername());

    }
}