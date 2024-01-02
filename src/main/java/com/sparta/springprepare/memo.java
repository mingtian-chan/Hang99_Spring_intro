package com.sparta.springprepare;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class memo {
    private String username;
    private String contents;


}

class Main {
    public static void main(String[] args) {
        memo memo = new memo();
        memo.setUsername("chan");
        System.out.println(memo.getUsername());

    }
}